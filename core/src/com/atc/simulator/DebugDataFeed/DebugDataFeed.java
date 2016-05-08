package com.atc.simulator.DebugDataFeed;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe.Aircraft;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.net.*;

/**
 * Created by luke on 2/05/16.
 *
 * 8/05/2016 Notes: May need pub-sub pattern to work properly.
 *                  Can store received data (from ADS-B, CAT62 etc
 *                  In String List in Server. DDFServerThread and
 *                  DDFClient Thread may will update the list asynchronously
 *                  Information that gets sent out from DDFClient will be deleted from the list
 */
public class DebugDataFeed
{
    static int PORT = 6969;

    //byte array to hex solution here: http://stackoverflow.com/a/9855338/446250
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public DebugDataFeed()
    {

    }

    public void run()
    {
        Thread serverThread = new Thread(new DDFServerThread());
        serverThread.start();
        //example data
        GeographicCoordinate position = new GeographicCoordinate(1,2,3);
        float aircraft_speed = 10f;

        System.out.println("Encoding the message");

        //comment
        //create the builder
        Aircraft.Builder aircraftBuilder = Aircraft.newBuilder();


        //set the values
        aircraftBuilder.addPosition(position.getRadius());
        aircraftBuilder.addPosition(position.getLatitude());
        aircraftBuilder.addPosition(position.getLongitude());
        aircraftBuilder.setSpeed(aircraft_speed);

        Aircraft aircraftSendProtobuf = aircraftBuilder.build();

        System.out.println("toString()");
        System.out.println(aircraftSendProtobuf.toString());

        //convert to byte array
        System.out.println("toByteArray()");
        System.out.println(bytesToHex(aircraftSendProtobuf.toByteArray()));

        //tutorial encourages the use of:
        //aircraftSendProtobuf.writeTo();
        //which can probably be used with sockets.

        System.out.println("\nDecoding the Message");


        try {
            Aircraft aircraftReceiveProtobuf;
            aircraftReceiveProtobuf = Aircraft.parseFrom(aircraftSendProtobuf.toByteArray());
            System.out.println("Position Count: " + aircraftReceiveProtobuf.getPositionCount());
            System.out.println("Radius: " + aircraftReceiveProtobuf.getPosition(0));
            System.out.println("Latitude: " + aircraftReceiveProtobuf.getPosition(1));
            System.out.println("Longitude: " + aircraftReceiveProtobuf.getPosition(2));
            System.out.println("Speed: " + aircraftReceiveProtobuf.getSpeed());

        }
        catch (InvalidProtocolBufferException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] arg)
    {
        try
        {
            (new DebugDataFeed()).run();
        }
        catch (Exception e)
        {
            System.out.println( "Cannot run server." );
            System.out.println( e.getMessage() );
        }
    }
}
