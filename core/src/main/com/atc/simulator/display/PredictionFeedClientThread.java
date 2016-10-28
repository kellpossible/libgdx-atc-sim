package com.atc.simulator.display;
import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.protocol_buffers.PredictionFeedServe;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * PredictionFeedClientThread connects to and receives messages from a PredictionFeedServerThread, feeding the received messages onto its display
 * These messages are passed as PredictionFeedServe Protocol Buffers
 *
 * PUBLIC FEATURES:
 * // Constructors
 *    PredictionFeedClientThread()
 * // Methods
 *    update() - Thread of checking buffer and passing messages to server
 *    kill() - Clears the flag that the client thread runs off, letting the thread finish gracefully
 *    start() - Start new Thread
 *    addListener(PredictionListener) - Add a new Listener
 *    removeListener(PredictionListener) - Remove a new Listener
 * COLLABORATORS:
 *    java.util.ArrayList
 *    vectors.GeographicCoordinate
 *    PredictionFeedServe.PredictionMessage
 *
 * MODIFIED:
 * @version 0.3, CC 29/05/16, Added sleep in Thread update()
 * @author    Chris Coleman, 7191375
 */
public class PredictionFeedClientThread implements RunnableThread {
    private static final boolean enableDebugPrint = ApplicationConfig.getBoolean("settings.debug.print-predictionfeedclient");

    // External config setup for port number and server IP
    private static final int PORT = ApplicationConfig.getInt("settings.display.prediction-feed-client.port-number");
    private static final String serverIp = ApplicationConfig.getString("settings.display.prediction-feed-client.server-ip");

    //Socket Definitions
    private Socket serversSock;
    //Thread Definitions
    private boolean continueThread = true;
    private Thread thread;
    private final String threadName = "PredictionFeedClientThread";
    //Output Definitions
    private ArrayList<PredictionListener> myListeners;

    /**
     * Constructor for PredictionFeedClientThread
     */
    public PredictionFeedClientThread(){myListeners = new ArrayList<PredictionListener>();}

    private AircraftState buildAircraftState(
            String aircraftID,
            PredictionFeedServe.PredictionAircraftStateMessage aircraftStateMessage)
    {
        PredictionFeedServe.GeographicCoordinateMessage positionMessage =
                aircraftStateMessage.getPosition();

        PredictionFeedServe.SphericalVelocityMessage velocityMessage =
                aircraftStateMessage.getVelocity();

        return new AircraftState(
                aircraftID,
                aircraftStateMessage.getTime(),
                new GeographicCoordinate(
                        positionMessage.getAltitude(),
                        positionMessage.getLatitude(),
                        positionMessage.getLongitude()
                ),
                new SphericalVelocity(
                        velocityMessage.getDr(),
                        velocityMessage.getDtheta(),
                        velocityMessage.getDphi()
                ),
                0.0
        );
    }

    /**
     * Build a {@link Track} from a {@link PredictionFeedServe.Track} message
     * @param messageTrack
     * @param aircraftID
     * @return A new track of aircraft states, or null.
     */
    private Track buildTrack(PredictionFeedServe.Track messageTrack, String aircraftID)
    {
        if (messageTrack == null)
        {
            return null;
        }

        ArrayList<AircraftState> aircraftStates = new ArrayList<AircraftState>();

        //For all the positions inside the message, add them to the Prediction
        for(int i = 0; i < messageTrack.getAircraftStateCount(); i++)
        {
            PredictionFeedServe.PredictionAircraftStateMessage aircraftStateMessage =
                    messageTrack.getAircraftState(i);


            aircraftStates.add(buildAircraftState(aircraftID, aircraftStateMessage));
        }

        return new Track(aircraftStates);
    }

    /**
     * Threaded routine, pull new data from socket, turn it into a Prediction type, and notify listeners
     * TODO: Will this need to feed a buffer that is then converted for listeners? Does a Java socket have a buffer inbuilt? :/
     */
    public void run(){
        PredictionFeedServe.AircraftPredictionMessage predictionMessage = null;
        InputStream inputStream = null;
        //I don't think this should really be here....
        try{
            serversSock = new Socket(serverIp, PORT);
        }catch(IOException e){System.err.println("PredictionFeedClientThread Initialisation Failed");System.exit(1);}


        while(continueThread)
        {
            try{
                //Get the PredictionMessage from the Socket
                inputStream = serversSock.getInputStream();

                //here this thread will block while waiting for a message to appear in the inputStream.
                predictionMessage = PredictionFeedServe.AircraftPredictionMessage.parseDelimitedFrom(inputStream);

                //check to see that stream was not at EOF when the parsing started
                while(predictionMessage != null)
                {
                    Track leftTrack = buildTrack(predictionMessage.getLeftTrack(), predictionMessage.getAircraftID());
                    Track centreTrack = buildTrack(predictionMessage.getCentreTrack(), predictionMessage.getAircraftID());
                    Track rightTrack = buildTrack(predictionMessage.getRightTrack(), predictionMessage.getAircraftID());

                    Prediction.State predictionState = Prediction.State.valueOf(predictionMessage.getState().name());

                    AircraftState aircraftState = buildAircraftState(
                            predictionMessage.getAircraftID(),
                            predictionMessage.getAircraftState());


                    //Made a new Prediction with ID and Time
                    Prediction newPred = new Prediction(
                            predictionMessage.getAircraftID(),
                            System.currentTimeMillis(),
                            aircraftState,
                            leftTrack,
                            centreTrack,
                            rightTrack,
                            predictionState);

                    if(enableDebugPrint){ System.out.println("PredictionFeedClient has received AircraftModel " + newPred.getAircraftID()); }

                    notifyAllListeners(newPred);

                    predictionMessage = PredictionFeedServe.AircraftPredictionMessage.parseDelimitedFrom(inputStream);
                }
                System.err.println(threadName + " possibly an empty input stream causing null value");
                System.err.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            }catch(IOException e){System.err.println(threadName + " Message Parse Failed");}
        }
        try{serversSock.close();}catch(IOException i){System.err.println("PredictionFeedClientThread Can't close ServerSocket socket");}
        System.out.println(threadName + " killed");
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
     * Adds a new Listener to the list
     * @param listener : PredictionListener to add
     */
    public void addListener(PredictionListener listener){myListeners.add(listener);}

    /**
     * Finds and deletes a Listener from the list
     * @param listener : PredictionListener to delete
     */
    public void removeListener(PredictionListener listener){myListeners.remove(listener);}

    /**
     * Notifies all the listeners that a new prediction has come, and sends them the update
     * @param prediction : The new prediction to be sent around
     */
    private void notifyAllListeners(Prediction prediction)
    {
        for(PredictionListener listener : myListeners)
            listener.onPredictionUpdate(prediction);
    }
}
