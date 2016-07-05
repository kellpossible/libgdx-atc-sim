//
// Pythagoras - a collection of geometry classes
// http://github.com/samskivert/pythagoras

package com.atc.simulator.vectors.PythagorasLibrary;

import org.junit.Assert;
import org.junit.Test;
import pythagoras.d.*;

import static org.junit.Assert.*;

public class AreaTest
{
    public void main (String[] arg) {
        areaWithPath();
    }

    @Test public void add(){
        Vector3 v1 = new Vector3(0,0,0);
        Vector3 v2 = new Vector3(1,1,1);

        Assert.assertEquals(v2, v1.add(v2)); //<(0+1),(0+1),(0+1)> = <1,1,1>
        Assert.assertEquals(v1, v2.add(new Vector3(-1,-1,-1))); //<(1-1),(1-1),(1-1)> = <0,0,0>
        Assert.assertEquals(new Vector3(2,2,2), v2.add(v2)); //<(1+1),(1+1),(1+1)> = <2,2,2>
    }

    @Test public void subtract(){
        Vector3 v1 = new Vector3(0,0,0);
        Vector3 v2 = new Vector3(1,1,1);
        Vector3 v3 = new Vector3(-1,-1,-1);

        Assert.assertEquals(v2, v2.subtract(v1)); //<(1-0),(1-0),(1-0)> = <1,1,1>
        Assert.assertEquals(v1, v2.subtract(v2)); //<(1-1),(1-1),(1-1)> = <0,0,0>
        Assert.assertEquals(v1, v3.subtract(new Vector3(-1,-1,-1))); //<(-1--1),(-1--1),(-1--1)> = <0,0,0>
    }

    @Test public void normalize(){
        Vector3 v1 = new Vector3(3,1,2);
        Vector3 v1Norm = new Vector3(0.8017837257372732,0.2672612419124244,0.5345224838248488);
        Vector3 v2 = new Vector3(5,5,5);
        Vector3 v2Norm = new Vector3(0.5773502691896257, 0.5773502691896257, 0.5773502691896257);

        Assert.assertEquals("http://www.fundza.com/vectors/normalize/",v1Norm,v1.normalize());
        Assert.assertEquals("http://snipd.net/2d-and-3d-vector-normalization-and-angle-calculation-in-c",v2Norm,v2.normalize());
    }

    @Test public void negative(){
        Vector3 v1 = new Vector3(3,1,2);
        Vector3 v1Norm = new Vector3(-3,-1,-2);
        Vector3 v2 = new Vector3(5,5,5);
        Vector3 v2Norm = new Vector3(-5,-5,-5);

        Assert.assertEquals(v1Norm,v1.negate());
        Assert.assertEquals(v2Norm,v2.negate());
    }

    @Test public void multiply(){
        Vector3 v1 = new Vector3(1,1,1);
        Vector3 v1Scalar = new Vector3(-3,-3,-3);
        Vector3 v2 = new Vector3(5,5,5);
        Vector3 v3 = new Vector3(6.2,5,0.1);
        Vector3 v23 = new Vector3(31,25,0.5);

        Assert.assertEquals(v1Scalar,v1.mult(-3));
        Assert.assertEquals(v23,v2.mult(v3));
    }

    @Test public void linearInterpolate(){
        Vector3 v1 = new Vector3(0,0,0);
        Vector3 v2 = new Vector3(10,0,0);
        Vector3 v3 = new Vector3(0,10,0);
        Vector3 v4 = new Vector3(0,0,10);

        Assert.assertEquals(new Vector3(5,0,0),v1.lerp(v2, 0.5));
        Assert.assertEquals(new Vector3(0,5,0),v1.lerp(v3, 0.5));
        Assert.assertEquals(new Vector3(0,0,5),v1.lerp(v4, 0.5));
        Assert.assertEquals(new Vector3(2.5,0,7.5),v2.lerp(v4, 0.75));
    }

    @Test public void areaWithPath() {
        Path path = new Path();
        path.moveTo(0d, 0d);
        for (int i = 1;  i <= 8; i++) {
            path.lineTo(2d * i, 3d * i);
        }
        Area areaWithNinePoints = new Area(path);
        path.closePath();
        Area areaWithNinePointsAndClose = new Area(path);
        assertEquals(areaWithNinePoints, areaWithNinePointsAndClose);

        path.reset();
        path.moveTo(0d, 0d);
        for (int i = 1;  i <= 10; i++) {
            path.lineTo(2d * i, 3d * i);
        }
        Area areaWithElevenPoints = new Area(path);
        path.closePath();
        Area areaWithElevenPointsAndClose = new Area(path);
        assertEquals(areaWithElevenPoints, areaWithElevenPointsAndClose);

        path.reset();
        path.moveTo(0d, 0d);
        for (int i = 1;  i <= 9; i++) {
            path.lineTo(2d * i, 3d * i);
        }
        // this used to ArrayIndexOutOfBoundsException
        Area areaWithTenPoints = new Area(path);
        path.closePath();
        Area areaWithTenPointsAndClose = new Area(path);
        assertEquals(areaWithTenPoints, areaWithTenPointsAndClose);
    }

    protected void assertEquals (Area one, Area two) {
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