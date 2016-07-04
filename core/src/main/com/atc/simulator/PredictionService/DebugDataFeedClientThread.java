package com.atc.simulator.PredictionService;
import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.ProtocolBuffers.DebugDataFeedServe;
import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.ProtocolBuffers.DebugDataFeedServe.*;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

import java.net.*;
import java.text.ParseException;
import java.util.*;
import java.io.*;


/**
 * Created by urke on 8/05/2016.
 *
 * @author Chris Coleman, Uros, Luke Frisken
 */
public class DebugDataFeedClientThread implements RunnableThread
{
    private PredictionEngineThread myEngine;
    private static int PORT = 6989;
    private Socket serversSock;
    private boolean continueThread = true;
    private Thread thread;
    private final String threadName = "DebugDataFeedClientThread";

    /**
     * Constructor for DebugDataFeedClientThread
     * @param myEngine : The Engine this client has been connected and will send updates to
     */
    public DebugDataFeedClientThread(PredictionEngineThread myEngine)
    {
        this.myEngine = myEngine;
        try
        {
            serversSock = new Socket("localhost", PORT);
            //this.predictionFeedServerThread = predictionFeedServerThread;
        }
        catch(IOException e)
        {
            System.err.println("DebugDataFeedClientThread Initialisation Failed");
            System.exit(1);
        }
    }

    public void run()
    {
        while (continueThread)
        {
            try
            {
                //Wait for message to arrive from Server:
                SystemStateMessage tempMessage = DebugDataFeedServe.SystemStateMessage.parseDelimitedFrom(serversSock.getInputStream());
                //Make an Array of all the AircraftStates that were sent:
                ArrayList<AircraftState> aircraftStatesReceived = new ArrayList<AircraftState>();
                for (int i = 0; i < tempMessage.getAircraftStateCount(); i++) {

                    aircraftStatesReceived.add(new AircraftState(
                            tempMessage.getAircraftState(i).getAircraftID(),
                            tempMessage.getAircraftState(i).getTime(),
                            new GeographicCoordinate(tempMessage.getAircraftState(i).getPosition().getAltitude(),
                                    tempMessage.getAircraftState(i).getPosition().getLatitude(),
                                    tempMessage.getAircraftState(i).getPosition().getLongitude()),
                            new SphericalVelocity(tempMessage.getAircraftState(i).getVelocity().getDr(),
                                    tempMessage.getAircraftState(i).getVelocity().getDtheta(),
                                    tempMessage.getAircraftState(i).getVelocity().getDphi()),
                            tempMessage.getAircraftState(i).getHeading()));
                }//End loop for all aircraft
                //Make a new System State
                SystemState testState = new SystemState(System.currentTimeMillis(), aircraftStatesReceived);
                //Send it to the connected Engine!
//                myEngine.onSystemUpdate(testState); //TODO: fix this
            }catch(IOException e){System.err.println("Prediction-side Debug Message Parse Failed");}//End Catch
            catch(NullPointerException n){
                System.err.println("Prediction-side Debug thread finishing");
            }
        }//End while
        kill();
    } //End run

    /**
     * Small method called too kill the server's threads when the have run through
     * TODO: what?? this comment doesn't even make sense
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
