package com.atc.simulator.PredictionService;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe.*;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

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
    private static int PORT = 6989;
    private Socket serversSock;
    private boolean continueThread = true;
    private Thread thread;
    private final String threadName = "DebugDataFeedClientThread";

    private PredictionFeedServerThread predictionFeedServerThread; //temporary hack to get a prediction feed server to pass values to upon system update

    /**
     * Constructor for DebugDataFeedClientThread
     * @param predictionFeedServerThread temporary hack to get a prediction feed server to pass values to upon system update
     */
    public DebugDataFeedClientThread(PredictionFeedServerThread predictionFeedServerThread)
    {
        try
        {
            serversSock = new Socket("localhost", PORT);
            this.predictionFeedServerThread = predictionFeedServerThread;
        }
        catch(IOException e)
        {
            System.err.println("DebugDataFeedClientThread Initialisation Failed");
            System.exit(1);
        }
    }

    public void run() {
        while (continueThread) {
            try {
                //doesn't this just run continously!?? shouldn't it wait for a message to arrive...?
                serversSock.getInputStream();
//                SystemStateMessage tempMessage = DebugDataFeedServe.SystemStateMessage.parseDelimitedFrom(serversSock.getInputStream());
                System.out.println("Message Received at DebugDataFeedClientThread");
//                System.out.println(tempMessage.getAircraftState(0).getAircraftID());
                /*ArrayList<AircraftState> aircraftStateReceived = new ArrayList<AircraftState>();

                for (int i = 0; i < tempMessage.getAircraftStateCount(); i++) {

                    AircraftState testAircraftState = new AircraftState(
                            tempMessage.getAircraftState(i).getAircraftID(),
                            ISO8601.toCalendar(tempMessage.getAircraftState(i).getTime()),
                            new GeographicCoordinate(tempMessage.getAircraftState(i).getPosition().getAltitude(),
                                    tempMessage.getAircraftState(i).getPosition().getLatitude(),
                                    tempMessage.getAircraftState(i).getPosition().getLongitude()),
                            new SphericalVelocity(tempMessage.getAircraftState(i).getVelocity().getDr(),
                                    tempMessage.getAircraftState(i).getVelocity().getDtheta(),
                                    tempMessage.getAircraftState(i).getVelocity().getDphi()),
                            tempMessage.getAircraftState(i).getHeading());
                    aircraftStateReceived.add(testAircraftState);
                    SystemState testState = new SystemState(Calendar.getInstance(), aircraftStateReceived);

                    for (AircraftState n : testState.getAircraftStates()) {
                            System.out.println(n.getAircraftID());
                            System.out.println(n.getTime());
                            System.out.println(n.getPosition());
                            System.out.println(n.getVelocity());
                            System.out.println(n.getHeading());

                    }*/

                //this system state class instance will be used as dummy data because the above code was not implemented yet
                ArrayList<AircraftState> dummyAircraftStateList = new ArrayList<AircraftState>();
                AircraftState dummyAircraftState = new AircraftState("dummy_aircraft",
                        Calendar.getInstance(),
                        new GeographicCoordinate(0, 0.5, 0.5),
                        new SphericalVelocity(0, 0.5, 0.5),
                        10.0);
                dummyAircraftStateList.add(dummyAircraftState);
                SystemState dummySystemState = new SystemState(Calendar.getInstance(), dummyAircraftStateList);

                ArrayList<GeographicCoordinate> predictedPositions = new ArrayList<GeographicCoordinate>();
                AircraftState aircraftState = dummySystemState.getAircraftStates().get(0);
                predictedPositions.add(aircraftState.getPosition());
                Prediction prediction = new Prediction(aircraftState.getAircraftID(), Calendar.getInstance(), predictedPositions);

                predictionFeedServerThread.sendPrediction(prediction);



            } catch (IOException e) {
                System.err.println("PredictionFeedClientThread Message Parse Failed");
                e.printStackTrace();
//                System.exit(1);
                kill();
            }
            try {
                serversSock.close();
            } catch (IOException i) {
                System.out.println("DebugDataFeedClientThread Can't close ServerSocket socket");
            }
        }

        System.out.println(threadName + " killed");
    }

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
