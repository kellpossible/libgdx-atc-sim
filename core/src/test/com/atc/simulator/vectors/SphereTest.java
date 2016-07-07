package com.atc.simulator.vectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import pythagoras.d.Ray3;
import pythagoras.d.Vector3;

/** 
* Sphere Tester. 
* 
* @author Luke Frisken
*/ 
public class SphereTest {
    Sphere sphere1;
    Sphere sphere2;

    static double EPSILON = 0.0001;

    @Before
    public void before() throws Exception {
        sphere1 = new Sphere(1.0, Vector3.ZERO);
        sphere2 = new Sphere(0.1, new Vector3(0, 0, 1));
    }

    @After
    public void after() throws Exception {
    }

    /**
    *
    * Method: getRadius()
    *
    */
    @Test
    public void testGetRadius() throws Exception {
        Assert.assertTrue(sphere1.getRadius() == 1.0);
        Assert.assertTrue(sphere2.getRadius() == 0.1);
    }

    /**
    *
    * Method: setRadius(double radius)
    *
    */
    @Test
    public void testSetRadius() throws Exception {
        sphere1.setRadius(0.8);
        Assert.assertEquals(0.8, sphere1.getRadius(), EPSILON);
    }

    /**
    *
    * Method: getPosition()
    *
    */
    @Test
    public void testGetPosition() throws Exception {
        Assert.assertEquals(sphere1.getPosition(), Vector3.ZERO);
        Assert.assertEquals(new Vector3(0, 0, 1), sphere2.getPosition());
    }

    /**
    *
    * Method: setPosition(Vector3 position)
    *
    */
    @Test
    public void testSetPosition() throws Exception {
        sphere1.setPosition(new Vector3(0.8, 0.2, 100.0));
        Assert.assertEquals(new Vector3(0.8, 0.2, 100.0), sphere1.getPosition());
    }

    /**
    *
    * Method: intersect(Ray3 ray)
    *
    */
    @Test
    public void testIntersect1() throws Exception {
        Ray3 ray = new Ray3(new Vector3(0, 0, 0), new Vector3(0, 0, 1));
        Assert.assertEquals(new Vector3(0, 0, 1), sphere1.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     *
     */
    @Test
    public void testIntersect2() throws Exception {
        Ray3 ray = new Ray3(new Vector3(0.5, 0, 0), new Vector3(0, 0, 1));
        Assert.assertNotEquals(new Vector3(0, 0, 1), sphere1.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     *
     */
    @Test
    public void testIntersect3() throws Exception {
        Ray3 ray = new Ray3(new Vector3(1.1, 0, 0), new Vector3(0, 0, 1));
        Assert.assertNull(sphere1.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     *
     */
    @Test
    public void testIntersect4() throws Exception {
        Ray3 ray = new Ray3(new Vector3(0, 0, 0), new Vector3(0, 1, 0));
        Assert.assertEquals(new Vector3(0, 1, 0), sphere1.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     *
     */
    @Test
    public void testIntersect5() throws Exception {
        Ray3 ray = new Ray3(new Vector3(0, 0, 0), new Vector3(0, 0, 1));
        Assert.assertEquals(new Vector3(0, 0, 0.9), sphere2.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     *
     */
    @Test
    public void testIntersect6() throws Exception {
        Ray3 ray = new Ray3(new Vector3(1, 0, 1), new Vector3(-1, 0, 0));
        Assert.assertEquals(0.1, sphere2.intersect(ray).x, EPSILON);
        Assert.assertEquals(0.0, sphere2.intersect(ray).y, EPSILON);
        Assert.assertEquals(1.0, sphere2.intersect(ray).z, EPSILON);
    }


} 
