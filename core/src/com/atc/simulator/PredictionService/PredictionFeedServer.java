package com.atc.simulator.PredictionService;

import com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage;
import com.atc.simulator.vectors.GeographicCoordinate;
import java.util.ArrayList;

/**
 * PredictionFeedServer is responsible for the encoding and sending of predictions for display/external systems. Version 0.2 will be capable of
 * threading the emptying of its buffer, with further versions sending messages to clients and, possibly, testing the information being passed.
 *
 *  Version 1 will be capable of connecting and sending to clients
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    PredictionFeedServer()
 * // Methods
 *    sendPredictionToServer(String aircraftID, GeographicCoordinate[] predictions)  - Encodes a Prediction and stores in internal buffer
 *    run() - Thread of checking buffer and removing first element
 *
 * COLLABORATORS:
 *    java.util.ArrayList
 *    vectors.GeographicCoordinate
 *    PredictionFeedServe.PredictionMessage
 *
 * MODIFIED:
 * @version 0.2, CC 18/05/16, Merged Encoder and Server
 * @author    Chris Coleman, 7191375
 */

public class PredictionFeedServer implements Runnable{
    private ArrayList<PredictionFeedServe.PredictionMessage> toBeSentBuffer; //Buffer of encoded messages

    /**
     * Constructor, instantiates a new buffer for storing of messages to be sent
     */
    public PredictionFeedServer()
    {
        toBeSentBuffer = new ArrayList<PredictionFeedServe.PredictionMessage>();
    }

    /**
     * Creates a PredictionMessage from the supplied information and adds it to the Server's internal buffer
     * @param aircraftID : The ID of the Aircraft being predicted
     * @param predictions: An Array of positions that make up this prediction
     */
    public synchronized void sendPredictionToServer(String aircraftID, GeographicCoordinate[] predictions)
    {
        PredictionMessage.Builder MesBuilder = PredictionMessage.newBuilder();
        PredictionMessage.Position.Builder tempPosBuilder;
        MesBuilder.setAircraftID(aircraftID);

        for(GeographicCoordinate temp : predictions)
        {
            tempPosBuilder = PredictionMessage.Position.newBuilder(); //New, fresh, builder
            tempPosBuilder.addPositionData(temp.getRadius());   //Add the location data
            tempPosBuilder.addPositionData(temp.getLatitude());
            tempPosBuilder.addPositionData(temp.getLongitude());

            MesBuilder.addPositionFuture(tempPosBuilder);   //Add this new position to the message
        }
        toBeSentBuffer.add(MesBuilder.build()); //Build message and add to buffer
    }

    /**
     * Version 1 Run. Will remove the first element if their is anything left in the buffer
     */
    public void run() {
        while(true){
            if(toBeSentBuffer.size() > 0)
            {
                toBeSentBuffer.remove(0);
                System.out.println("Left in Server Buffer: " + toBeSentBuffer.size());
            }
            try{Thread.sleep(50);}catch(InterruptedException i){}
        }
    }

}