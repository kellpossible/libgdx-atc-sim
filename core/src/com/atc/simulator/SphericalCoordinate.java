package com.atc.simulator;

import pythagoras.d.Vector3;

/**
 * Created by luke on 9/04/16.
 * See: http://mathworld.wolfram.com/SphericalCoordinates.html
 */
public class SphericalCoordinate extends Vector3 {
    public SphericalCoordinate(double r, double theta, double phi) {
        this.x = r;
        this.y = theta;
        this.z = phi;
    }

    public double getR() {
        return this.x;
    }

    public double getTheta() {
        return this.y;
    }

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
