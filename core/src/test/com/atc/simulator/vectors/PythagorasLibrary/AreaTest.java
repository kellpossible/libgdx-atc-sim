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