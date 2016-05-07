package com.atc.simulator.vectors;

import pythagoras.d.Vector3;

/**
 * Created by luke on 7/05/16.
 */
public class SphericalVelocity extends Vector3 {
    public SphericalVelocity(SphericalVelocity other)
    {
        super(other);
    }

    public SphericalVelocity(Vector3 other)
    {
        super(other);
    }

    public SphericalVelocity(double dr, double dtheta, double dphi) {
        this.x = dr;
        this.y = dtheta;
        this.z = dphi;
    }

    /**
     * Get Radius Component
     * @return
     */
    public double getDR() {
        return this.x;
    }

    /**
     * Get Theta Component
     * @return
     */
    public double getDTheta() {
        return this.y;
    }

    /**
     * Get Phi Component
     * @return
     */
    public double getDPhi() {
        return this.z;
    }

    /**
     * get the equivalent cartesian coordinate vector of this spherical velocity.
     * @return
     */
    public Vector3 getCartesian(SphericalCoordinate from) {
        SphericalCoordinate to = new SphericalCoordinate(from.add(this));
        return to.getCartesian().subtract(from.getCartesian());
    }

    public double getSpeed()
    {
        return this.getCartesian(new SphericalCoordinate(new Vector3(0,0,0))).length();
    }

    /**
     * Get the cartesian libgdx vector3 required for 3D drawing.
     * @return
     */
    public com.badlogic.gdx.math.Vector3 getCartesianDrawVector(SphericalCoordinate from)
    {
        System.out.println("Coords after transform DR:"+this.getDR()+" DTheta:"+this.getDTheta()+" DPhi:" + this.getDPhi());
        Vector3 cartesian = this.getCartesian(from);
        return new com.badlogic.gdx.math.Vector3((float) cartesian.x, (float) cartesian.z, (float) cartesian.y);
    }
}
