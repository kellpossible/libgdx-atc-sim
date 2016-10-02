package com.atc.simulator.vectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import pythagoras.d.Ray3;
import pythagoras.d.Vector3;

import static org.junit.Assert.assertEquals;

/** 
* Sphere Tester. 
* 
* @author Luke Frisken
*/ 
public class TestSphere {
    private static double EPSILON = 0.0001;

    /**
     * Before the tests.
     * @throws Exception exception thrown
     */
    @Before
    public void before() throws Exception {

    }

    /**
     * After the tests
     * @throws Exception exception thrown
     */
    @After
    public void after() throws Exception {
    }

    /**
    *
    * Method: getRadius()
    *
    */
    @Test
    public void testGetRadius1() throws Exception {
        Sphere sphere1 = new Sphere(1.0, Vector3.ZERO);
        Sphere sphere2 = new Sphere(0.1, new Vector3(0, 0, 1));
        Assert.assertTrue(sphere1.getRadius() == 1.0);
        Assert.assertTrue(sphere2.getRadius() == 0.1);
    }

    /**
     * Method: getRadius()
     * @throws Exception
     */
    @Test public void testGetRadius2() throws Exception
    {
        Sphere t1 = new Sphere(10, new Vector3(1,2,3));
        Sphere t2 = new Sphere(27.9823, new Vector3(1,2,3));
        Sphere t3 = new Sphere(58.20203, new Vector3(1,2,3));

        Assert.assertEquals(10,t1.getRadius(),0);
        Assert.assertEquals(27.9823,t2.getRadius(),0);
        Assert.assertEquals(58.20203,t3.getRadius(),0);
    }

    /**
    *
    * Method: setRadius(double radius)
    * Tests setting the radius of a sphere
    */
    @Test
    public void testSetRadius1() throws Exception {
        Sphere sphere1 = new Sphere(1.0, Vector3.ZERO);
        sphere1.setRadius(0.8);
        Assert.assertEquals(0.8, sphere1.getRadius(), EPSILON);
    }

    /**
    * Method: setRadius(double radius)
    * Tests setting the radius of a sphere
    * @throws Exception
    */
    @Test public void testSetRadius2() throws Exception
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
    *
    * Method: getPosition()
    * Tests getting the vector position of a sphere
     * @throws Exception
    */
    @Test
    public void testGetPosition1() throws Exception {
        Sphere sphere1 = new Sphere(1.0, Vector3.ZERO);
        Sphere sphere2 = new Sphere(0.1, new Vector3(0, 0, 1));
        Assert.assertEquals(sphere1.getPosition(), Vector3.ZERO);
        Assert.assertEquals(new Vector3(0, 0, 1), sphere2.getPosition());
    }

    /**
     * Method: getPosition()
     * Tests getting the vector position of a sphere
     * @throws Exception
     */
    @Test public void testGetPosition2() throws Exception
    {
        Sphere t1 = new Sphere(10, new Vector3(1,2,3));
        Sphere t2 = new Sphere(10, new Vector3(12.92,2.492,3.333));
        Sphere t3 = new Sphere(10, new Vector3(5,10,20));

        Assert.assertEquals(new Vector3(1,2,3),t1.getPosition());
        Assert.assertEquals(new Vector3(12.92,2.492,3.333),t2.getPosition());
        Assert.assertEquals(new Vector3(5,10,20),t3.getPosition());
    }

    /**
    *
    * Method: setPosition(Vector3 position)
    */
    @Test
    public void testSetPosition1() throws Exception {
        Sphere sphere1 = new Sphere(1.0, Vector3.ZERO);
        sphere1.setPosition(new Vector3(0.8, 0.2, 100.0));
        Assert.assertEquals(new Vector3(0.8, 0.2, 100.0), sphere1.getPosition());
    }

    /**
     * Method: setPosition(Vector3 position)
     * Tests setting the vector position of a sphere.
     * @throws Exception
     */
    @Test public void testSetPosition2() throws Exception
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
    *
    * Method: intersect(Ray3 ray)
    * Test a ray shooting up in the z direction from the inside of the sphere,
     * which should intersect.
    */
    @Test
    public void testIntersect1() throws Exception {
        Sphere sphere1 = new Sphere(1.0, Vector3.ZERO);
        Ray3 ray = new Ray3(new Vector3(0, 0, 0), new Vector3(0, 0, 1));
        Assert.assertEquals(new Vector3(0, 0, 1), sphere1.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     * test a ray shooting up in the z direction from outside the sphere,
     * which should not intersect with the spot if ray origin translation did not work.
     */
    @Test
    public void testIntersect2() throws Exception {
        Sphere sphere1 = new Sphere(1.0, Vector3.ZERO);
        Ray3 ray = new Ray3(new Vector3(0.5, 0, 0), new Vector3(0, 0, 1));
        Assert.assertNotEquals(new Vector3(0, 0, 1), sphere1.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     * Test a ray shooting up in the y direction from outside the sphere,
     * which should not intersect.
     */
    @Test
    public void testIntersect3() throws Exception {
        Sphere sphere1 = new Sphere(1.0, Vector3.ZERO);
        Ray3 ray = new Ray3(new Vector3(1.1, 0, 0), new Vector3(0, 0, 1));
        Assert.assertNull(sphere1.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     * Test a ray shooting out in the x direction from inside the sphere,
     * which should intersect.
     */
    @Test
    public void testIntersect4() throws Exception {
        Sphere sphere1 = new Sphere(1.0, Vector3.ZERO);
        Ray3 ray = new Ray3(new Vector3(0, 0, 0), new Vector3(0, 1, 0));
        Assert.assertEquals(new Vector3(0, 1, 0), sphere1.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     * Test a ray shooting out in the z direction intersects in the correct spot
     * on the inside of a sphere with a different size.
     */
    @Test
    public void testIntersect5() throws Exception {
        Sphere sphere1 = new Sphere(1.0, Vector3.ZERO);
        Sphere sphere2 = new Sphere(0.1, new Vector3(0, 0, 1));
        Ray3 ray = new Ray3(new Vector3(0, 0, 0), new Vector3(0, 0, 1));
        Assert.assertEquals(new Vector3(0, 0, 0.9), sphere2.intersect(ray));
    }

    /**
     *
     * Method: intersect(Ray3 ray)
     * Test a ray shooting off in the negative x direction from outside the sphere
     * intersects with the sphere in the correct location.
     */
    @Test
    public void testIntersect6() throws Exception {
        Sphere sphere2 = new Sphere(0.1, new Vector3(0, 0, 1));
        Ray3 ray = new Ray3(new Vector3(1, 0, 1), new Vector3(-1, 0, 0));
        Assert.assertEquals(0.1, sphere2.intersect(ray).x, EPSILON);
        Assert.assertEquals(0.0, sphere2.intersect(ray).y, EPSILON);
        Assert.assertEquals(1.0, sphere2.intersect(ray).z, EPSILON);
    }

    /**
     * Method: intersect(Ray3 ray)
     * Tests Ray intersection with a sphere
     * @throws Exception
     */
    @Test public void testIntersect7() throws Exception
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


    /**
     * Method: arcDistance(SphericalCoordinate, SphericalCoordinate)
     * @throws Exception
     */
    @Test
    public void testArcDistance1() throws Exception {
        GeographicCoordinate pos1 = GeographicCoordinate.fromDegrees(0, 38, 148);
        GeographicCoordinate pos2 = GeographicCoordinate.fromDegrees(0, 38, 149);

        Sphere sphere = Sphere.EARTH;

        double d = sphere.arcDistance(pos1, pos2);
        assertEquals(87622.0, d, 2.0);
    }

    /**
     * Method: arcDistance(SphericalCoordinate, SphericalCoordinate)
     * @throws Exception
     */
    @Test
    public void testArcDistance2() throws Exception {
        GeographicCoordinate pos1 = GeographicCoordinate.fromDegrees(0, -40, 120);
        GeographicCoordinate pos2 = GeographicCoordinate.fromDegrees(0, -45, 170);

        Sphere sphere = Sphere.EARTH;

        double d = sphere.arcDistance(pos1, pos2);
        assertEquals(4070969.0, d, 10.0);
    }


} 
