package com.atc.simulator.Display;
import com.atc.simulator.PredictionService.PredictionFeedServe;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.net.Socket;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * PredictionFeedClientThread connects to and receives messages from a PredictionFeedServerThread, feeding the received messages onto its display
 * These messages are passed as PredictionFeedServe Protocol Buffers
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    PredictionFeedClientThread()
 * // Methods
 *    run() - Thread of checking buffer and passing messages to server
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
 * @version 0.3, CC 29/05/16, Added sleep in Thread run()
 * @author    Chris Coleman, 7191375
 */
public class PredictionFeedClientThread implements RunnableThread {
    //Socket Definitions
    private static int PORTNUMBER = 6789;
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

    /**
     * Threaded routine, pull new data from socket, turn it into a Prediction type, and notify listeners
     * TODO: Will this need to feed a buffer that is then converted for listeners? Does a Java socket have a buffer inbuilt? :/
     */
    public void run(){

        //I don't think this should really be here....
        try{
            serversSock = new Socket("localhost", PORTNUMBER);
        }catch(IOException e){System.err.println("PredictionFeedClientThread Initialisation Failed");System.exit(1);}


        while(continueThread)
        {
            try{
                //Get the PredictionMessage from the Socket
                PredictionFeedServe.AircraftPredictionMessage tempMes = PredictionFeedServe.AircraftPredictionMessage.parseDelimitedFrom(serversSock.getInputStream());

                ArrayList<GeographicCoordinate> predictionPositions = new ArrayList<GeographicCoordinate>();

                //For all the positions inside the message, add them to the Prediction
                for(int i = 0; i < tempMes.getPositionCount(); i++)
                {
                    predictionPositions.add(new GeographicCoordinate(tempMes.getPosition(i).getAltitude(),
                            tempMes.getPosition(i).getLatitude(),
                            tempMes.getPosition(i).getLongitude() )
                    );
                }

                //Made a new Prediction with ID and Time
                Prediction newPred = new Prediction(tempMes.getAircraftID(), Calendar.getInstance(), predictionPositions);
                System.out.println("PredictionFeedClient has received Aircraft " + newPred.getAircraftID());
                notifyAllListeners(newPred);
            }catch(IOException e){System.err.println("PredictionFeedClientThread Message Parse Failed");}
            catch(NullPointerException n){;}

            try {
                Thread.sleep(50);
            } catch (InterruptedException i) {
            }
        }
        try{serversSock.close();}catch(IOException i){System.err.println("PredictionFeedClientThread Can't close ServerSocket socket");}
        System.out.println(threadName + " killed");
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
