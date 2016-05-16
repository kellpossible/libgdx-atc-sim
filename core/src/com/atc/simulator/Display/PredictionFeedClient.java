package com.atc.simulator.Display;
import com.atc.simulator.PredictionService.PredictionFeedServe;
import com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage;

import java.net.Socket;
import java.io.*;
/**
 * PredictionFeedClient connects to and receives messages from a PredictionFeedServer, feeding the received messages onto its display
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    PredictionFeedClient()
 * // Methods
 *    run() - Thread of checking buffer and passing messages to server
 *
 * COLLABORATORS:
 *    java.util.ArrayList
 *    vectors.GeographicCoordinate
 *    PredictionFeedServe.PredictionMessage
 *
 * MODIFIED:
 * @version 0.1, CC 16/05/16
 * @author    Chris Coleman, 7191375
 */
public class PredictionFeedClient implements Runnable{
    private static int PORTNUMBER = 6969;
    private Socket serversSock;

    public PredictionFeedClient()
    {
        try{
            serversSock = new Socket("localhost", PORTNUMBER);
        }catch(IOException e){System.err.println("PredictionFeedClient Initialisation Failed");System.exit(1);}
    }

    public void run(){
        while(true)
        {
            try{
                PredictionMessage tempMes = PredictionFeedServe.PredictionMessage.parseDelimitedFrom(serversSock.getInputStream());
                //Temporary Message Handler
                System.out.print("Message Received: (");
                System.out.print(tempMes.getPositionFuture(0).getPositionData(0)+", "); //Print the current positions vectors
                System.out.print(tempMes.getPositionFuture(0).getPositionData(1)+", ");
                System.out.println(tempMes.getPositionFuture(0).getPositionData(2)+")");

            }catch(IOException e){System.err.println("PredictionFeedClient Message Parse Failed");System.exit(1);}

        }
    }
}
