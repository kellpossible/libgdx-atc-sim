package com.atc.simulator.vectors;

import com.atc.simulator.vectors.SphericalCoordinate;
import pythagoras.d.Vector3;

/**
 * Represents a geographic position using latitude, longitude, altitude (above mean sea level)
 * See https://en.wikipedia.org/wiki/Geographic_coordinate_system
 */
public class GeographicCoordinate extends SphericalCoordinate
{
    public static final double EARTH_CIRCUMFRANCE = 6371000.0;
    public GeographicCoordinate(Vector3 other)
    {
        super(other);
    }

    /**
     * Copy Constructor
     * @param other
     */
    public GeographicCoordinate(GeographicCoordinate other)
    {
        super(other);
    }

    /**
     * Copy Constructor
     * @param other
     */
    public GeographicCoordinate(SphericalCoordinate other)
    {
        super(other);
    }

    /**
     * Create a new spherical position
     * @param altitude
     * @param latitude
     * @param longitude
     */
    public GeographicCoordinate(double altitude, double latitude, double longitude)
    {
        super(altitude+EARTH_CIRCUMFRANCE, latitude - Math.PI/2.0, longitude + Math.PI/2.0);
    }

    /**
     * Get the Radius.
     * @return
     */
    public double getRadius()
    {
        return this.x;
    }


    /**
     * Get the Latitude.
     * @return
     */
    public double getLatitude()
    {
        return this.y + Math.PI/2.0;
    }

    /**
     * Get the Longitude.
     * @return
     */
    public double getLongitude()
    {
        return this.z - Math.PI/2.0;
    }

    /**
     * Assuming MSL/Circumference of the earth is 6371000m
     * @return
     */
    public double getAltitude()
    {
        return this.x - EARTH_CIRCUMFRANCE;
    }

    @Override
    public String toString()
    {
        return "[" + getAltitude() + ", " + Math.toDegrees(getLatitude()) + ", " + Math.toDegrees(getLongitude()) + "]";
    }
}

