package com.atc.simulator.DebugDataFeed;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.google.protobuf.InvalidProtocolBufferException;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Created by Uros on 8/05/2016.
 */
public class DebugDataFeedServerThread implements Runnable, DataPlaybackListener
{
    static int OFFSET = 0;
    private DatagramSocket serverSocket;
    private byte[] receiveData;

    public DebugDataFeedServerThread()
    {
        try
        {
            serverSocket = new DatagramSocket(6969);
            receiveData = new byte[1024];
            System.out.println("Test Server Thread");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void run()
    {
        System.out.println("Server Thread Started");
        while (!Thread.interrupted())
        {
            DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
            try
            {
                serverSocket.receive(receivedPacket);
                if (Thread.interrupted())
                    break;
                String message = (new String( receivedPacket.getData(),OFFSET, receivedPacket.getLength() )).trim();
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * This method gets called when there is a system update, and gets
     * passed the new system state
     *
     * @param systemState the updated system state
     */
    @Override
    public void onSystemUpdate(SystemState systemState) {
        //TODO: send message to client when this is called by placing it in the buffer.
    }
}
