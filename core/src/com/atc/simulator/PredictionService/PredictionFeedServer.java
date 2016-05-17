package com.atc.simulator.PredictionService;
import com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.*;
/**
 * PredictionFeedServer holds onto messages that need to be sent to Displays and sends them when able
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    PredictionFeedServer()
 * // Methods
 *    addNewPrediction(String aircraftID, GeographicCoordinate[] predictions)  - Encodes a Prediction and stores in internal buffer
 *    run() - Thread of checking buffer and passing messages to server
 *
 * COLLABORATORS:
 *    java.util.ArrayList
 *    vectors.GeographicCoordinate
 *    PredictionFeedServe.PredictionMessage
 *
 * MODIFIED:
 * @version 0.2, CC 16/05/16
 * @author    Chris Coleman, 7191375
 */

public class PredictionFeedServer{
    private ArrayList<PredictionFeedServe.PredictionMessage> toBeSentBuffer; //Buffer of encoded messages


    /**
     * Constructor, instantiates a new buffer and opens a ServerSocket
     */
    public PredictionFeedServer()
    {
        toBeSentBuffer = new ArrayList<PredictionFeedServe.PredictionMessage>();
    }

    /**
     * Stores a message in the buffer
     * @param mes : The PredicationMessage that is wanting to be sent
     */
    public synchronized void addNewMessage(PredictionMessage mes)
    {
        toBeSentBuffer.add(mes);
        System.out.println("Item added to Server. Size " + toBeSentBuffer.size());
    }

    /**
     * Version 1 Run. Will empty the buffer if it finds anything in there
     */
 /*   public void run() {
        while(true){
            if(toBeSentBuffer.size() > 0)
            {
                toBeSentBuffer.remove(0);
                System.out.println("Left in Server Buffer: " + toBeSentBuffer.size());
            }
        }
    }
*/
}