package com.atc.simulator.PredictionService;
import com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage;

import java.net.ServerSocket;
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
 * @version 0.1, CC 12/05/16
 * @author    Chris Coleman, 7191375
 */

public class PredictionFeedServer implements Runnable{
    private ArrayList<PredictionFeedServe.PredictionMessage> toBeSentBuffer; //Buffer of encoded messages
    private static int PORTNUMBER = 6971; //Can we store this in a config?
    private ServerSocket pDServer;



    /**
     * Constructor, instantiates a new buffer and opens a socket
     */
    public PredictionFeedServer()
    {
        toBeSentBuffer = new ArrayList<PredictionFeedServe.PredictionMessage>();
        try{
            pDServer = new ServerSocket(PORTNUMBER);
        }catch(IOException e)
            {System.err.println("PredictionFeedServer Failed");
            System.exit(1);}
    }

    /**
     * Thread to remove top element of buffer and sends it to the PredictionFeedServer
     */
    public void run()
    {
        if (toBeSentBuffer.size() > 0)
        {

        }
    }


    /**
     * Stores a message in the buffer
     * @param mes : The PredicationMessage that is wanting to be sent
     */
    public void addNewMessage(PredictionMessage mes)
    {
        toBeSentBuffer.add(mes); //Add
    }
}