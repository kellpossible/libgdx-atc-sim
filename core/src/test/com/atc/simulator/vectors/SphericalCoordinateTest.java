package com.atc.simulator.vectors;

import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import pythagoras.d.Vector3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** 
* SphericalCoordinate Tester. 
* 
* @author <Authors name> 
* @since <pre>May 27, 2016</pre> 
* @version 1.0 
*/ 
public class SphericalCoordinateTest {
    SphericalCoordinate sc1, sc2, sc3, sc4, sc5, sc6, sc7;

@Before
public void before() throws Exception {
    sc1 = new SphericalCoordinate(0, 0, 0);
    sc2 = new SphericalCoordinate(1, 1, 1);
    sc3 = new SphericalCoordinate(23.45, 0, 0);
    sc4 = new SphericalCoordinate(0.2, 1.58, 0.2123);
    sc5 = new SphericalCoordinate(0.4, 0.56, 0.67);
    sc6 = new SphericalCoordinate(0.4, Math.PI, Math.PI);
    sc7 = new SphericalCoordinate(6371000.0, -2.22839275571506, 4.0988411711263595);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: fromCartesian(Vector3 cv) 
* 
*/ 
@Test
public void testFromCartesian() throws Exception {
    Vector3 cv;
    SphericalCoordinate scT;

    scT = sc4;
    cv = scT.getCartesian();
    System.out.println(cv);

    SphericalCoordinate scC = SphericalCoordinate.fromCartesian(cv);
    assertTrue(scT.almostEqual(scC, 0.0001));

    scT = sc5;
    cv = scT.getCartesian();
    scC = SphericalCoordinate.fromCartesian(cv);
    assertTrue(scT.almostEqual(scC, 0.0001));

    scT = sc6;
    cv = scT.getCartesian();
    scC = SphericalCoordinate.fromCartesian(cv);
    cv = scC.getCartesian();
    SphericalCoordinate scC2 = SphericalCoordinate.fromCartesian(cv);
    assertEquals("x value",
            scC.x,
            scC2.x,
            0.001);
    assertEquals("y value",
            scC.y,
            scC2.y,
            0.001);
    assertEquals("z value",
            scC.z,
            scC2.z,
            0.001);

    scT = sc7;
    Vector3 cv1 = scT.getCartesian();
    scC = SphericalCoordinate.fromCartesian(cv1);
    Vector3 cv2 = scC.getCartesian();
    scC2 = SphericalCoordinate.fromCartesian(cv2);
    System.out.println("SCC" + scC);
    System.out.println("cv1" + cv1);
    System.out.println("cv2" + cv2);

    assertEquals("x value",
            cv1.x,
            cv2.x,
            1);
    assertEquals("y value",
            cv1.y,
            cv2.y,
            0.001);
    assertEquals("z value",
            cv1.z,
            cv2.z,
            0.001);
} 

/** 
* 
* Method: getR() 
* 
*/ 
@Test
public void testGetR() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getTheta() 
* 
*/ 
@Test
public void testGetTheta() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getPhi() 
* 
*/ 
@Test
public void testGetPhi() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getCartesian() 
* 
*/ 
@Test
public void testGetCartesian() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getCartesianDrawVector() 
* 
*/ 
@Test
public void testGetCartesianDrawVector() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getModelDrawVector() 
* 
*/ 
@Test
public void testGetModelDrawVector() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getModelDrawVector(double adjust) 
* 
*/ 
@Test
public void testGetModelDrawVectorAdjust() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: arcDistance(SphericalCoordinate other) 
* 
*/ 
@Test
public void testArcDistance() throws Exception { 
//TODO: Test goes here... 
} 


} 
