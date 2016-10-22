package com.atc.simulator.prediction_service;

import com.atc.simulator.display.PredictionFeedClientThread;
import com.atc.simulator.prediction_service.engine.PredictionEngineSystemStateDatabase;
import com.atc.simulator.prediction_service.engine.PredictionEngineThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.TimeSource;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Chris on 7/05/2016.
 *
 *  This is a test/familiarisation class for PredictionFeedServe's protocol buffer
 *  
 * @author    Chris Coleman, 7191375
 */
public class PredictionFeedTest {
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

    //Test method to be comfortable with using PredictionFeedServe's protocol buffer
    public static void main(String[] arg) {
        TimeSource myTimeSource = new MyTimeSource();
        //Create SystemState Test Data:
        ArrayList<AircraftState> testAStates = new ArrayList<AircraftState>();
        testAStates.add(new AircraftState("TestPlane1", System.currentTimeMillis(), new GeographicCoordinate(0,0,0), new SphericalVelocity(1,2,3), 0.5));
        PredictionEngineSystemStateDatabase systemStateDatabase = new PredictionEngineSystemStateDatabase(myTimeSource);
        //Create Server/Client objects
        PredictionFeedServerThread testServer = new PredictionFeedServerThread();
        PredictionFeedClientThread testClient = new PredictionFeedClientThread();
        PredictionEngineThread testEngine = new PredictionEngineThread(testServer, systemStateDatabase, 1);
        systemStateDatabase.addListener(testEngine);
        //Fill up the buffer a little bit
        for (AircraftState testState : testAStates)
        {
            systemStateDatabase.update(testState);
        }
        //Start the threads
        testClient.start();
        testServer.start();
        testEngine.start();
        //Send messages on every entered item, loop out on 'q' being sent
        try{
            while(System.in.read() != 'q') {
                for (AircraftState testState : testAStates)
                {
                    systemStateDatabase.update(testState);
                }
            }
        }catch(IOException i){}

        //Close the Threads
        testEngine.kill();
        testClient.kill();
        testServer.kill();

       System.out.println("Test Complete");
    }

}

