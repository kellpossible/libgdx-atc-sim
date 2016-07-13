package com.atc.simulator.vectors;

import pythagoras.d.Plane;
import pythagoras.d.Ray3;
import pythagoras.d.Vector3;

/**
 * Created by luke on 7/07/16.
 *
 * Represents a coordinate in the gnomonic projection
 * https://en.wikipedia.org/wiki/Gnomonic_projection
 *
 * @author Luke Frisken
 */
public class GnomonicCoordinate extends Vector3 {
    private GeographicCoordinate projectionReference;



    /**
     * Convert a GeographicCoordinate into a GnomonicCoordinate based on its projection reference
     * @param geographicCoordinate the coordinate we are converting
     * @param projectionReference the reference point of the gnomonic coordinate system
     */
    public GnomonicCoordinate(GeographicCoordinate geographicCoordinate, GeographicCoordinate projectionReference)
    {
        this.projectionReference = projectionReference;

        Vector3 projectionPlanePosition =projectionReference.getCartesian();
        Vector3 projectionNormal = projectionPlanePosition.normalize();
        Vector3 projectionNorthNormal = new SphericalCoordinate(projectionReference.add(0, 0, 0.001)).getCartesian().normalize();
        Vector3 zeroVector = new Vector3(0,0,0);

        Ray3 projectionNorthNormalRay = new Ray3(zeroVector, projectionNorthNormal);
        Ray3 projectionNormalRay = new Ray3(zeroVector, projectionNormal);

        double projectionD = GeographicCoordinate.EARTH_MSL_RADIUS;

        Vector3 projectionOrigin = projectionNormal.mult(projectionD);

        Plane projectionPlane = new Plane().fromPointNormal(projectionPlanePosition, projectionNormal.negate());

        Vector3 cartesianPlaneOrigin = new Vector3(); //this isn't required, we already have this in projectionPlanePosition
        boolean found = projectionPlane.intersection(projectionNormalRay, cartesianPlaneOrigin);

        Vector3 cartesianPlaneNorth = new Vector3();
        found = projectionPlane.intersection(projectionNorthNormalRay, cartesianPlaneNorth);

        Vector3 northNormal = cartesianPlaneOrigin.subtract(cartesianPlaneNorth).normalize();

        //not sure if this is east or west (depends on cross product direction)
        Vector3 eastNormal = northNormal.cross(projectionNormal);

        Vector3 coordinateNormal = geographicCoordinate.getCartesian().normalize();
        Ray3 coordinateRay = new Ray3(zeroVector, coordinateNormal);

        //everthing before this could probably be moved into a "projection" class
        //because it only really needs to get executed once per projection.

        Vector3 cartesianCoordinateProjection = new Vector3();
        found = projectionPlane.intersection(coordinateRay, cartesianCoordinateProjection);

        //the projection of the coordinate on the plane of projection.
        Vector3 gnomonicVector = cartesianCoordinateProjection.subtract(projectionOrigin); //don't use projection origin, just use the plane position

        //vector projection to get the coordinates
        this.x = gnomonicVector.dot(eastNormal);
        this.y = -gnomonicVector.dot(northNormal);
        this.z = geographicCoordinate.getAltitude();
    }

    /**
     * Method getProjectionReference returns the projectionReference of this GnomonicCoordinate object.
     *
     * @return the projectionReference (type GeographicCoordinate) of this GnomonicCoordinate object.
     */
    public GeographicCoordinate getProjectionReference() {
        return projectionReference;
    }
}
