package com.atc.simulator.Display;
import com.atc.simulator.PredictionService.PredictionFeedServe;
import com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage;

import java.net.Socket;
import java.io.*;
/**
 * PredictionFeedClient connects to and receives messages from a PredictionFeedServer, feeding the received messages onto its display
 * These messages are passed as PredictionFeedServe Protocol Buffers
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    PredictionFeedClient()
 * // Methods
 *    run() - Thread of checking buffer and passing messages to server
 *    killThread() - Clears the flag that the client thread runs off, letting the thread finish gracefully
 *
 * COLLABORATORS:
 *    java.util.ArrayList
 *    vectors.GeographicCoordinate
 *    PredictionFeedServe.PredictionMessage
 *
 * MODIFIED:
 * @version 0.2, CC 23/05/16
 * @author    Chris Coleman, 7191375
 */
public class PredictionFeedClient implements Runnable{
    private static int PORTNUMBER = 6789;
    private Socket serversSock;
    private boolean continueThread = true;

    public PredictionFeedClient()
    {
        try{
            serversSock = new Socket("localhost", PORTNUMBER);
        }catch(IOException e){System.err.println("PredictionFeedClient Initialisation Failed");System.exit(1);}
    }

    public void run(){
        while(continueThread)
        {
            try{
                PredictionMessage tempMes = PredictionFeedServe.PredictionMessage.parseDelimitedFrom(serversSock.getInputStream());
                System.out.println("Message Received at Client");
            }catch(IOException e){System.err.println("PredictionFeedClient Message Parse Failed");System.exit(1);}
        }
        try{serversSock.close();}catch(IOException i){System.out.println("Can't close display socket");}
    }

    /**
     * Small method called too kill the server's threads when the have run through
     */
    public void killThread()
    {
        continueThread = false;
    }
}
