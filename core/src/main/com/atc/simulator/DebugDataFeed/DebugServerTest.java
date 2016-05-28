package com.atc.simulator.DebugDataFeed;
import com.atc.simulator.PredictionService.DebugDataFeedClient;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe.*;

/**
 * Created by urke on 28/05/2016.
 */
public class DebugServerTest
{
//Test method for debugDataFeed Server
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
        DebugDataFeedClient testClient = new DebugDataFeedClient();

        //Start the threads
        Thread tServer = new Thread(testServer, "ServerThread");
        tServer.start();

        Thread tClient = new Thread(testClient,"ClientThread");
        tClient.start();

        // Test to see data has been serialized by Server.
        //calls system update.
        testServer.onSystemUpdate(testState);

        for (SystemStateMessage n: testServer.getSystemStateMessage())
        {
            System.out.println(n);
            System.out.println("----------------------");
            System.out.println("SERVER TEST COMPLETE");
        }

        //Close Threads
        testServer.killThread();
        testClient.killThread();

        System.out.println("Test Complete");
    }
}
