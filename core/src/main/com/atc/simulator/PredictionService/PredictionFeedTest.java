package com.atc.simulator.PredictionService;

import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Chris on 7/05/2016.
 *
 *  This is a test/familiarisation class for PredictionFeedServe's protocol buffer
 *  
 * @author    Chris Coleman, 7191375
 */
public class PredictionFeedTest {
    //Test method to be comfortable with using PredictionFeedServe's protocol buffer
    public static void main(String[] arg) {
        //Create SystemState Test Data:
        ArrayList<AircraftState> testAStates = new ArrayList<AircraftState>();
        testAStates.add(new AircraftState("TestPlane1", System.currentTimeMillis(), new GeographicCoordinate(0,0,0), new SphericalVelocity(1,2,3), 0.5));
        SystemStateDatabase systemStateDatabase = new SystemStateDatabase();
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

