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


    /**
     * Constructor for GnomonicProjection
     * @param projectionReference
     */
    public GnomonicProjection(GeographicCoordinate projectionReference)
    {
        this.projectionReference = projectionReference;

        //get the position of the plane in cartesian space
        projectionPlanePosition = projectionReference.getCartesian();

        //get the normal of the projection (from the centre of the earth to the projection reference)
        Vector3 projectionNormal = projectionPlanePosition.normalize();

        //create a coordinate which is just a tiny bit north of the reference
        GeographicCoordinate projectionNorthAdjust = new GeographicCoordinate(projectionReference);
        projectionNorthAdjust.setLatitude(projectionReference.getLatitude()+0.001); //TODO: this may need alternate behaviour close to the poles
        //get the normal of the projection of this adjusted north position.
        Vector3 projectionNorthNormal = new SphericalCoordinate(projectionNorthAdjust).getCartesian().normalize();

        //create a ray from this normal
        Ray3 projectionNorthNormalRay = new Ray3(zeroVector, projectionNorthNormal);

        //create the projection plane. ensure that the plane normal is facing towards the earth
        //so that intersections will work.
        projectionPlane = new Plane().fromPointNormal(projectionPlanePosition, projectionNormal.negate());

        //do an intersection with the plane and the projected adjusted north coordinate.
        Vector3 cartesianPlaneNorth = new Vector3();
        boolean found = projectionPlane.intersection(projectionNorthNormalRay, cartesianPlaneNorth);

        //use this intersection point to figure out which way is north in the
        //gnomonic coordinate space.
        northNormal = cartesianPlaneNorth.subtract(projectionPlanePosition).normalize();

        //do a cross product (and negate it) to get the east normal vector.
        eastNormal = northNormal.cross(projectionNormal).negate();
    }

    /**
     * Transforms a position coordinate from GeographicCoordinate (spherical) to
     * a cartesian Gnomonic coordinate space using this projection.
     * @param geographicCoordinate
     * @return
     */
    @Override
    public Vector3 transformPositionTo(GeographicCoordinate geographicCoordinate) {
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

    /**
     * Transforms a cartesian Gnomonic position coordinate that was created using this projection
     * into a Geographic (spherical) coordinate using this projection.
     * @param projectionCoordinate
     * @return
     */
    @Override
    public GeographicCoordinate transformPositionFrom(Vector3 projectionCoordinate) {
        Vector3 relativeToPlane = northNormal.mult(projectionCoordinate.y).add(eastNormal.mult(projectionCoordinate.x));
        Vector3 cartesianPosition = projectionPlanePosition.add(relativeToPlane);

        GeographicCoordinate position = GeographicCoordinate.fromCartesian(cartesianPosition);
        position.setAltitude(projectionCoordinate.z);

        return position;
    }

    /**
     *
     * @param velocity velocity in the Spherical coordinate system
     * @param referencePosition reference position in Geographic coordinate system
     * @param referencePositionCartesian reference position in this projection's cartesian coordiant system
     * @return
     */
    @Override
    public Vector3 tranformVelocityTo(
            SphericalVelocity velocity,
            GeographicCoordinate referencePosition,
            Vector3 referencePositionCartesian) {

        GeographicCoordinate velocityPlusReference = new GeographicCoordinate(referencePosition.add(velocity));
        Vector3 velocityPlusReferenceCartesian = transformPositionTo(velocityPlusReference);
        return velocityPlusReferenceCartesian.subtract(referencePositionCartesian);
    }

    /**
     *
     * @param velocity velocity in this projection's cartesian coordinate system
     * @param referencePosition reference position in Geographic coordinate system
     * @param referencePositionCartesian reference position in this projection's cartesian coordiant system
     * @return
     */
    @Override
    public SphericalVelocity transformVelocityFrom(
            Vector3 velocity,
            GeographicCoordinate referencePosition,
            Vector3 referencePositionCartesian) {

        Vector3 velocityPlusReferenceCartesian = referencePositionCartesian.add(velocity);
        GeographicCoordinate velocityPlusReference = transformPositionFrom(velocityPlusReferenceCartesian);
        return new SphericalVelocity(velocityPlusReference.subtract(referencePosition));
    }
}
