/**
* Unit Testing for all methods used from  Pythagoras library(http://github.com/samskivert/pythagoras)
*
* @author Adam Miritis
**/


package com.atc.simulator.vectors.PythagorasLibrary;

import org.junit.Assert;
import org.junit.Test;
import pythagoras.d.*;

import static org.junit.Assert.*;

public class AreaTest
{
    public void main (String[] arg)
    {
        areaWithPath();
    }

    /**
     * Vector addition
     **/
    @Test public void add()
    {
        Vector3 v1 = new Vector3(0,0,0);
        Vector3 v2 = new Vector3(1,1,1);

        Assert.assertEquals(v2, v1.add(v2)); //<(0+1),(0+1),(0+1)> = <1,1,1> Simple addition
        Assert.assertEquals(v1, v2.add(new Vector3(-1,-1,-1))); //<(1-1),(1-1),(1-1)> = <0,0,0> Addition of negative numbers
        Assert.assertEquals(new Vector3(2,2,2), v2.add(v2)); //<(1+1),(1+1),(1+1)> = <2,2,2> Addition of two numbers.

    }

    /**
     * Vector subtraction
     **/
    @Test public void subtract()
    {
        Vector3 v1 = new Vector3(0,0,0);
        Vector3 v2 = new Vector3(1,1,1);
        Vector3 v3 = new Vector3(-1,-1,-1);

        Assert.assertEquals(v2, v2.subtract(v1)); //<(1-0),(1-0),(1-0)> = <1,1,1> Simple subtraction.
        Assert.assertEquals(v1, v2.subtract(v2)); //<(1-1),(1-1),(1-1)> = <0,0,0> Subtraction of self.
        Assert.assertEquals(v1, v3.subtract(new Vector3(-1,-1,-1))); //<(-1--1),(-1--1),(-1--1)> = <0,0,0> Subtracting negative numbers.
    }
    /**
     * Vector normalisation
     **/
    @Test public void normalize()
    {
        Vector3 v1 = new Vector3(3,1,2);
        Vector3 v1Norm = new Vector3(0.8017837257372732,0.2672612419124244,0.5345224838248488);
        Vector3 v2 = new Vector3(5,5,5);
        Vector3 v2Norm = new Vector3(0.5773502691896257, 0.5773502691896257, 0.5773502691896257);

        Assert.assertEquals("http://www.fundza.com/vectors/normalize/",v1Norm,v1.normalize());
        Assert.assertEquals("http://snipd.net/2d-and-3d-vector-normalization-and-angle-calculation-in-c",v2Norm,v2.normalize());
    }

    /**
     * Negation of negative numbers
     **/
    @Test public void negative()
    {
        Vector3 v1 = new Vector3(3,1,2);
        Vector3 v1Norm = new Vector3(-3,-1,-2);
        Vector3 v2 = new Vector3(5,5,5);
        Vector3 v2Norm = new Vector3(-5,-5,-5);

        Assert.assertEquals(v1Norm,v1.negate()); // negating positive numbers
        Assert.assertEquals(v2,v2Norm.negate()); // negating negative numbers.
    }
    /**
     * Vector Multiplication
     **/
    @Test public void multiply()
    {
        Vector3 v1 = new Vector3(1,1,1);
        Vector3 v1Scalar = new Vector3(-3,-3,-3);
        Vector3 v2 = new Vector3(5,5,5);
        Vector3 v3 = new Vector3(6.2,5,0.1);
        Vector3 v4 = new Vector3(6,4,3);
        Vector3 v5 = new Vector3(3,2,7);
        Vector3 v23 = new Vector3(31,25,0.5);

        Assert.assertEquals(new Vector3(18,8,21),v4.mult(v5));
        Assert.assertEquals(v1Scalar,v1.mult(-3)); // multiplying by negative
        Assert.assertEquals(v23,v2.mult(v3)); // Multiplying by floating point numbers.
    }
    /**
     * Linear interplotation of vectors.
     **/
    @Test public void linearInterpolate()
    {
        Vector3 v1 = new Vector3(0,0,0);
        Vector3 v2 = new Vector3(10,0,0);
        Vector3 v3 = new Vector3(0,10,0);
        Vector3 v4 = new Vector3(0,0,10);

        Assert.assertEquals(new Vector3(5,0,0),v1.lerp(v2, 0.5));
        Assert.assertEquals(new Vector3(0,5,0),v1.lerp(v3, 0.5));
        Assert.assertEquals(new Vector3(0,0,5),v1.lerp(v4, 0.5));
        Assert.assertEquals(new Vector3(2.5,0,7.5),v2.lerp(v4, 0.75));
    }

    /**
     * Distance calculation between two points.
     * Results were calculated using Wolfram Alpha(https://www.wolframalpha.com)
     **/
    @Test public void distance()
    {
        Vector3 v1 = new Vector3(0,0,0);
        Vector3 v2 = new Vector3(5,5,5);

        Vector3 v3 = new Vector3(10,11,12);
        Vector3 v4 = new Vector3(1,4,6);

        Vector3 v5 = new Vector3(4,17,9);
        Vector3 v6 = new Vector3(23,11,14);

        Assert.assertEquals(8.66025,v1.distance(v2),0.001); // Zero coordinates https://www.wolframalpha.com/input/?i=distance+from+(0,0,0)+to+(5,5,5)
        Assert.assertEquals(12.8841,v3.distance(v4),0.001); // From larger to smaller Coordinate https://www.wolframalpha.com/input/?i=distance+from+(10,11,12)+to+(1,4,6)
        Assert.assertEquals(20.5426,v5.distance(v6),0.001); // Mixture of larger to smaller and vice versa https://www.wolframalpha.com/input/?i=distance+from+(4,17,9)+to+(23,11,14)
    }

    /**
     * Calculates the area between the path of two points.
     */
    @Test public void areaWithPath()
    {
        Path path = new Path();
        path.moveTo(0d, 0d);
        for (int i = 1;  i <= 8; i++)
        {
            path.lineTo(2d * i, 3d * i);
        }
        Area areaWithNinePoints = new Area(path);
        path.closePath();
        Area areaWithNinePointsAndClose = new Area(path);
        assertEquals(areaWithNinePoints, areaWithNinePointsAndClose);

        path.reset();
        path.moveTo(0d, 0d);

        for (int i = 1;  i <= 10; i++)
        {
            path.lineTo(2d * i, 3d * i);
        }
        Area areaWithElevenPoints = new Area(path);
        path.closePath();
        Area areaWithElevenPointsAndClose = new Area(path);
        assertEquals(areaWithElevenPoints, areaWithElevenPointsAndClose);

        path.reset();
        path.moveTo(0d, 0d);
        for (int i = 1;  i <= 9; i++)
        {
            path.lineTo(2d * i, 3d * i);
        }
        // this used to ArrayIndexOutOfBoundsException
        Area areaWithTenPoints = new Area(path);
        path.closePath();
        Area areaWithTenPointsAndClose = new Area(path);
        assertEquals(areaWithTenPoints, areaWithTenPointsAndClose);
    }

    protected void assertEquals (Area one, Area two)
    {
        PathIterator iter1 = one.pathIterator(new IdentityTransform());
        PathIterator iter2 = two.pathIterator(new IdentityTransform());
        double[] coords1 = new double[2], coords2 = new double[2];
        while (!iter1.isDone()) {
            if (iter2.isDone()) fail(two + " path shorter than " + one);
            int seg1 = iter1.currentSegment(coords1);
            int seg2 = iter2.currentSegment(coords2);
            Assert.assertEquals("Same path segment", seg1, seg2);
            Assert.assertEquals("Same x coord", coords1[0], coords2[0], MathUtil.EPSILON);
            Assert.assertEquals("Same y coord", coords1[1], coords2[1], MathUtil.EPSILON);
            iter1.next();
            iter2.next();
        }
        if (!iter2.isDone()) fail(one + " path shorter than " + two);
    }
}