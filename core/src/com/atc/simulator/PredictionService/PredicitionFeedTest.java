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
 * MODIFIED:
 * @version 0.3, CC 18/05/16
 * @author    Chris Coleman, 7191375
 */
public class PredicitionFeedTest {
    //Test method to be comfortable with using PredictionFeedServe's protocol buffer


    public static void main(String[] arg) {
        PredicitionFeedTest p = new PredicitionFeedTest();
        GeographicCoordinate tempPos2[] = {new GeographicCoordinate(1, 2, 3)};

        PredictionFeedServer testServer = new PredictionFeedServer();

        for(int i = 0; i<10; i++)
            p.sendPred(testServer);

        Thread tServer = new Thread(testServer);
        tServer.start();

        try{while(System.in.read() != 'q'){p.sendPred(testServer);}}catch(IOException i){}

    }

    private synchronized void sendPred(PredictionFeedServer enc)
    {
        GeographicCoordinate tempPos[] = {new GeographicCoordinate(0, 0, 0)};
        enc.sendPredictionToServer("TestPos", tempPos);
    }
}

