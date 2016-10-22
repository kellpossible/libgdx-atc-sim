package com.atc.simulator.prediction_service;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.protocol_buffers.PredictionFeedServe;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * PredictionFeedServerThread is responsible for the encoding and sending of predictions for display/external systems. Currently able to connect and send
 * to a single client, emptying its buffer if no client has connected.
 *
 * @author    Chris Coleman - 7191375, Luke Frisken
 * TODO: implement the PredictionEngineListener functionality
 */

public class PredictionFeedServerThread implements RunnableThread{
    private static final boolean enableTimer = ApplicationConfig.getBoolean("settings.debug.predictionfeedserver-timer");
    private static final boolean enableDebugPrint = ApplicationConfig.getBoolean("settings.debug.print-predictionfeedserver");
    private static final boolean enableDebugPrintQueues = ApplicationConfig.getBoolean("settings.debug.print-queues");
    private static final boolean enableDebugPrintThreading = ApplicationConfig.getBoolean("settings.debug.print-threading");

    private static final int PORT = ApplicationConfig.getInt("settings.prediction-service.server.port-number");


    private ArrayBlockingQueue<PredictionFeedServe.AircraftPredictionMessage> toBeSentBuffer; //Buffer of encoded messages
    //Socket definitions
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

        if(enableDebugPrint)
        {
            System.out.println("Server/ArrayList created");
        }
    }

    private PredictionFeedServe.PredictionAircraftStateMessage buildAircraftStateMessage(AircraftState aircraftState)
    {
        PredictionFeedServe.PredictionAircraftStateMessage.Builder aircraftStateMessageBuilder =
                PredictionFeedServe.PredictionAircraftStateMessage.newBuilder();

        //TimeStamp
        aircraftStateMessageBuilder.setTime(aircraftState.getTime());
        //A current position
        GeographicCoordinate position = aircraftState.getPosition();
        aircraftStateMessageBuilder.setPosition(
                buildGeographicCoordinateMessage(position)
        );
        //A super complicated velocity
        SphericalVelocity velocity = aircraftState.getVelocity();
        aircraftStateMessageBuilder.setVelocity(
                PredictionFeedServe.SphericalVelocityMessage.newBuilder()
                        .setDr(velocity.getDR())
                        .setDtheta(velocity.getDTheta())
                        .setDphi(velocity.getDPhi())
        );
        return aircraftStateMessageBuilder.build();
    }

    /**
     * Build a {@link com.atc.simulator.protocol_buffers.PredictionFeedServe.Track} message from a {@link Track}
     * @param track track to use to build the message
     * @return new message
     */
    private PredictionFeedServe.Track buildTrackMessage(Track track)
    {
        PredictionFeedServe.Track.Builder trackMessageBuilder = PredictionFeedServe.Track.newBuilder();

        //Now, loop through all the positions, Build Coordinates and add them to the Message
        for (AircraftState aircraftState : track) {
            trackMessageBuilder.addAircraftState(buildAircraftStateMessage(aircraftState));
        }

        return trackMessageBuilder.build();
    }


    PredictionFeedServe.GeographicCoordinateMessage buildGeographicCoordinateMessage(GeographicCoordinate coordinate) {
        PredictionFeedServe.GeographicCoordinateMessage.Builder builder = PredictionFeedServe.GeographicCoordinateMessage.newBuilder();
        if (coordinate == null) {
            System.err.println("Bad coord");
        }
        builder.setAltitude(coordinate.getAltitude());
        builder.setLatitude(coordinate.getLatitude());
        builder.setLongitude(coordinate.getLongitude());
        return builder.build();
    }

    /**
     * Build an {@link PredictionFeedServe.AircraftPredictionMessage} from a {@link Prediction}
     * @param newPrediction prediction to use to build the message
     * @return new message
     */
    private PredictionFeedServe.AircraftPredictionMessage buildMessage(Prediction newPrediction)
    {
        PredictionFeedServe.AircraftPredictionMessage.Builder predictionMessageBuilder =
                PredictionFeedServe.AircraftPredictionMessage.newBuilder(); //PredictionMessage Builder

        predictionMessageBuilder.setAircraftID(newPrediction.getAircraftID()); //Add the AircraftID to the Message

        predictionMessageBuilder.setState(PredictionFeedServe.AircraftPredictionMessage.State.valueOf(newPrediction.getPredictionState().name()));

        long time = newPrediction.getPredictionTime();
        predictionMessageBuilder.setTime(time); //Set the time

        //left track is optional
        if (newPrediction.hasLeftTrack())
        {
            predictionMessageBuilder.setLeftTrack(buildTrackMessage(newPrediction.getLeftTrack()));
        }

        //centre track is required
        predictionMessageBuilder.setCentreTrack(buildTrackMessage(newPrediction.getCentreTrack()));

        //right track is optional
        if (newPrediction.hasRightTrack())
        {
            predictionMessageBuilder.setRightTrack(buildTrackMessage(newPrediction.getRightTrack()));
        }

        predictionMessageBuilder.setAircraftState(buildAircraftStateMessage(newPrediction.getAircraftState()));

        return predictionMessageBuilder.build();

    }

    /**
     * Creates a new AircraftPredictionMessage. Is given a Prediction, takes the ID and Time, and then loops through
     * all the positions and builds GeographicCoordinateMessages. Once finished, wraps it all up nicely and places the
     * new message in the buffer, ready to be sent
     * @param newPrediction : The prediction datatype created by the Engine
     */
    public synchronized void sendPrediction(Prediction newPrediction)
    {
        com.atc.simulator.protocol_buffers.PredictionFeedServe.AircraftPredictionMessage message = buildMessage(newPrediction);

        if (enableDebugPrint)
        {
            System.out.println(threadName + " adding to toBeSentBuffer which has a size of " + toBeSentBuffer.size());
        }

        toBeSentBuffer.add(message); //Build message and add to buffer
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
                        if(enableDebugPrint){ System.out.println(threadName + " No client connected, data not sent"); }

                    } else {
                        for(Socket clientSocket : connectedClients) {
                            message.writeDelimitedTo(clientSocket.getOutputStream()); //Try to send message
                        }

                        if(enableDebugPrint){ System.out.println(threadName + " Data sent to Client"); }
                    }

                    if(enableDebugPrintQueues){ System.out.println(threadName + " toBeSentBuffer size now " + toBeSentBuffer.size()); }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    System.err.println("Send to LayerManager failed");
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

        if(enableDebugPrintThreading){ System.out.println(threadName + " killed"); }
    }

    /**
     * Small method called too kill the server's threads when the have update through
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
                if(enableDebugPrintThreading){ System.out.println(threadName + " killed"); }
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