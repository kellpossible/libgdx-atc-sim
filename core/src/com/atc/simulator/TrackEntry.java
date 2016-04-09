package com.atc.simulator;

/**
 * Created by amiritis on 9/04/2016.
 */
public class TrackEntry
{
    private Position fPosition;
    private double fTime;
    private double fAltitude;
    private double fHeading;
    private double fRoll;
    private double fPitch;


    public TrackEntry(double aTime, Position aPosition, double aAltitude, double aHeading, double aRoll, double aPitch)
    {
        fTime = aTime;
        fPosition = aPosition;
        fAltitude = aAltitude;
        fHeading = aHeading;
        fRoll = aRoll;
        fPitch = aPitch;
    }

}
