package com.atc.simulator.PredictionService;

import com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage;
import com.atc.simulator.flightdata.AircraftState;
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

    private ArrayList<PredictionFeedServe.PredictionMessage> toBeSentBuffer; //Buffer of encoded messages
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
        toBeSentBuffer = new ArrayList<PredictionFeedServe.PredictionMessage>();
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
     * Creates a PredictionMessage from the supplied information and adds it to the Server's internal buffer
     * @param newPrediction : The prediction datatype created by the PredictionEngine
     */
    public synchronized void sendPredictionToServer(Prediction newPrediction)
    {
        PredictionMessage.Builder MesBuilder = PredictionMessage.newBuilder();
        PredictionMessage.Position.Builder tempPosBuilder;
        MesBuilder.setAircraftID(newPrediction.getListOfStates().get(0).getAircraftID());

        for(AircraftState temp : newPrediction.getListOfStates())
        {
            tempPosBuilder = PredictionMessage.Position.newBuilder(); //New, fresh, builder
            tempPosBuilder.addPositionData(temp.getPosition().getRadius());   //Add the location data
            tempPosBuilder.addPositionData(temp.getPosition().getLatitude());
            tempPosBuilder.addPositionData(temp.getPosition().getLongitude());

            MesBuilder.addPositionFuture(tempPosBuilder);   //Add this new position to the message
        }
        toBeSentBuffer.add(MesBuilder.build()); //Build message and add to buffer
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