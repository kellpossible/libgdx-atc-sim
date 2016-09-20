package com.atc.simulator.vectors;

import pythagoras.d.Vector3;

/**
 * Represents a geographic position using latitude, longitude, altitude (above mean sea level)
 * See https://en.wikipedia.org/wiki/Geographic_coordinate_system
 *
 * @author Luke Frisken
 * @modified Chris Coleman 10/9/16
 */
public class GeographicCoordinate extends SphericalCoordinate
{
    public static final double EARTH_MSL_RADIUS = 6371000.0;

    /**
     * Create a new GeographicCoordinate from latitude and longitude expres0sed in degrees
     * @param altitude
     * @param latitude
     * @param longitude
     * @return
     */
    public static GeographicCoordinate fromDegrees(double altitude, double latitude, double longitude)
    {
        return new GeographicCoordinate(Math.toRadians(altitude), Math.toRadians(latitude), Math.toRadians(longitude));
    }

    /**
     * Transform a 3d cartesian vector into a GeographicCoordinate
     * @param cv
     * @return
     */
    public static GeographicCoordinate fromCartesian(Vector3 cv)
    {
        return new GeographicCoordinate(SphericalCoordinate.fromCartesian(cv));
    }

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
     * @param altitude in meters
     * @param latitude in radians
     * @param longitude in radians
     */
    public GeographicCoordinate(double altitude, double latitude, double longitude)
    {
        super(altitude+ EARTH_MSL_RADIUS, longitude + Math.PI, latitude + Math.PI/2.0);
    }

    /**
     * Get the Radius in meters.
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
        return this.z - Math.PI/2.0;
    }

    /**
     * Set the Latitude
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.z = latitude + Math.PI/2.0;
    }

    /**
     * Get the Longitude.
     * @return
     */
    public double getLongitude()
    {
        return this.y - Math.PI;
    }

    /**
     * Set the Longitude
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.y = longitude + Math.PI;
    }

    /**
     * Assuming MSL/Circumference of the earth is 6371000m
     * @return altitude in meters
     */
    public double getAltitude()
    {
        return this.x - EARTH_MSL_RADIUS;
    }

    /**
     * Set the altitude
     * @param altitude
     */
    public void setAltitude(double altitude)
    {
        this.x = EARTH_MSL_RADIUS + altitude;
    }

    /**
     * REQUIRES TESTING
     * Arc Distance between two Geographic Coordinates.
     *  https://en.wikipedia.org/wiki/Great-circle_distance
     *
     * @param other The GeographicCoordinate we are comparing this Coord to
     * @return double The distance in Metres (or whatever your altitude was written in)
     */
    public double arcDistance(GeographicCoordinate other)
    {
        return this.x * Math.acos(Math.sin(Math.toRadians(this.getLatitude()))*Math.sin(Math.toRadians(other.getLatitude()))
                + Math.cos(Math.toRadians(this.getLatitude()))*Math.cos(Math.toRadians(other.getLatitude()))*Math.cos(Math.toRadians(this.getLongitude() - other.getLongitude())));
    }

    /**
     * Linear interpolation between two Geographic coordinates. Going from this Coord to the supplied coordinate
     *
     * @param toCoord   The coordinate we are interpolating towards
     * @param interpolant   The interpolant, ratio between the two coordinates
     * @return
     */
    public GeographicCoordinate linearIntepolate(GeographicCoordinate toCoord, float interpolant)
    {
        double newLat = this.getLatitude() + Math.abs(this.getLatitude() - toCoord.getLatitude()) * interpolant;
        double newLong = this.getLongitude() + Math.abs(this.getLongitude() - toCoord.getLongitude()) * interpolant;
        double newAlt = this.getAltitude() + Math.abs(this.getAltitude() - toCoord.getAltitude()) * interpolant;
        return new GeographicCoordinate(newLat,newLong,newAlt);
    }


    @Override
    public String toString()
    {
        return "[" + getAltitude() + ", " + Math.toDegrees(getLatitude()) + ", " + Math.toDegrees(getLongitude()) + "] (degrees)";
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
     * <a href="http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates">Reference</a>
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

