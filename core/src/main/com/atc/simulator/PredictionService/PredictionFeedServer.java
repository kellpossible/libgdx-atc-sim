package com.atc.simulator.PredictionService;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * PredictionFeedServer is responsible for the encoding and sending of predictions for display/external systems. Currently able to connect and send
 * to a single client, emptying its buffer if no client has connected.
 *
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    PredictionFeedServer()
 * // Methods
 *    sendPredictionToServer(String aircraftID, GeographicCoordinate[] predictions)  - Encodes a Prediction and stores in internal buffer
 *    run() - Thread of checking buffer and removing first element
 *    killThread() - flags that the Server has been finished with, and to stop all threads running (clearing of buffer and accepting clients)
 *
 * COLLABORATORS:
 *    java.util.ArrayList
 *    vectors.GeographicCoordinate
 *    PredictionFeedServe.PredictionMessage
 *
 * MODIFIED:
 * @version 0.2, CC 21/05/16, Merged Encoder and Server
 * @author    Chris Coleman, 7191375
 */

public class PredictionFeedServer implements Runnable{
    static int PORT = 6789;

    private ArrayList<PredictionFeedServe.AircraftPredictionMessage> toBeSentBuffer; //Buffer of encoded messages
    private Thread serverConnectThread; //Thread to accept connections by clients
    private ServerSocket connectionSocket; //ServerSocket that handles connect requests by clients
    private Socket connectedClient; //The socket that a successful client connection can be sent message through
    private boolean continueThread = true;  //Simple flag that dictates whether the Server threads will keep looping

    /**
     * Constructor, instantiates a new buffer for storing of messages to be sent.
     * Also creates a separate thread that handles external client connection. Currently only accepts a single client
     * to be connected before not accepting any more.
     */
    public PredictionFeedServer()
    {
        toBeSentBuffer = new ArrayList<PredictionFeedServe.AircraftPredictionMessage>();
        connectedClient = new Socket();

        try{
            connectionSocket = new ServerSocket(PORT);

            serverConnectThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        connectedClient = connectionSocket.accept();
                    }catch(IOException e){System.out.println("PredictionFeed Server accept error");}
                    System.out.println("Thread Killed (Connection)");
                }
            });


        }catch(IOException e){System.out.println("PredictionFeed Server Creation error");}
        System.out.println("Server/ArrayList created");
    }

    /**
     * Creates a new AircraftPredictionMessage. Is given a Prediction, takes the ID and Time, and then loops through
     * all the positions and builds GeographicCoordinateMessages. Once finished, wraps it all up nicely and places the
     * new message in the buffer, ready to be sent
     * @param newPrediction : The prediction datatype created by the PredictionEngine
     */
    public synchronized void sendPredictionToServer(Prediction newPrediction)
    {
        PredictionFeedServe.AircraftPredictionMessage.Builder MessageBuilder = PredictionFeedServe.AircraftPredictionMessage.newBuilder(); //PredictionMessage Builder

        MessageBuilder.setAircraftID(newPrediction.getAircraftID()); //Add the AircraftID to the Message
        MessageBuilder.setTime(ISO8601.fromCalendar(newPrediction.getPredictionTime())); //Set the time

        //Now, loop through all the positions, Build Coordinates and add them to the Message
        for(GeographicCoordinate temp : newPrediction.getListOfPositions())
        {
            MessageBuilder.addPosition(
                    PredictionFeedServe.GeographicCoordinateMessage.newBuilder()
                            .setAltitude(temp.getAltitude())
                            .setLatitude(temp.getLatitude())
                            .setLongitude(temp.getLongitude())
            );
        }
        toBeSentBuffer.add(MessageBuilder.build()); //Build message and add to buffer
    }

    /**
     * This thread will begin the Server connection thread and then loop; checking its buffer and either sending or deleting
     * the messages depending on whether a client has connected.
     * When the continueThread flag is cleared, both the ServerSocket and connecting to Client will be closed.
     */
    public void run() {
        serverConnectThread.start();
        try {
            while (continueThread) {
                if (toBeSentBuffer.size() > 0) {
                    if (!connectedClient.isConnected()) //If nothing is connected
                    {
                        toBeSentBuffer.remove(0); //Delete the data
                        System.out.println("No client, data deleted");
                    }
                    else
                    {
                        try
                        {
                            toBeSentBuffer.get(0).writeDelimitedTo(connectedClient.getOutputStream()); //Try to send message
                        } catch (IOException e) {System.out.println("Send to Display failed");}
                        toBeSentBuffer.remove(0);
                        System.out.println("Data sent to Client");
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException i) {
                }
            }
        }
        finally{
            try{connectionSocket.close();}catch(IOException i){System.out.println("Can't close ServerSocket");}
            try{connectedClient.close();}catch(IOException i){System.out.println("Can't close clientSocket");}
        }
        System.out.println("Thread Killed (Server)");
    }

    /**
     * Small method called too kill the server's threads when the have run through
     */
    public void killThread()
    {
        continueThread = false;
    }

}