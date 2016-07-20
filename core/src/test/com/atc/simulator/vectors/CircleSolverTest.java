package com.atc.simulator.vectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import pythagoras.d.Circle;
import pythagoras.d.Vector3;

/** 
* CircleSolver Tester. 
* 
* @author <Authors name> 
* @since <pre>Jul 20, 2016</pre> 
* @version 1.0 
*/ 
public class CircleSolverTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

    /**
    *
    * Method: FromThreePoints(Vector3 p1, Vector3 p2, Vector3 p3)
    *
    */
    @Test
    public void testFromThreePoints1() throws Exception {
        Vector3 p1 = new Vector3(1, 0, 0);
        Vector3 p2 = new Vector3(0, 1, 0);
        Vector3 p3 = new Vector3(-1, 0, 0);
        Circle circle = CircleSolver.FromThreePoints(p1, p2, p3);

        Assert.assertEquals(0, circle.x, 0.0001);
        Assert.assertEquals(0, circle.y, 0.0001);
        Assert.assertEquals(1.0, circle.radius, 0.0001);

        System.out.println("Circle (" + circle.x + "," + circle.y + "," + circle.radius + ")");
}

    /**
     *
     * Method: FromThreePoints(Vector3 p1, Vector3 p2, Vector3 p3)
     *
     */
    @Test
    public void testFromThreePoints2() throws Exception {
        Vector3 p1 = new Vector3(-4, 4, 0);
        Vector3 p2 = new Vector3(-7, 3, 0);
        Vector3 p3 = new Vector3(-8, 2, 0);
        Circle circle = CircleSolver.FromThreePoints(p1, p2, p3);

        Assert.assertEquals(-4, circle.x, 0.0001);
        Assert.assertEquals(-1, circle.y, 0.0001);
        Assert.assertEquals(5.0, circle.radius, 0.0001);

        System.out.println("Circle (" + circle.x + "," + circle.y + "," + circle.radius + ")");


    }

    /**
     *
     * Method: FromThreePoints(Vector3 p1, Vector3 p2, Vector3 p3)
     *
     */
    @Test
    public void testFromThreePoints3() throws Exception {
        Vector3 p1 = new Vector3(-23, 8, 0);
        Vector3 p2 = new Vector3(4.231, 82.5, 0);
        Vector3 p3 = new Vector3(1000.123, 456, 0);
        Circle circle = CircleSolver.FromThreePoints(p1, p2, p3);

        Assert.assertEquals(680.79899, circle.x, 0.0001);
        Assert.assertEquals(-207.02364, circle.y, 0.0001);
        Assert.assertEquals(735.91316, circle.radius, 0.0001);

        System.out.println("Circle (" + circle.x + "," + circle.y + "," + circle.radius + ")");
    }


} 
