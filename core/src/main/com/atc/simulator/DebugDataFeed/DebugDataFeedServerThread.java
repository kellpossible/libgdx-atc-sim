package com.atc.simulator.DebugDataFeed;
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
 * Created by Uros on 8/05/2016.
 */
public class DebugDataFeedServerThread implements Runnable, DataPlaybackListener
{
    static int OFFSET = 0;
    static int PORT = 6989;

    private ArrayList<SystemStateMessage> toBeSentBuffer;

    private Thread serverThread; //Thread to accept connections by clients

    private ServerSocket serverSocket; //ServerSocket that handles connect requests by clients
    private Socket clientSocket; //The socket that a successful client connection can be sent message through
    private boolean continueThread = true;

    public DebugDataFeedServerThread()
    {
        toBeSentBuffer = new ArrayList<SystemStateMessage>();
        clientSocket = new Socket();
        try
        {
            serverSocket = new ServerSocket(PORT);
            serverThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        clientSocket = serverSocket.accept();
                    }
                    catch(IOException e)
                    {
                        System.out.println("PredictionFeed Server accept error");
                    }
                    System.out.println("Thread Killed (Connection)");
                }
            });
        }
        catch(IOException e)
        {
            System.out.println("PredictionFeed Server Creation error");
        }
        System.out.println("Server/ArrayList created");
    }

    public void run()
    {
        serverThread.start();
        try
        {
            while (continueThread)
            {
                if (toBeSentBuffer.size() > 0)
                {
                    if (!clientSocket.isConnected()) //If nothing is connected
                    {
                        toBeSentBuffer.remove(0); //Delete the data
                        System.out.println("No client, data deleted");
                    }
                    else
                    {
                        try
                        {
                            toBeSentBuffer.get(0).writeDelimitedTo(clientSocket.getOutputStream()); //Try to send message
                        }
                        catch (IOException e)
                        {
                            System.out.println("Send to Display failed");
                        }
                        toBeSentBuffer.remove(0);
                        System.out.println("Data sent to Client");
                    }
                }
                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException i)
                {
                }
            }
        }

        finally
        {
            try{clientSocket.close();}catch(IOException i){System.out.println("Can't close ServerSocket");}
            try{clientSocket.close();}catch(IOException i){System.out.println("Can't close clientSocket");}
        }
        System.out.println("Thread Killed (Server)");
    }


    /**
     * This method gets called when there is a system update, and gets
     * passed the new system state
     *
     * @param systemState the updated system state
     */
    @Override
    public void onSystemUpdate(SystemState systemState) {
        SystemStateMessage.Builder systemStateMessageBuilder = SystemStateMessage.newBuilder();

        systemStateMessageBuilder.setTime(ISO8601.fromCalendar(systemState.getTime()));

        for (AircraftState aircraftState : systemState.getAircraftStates())
        {
            AircraftStateMessage.Builder aircraftStateMessageBuilder = AircraftStateMessage.newBuilder();
            aircraftStateMessageBuilder.setAircraftID(aircraftState.getAircraftID());
            aircraftStateMessageBuilder.setTime(ISO8601.fromCalendar(aircraftState.getTime()));

            GeographicCoordinate position = aircraftState.getPosition();
            aircraftStateMessageBuilder.setPosition(
                    GeographicCoordinateMessage.newBuilder()
                            .setAltitude(position.getAltitude())
                            .setLatitude(position.getLatitude())
                            .setLongitude(position.getLongitude())
            );

            SphericalVelocity velocity = aircraftState.getVelocity();
            aircraftStateMessageBuilder.setVelocity(
                    SphericalVelocityMessage.newBuilder()
                            .setDr(velocity.getDR())
                            .setDtheta(velocity.getDTheta())
                            .setDphi(velocity.getDPhi())
            );

            aircraftStateMessageBuilder.setHeading(aircraftState.getHeading());

            systemStateMessageBuilder.addAircraftState(aircraftStateMessageBuilder);
        }

        SystemStateMessage systemStateMessage = systemStateMessageBuilder.build(); //yay the final message all built

        toBeSentBuffer.add(systemStateMessage);

        //TODO: send message to client when this is called by placing it in the buffer.
    }
    public void killThread()
    {
        continueThread = false;
    }
}
