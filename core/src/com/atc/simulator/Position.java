package com.atc.simulator;

/**
 * Created by amiritis on 3/04/2016.
 */
public class Position
{
    private double fLatitude;
    private double fLongitude;

    public Position(double aLatitude, double aLongitude)
    {
        fLatitude = aLatitude;
        fLongitude = aLatitude;
    }

    public double getLatitude()
    {
        return fLatitude;
    }

    public double getLongitude()
    {
        return fLongitude;
    }
}

