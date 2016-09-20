package com.atc.simulator.vectors;

import org.junit.Assert;
import org.junit.Test;
import pythagoras.d.IVector3;
import pythagoras.d.Ray3;
import pythagoras.d.Vector;
import pythagoras.d.Vector3;

import java.nio.DoubleBuffer;

import static org.junit.Assert.*;

/**
 * Tests the Sphere Class
 * @author Adam Miritis
 */
public class TestSphere
{

    /**
     * Returns the radius of a Sphere.
     * @throws Exception
     */
    @Test public void getRadius() throws Exception
    {
        Sphere t1 = new Sphere(10, new Vector3(1,2,3));
        Sphere t2 = new Sphere(27.9823, new Vector3(1,2,3));
        Sphere t3 = new Sphere(58.20203, new Vector3(1,2,3));

        Assert.assertEquals(10,t1.getRadius(),0);
        Assert.assertEquals(27.9823,t2.getRadius(),0);
        Assert.assertEquals(58.20203,t3.getRadius(),0);
    }

    /**
     * Tests setting the radius of a sphere
     * @throws Exception
     */
    @Test public void setRadius() throws Exception
    {
        Sphere t1 = new Sphere(10.2938, new Vector3(0,0,0));
        Sphere t2 = new Sphere(10, new Vector3(1,2,3));
        Sphere t3 = new Sphere(10.3127, new Vector3(1,2,3));

        t1.setRadius(5); //From 10.2983 to 5
        t2.setRadius(58.2323); //From 10 to 57.2323
        t3.setRadius(29.5332); // From 10.3127 to 29.5332

        Assert.assertEquals(5,t1.getRadius(),0);
        Assert.assertEquals(58.2323,t2.getRadius(),0);
        Assert.assertEquals(29.5332,t3.getRadius(),0);
    }

    /**
     * Tests getting the vector position of a sphere
     * @throws Exception
     */
    @Test public void getPosition() throws Exception
    {
        Sphere t1 = new Sphere(10, new Vector3(1,2,3));
        Sphere t2 = new Sphere(10, new Vector3(12.92,2.492,3.333));
        Sphere t3 = new Sphere(10, new Vector3(5,10,20));

        Assert.assertEquals(new Vector3(1,2,3),t1.getPosition());
        Assert.assertEquals(new Vector3(12.92,2.492,3.333),t2.getPosition());
        Assert.assertEquals(new Vector3(5,10,20),t3.getPosition());
    }

    /**
     * Tests setting the vector position of a sphere.
     * @throws Exception
     */
    @Test public void setPosition() throws Exception
    {
        Sphere t1 = new Sphere(10, new Vector3(1,2,3));
        Sphere t2 = new Sphere(10, new Vector3(12.92,2.492,3.333));
        Sphere t3 = new Sphere(10, new Vector3(5,10,20));

        t1.setPosition(new Vector3(38.92,1.234,3.8363)); // From integer to double
        t2.setPosition(new Vector3(12,2,4)); // From double to integer
        t3.setPosition(new Vector3(8.393,10,1.233)); // Combination

        Assert.assertEquals(new Vector3(38.92,1.234,3.8363),t1.getPosition());
        Assert.assertEquals(new Vector3(12,2,4),t2.getPosition());
        Assert.assertEquals(new Vector3(8.393,10,1.233),t3.getPosition());
    }

    /**
     * Tests Ray intersection with a sphere
     * @throws Exception
     */
    @Test public void intersect() throws Exception
    {
        Sphere ts1 = new Sphere(1, new Vector3(0, 0, 0));
        Sphere ts2 = new Sphere(0.1, new Vector3(0, 0, 1));

        Ray3 tr1 = new Ray3(new Vector3(0, 0, 0), new Vector3(0, 0, 55));
        Ray3 tr2 = new Ray3(new Vector3(1.1, 0, 0), new Vector3(0, 0, 1));
        Ray3 tr3 = new Ray3(new Vector3(0, 0, 0), new Vector3(0, 0, 1));


        Assert.assertEquals(new Vector3(0, 0, 55), ts1.intersect(tr1)); //straightforward intersection
        Assert.assertNull(ts1.intersect(tr2)); //does not intersect
        Assert.assertEquals(new Vector3(0, 0, 0.9), ts2.intersect(tr3)); //Different sized sphere intersection



    }

}