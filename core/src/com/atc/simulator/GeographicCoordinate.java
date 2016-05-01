package com.atc.simulator;

/**
 * Represents a geographic position using latitude, longitude, altitude (above mean sea level)
 * See https://en.wikipedia.org/wiki/Geographic_coordinate_system
 */
public class GeographicCoordinate extends SphericalCoordinate
{
    /**
     * Create a new spherical position
     * @param radius
     * @param latitude
     * @param longitude
     */
    public GeographicCoordinate(double radius, double latitude, double longitude)
    {
        super(radius, latitude - Math.PI/2.0, longitude + Math.PI/2.0);
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
        return this.x - 6371000.0;
    }
}

