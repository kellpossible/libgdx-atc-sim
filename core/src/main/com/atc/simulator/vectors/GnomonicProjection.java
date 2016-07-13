package com.atc.simulator.vectors;

import pythagoras.d.Plane;
import pythagoras.d.Ray3;
import pythagoras.d.Vector3;

/**
 * Created by luke on 13/07/16.
 * Represents a Gnomonic map projection
 * @author Luke Frisken
 */
public class GnomonicProjection extends Projection {
    private GeographicCoordinate projectionReference;
    private Vector3 northNormal;
    private Vector3 eastNormal;
    private Vector3 projectionPlanePosition;
    private static Vector3 zeroVector = new Vector3(0,0,0);
    private Plane projectionPlane;

    public GnomonicProjection(GeographicCoordinate projectionReference)
    {
        this.projectionReference = projectionReference;
        projectionPlanePosition = projectionReference.getCartesian();
        Vector3 projectionNormal = projectionPlanePosition.normalize();

        GeographicCoordinate projectionNorthAdjust = new GeographicCoordinate(projectionReference);
        projectionNorthAdjust.setLatitude(projectionReference.getLatitude()+0.001);
        Vector3 projectionNorthNormal = new SphericalCoordinate(projectionNorthAdjust).getCartesian().normalize();


        Ray3 projectionNorthNormalRay = new Ray3(zeroVector, projectionNorthNormal);

        projectionPlane = new Plane().fromPointNormal(projectionPlanePosition, projectionNormal.negate());

        Vector3 cartesianPlaneNorth = new Vector3();
        boolean found = projectionPlane.intersection(projectionNorthNormalRay, cartesianPlaneNorth);

        northNormal = cartesianPlaneNorth.subtract(projectionPlanePosition).normalize();
        eastNormal = northNormal.cross(projectionNormal).negate();
    }

    @Override
    public Vector3 transformTo(GeographicCoordinate geographicCoordinate) {
        Vector3 coordinateNormal = geographicCoordinate.getCartesian().normalize();
        Ray3 coordinateRay = new Ray3(zeroVector, coordinateNormal);

        //everthing before this could probably be moved into a "projection" class
        //because it only really needs to get executed once per projection.

        Vector3 cartesianCoordinateProjection = new Vector3();
        boolean found = projectionPlane.intersection(coordinateRay, cartesianCoordinateProjection);

        //the projection of the coordinate on the plane of projection.
        Vector3 gnomonicVector = cartesianCoordinateProjection.subtract(projectionPlanePosition);

        //vector projection to get the coordinates
        return new Vector3(
                gnomonicVector.dot(eastNormal),
                gnomonicVector.dot(northNormal),
                geographicCoordinate.getAltitude());
    }

    @Override
    public GeographicCoordinate transformFrom(Vector3 projectionCoordinate) {
        return null;
    }
}
