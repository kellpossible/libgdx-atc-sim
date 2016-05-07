package com.atc.simulator.PredictionService;

import com.atc.simulator.PredictionService.PredictionFeedServe.PredictionMessage;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by Chris on 7/05/2016.
 *
 *  This is a test/familiarisation class for PredictionFeedServe's protocol buffer
 *  The buffer is called a PredictionMessage and contains: (valid 07/05/16)
 *      - String : AircraftID
 *      - Position[] : positionFuture (contains all the predicted future positions w/ (0) next point, (1) 2nd pos, etc)
 *      - Position[] : positionPast     (contains the past w/ (0) being currentPos, (1) most recent, etc)
 *      -other optional data that can be altered later
 *
 *      The Position datatype contains three doubles, similar to the DebugDataFeedServe's protocol
 *
 */
public class PredicitionFeedTest {
    //Test method to be comfortable with using PredictionFeedServe's protocol buffer

    //Stealing this from Luke's DebugDataFeed:
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static void main(String[] arg)
    {
        //example data
        String planeID = "ABC123"; //ID string
        //Positions, gets confusing having to build temporary Positions (radius/lat/long) and then build those
        //  into the overarching message (I'm sure this can be simplified at some stage)
        GeographicCoordinate tempPos = new GeographicCoordinate(0,0,0);
        PredictionMessage.Position.Builder tempPosBuilder = PredictionMessage.Position.newBuilder();
            tempPosBuilder.addPositionData(tempPos.getRadius());
            tempPosBuilder.addPositionData(tempPos.getLatitude());
            tempPosBuilder.addPositionData(tempPos.getLongitude());
        PredictionMessage.Position positionOld = tempPosBuilder.build();

        tempPos = new GeographicCoordinate(1,2,3);
        tempPosBuilder = PredictionMessage.Position.newBuilder();
            tempPosBuilder.addPositionData(tempPos.getRadius());
            tempPosBuilder.addPositionData(tempPos.getLatitude());
            tempPosBuilder.addPositionData(tempPos.getLongitude());
        PredictionMessage.Position positionCur = tempPosBuilder.build();

        tempPos = new GeographicCoordinate(2,3,4);
        tempPosBuilder = PredictionMessage.Position.newBuilder();
            tempPosBuilder.addPositionData(tempPos.getRadius());
            tempPosBuilder.addPositionData(tempPos.getLatitude());
            tempPosBuilder.addPositionData(tempPos.getLongitude());
        PredictionMessage.Position positionFut = tempPosBuilder.build();

        //Create the message
        PredictionMessage.Builder mesBuilder = PredictionMessage.newBuilder();
        //Add in all the data
        mesBuilder.setAircraftID(planeID);
        mesBuilder.addPositionPast(0, positionCur);  //You can either add the Index,Value
        mesBuilder.addPositionPast(positionOld);     //Just the value to the next array location
        mesBuilder.addPositionFuture(tempPosBuilder);//Or the builder, and it builds for you (might be easier option later)
        //Then we Build the message
        PredictionMessage messageToSend = mesBuilder.build();
        System.out.println("Message has been created:");
        System.out.println(messageToSend.toString());

        System.out.println("And as Hex String:");
        System.out.println(bytesToHex(messageToSend.toByteArray()));
        System.out.println("Easy to read!\n");

        System.out.println("Decoding the Message");
        try {
            PredictionMessage messageToRec;
            messageToRec = PredictionMessage.parseFrom(messageToSend.toByteArray());
            System.out.println("Plane ID: " + messageToRec.getAircraftID()); //Print ID
            System.out.print("Currently at: (");
                System.out.print(messageToRec.getPositionPast(0).getPositionData(0)+", "); //Print the current positions vectors
                System.out.print(messageToRec.getPositionPast(0).getPositionData(1)+", ");
                System.out.println(messageToRec.getPositionPast(0).getPositionData(2)+")");

            System.out.println("Come from:");
            for(PredictionMessage.Position temp : messageToRec.getPositionPastList())
            {
                System.out.println("("+temp.getPositionData(0)+", "+temp.getPositionData(1)+", "+temp.getPositionData(2)+")");
            }

            System.out.println("Going to:");
            for(PredictionMessage.Position temp : messageToRec.getPositionFutureList())
            {
                System.out.println("("+temp.getPositionData(0)+", "+temp.getPositionData(1)+", "+temp.getPositionData(2)+")");
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
