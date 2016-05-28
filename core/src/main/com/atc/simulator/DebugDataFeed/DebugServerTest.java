package com.atc.simulator.DebugDataFeed;

import com.atc.simulator.Display.PredictionFeedClient;
import com.atc.simulator.PredictionService.DebugDataFeedClient;
import com.atc.simulator.PredictionService.PredictionFeedServer;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by urke on 28/05/2016.
 */
public class DebugServerTest
{
//Test method to be comfortable with using PredictionFeedServe's protocol buffer
    public static void main(String[] arg) {

    //Create some Test data
    Prediction testPrediction = new Prediction();
    testPrediction.addState(new AircraftState("TestState", Calendar.getInstance(), new GeographicCoordinate(0,0,0), new SphericalVelocity(0,0,0), 0));

    //Create Server/Client objects
    DebugDataFeedServerThread testServer = new DebugDataFeedServerThread();
    DebugDataFeedClient testClient = new DebugDataFeedClient();
        
    //Fill up the buffer a little bit
    for(int i = 0; i<10; i++)
        testServer.sendPredictionToServer(testPrediction);
    //Start the threads
    Thread tServer = new Thread(testServer, "ServerThread");
    tServer.start();
    Thread tClient = new Thread(testClient,"ClientThread");
    tClient.start();
    //Send messages on every entered item, loop out on 'q' being sent
    try{while(System.in.read() != 'q'){testServer.sendPredictionToServer(testPrediction);}}catch(IOException i){}

    //Close the Threads
    testServer.killThread();
    testClient.killThread();

    System.out.println("Test Complete");
}

    }
}
