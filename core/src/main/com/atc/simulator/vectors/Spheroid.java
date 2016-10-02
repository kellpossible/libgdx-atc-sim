package com.atc.simulator.vectors;

/**
 * A spheroid representation of the planet.
 * @author Luke Frisken
 */
public interface Spheroid {
    /**
     * Get the radius of the spheroid at the given position.
     * @param pos
     * @return
     */
    double getRadius(SphericalCoordinate pos);

    /**
     * Get the average radius of the spheroid.
     * @return
     */
    double getAverageRadius();

    /**
     * Get the arc distance (shortest distance) along the spheroid surface from one point
     * to another.
     *
     * It can also be thought of as the distance along the line of intersection of the plane
     * and the spheroid, where the plane is defined by the cross product of the cartesian
     * coordinates of this point and the other point.
     *
     * @param pos1 from this point
     * @param pos2 to this point
     * @return
     */
    double arcDistance(SphericalCoordinate pos1, SphericalCoordinate pos2);
}
