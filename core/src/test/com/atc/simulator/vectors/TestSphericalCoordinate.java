package com.atc.simulator.vectors;

import org.junit.Assert;
import org.junit.Test;
import pythagoras.d.Vector;
import pythagoras.d.Vector3;

import static org.junit.Assert.*;

/**
 * Tests the spherical coordinate class
 * @author Adam Miritis
 */
public class TestSphericalCoordinate
{
    /**
     * Tests creating a new spherical coordinate from a cartisien point.
     * Uses http://keisan.casio.com/exec/system/1359533867 for calculation of new points.
     * @throws Exception
     */
    @Test public void fromCartesian() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(0,0,0);
        SphericalCoordinate t2 = new SphericalCoordinate(24.321,232.321,232.22);

        t1 =  t1.fromCartesian(new Vector3(10,20,30)); //taking in 10,20,30 Radian Coordinates. Testing against position zero
        Assert.assertEquals(37.416573867739,t1.getR(),0.001);
        Assert.assertEquals(1.1071487177941,t1.getTheta(),0.001);
        Assert.assertEquals(0.64052231267942,t1.getPhi(),0.001);

        t2 =  t2.fromCartesian(new Vector3(10,20,30)); //taking in 10,20,30 Radian Coordinates. Testing against set position
        Assert.assertEquals(37.416573867739,t1.getR(),0.001);
        Assert.assertEquals(1.1071487177941,t1.getTheta(),0.001);
        Assert.assertEquals(0.64052231267942,t1.getPhi(),0.001);
    }

    /**
     * Getter for R
     * @throws Exception
     */
    @Test public void getR() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(1,2,3);
        SphericalCoordinate t2 = new SphericalCoordinate(51.33,3.8723,23.2);
        SphericalCoordinate t3 = new SphericalCoordinate(28,2.233,34.238);

        Assert.assertEquals(1,t1.getR(),0);
        Assert.assertEquals(51.33,t2.getR(),0);
        Assert.assertEquals(28,t3.getR(),0);

    }

    /**
     * Getter for Theta
     * @throws Exception
     */
    @Test public void getTheta() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(1,2,3);
        SphericalCoordinate t2 = new SphericalCoordinate(51.33,3.8723,23.2);
        SphericalCoordinate t3 = new SphericalCoordinate(28,2.233,34.238);

        Assert.assertEquals(2,t1.getTheta(),0);
        Assert.assertEquals(3.8723,t2.getTheta(),0);
        Assert.assertEquals(2.233,t3.getTheta(),0);
    }

    /**
     * Getter for Phi
     * @throws Exception
     */
    @Test public void getPhi() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(1,2,3);
        SphericalCoordinate t2 = new SphericalCoordinate(51.33,3.8723,23.2);
        SphericalCoordinate t3 = new SphericalCoordinate(28,2.233,34.238);

        Assert.assertEquals(3,t1.getPhi(),0);
        Assert.assertEquals(23.2,t2.getPhi(),0);
        Assert.assertEquals(34.238,t3.getPhi(),0);
    }

    /**
     * Tests Rectifying the bounds of a Spherical Coordinate.
     * Status: Passes on all except Rectifying Phi.
     */
    @Test public void rectifyBounds() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(1,2,3); // Does not need rectifying
        SphericalCoordinate r1 = t1.rectifyBounds();

        SphericalCoordinate t2 = new SphericalCoordinate(-1,1,2); //Needs rectifying from R
        SphericalCoordinate r2 = t2.rectifyBounds();

        SphericalCoordinate t3 = new SphericalCoordinate(1,2,14); //Needs Rectifying from Phi
        SphericalCoordinate r3 = t3.rectifyBounds();

        Assert.assertEquals(1,r1.getR(),0.01);
        Assert.assertEquals(2,r1.getTheta(),0.01);
        Assert.assertEquals(3,r1.getPhi(),0.01);

        Assert.assertEquals(-1,r2.getR(),0.01); // Negation of R
        Assert.assertEquals(1,r2.getTheta(),0.01); // Theta + Pi, then Pi - Theta
        Assert.assertEquals(2,r2.getPhi(),0.01);//Pi

        Assert.assertEquals(1,r3.getR(),0.01);
        Assert.assertEquals(2,r3.getTheta(),0.01);
        Assert.assertEquals(1.4336,r3.getPhi(),0.01); // Phi - 2*Pi = 7.71681469282, - 2*Pi = 1.43362938564
    }

    /**
     * Tests returning the cartesian coordinates from the spherical coordinate.
     * Used http://keisan.casio.com/exec/system/1359533867 for calculation
     * @throws Exception
     */
    @Test public void getCartesian() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(7.071067812, 0.927295218, 0.7853981634);
        SphericalCoordinate t2 = new SphericalCoordinate(103.6153777, 1.096510361, 1.508603304);
        SphericalCoordinate t3 = new SphericalCoordinate(39.75447648,1.277996721,0.6561225817);

        Assert.assertEquals(new Vector3(3,4,5), t1.getCartesian());
        Assert.assertEquals(new Vector3(47.22999954223633,92,6.440000057220459), t2.getCartesian());
        Assert.assertEquals(new Vector3(7,23.219999313354492,31.5), t3.getCartesian());
    }

    /**
     * Tests almost equal, to see if one spherical coordiante is almost equal to another, based upon a described tolerance.
     * @throws Exception
     */
    @Test public void almostEqual() throws Exception
    {
        SphericalCoordinate t11 = new SphericalCoordinate(1,1,1);
        SphericalCoordinate t12 = new SphericalCoordinate(2,2,2);
        SphericalCoordinate t13 = new SphericalCoordinate(2,2,3);

        Assert.assertTrue(t11.almostEqual(t12,3)); //Within
        Assert.assertFalse(t11.almostEqual(t13,1)); //Two fail, one pass
        Assert.assertFalse(t11.almostEqual(t12,0.5)); // ALl fail
    }

    /**
     * Testing returning cartesian point in a vector. uses same values as getCartesian test for vector drawing.
     * @throws Exception
     */
    @Test public void getCartesianDrawVector() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(7.071067812, 0.927295218, 0.7853981634);
        com.badlogic.gdx.math.Vector3 r1 = t1.getCartesianDrawVector();

        SphericalCoordinate t2 = new SphericalCoordinate(103.6153777, 1.096510361, 1.508603304);
        com.badlogic.gdx.math.Vector3 r2 = t2.getCartesianDrawVector();

        SphericalCoordinate t3 = new SphericalCoordinate(39.75447648,1.277996721,0.6561225817);
        com.badlogic.gdx.math.Vector3 r3 = t3.getCartesianDrawVector();

        Assert.assertEquals(3,r1.x,0.01);
        Assert.assertEquals(-5,r1.y,0.01);
        Assert.assertEquals(4,r1.z,0.01);

        Assert.assertEquals(47.22999954223633,r2.x,0.01);
        Assert.assertEquals(-6.4400000572204,r2.y,0.01);
        Assert.assertEquals(92.0,r2.z,0.01);

        Assert.assertEquals(7,r3.x,0.01);
        Assert.assertEquals(-31.5,r3.y,0.01);
        Assert.assertEquals(23.219999313354492,r3.z,0.01);
    }

    /**
     * Tests the creation of a model draw vector (without adjustment).
     * @throws Exception
     */
    @Test public void getModelDrawVector() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(7.071067812, 0.927295218, 0.7853981634);
        com.badlogic.gdx.math.Vector3 r1 = t1.getModelDrawVector();

        SphericalCoordinate t2 = new SphericalCoordinate(103.6153777, 1.096510361, 1.508603304);
        com.badlogic.gdx.math.Vector3 r2 = t2.getModelDrawVector();

        SphericalCoordinate t3 = new SphericalCoordinate(39.75447648,1.277996721,0.6561225817);
        com.badlogic.gdx.math.Vector3 r3 = t3.getModelDrawVector();

        Assert.assertEquals(0.420,r1.x,0.01);
        Assert.assertEquals(-0.7,r1.y,0.001);
        Assert.assertEquals(0.56,r1.z,0.01);

        Assert.assertEquals(0.451,r2.x,0.01);
        Assert.assertEquals(-0.06,r2.y,0.01);
        Assert.assertEquals(0.87,r2.z,0.01);

        Assert.assertEquals(0.17,r3.x,0.01);
        Assert.assertEquals(-0.78,r3.y,0.01);
        Assert.assertEquals(0.5782,r3.z,0.01);
    }
    /**
     * Tests the creation of a model draw vector (with adjustment).
     * @throws Exception
     */
    @Test public void getModelDrawVector1() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(7.071067812, 0.927295218, 0.7853981634);
        com.badlogic.gdx.math.Vector3 r1 = t1.getModelDrawVector(12);

        SphericalCoordinate t2 = new SphericalCoordinate(103.6153777, 1.096510361, 1.508603304);
        com.badlogic.gdx.math.Vector3 r2 = t2.getModelDrawVector(0.582);

        SphericalCoordinate t3 = new SphericalCoordinate(39.75447648,1.277996721,0.6561225817);
        com.badlogic.gdx.math.Vector3 r3 = t3.getModelDrawVector(8.283);

        Assert.assertEquals(5.51,r1.x,0.01);
        Assert.assertEquals(-9.18531,r1.y,0.001);
        Assert.assertEquals(7.3482,r1.z,0.01);

        Assert.assertEquals(0.7165,r2.x,0.01);
        Assert.assertEquals(-0.097,r2.y,0.01);
        Assert.assertEquals(1.395,r2.z,0.01);

        Assert.assertEquals(1.63,r3.x,0.01);
        Assert.assertEquals(-7.34,r3.y,0.01);
        Assert.assertEquals(5.41,r3.z,0.01);
    }

    /**
     * Tests the calculation of the arc distance between two spherical coordinates.
     * @throws Exception
     */
    @Test public void arcDistance() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(1,2,3);
        SphericalCoordinate t2 = new SphericalCoordinate(3,4,5);

        SphericalCoordinate t3 = new SphericalCoordinate(27.9238, 58.2932, 29.92);
        SphericalCoordinate t4 = new SphericalCoordinate(28.86789, 33.283873, 47.827);

        SphericalCoordinate t5 = new SphericalCoordinate(86.345,87.362,27.654);
        SphericalCoordinate t6 = new SphericalCoordinate(87.363,29.2983,65.43);

        Assert.assertEquals(0.978356479,t1.arcDistance(t2),0.01);
        Assert.assertEquals(25.749560,t3.arcDistance(t4),0.01);
        Assert.assertEquals(130.939,t5.arcDistance(t6),0.01);
    }

    /**
     * Tests the creation of an R Cartesian Unit Vector
     * @throws Exception
     */
    @Test public void rCartesianUnitVector() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(1,1,1);
        Vector3 r1 = t1.rCartesianUnitVector();

        SphericalCoordinate t2 = new SphericalCoordinate(92.33,76.38,39.22);
        Vector3 r2 = t2.rCartesianUnitVector();

        Assert.assertEquals(0.4546487033367157,r1.x,0.01);
        Assert.assertEquals(0.7080734372138977,r1.y,0.01);
        Assert.assertEquals(0.5403022766113281,r1.z,0.01);

        Assert.assertEquals(0.554854691028595,r2.x,0.01);
        Assert.assertEquals(0.830450177192688,r2.y,0.01);
        Assert.assertEquals(0.04988745227456093,r2.z,0.01);
    }

    /**
     * Tests the creation of a Phi Cartesian Unit Vector
     * @throws Exception
     */
    @Test public void phiCartesianUnitVector() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(1,1,1);
        Vector3 r1 = t1.phiCartesianUnitVector();

        SphericalCoordinate t2 = new SphericalCoordinate(92.33,76.38,39.22);
        Vector3 r2 = t2.phiCartesianUnitVector();

        Assert.assertEquals(0.2919265817264289,r1.x,0.01);
        Assert.assertEquals(0.4546487134128409,r1.y,0.01);
        Assert.assertEquals(-0.8414709848078965,r1.z,0.01);

        Assert.assertEquals(0.02771479754010994,r2.x,0.01);
        Assert.assertEquals(0.04148069470976872,r2.y,0.01);
        Assert.assertEquals(-0.9987548457773338,r2.z,0.01);
    }

    /**
     * Tests the creation of a Theta Cartesian Unit Vector
     * @throws Exception
     */
    @Test public void thetaCartesianUnitVector() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(1,1,1);
        Vector3 r1 = t1.thetaCartesianUnitVector();

        SphericalCoordinate t2 = new SphericalCoordinate(92.33,76.38,39.22);
        Vector3 r2 = t2.thetaCartesianUnitVector();

        Assert.assertEquals(-0.8414709848078965,r1.x,0.01);
        Assert.assertEquals(0.5403023058681398,r1.y,0.01);
        Assert.assertEquals(0,r1.z,0.01);

        Assert.assertEquals(-0.8314855066033674,r2.x,0.01);
        Assert.assertEquals(0.5555464447807595,r2.y,0.01);
        Assert.assertEquals(0,r2.z,0.01);
    }

}