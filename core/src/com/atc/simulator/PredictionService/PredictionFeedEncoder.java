package com.atc.simulator.PredictionService;
import com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage;
import com.atc.simulator.PredictionService.PredictionFeedServer;
import com.atc.simulator.vectors.GeographicCoordinate;
import java.util.ArrayList;

/**
 * PredictionFeedEncoder receives an aircraftID and a number of locations from the PredictionEngine.
 * This data is built into a PredictionFeedServe Protocol Buffer, and sent to the Server to be transmitted.
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    PredictionFeedEncoder(PredictionFeedServer serv)
 * // Methods
 *    addNewPrediction(String aircraftID, GeographicCoordinate[] predictions)  - Encodes a Prediction and stores in internal buffer
 *    run() - Thread of checking buffer and passing messages to server
 *
 * COLLABORATORS:
 *    java.util.ArrayList
 *    com.atc.simulator.vectors.GeographicCoordinate
 *    com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage
 *    com.atc.simulator.PredictionService.PredictionFeedServer
 *
 * MODIFIED:
 * @version 0.1, CC 18/05/16
 * @author    Chris Coleman, 7191375
 */

public class PredictionFeedEncoder implements Runnable{
    private ArrayList<PredictionFeedServe.PredictionMessage> toBeSentBuffer; //Buffer of encoded messages
    private PredictionFeedServer myServer; //Server this encoder is passing messages to

    /**
     * Constructor that is given a connecting server and instantiates a new buffer
     * @param server : The server this encoder has to hand messages to
     */
    public PredictionFeedEncoder(PredictionFeedServer server)
    {
        toBeSentBuffer = new ArrayList<PredictionFeedServe.PredictionMessage>();
        myServer = server;
        System.out.println("Encoder Created");
    }

    /**
     * Thread to remove top element of buffer and sends it to the PredictionFeedServer
     */
    public void run() {
        while(true) {
            if (toBeSentBuffer.size() > 0) {
                myServer.addNewMessage(toBeSentBuffer.get(0));
                toBeSentBuffer.remove(0);
            }
            try{Thread.sleep(50);}catch(InterruptedException i){}
        }
    }

    /**
     * Encodes a new Prediction message and adds it to the buffer
     * @param aircraftID : The aircraft this prediction is meant for
     * @param predictions : Array of Coordinates in prediction
     */
    public synchronized void addNewPrediction(String aircraftID, GeographicCoordinate[] predictions)
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
}