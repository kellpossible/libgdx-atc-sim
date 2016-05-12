package com.atc.simulator.vectors;

import pythagoras.d.Vector3;

/**
 * Created by luke on 9/04/16.
 * See: http://mathworld.wolfram.com/SphericalCoordinates.html
 */
public class SphericalCoordinate extends Vector3 {
    public SphericalCoordinate(SphericalCoordinate other)
    {
        super(other);
    }
    public SphericalCoordinate(Vector3 other)
    {
        super(other);
    }
    public SphericalCoordinate(double r, double theta, double phi) {
        this.x = r;
        this.y = theta;
        this.z = phi;
    }

    /**
     * Get Radius Component
     * @return
     */
    public double getR() {
        return this.x;
    }

    /**
     * Get Theta Component
     * @return
     */
    public double getTheta() {
        return this.y;
    }

    /**
     * Get Phi Component
     * @return
     */
    public double getPhi() {
        return this.z;
    }

    /**
     * get the equivalent cartesian coordinate vector of this spherical coordinate.
     * @return
     */
    public Vector3 getCartesian() {
        return new Vector3(
                (float) (x * Math.sin(y) * Math.cos(z)),
                (float) (x * Math.sin(y) * Math.sin(z)),
                (float) (x * Math.cos(y)));
    }

    /**
     * Get the cartesian libgdx vector3 required for 3D drawing.
     * @return
     */
    public com.badlogic.gdx.math.Vector3 getCartesianDrawVector()
    {
        System.out.println("Coords after transform R:"+this.getR()+" Theta:"+this.getTheta()+" Phi:" + this.getPhi());
        Vector3 cartesian = this.getCartesian();
        return new com.badlogic.gdx.math.Vector3((float) cartesian.x, (float) cartesian.z, (float) cartesian.y);
    }
}