package com.atc.simulator.PredictionService;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe.*;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.google.protobuf.InvalidProtocolBufferException;
import java.net.*;
import java.util.*;
import java.io.*;


/**
 * Created by urke on 8/05/2016.
 */
public class DebugDataFeedClient implements Runnable
{
    private static int PORT = 6989;
    private Socket serversSock;
    private boolean continueThread = true;
    private Thread thread;
    private static String threadName = "DebugDataFeedClient";

    public DebugDataFeedClient()
    {
        try
        {
            serversSock = new Socket("localhost", PORT);
        }
        catch(IOException e)
        {
            System.err.println("DebugDataFeedClient Initialisation Failed");
            System.exit(1);
        }
    }

    public void run(){
        while(continueThread)
        {
            try
            {
                SystemStateMessage tempMessage = DebugDataFeedServe.SystemStateMessage.parseDelimitedFrom(serversSock.getInputStream());
                System.out.println("Message Received at Client");
            }
            catch(IOException e)
            {
                System.err.println("PredictionFeedClient Message Parse Failed");
                System.exit(1);
            }
        }
        try
        {
            serversSock.close();
        }
        catch(IOException i)
        {
            System.out.println("Can't close display socket");
        }
    }

    /**
     * Small method called too kill the server's threads when the have run through
     */
    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
    public void killThread()
    {
        continueThread = false;
    }
}
