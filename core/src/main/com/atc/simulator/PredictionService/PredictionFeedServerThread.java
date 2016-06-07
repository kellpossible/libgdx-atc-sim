package com.atc.simulator.PredictionService;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.ProtocolBuffers.PredictionFeedServe;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * PredictionFeedServerThread is responsible for the encoding and sending of predictions for display/external systems. Currently able to connect and send
 * to a single client, emptying its buffer if no client has connected.
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    PredictionFeedServerThread()
 * // Methods
 *    sendPrediction(String aircraftID, GeographicCoordinate[] predictions)  - Encodes a Prediction and stores in internal buffer
 *    run() - Thread of checking buffer and removing first element
 *    killThread() - flags that the Server has been finished with, and to stop all threads running (clearing of buffer and accepting clients)
 *
 *
 * MODIFIED:
 * @version 1.0, CC 30/05/16, Neatened the Connection Thread to be its own class. Added capability for multiple clients
 * @author    Chris Coleman - 7191375, Luke Frisken
 * TODO: implement the PredictionEngineListener functionality
 */

public class PredictionFeedServerThread implements RunnableThread{


    private ArrayBlockingQueue<PredictionFeedServe.AircraftPredictionMessage> toBeSentBuffer; //Buffer of encoded messages
    //Socket definitions
    static int PORT = 6789;
    private ServerSocket connectionSocket; //ServerSocket that handles connect requests by clients
    //TODO: Some method of removing clients or handling when they disappear
    private ArrayList<Socket> connectedClients; //List of successfully connected clients that will be sent the new predictions
    //Thread definitions
    private boolean continueThread = true;  //Simple flag that dictates whether the Server threads will keep looping
    private Thread thread;  //Simple flag that dictates whether the Server threads will keep looping
    private PredictionFeedServerConnectThread serverConnectThread; //Thread to accept connections by clients
    private final String threadName = "PredictionFeedServerThread";

    /**
     * Constructor, instantiates a new buffer for storing of messages to be sent.
     * Also creates a separate thread that handles external client connection.
     */
    public PredictionFeedServerThread()
    {
        toBeSentBuffer = new ArrayBlockingQueue<PredictionFeedServe.AircraftPredictionMessage>(400);
        connectedClients = new ArrayList<Socket>();

        try{
            connectionSocket = new ServerSocket(PORT);
        }catch(IOException e){System.out.println("PredictionFeed Server Socket error");}
        serverConnectThread = new PredictionFeedServerConnectThread(connectionSocket);
        ApplicationConfig.debugPrint("print-predictionfeedserver", "Server/ArrayList created");
    }

    /**
     * Creates a new AircraftPredictionMessage. Is given a Prediction, takes the ID and Time, and then loops through
     * all the positions and builds GeographicCoordinateMessages. Once finished, wraps it all up nicely and places the
     * new message in the buffer, ready to be sent
     * @param newPrediction : The prediction datatype created by the Engine
     */
    public synchronized void sendPrediction(Prediction newPrediction)
    {
        PredictionFeedServe.AircraftPredictionMessage.Builder MessageBuilder = PredictionFeedServe.AircraftPredictionMessage.newBuilder(); //PredictionMessage Builder

        MessageBuilder.setAircraftID(newPrediction.getAircraftID()); //Add the AircraftID to the Message
        MessageBuilder.setTime(ISO8601.fromCalendar(newPrediction.getPredictionTime())); //Set the time

        //Now, loop through all the positions, Build Coordinates and add them to the Message
        for(GeographicCoordinate temp : newPrediction.getPredictedPositions())
        {
            MessageBuilder.addPosition(
                    PredictionFeedServe.GeographicCoordinateMessage.newBuilder()
                            .setAltitude(temp.getAltitude())
                            .setLatitude(temp.getLatitude())
                            .setLongitude(temp.getLongitude())
            );
        }
        ApplicationConfig.debugPrint(
                "print-queues",
                threadName + " adding to toBeSentBuffer which has a size of " + toBeSentBuffer.size());
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
                try {
                    PredictionFeedServe.AircraftPredictionMessage message = toBeSentBuffer.take();
                    if (connectedClients.size() == 0) //If nothing is connected
                    {
                        ApplicationConfig.debugPrint("print-predictionfeedserver",
                                threadName + " No client connected, data not sent");
                    } else {
                        for(Socket clientSocket : connectedClients) {
                            message.writeDelimitedTo(clientSocket.getOutputStream()); //Try to send message
                        }

                        ApplicationConfig.debugPrint("print-predictionfeedserver", threadName + " Data sent to Client");
                    }

                    ApplicationConfig.debugPrint("print-queues",
                            threadName + " toBeSentBuffer size now " + toBeSentBuffer.size());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    System.err.println("Send to Display failed");
                }
            }
        }
        finally
        {
            serverConnectThread.kill();
            try{
                for(Socket temp : connectedClients)
                {
                    temp.close();
                }
            }catch(IOException i){System.err.println("Can't close clientSocket");}
        }
        ApplicationConfig.debugPrint("print-threading", threadName + " killed");
    }

    /**
     * Small method called too kill the server's threads when the have run through
     */
    public void kill()
    {
        continueThread = false;
    }

    /**
     * Join this thread.
     */
    @Override
    public void join() throws InterruptedException {
        thread.join();
    }

    /**
     * Start this thread
     */
    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

    /**
     * Private class that is responsible for handling the connection of sockets with PredictionFeed's Clients
     * An internal thread will accept connections and inform the server, storing the new clients in an array
     *
     * @author Chris Coleman, Luke Frisken
     */
    private class PredictionFeedServerConnectThread implements RunnableThread
    {
        private ServerSocket connectionSocket; //The socket that will be listening for, and accepting, connections
        private Socket connectedClient; //Temp holder for any clients that are connected and need to be given to the server
        private Thread thread;
        private final String threadName = "PredictionFeedServerConnectThread";

        /**
         * Constructor for PredictionFeedServerConnectThread
         * @param connectionSocket : The serverSocket this thread will be listening on
         */
        public PredictionFeedServerConnectThread(ServerSocket connectionSocket){this.connectionSocket = connectionSocket;}

        /**
         * This thread will loop forever, waiting to accept client connection requests.. these clients are added to the list held by the server.
         * The thread exits via Exceptions, thrown when the connection socket is closed or overarching server is removed
         */
        public void run()
        {
            try {
                while(true)
                {
                    connectedClient = connectionSocket.accept(); //Wait and accept a connection request
                    connectedClients.add(connectedClient); //Add the new client to the list of clients
                }
            }catch(IOException e){;}
            catch (NullPointerException n){;}
            finally{
                ApplicationConfig.debugPrint("print-threading", threadName + " killed");
            }

        }

        /**
         * Start this thread
         */
        @Override
        public void start() {
            if (thread == null)
            {
                thread = new Thread(this, threadName);
                thread.start();
            }
        }

        /**
         * Kill this thread.
         */
        @Override
        public void kill(){
            try{connectionSocket.close();}catch (IOException e){System.err.println("Closing "+ threadName+ "'s Socket has failed");}
        }

        /**
         * Join this thread.
         */
        @Override
        public void join() throws InterruptedException {
            thread.join();
        }

    }


}