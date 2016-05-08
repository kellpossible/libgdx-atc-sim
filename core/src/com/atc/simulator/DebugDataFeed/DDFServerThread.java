package com.atc.simulator.DebugDataFeed;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe.Aircraft;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.google.protobuf.InvalidProtocolBufferException;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Created by Uros on 8/05/2016.
 */
public class DDFServerThread implements Runnable
{
    static int OFFSET = 0;
    private DatagramSocket serverSocket;
    private byte[] receiveData;

    public DDFServerThread()
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
}
