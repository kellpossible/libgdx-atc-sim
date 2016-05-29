package com.atc.simulator.DebugDataFeed;
import com.atc.simulator.PredictionService.DebugDataFeedClientThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by urke on 28/05/2016.
 */
public class DebugServerTest
{
    /**
     * Test method for debugDataFeed Server
     * @param arg
     */
    public static void main(String[] arg)
    {
        ArrayList<AircraftState> aircraftStateArray = new ArrayList<AircraftState>();

        //create 5 aircraft states
        for (int i = 0; i < 5; i++)
        {
            String lName = "TestState " + i;
            AircraftState testAircraftState = new AircraftState(lName, Calendar.getInstance(), new GeographicCoordinate(i,i,i), new SphericalVelocity(i,i,i), i);
            aircraftStateArray.add(testAircraftState);
        }
        
        SystemState testState = new SystemState(Calendar.getInstance(),aircraftStateArray);

        // creates new server/client
        DebugDataFeedServerThread testServer = new DebugDataFeedServerThread();
        DebugDataFeedClientThread testClient = new DebugDataFeedClientThread(null);

        //Start the threads
        Thread tServer = new Thread(testServer, "ServerThread");
        tServer.start();

        Thread tClient = new Thread(testClient,"ClientThread");
        tClient.start();

        // Test to see data has been serialized by Server.
        //calls system update.
        testServer.onSystemUpdate(testState);

        //Close Threads
        testServer.kill();
        testClient.kill();

        System.out.println("Test Complete");
    }
}
