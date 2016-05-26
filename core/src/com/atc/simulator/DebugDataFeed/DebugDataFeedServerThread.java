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

        //TODO: send message to client when this is called by placing it in the buffer.
    }
}
