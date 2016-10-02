package com.atc.simulator.vectors;

import org.junit.Assert;
import org.junit.Test;
import pythagoras.d.Vector3;

import static org.junit.Assert.*;

/**
 * Tests the Spherical Velocity Class
 * @author Adam Miritis
 */

public class TestSphericalVelocity
{
    /**
     * Tests getting R
     * @throws Exception
     */
    @Test public void getDR() throws Exception
    {
        SphericalVelocity t1 = new SphericalVelocity(new Vector3(1,2,3));
        SphericalVelocity t2 = new SphericalVelocity(new Vector3(73.3,29.283,87.23));
        SphericalVelocity t3 = new SphericalVelocity(new Vector3(28.28,13.4,59.2));

        Assert.assertEquals(1,t1.getDR(),0.001);
        Assert.assertEquals(73.3,t2.getDR(),0.001);
        Assert.assertEquals(28.28,t3.getDR(),0.001);
    }

    /**
     * Tests getting Theta
     * @throws Exception
     */
    @Test public void getDTheta() throws Exception
    {
        SphericalVelocity t1 = new SphericalVelocity(new Vector3(1,2,3));
        SphericalVelocity t2 = new SphericalVelocity(new Vector3(73.3,29.283,87.23));
        SphericalVelocity t3 = new SphericalVelocity(new Vector3(28.28,13.4,59.2));

        Assert.assertEquals(2,t1.getDTheta(),0.001);
        Assert.assertEquals(29.283,t2.getDTheta(),0.001);
        Assert.assertEquals(13.4,t3.getDTheta(),0.001);
    }

    /**
     * Tests getting Phi
     * @throws Exception
     */
    @Test public void getDPhi() throws Exception
    {
        SphericalVelocity t1 = new SphericalVelocity(new Vector3(1,2,3));
        SphericalVelocity t2 = new SphericalVelocity(new Vector3(73.3,29.283,87.23));
        SphericalVelocity t3 = new SphericalVelocity(new Vector3(28.28,13.4,59.2));

        Assert.assertEquals(3,t1.getDPhi(),0.001);
        Assert.assertEquals(87.23,t2.getDPhi(),0.001);
        Assert.assertEquals(59.2,t3.getDPhi(),0.001);
    }

    /**
     * Tests getting the speed of a vector.
     * @throws Exception
     */
    @Test public void getSpeed() throws Exception
    {
        SphericalVelocity t1 = new SphericalVelocity(new Vector3(1,2,3));
        SphericalVelocity t2 = new SphericalVelocity(new Vector3(73.3,29.283,87.23));
        SphericalVelocity t3 = new SphericalVelocity(new Vector3(28.28,13.4,59.2));

        Assert.assertEquals(1,t1.getSpeed(),0.001);
        Assert.assertEquals(73.3,t2.getSpeed(),0.001);
        Assert.assertEquals(28.28,t3.getSpeed(),0.001);
    }

    /**
     * Tests building the draw vector.
     * @throws Exception
     */
    @Test public void getCartesianDrawVector() throws Exception
    {
        SphericalVelocity t1 = new SphericalVelocity(new Vector3(1,2,3));
        com.badlogic.gdx.math.Vector3 r1 = t1.getCartesianDrawVector(new SphericalCoordinate(1,2,3));

        SphericalVelocity t2 = new SphericalVelocity(new Vector3(73.3,29.283,87.23));
        com.badlogic.gdx.math.Vector3 r2 = t1.getCartesianDrawVector(new SphericalCoordinate(86.345,87.362,27.654));

        SphericalVelocity t3 = new SphericalVelocity(new Vector3(28.28,13.4,59.2));
        com.badlogic.gdx.math.Vector3 r3 = t1.getCartesianDrawVector(new SphericalCoordinate(87.363,29.2983,65.43));

        Assert.assertEquals(0.9205,r1.x,0.001);
        Assert.assertEquals(-1.4133,r1.y,0.001);
        Assert.assertEquals(-2.68972,r1.z,0.001);

        Assert.assertEquals(-116.2731,r2.x,0.001);
        Assert.assertEquals(-151.3925,r2.y,0.001);
        Assert.assertEquals(201.8373,r2.z,0.001);

        Assert.assertEquals(193.5527,r3.x,0.001);
        Assert.assertEquals(-136.3791,r3.y,0.001);
        Assert.assertEquals(144.1963,r3.z,0.001);
    }

    /**
     * Tests getting the cartesian velocity
     * @throws Exception
     */
    @Test public void getCartesian() throws Exception
    {
        SphericalVelocity t1 = new SphericalVelocity(new Vector3(1,2,3));
        Vector3 r1 = t1.getCartesian(new SphericalCoordinate(1,2,3));

        SphericalVelocity t2 = new SphericalVelocity(new Vector3(73.3,29.283,87.23));
        Vector3 r2 = t2.getCartesian(new SphericalCoordinate(86.345,87.362,27.654));

        SphericalVelocity t3 = new SphericalVelocity(new Vector3(28.28,13.4,59.2));
        Vector3 r3 = t3.getCartesian(new SphericalCoordinate(87.363,29.2983,65.43));

        Assert.assertEquals(0.9205,r1.x,0.001);
        Assert.assertEquals(-2.6897,r1.y,0.001);
        Assert.assertEquals(-1.4133,r1.z,0.001);

        Assert.assertEquals(-4180.9710,r2.x,0.001);
        Assert.assertEquals(4660.331,r2.y,0.001);
        Assert.assertEquals(-4437.9733,r2.z,0.001);

        Assert.assertEquals(2811.311,r3.x,0.001);
        Assert.assertEquals(3454.0036,r3.y,0.001);
        Assert.assertEquals(-2698.5296,r3.z,0.001);
    }

    /**
     * Tests getting the cartesian angular velocity.
     * @throws Exception
     */
    @Test public void getCartesianAngularVelocity() throws Exception
    {
        SphericalVelocity t1 = new SphericalVelocity(new Vector3(1,2,3));
        Vector3 r1 = t1.getCartesianAngularVelocity(new SphericalCoordinate(1,2,3));

        SphericalVelocity t2 = new SphericalVelocity(new Vector3(73.3,29.283,87.23));
        Vector3 r2 = t2.getCartesianAngularVelocity(new SphericalCoordinate(86.345,87.362,27.654));

        SphericalVelocity t3 = new SphericalVelocity(new Vector3(28.28,13.4,59.2));
        Vector3 r3 = t3.getCartesianAngularVelocity(new SphericalCoordinate(87.363,29.2983,65.43));

        Assert.assertEquals(-2.7278,r1.x,0.001);
        Assert.assertEquals(-1.2484,r1.y,0.001);
        Assert.assertEquals(2,r1.z,0.001);

        Assert.assertEquals(49.4403,r2.x,0.001);
        Assert.assertEquals(71.8660,r2.y,0.001);
        Assert.assertEquals(29.283,r2.z,0.001);

        Assert.assertEquals(50.5672,r3.x,0.001);
        Assert.assertEquals(-30.7829,r3.y,0.001);
        Assert.assertEquals(13.4,r3.z,0.001);
    }

    /**
     * Tests rotation of a spherical coordinate using this Spherical velocity as an angular velocity around a sphere.
     * @throws Exception
     */
    @Test public void angularVelocityTranslate() throws Exception
    {
        SphericalVelocity t1 = new SphericalVelocity(new Vector3(1,2,3));
        Vector3 r1 = t1.angularVelocityTranslate(new SphericalCoordinate(1,2,3),10);

        SphericalVelocity t2 = new SphericalVelocity(new Vector3(73.3,29.283,87.23));
        Vector3 r2 = t2.angularVelocityTranslate(new SphericalCoordinate(86.345,87.362,27.654),82.2);

        SphericalVelocity t3 = new SphericalVelocity(new Vector3(28.28,13.4,59.2));
        Vector3 r3 = t3.angularVelocityTranslate(new SphericalCoordinate(87.363,29.2983,65.43),23.22);

        Assert.assertEquals(1,r1.x,0.001);
        Assert.assertEquals(1.3889,r1.y,0.001);
        Assert.assertEquals(1.7088,r1.z,0.001);

        Assert.assertEquals(86.3450,r2.x,0.001);
        Assert.assertEquals(5.2543,r2.y,0.001);
        Assert.assertEquals(1.2204,r2.z,0.001);

        Assert.assertEquals(87.3629,r3.x,0.001);
        Assert.assertEquals(1.1771,r3.y,0.001);
        Assert.assertEquals(1.7666,r3.z,0.001);
    }

}