package com.atc.simulator.PredictionService;

import com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage;
import com.atc.simulator.Display.PredictionFeedClient;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;

/**
 * Created by Chris on 7/05/2016.
 *
 *  This is a test/familiarisation class for PredictionFeedServe's protocol buffer
 *  The buffer is called a PredictionMessage and contains: (valid 09/05/16)
 *      - String : AircraftID
 *      - Position[] : positionFuture (contains all the predicted future positions w/ (0)current, (1)first prediction, etc)
 *      -other optional data that can be altered later
 *
 *      The Position datatype contains three doubles, similar to the DebugDataFeedServe's protocol
 *
 */
public class PredicitionFeedTest {
    //Test method to be comfortable with using PredictionFeedServe's protocol buffer


    public static void main(String[] arg) {
        PredictionFeedServer testServer = new PredictionFeedServer();
        PredictionFeedEncoder testEncoder = new PredictionFeedEncoder(testServer);

        //If you comment out these 3 lines of code, nothing will run..?
        System.out.println("Adding message before Thread start");
        GeographicCoordinate tempPos[] = {new GeographicCoordinate(0, 0, 0)};
        testEncoder.addNewPrediction("TestPos", tempPos);

        //Thread tServer = new Thread(testServer);
        Thread tEncoder = new Thread(testEncoder);

        //tServer.start();
        tEncoder.start();
/*
        System.out.println("Adding message after Thread start");
        GeographicCoordinate tempPos2[] = {new GeographicCoordinate(1, 2, 3)};
        testEncoder.addNewPrediction("TestPos2", tempPos2);
*/
        for(int i = 0; i < 10; i++)
        {
            GeographicCoordinate tempPos3[] = {new GeographicCoordinate(0, 0, 0)};
            testEncoder.addNewPrediction("TestPos", tempPos3);
        }
    }
}

