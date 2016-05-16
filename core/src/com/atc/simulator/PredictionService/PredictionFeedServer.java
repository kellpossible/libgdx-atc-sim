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

public class PredictionFeedServer implements Runnable{
    private ArrayList<PredictionFeedServe.PredictionMessage> toBeSentBuffer; //Buffer of encoded messages

    //Sockets and Related
    private static int PORTNUMBER = 9000; //Can we store this in a config?
    private ServerSocket pDServer;
    private Socket singleClientSocket; //This is a single client for the single display version

    /**
     * Constructor, instantiates a new buffer and opens a ServerSocket
     */
    public PredictionFeedServer()
    {
        toBeSentBuffer = new ArrayList<PredictionFeedServe.PredictionMessage>();
        try{
            pDServer = new ServerSocket(PORTNUMBER);
        }catch(IOException e){System.err.println("PredictionFeedServer Initialisation Failed");e.printStackTrace();System.exit(1);}
    }

    /**
     * Version 1 thread. This will accept a single client and send it updated predictions whenever they are available
     */
    public void run()
    {
        //Receive/Accept request from, and connect to, the display Client.
        try{
            singleClientSocket = pDServer.accept();
        }catch(IOException e){System.err.println("PredictionFeedServer Client Connect Failed");System.exit(1);}

        //Now loop and send new predictions whenever they are placed in the Buffer
        while(true)
        {
            if (toBeSentBuffer.size() > 0)
            {
                try {
                    toBeSentBuffer.get(0).writeDelimitedTo(singleClientSocket.getOutputStream());
                }catch(IOException e){System.err.println("PredictionFeedServer Send to Client Failed");System.exit(1);}
            }
        }
    }

    /**
     * Stores a message in the buffer
     * @param mes : The PredicationMessage that is wanting to be sent
     */
    public void addNewMessage(PredictionMessage mes)
    {
        toBeSentBuffer.add(mes);
    }
}