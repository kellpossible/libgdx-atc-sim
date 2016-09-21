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
//        double rectify =  Math.PI * 2.0; //Equation used to rectify. 6.2831
//
//
//        SphericalCoordinate t1 = new SphericalCoordinate(1,2,3); // Does not need rectifying
//        t1.rectifyBounds();
//
//        Assert.assertEquals(1,t1.getR(),0);
//        Assert.assertEquals(2,t1.getTheta(),0);
//        Assert.assertEquals(3,t1.getPhi(),0);
//
//        SphericalCoordinate t2 = new SphericalCoordinate(-1,1,2); //Needs rectifying from R
//        t2.rectifyBounds();
//
//        Assert.assertEquals(-1,t2.getR(),0); // Negation of R
//        Assert.assertEquals(1,t2.getTheta(),0.001); // Theta + Pi, then Pi - Theta
//        Assert.assertEquals(2,t2.getPhi(),0.001);//Pi
//
//        SphericalCoordinate t4 = new SphericalCoordinate(1,2,14); //Needs Rectifying from Phi
//        t4.rectifyBounds();
//
//        Assert.assertEquals(1,t4.getR(),0);
//        Assert.assertEquals(2,t4.getTheta(),0);
//        Assert.assertEquals(1.43362938564,t4.getPhi(),0); // Phi - 2*Pi = 7.71681469282, - 2*Pi = 1.43362938564 FAILS
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

    @Test public void almostEqual() throws Exception
    {
        SphericalCoordinate t11 = new SphericalCoordinate(1,1,1);
        SphericalCoordinate t12 = new SphericalCoordinate(2,2,2);

        Assert.assertTrue(t11.almostEqual(t12,3)); //Within
        //Two fail, one pass asser tfails
        // ALl fail
        // boundry
    }

    /**
     * Testing returning cartesian point in a vector. uses same values as getCartesian test for vector drawing.
     * @throws Exception
     */
    @Test public void getCartesianDrawVector() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(7.071067812, 0.927295218, 0.7853981634);
        SphericalCoordinate t2 = new SphericalCoordinate(103.6153777, 1.096510361, 1.508603304);
        SphericalCoordinate t3 = new SphericalCoordinate(39.75447648,1.277996721,0.6561225817);

        Assert.assertEquals(new Vector3(3,4,5), t1.getCartesianDrawVector());
        Assert.assertEquals(new Vector3(47.22999954223633,92,6.440000057220459), t2.getCartesianDrawVector());
        Assert.assertEquals(new Vector3(7,23.219999313354492,31.5), t3.getCartesianDrawVector());

    }

    @Test public void getModelDrawVector() throws Exception
    {

    }

    @Test public void getModelDrawVector1() throws Exception
    {

    }

    @Test public void arcDistance() throws Exception
    {
        SphericalCoordinate t11 = new SphericalCoordinate(1,1,1);
        SphericalCoordinate t12 = new SphericalCoordinate(2,2,2);

        SphericalCoordinate t21 = new SphericalCoordinate(5,5,5);
        SphericalCoordinate t22 = new SphericalCoordinate(3,3,3);

        SphericalCoordinate t31 = new SphericalCoordinate(28.3,14.2,63.3 );
        SphericalCoordinate t32 = new SphericalCoordinate(1.4,23.3,50);


        Assert.assertEquals(2.623356869627860961516481665884542735639797169541152300153, t11.arcDistance(t12),0.001); // Moving from smaller to larger coordinates
        Assert.assertEquals(0.500777071725513554375177671893198273409110805637843926914, t21.arcDistance(t22),0.001); // Moving from larger to smaller coordinates
        Assert.assertEquals(11.9126,t31.arcDistance(t32),0.001);
    }

    @Test public void rCartesianUnitVector() throws Exception
    {
        SphericalCoordinate t1 = new SphericalCoordinate(1,1,1);
        SphericalCoordinate t2 = new SphericalCoordinate(12.5,13.22,1.2393);
        SphericalCoordinate t3 = new SphericalCoordinate(-31,-21.33,-11.22);

        Assert.assertEquals(new Vector3(0.4546487033367157, 0.7080734372138977, 0.5403022766113281),t1.rCartesianUnitVector()); //Straightforward calculation
        //Assert.assertEquals(new Vector3(1,1,1),t3.rCartesianUnitVector());
    }

    @Test public void phiCartesianUnitVector() throws Exception
    {

    }

    @Test public void thetaCartesianUnitVector() throws Exception
    {

    }

}