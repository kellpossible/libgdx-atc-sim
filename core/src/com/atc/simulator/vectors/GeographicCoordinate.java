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

    /**
     * Obtain the bearing from this coordinate to another coordinate
     *
     * TODO: implement an alternative method in order to cross check the values coming out of here
     * TODO: Pretty sure this is broken!!
     *
     * Uses formula from http://mathforum.org/library/drmath/view/55417.html
     * @param other the other coordinate, that this bearing points to
     * @return
     */
    public double bearingTo1(GeographicCoordinate other)
    {
        double lat1 = this.getLatitude();
        double lat2 = other.getLatitude();
        double lon1 = this.getLongitude();
        double lon2 = other.getLongitude();
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double y = Math.sin(lon2-lon1) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
        double bearing = Double.NaN;

        if (y > 0.0) {
            if (x > 0.0) {
                bearing = Math.atan(x/y);
            }
            if (x < 0.0) {
                bearing = Math.PI - Math.atan(-y/x);
            }
            if (x == 0.0)
            {
                bearing = Math.PI/2.0;
            }
        } else if (y < 0.0) {
            if (x > 0.0) {
                bearing = -Math.atan(-y/x);
            }
            if (x < 0.0) {
                bearing = Math.atan(y/x) - Math.PI;
            }
            if (x == 0.0)
            {
                bearing = Math.PI + Math.PI/2.0;
            }
        } else if (y == 0.0) {
            if (x > 0.0) {
                bearing = 0.0;
            }
            if (x < 0.0) {
                bearing = Math.PI;
            }
            if (x == 0.0)
            {
                bearing = Double.NaN; //the two points are the same
            }
        }

        return Math.PI/2.0 - bearing; //seems to fix it??
    }

    /**
     * This one actually seems to work a lot better
     * @param other
     * @return
     */
    public double bearingTo(GeographicCoordinate other)
    {
        double lat1 = this.getLatitude();
        double lat2 = other.getLatitude();
        double lon1 = this.getLongitude();
        double lon2 = other.getLongitude();

        double y = Math.sin(lon2-lon1) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
        double bearing = Math.atan2(x, y);

        return Math.PI/2.0 - bearing; //seems to fix it?
    }

    /**
     * My own crappy attempt
     * @param other
     * @return
     */
    public double bearingTo3(GeographicCoordinate other)
    {
        SphericalVelocity velocity = new SphericalVelocity(other.subtract(this));

        return Math.PI/2.0 - Math.atan(velocity.getDTheta()/velocity.getDPhi());
    }

    /**
     * http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates
     * @param other
     * @return
     */
    public double bearingTo4(GeographicCoordinate other) {
        double lat1 = this.getLatitude();
        double lat2 = other.getLatitude();
        double lon1 = this.getLongitude();
        double lon2 = other.getLongitude();

        double dLong = lon2 - lon1;
        double dPhi = Math.log(Math.tan(lat2/2.0 + Math.PI/4.0)/Math.tan(lat1/2.0 + Math.PI/4.0));
        if (Math.abs(dLong) > Math.PI)
        {
            if (dLong > 0.0)
            {
                dLong = -(2.0 * Math.PI - dLong);
            } else {
                dLong = (2.0 * Math.PI + dLong);
            }
        }

        return Math.atan2(dLong, dPhi);
    }

}

