package com.atc.simulator.PredictionService;
import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.DebugDataFeed.DataPlaybackListener;
import com.atc.simulator.ProtocolBuffers.DebugDataFeedServe;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.ProtocolBuffers.DebugDataFeedServe.*;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.atc.simulator.flightdata.SystemStateDatabase.SystemStateDatabase;
import java.net.*;
import java.util.*;
import java.io.*;


/**
 * Created by urke on 8/05/2016.
 *
 * @author Chris Coleman, Uros, Luke Frisken
 */
public class DebugDataFeedClientThread implements RunnableThread
{
    // External config setup for debugDataFeedClientThread.
    private static final int PORT = ApplicationConfig.getInt("settings.prediction-service.debug-data-feed-client.port-number");
    private static final String serverIp = ApplicationConfig.getString("settings.prediction-service.debug-data-feed-client.server-ip");

    // System state database
    private SystemStateDatabase systemStateDatabase;
    private Socket serversSock;
    private boolean continueThread = true;
    private Thread thread;
    private final String threadName = "DebugDataFeedClientThread";
    private ArrayList<DataPlaybackListener> dataPlaybackListeners;

    /**
     * Constructor for DebugDataFeedClientThread
     * @param aSystemStateDatabase : the System State database the DebugDataFeedClient is connected to.
     */
    public DebugDataFeedClientThread(SystemStateDatabase aSystemStateDatabase)
    {
        // takes a reference from the aSystemStateDatabase that is being passed in
        this.systemStateDatabase = aSystemStateDatabase;
        dataPlaybackListeners = new ArrayList<DataPlaybackListener>();
        try
        {
            serversSock = new Socket(serverIp, PORT);
            //this.predictionFeedServerThread = predictionFeedServerThread;
        }
        catch(IOException e)
        {
            System.err.println("DebugDataFeedClientThread Initialisation Failed");
            System.exit(1);
        }
    }

    /**
     * Add a data playback listener to this thread.
     * @param dataPlaybackListener
     */
    public void addDataPlaybackListener(DataPlaybackListener dataPlaybackListener)
    {
        dataPlaybackListeners.add(dataPlaybackListener);
    }


    /**
     * Run method for this thread
     */
    public void run()
    {
        while (continueThread)
        {
            try
            {
                //Wait for message to arrive from Server:
                SystemStateMessage tempMessage = DebugDataFeedServe.SystemStateMessage.parseDelimitedFrom(serversSock.getInputStream());

//                System.out.println(tempMessage);

                //Make an Array of all the AircraftStates that were sent:
                ArrayList<AircraftState> aircraftStatesReceived = new ArrayList<AircraftState>();
                for (int i = 0; i < tempMessage.getAircraftStateCount(); i++) {

                    AircraftState newAircraftState = new AircraftState(
                            tempMessage.getAircraftState(i).getAircraftID(),
                            tempMessage.getAircraftState(i).getTime(),
                            new GeographicCoordinate(tempMessage.getAircraftState(i).getPosition().getAltitude(),
                                    tempMessage.getAircraftState(i).getPosition().getLatitude(),
                                    tempMessage.getAircraftState(i).getPosition().getLongitude()),
                            new SphericalVelocity(tempMessage.getAircraftState(i).getVelocity().getDr(),
                                    tempMessage.getAircraftState(i).getVelocity().getDtheta(),
                                    tempMessage.getAircraftState(i).getVelocity().getDphi()),
                            tempMessage.getAircraftState(i).getHeading());
                    aircraftStatesReceived.add(newAircraftState);
                }//End loop for all aircraft
                //Make a new System State

                if (aircraftStatesReceived.size() > 0) {
                    SystemState testState = new SystemState(System.currentTimeMillis(), aircraftStatesReceived);
                    //Send it to the connected systemStateDatabase
                    //Uros: I replaced the method in systemStateDatabase that was previously
                    // connected directly to the display, calls a simple method to update the system state.
                    systemStateDatabase.systemStateUpdate(testState);
                    onSystemUpdate(testState);
                }

            }catch(IOException e){
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println("Prediction-side Debug Message Parse Failed");
            }
        }//End while
        kill();
    } //End run

    private void onSystemUpdate(SystemState systemState)
    {
//        System.out.println("performing on system update to " + dataPlaybackListeners.size() + " listeners");
        for (DataPlaybackListener listener : dataPlaybackListeners)
        {
            listener.onSystemUpdate(systemState);
        }
    }

    /**
     * Method called to start the thread.
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
     * Kill this thread.
     */
    @Override
    public void kill() {
        continueThread = false;
    }

    /**
     * Join this thread.
     */
    @Override
    public void join() throws InterruptedException {
        thread.join();
    }

}
