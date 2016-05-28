package com.atc.simulator.DebugDataFeed;
import com.atc.simulator.Display.PredictionFeedClient;
import com.atc.simulator.PredictionService.DebugDataFeedClient;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by urke on 28/05/2016.
 */
public class DebugServerTest
{
//Test method to be comfortable with using PredictionFeedServe's protocol buffer
    public static void main(String[] arg)
    {

        //Create some Test data

        //Create Server/Client objects

        ArrayList<AircraftState> aircraftStateArray = new ArrayList<AircraftState>();


        //create 5 aircraft states
        for (int i = 0; i < 5; i++)
        {
            String lName = "TestState " + i;
            AircraftState testAircraftState = new AircraftState(lName, Calendar.getInstance(), new GeographicCoordinate(i,i,i), new SphericalVelocity(i,i,i), i);
            aircraftStateArray.add(testAircraftState);
        }
        
        SystemState testState = new SystemState(Calendar.getInstance(),aircraftStateArray);

        DebugDataFeedServerThread testServer = new DebugDataFeedServerThread();
        DebugDataFeedClient testClient = new DebugDataFeedClient();

        //Fill up the buffer a little bit
        testServer.onSystemUpdate(testState);

        //Start the threads
        Thread tServer = new Thread(testServer, "ServerThread");
        tServer.start();

        Thread tClient = new Thread(testClient,"ClientThread");
        tClient.start();

//        Send messages on every entered item, loop out on 'q' being sent
//        try
//        {
//            while(System.in.read() != 'q')
//            {
//                testServer.sendPredictionToServer(testPrediction);
//            }
//        }
//        catch(IOException i){}

        //Close the Threads
        testServer.killThread();
        testClient.killThread();

        System.out.println("Test Complete");
    }
}
