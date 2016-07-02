package com.atc.simulator.vectors;

import pythagoras.d.Vector3;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chris on 2/07/2016.
 */
public class GeographicCoordinateTest {
    GeographicCoordinate constGeoCoord1, constGeoCoord2, constGeoCoord3, constGeoCoord4;
    GeographicCoordinate testGeoCoord;
    Vector3 cartesianCoord;

    @Before
    public void before() throws Exception {
        constGeoCoord1 = new GeographicCoordinate(0, 1, 1);
        constGeoCoord2 = new GeographicCoordinate(31, -0.65990899, 2.53002928);
        constGeoCoord3 = new GeographicCoordinate(0, 0.682422201638, -1.65075357738);
        constGeoCoord4 = new GeographicCoordinate(0, 0.674169883509, -1.57429052831);
    }

    //Test whether converting to and from cartesian keeps the coordinate information as the original
    @Test
    public void fromCartesian() throws Exception {
        //set the variable to a constant Coordinate
        testGeoCoord = new GeographicCoordinate(constGeoCoord1);
        //Transform it into a Cartesian Coordinate and back
        cartesianCoord = testGeoCoord.getCartesian();
        testGeoCoord = GeographicCoordinate.fromCartesian(cartesianCoord);
        //Assert for equality
        assertTrue(testGeoCoord.almostEqual(constGeoCoord1, 0.0001));
            //--------------------------------------------
        //set the variable to a constant Coordinate
        testGeoCoord = new GeographicCoordinate(constGeoCoord2);
        //Transform it into a Cartesian Coordinate and back
        cartesianCoord = testGeoCoord.getCartesian();
        testGeoCoord = GeographicCoordinate.fromCartesian(cartesianCoord);
        //Assert for equality
        assertTrue(testGeoCoord.almostEqual(constGeoCoord2, 0.0001));
            //--------------------------------------------

    }

    @Test
    public void bearingTo1() throws Exception {
        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
    }

    @Test
    public void bearingTo() throws Exception {
        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
    }

    @Test
    public void bearingTo3() throws Exception {
        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
    }

    @Test
    public void bearingTo4() throws Exception {
        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
    }

}