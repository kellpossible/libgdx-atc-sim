package com.atc.simulator.DebugDataFeed;
import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.PredictionService.DebugDataFeedClientThread;
import com.atc.simulator.PredictionService.Engine.PredictionEngineSystemStateDatabase;
import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.PredictionService.PredictionFeedServerThread;
import com.atc.simulator.flightdata.SystemStateDatabase.SystemStateDatabase;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.flightdata.TimeSource;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Modified by Chris, 30/05/16 -> Added the whole data->prediction->display feed through
 */
public class DebugServerTest
{

    private static class MyTimeSource implements TimeSource
    {

        /**
         * Get the current time in milliseconds since epoch
         *
         * @return
         */
        @Override
        public long getCurrentTime() {
            return System.currentTimeMillis();
        }
    }

    /**
     * Test method for debugDataFeed Server
     * @param arg
     */
    public static void main(String[] arg)
    {
        MyTimeSource myTimeSource = new MyTimeSource();
        ArrayList<AircraftState> aircraftStateArray = new ArrayList<AircraftState>();

        //create 5 aircraft state
        for (int i = 1; i <= 5; i++)
        {
            String lName = "TestState " + i;
            AircraftState testAircraftState = new AircraftState(lName, System.currentTimeMillis(), new GeographicCoordinate(i,i,i), new SphericalVelocity(i,i,i), i);
            aircraftStateArray.add(testAircraftState);
        }
        //Make a new SystemState with the above AircraftModel States array
        SystemState testState = new SystemState(System.currentTimeMillis(),aircraftStateArray);
        PredictionEngineSystemStateDatabase systemStateDatabase = new PredictionEngineSystemStateDatabase(myTimeSource);

        // creates new servers/clients
        PredictionFeedClientThread testPredictionClient = new PredictionFeedClientThread(); //Goes first, can chill by itself
        PredictionFeedServerThread testPredictionServer = new PredictionFeedServerThread(); //Goes second, can also chill by itself
        DebugDataFeedServerThread testDataServer = new DebugDataFeedServerThread(); //Can also chill by itself

        PredictionEngineThread testEngine = new PredictionEngineThread(
                testPredictionServer, systemStateDatabase, 1); //Must be after PredictionFeedServer
        DebugDataFeedClientThread testDataClient = new DebugDataFeedClientThread(systemStateDatabase); //Must be after Engine
        systemStateDatabase.addListener(testEngine);

        //Start the threads
        testDataServer.start();
        testDataClient.start();
        testPredictionServer.start();
        testPredictionClient.start();
        testEngine.start();

        // Test to see data has been serialized by Server.
        //calls system update.
        testDataServer.onSystemUpdate(testState);


        //Please keep this. Allows everything time to send so you don't prematurely close all the threads.
        //q + [ENTER] will break it out
        try{
            while(System.in.read() != 'q'){}
        }catch(IOException i){}

        //Close Threads
        testDataServer.kill();
        testDataClient.kill();
        testPredictionServer.kill();
        testPredictionClient.kill();
        testEngine.kill();

        System.out.println("Test Complete");
    }
}
