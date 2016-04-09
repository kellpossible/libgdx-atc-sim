package com.atc.simulator;

import com.badlogic.gdx.math.Vector3;

/**
 * latitude, longitude, altitude (above mean sea level)
 */
public class SphericalPosition extends SphericalCoordinate
{
    public SphericalPosition(double radius, double latitude, double longitude)
    {
        super(radius, latitude-Math.PI/2.0, longitude+Math.PI/2.0);
    }

    public double getRadius()
    {
        return this.x;
    }


    public double getLatitude()
    {
        return this.y+Math.PI/2.0;
    }

    public double getLongitude()
    {
        return this.z-Math.PI/2.0;
    }

    public double getAltitude()
    {
        return this.x - 6371000.0;
    }
}

