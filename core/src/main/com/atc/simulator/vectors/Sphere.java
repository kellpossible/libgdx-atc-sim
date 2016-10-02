package com.atc.simulator.vectors;

import pythagoras.d.IVector3;
import pythagoras.d.Matrix3;
import pythagoras.d.Ray3;
import pythagoras.d.Vector3;

/**
 * Represents a sphere
 *
 * @author Luke Frisken
 */
public class Sphere implements Spheroid {
    private static final double EARTH_MSL_RADIUS = 6371000.0;
    public static final Sphere EARTH = new Sphere(EARTH_MSL_RADIUS, new Vector3(0,0,0));
    public double radius;
    public IVector3 position;
    private static double oneDegreeInRadians = Math.toRadians(1);

    /**
     * Constructor Sphere creates a new Sphere instance.
     *
     * @param radius of type double
     * @param position of type Vector3
     */
    public Sphere(double radius, IVector3 position)
    {
        this.radius = radius;
        this.position = position;
    }

    /**
     * Method getRadius returns the radius of this Sphere object.
     *
     * @return the radius (type double) of this Sphere object.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Method setRadius sets the radius of this Sphere object.
     *
     * @param radius the radius of this Sphere object.
     *
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Method getPosition returns the position of this Sphere object.
     *
     * @return the position (type Vector3) of this Sphere object.
     */
    public IVector3 getPosition() {
        return position;
    }

    /**
     * Method setPosition sets the position of this Sphere object.
     *
     * @param position the position of this Sphere object.
     *
     */
    public void setPosition(IVector3 position) {
        this.position = position;
    }

    /**
     * Get the radius of the spheroid at the given position.
     *
     * @param pos
     * @return
     */
    @Override
    public double getRadius(SphericalCoordinate pos) {
        return this.radius;
    }

    /**
     * Get the average radius of the spheroid.
     *
     * @return
     */
    @Override
    public double getAverageRadius() {
        return this.radius;
    }

    /**
     * Uses euler integration to find the arc distance between two points.
     * @param p1
     * @param p2
     * @return
     */
    public double arcDistance(SphericalCoordinate p1, SphericalCoordinate p2) {
        return arcDistanceEulerIntegrate(p1, p2, 4);
    }

    /**
     * Uses euler integration to find the arc distance between two points.
     * @param p1
     * @param p2
     * @param stepsPerDegree number of integration steps per degree of angle between the two points.
     * @return
     */
    public double arcDistanceEulerIntegrate(SphericalCoordinate p1, SphericalCoordinate p2, int stepsPerDegree) {
        Vector3 p1Cart = p1.getCartesian();
        Vector3 p2Cart = p2.getCartesian();

        double angle = p1Cart.angle(p2Cart);

        if (angle == 0.0) {
            return 0.0;
        }

        Vector3 rotationAxis = p1Cart.cross(p2Cart).normalize();

        double maxH = oneDegreeInRadians/((double) stepsPerDegree);

        int steps = (int) (angle/maxH);
        steps = Math.max(steps, 1); //at least one step

        double h = angle/((double) steps);

        double distanceSum = 0.0;
        Vector3 prevHVec = p1Cart;
        for (int i = 1; i <= steps; i++) {
            //TODO: I'm not sure if this is rotating in the correct direction. It won't matter on a sphere.
            Matrix3 rotation = new Matrix3().setToRotation(h*i, rotationAxis);
            Vector3 hVec = rotation.transform(p1Cart);
            distanceSum += hVec.subtract(prevHVec).length();
            prevHVec = hVec;
        }

        return distanceSum;
    }

    /**
     * Calculate the intersection point between a ray and this sphere
     * Borrowed this from my ray tracer code, it should be pretty fast.
     * @param ray
     * @return the intersection point (or null if no intersection)
     */
    public Vector3 intersect(Ray3 ray)
    {
        Vector3 OC = this.position.subtract(ray.origin); //ray origin -> circle origin
        double L2OC = OC.dot(OC); //length(OC)^2
        double R2 = this.radius*this.radius; //radius ^2

        double Tca = OC.dot(ray.direction); //closest approach along ray

        // if L20C < this.radius^2 then origin inside sphere
        if (L2OC < R2)
        { //ray origin is insisde the sphere so it will definitely hit the sphere
            //D^2 distance from ray's closest approach to sphere's center
            double D2 = L2OC - (Tca * Tca);

            double T2hc = R2 - D2; //half chord distance squared

            double t = Tca + Math.sqrt(T2hc);
            Vector3 OP = ray.direction.mult(t); //ray origin to point of intersection
            return OP.add(ray.origin); //intersection point

        } else {//the ray is outside the sphere but it could still hit it
            if(Tca > 0)
            { //it is still possible for the ray to hit.
                //D^2 distance from ray's closest approach to sphere's center
                double D2 = L2OC - (Tca * Tca);
                double T2hc = R2 - D2;

                if (T2hc >= 0)
                { //the ray will definitely hit
                    double t = Tca - Math.sqrt(T2hc);
                    Vector3 OP = ray.direction.mult(t); //ray origin to point of intersection
                    return OP.add(ray.origin); //intersection point

                } else { //the ray cannot hit
                    return null;
                }

            } else { //the ray cannot hit
                return null;
            }
        }

    }
}
