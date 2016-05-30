package com.atc.simulator.PredictionService;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe.*;
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
    private PredictionEngine myEngine;
    private static int PORT = 6989;
    private Socket serversSock;
    private boolean continueThread = true;
    private Thread thread;
    private final String threadName = "DebugDataFeedClientThread";

    /**
     * Constructor for DebugDataFeedClientThread
     * @param myEngine : The Engine this client has been connected and will send updates to
     */
    public DebugDataFeedClientThread(PredictionEngine myEngine)
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
                    try
                    {
                        aircraftStatesReceived.add(new AircraftState(
                                tempMessage.getAircraftState(i).getAircraftID(),
                                ISO8601.toCalendar(tempMessage.getAircraftState(i).getTime()),
                                new GeographicCoordinate(tempMessage.getAircraftState(i).getPosition().getAltitude(),
                                        tempMessage.getAircraftState(i).getPosition().getLatitude(),
                                        tempMessage.getAircraftState(i).getPosition().getLongitude()),
                                new SphericalVelocity(tempMessage.getAircraftState(i).getVelocity().getDr(),
                                        tempMessage.getAircraftState(i).getVelocity().getDtheta(),
                                        tempMessage.getAircraftState(i).getVelocity().getDphi()),
                                tempMessage.getAircraftState(i).getHeading()));
                    }catch(ParseException p){System.err.println("DebugClient parsing Time failed");}
                }//End loop for all aircraft
                //Make a new System State
                SystemState testState = new SystemState(Calendar.getInstance(), aircraftStatesReceived);
                //Send it to the connected Engine!
                myEngine.onSystemUpdate(testState);
            }catch(IOException e){System.err.println("Prediction-side Debug Message Parse Failed");}//End Catch
            catch(NullPointerException n){System.out.println("Prediction-side Debug thread finishing");}
        }//End while
        kill();
        System.out.println(threadName + " killed");
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
