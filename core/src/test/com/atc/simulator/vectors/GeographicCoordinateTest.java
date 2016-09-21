package com.atc.simulator.vectors;

import pythagoras.d.Vector3;
import org.junit.Before;
import org.junit.Test;

import static java.lang.Math.PI;
import static org.junit.Assert.*;

/**
 * @author Chris Coleman
 */
public class GeographicCoordinateTest {
    GeographicCoordinate constGeoCoord1, constGeoCoord2, constGeoCoord3, constGeoCoord4, constGeoCoord5, constGeoCoord6, northPole, southPole;
    GeographicCoordinate testGeoCoord;
    Vector3 cartesianCoord;

    @Before
    public void before() throws Exception {
        constGeoCoord1 = new GeographicCoordinate(0, 1, 1);
        constGeoCoord2 = new GeographicCoordinate(31, -0.65990899, 2.53002928);
        constGeoCoord3 = new GeographicCoordinate(0, 0.682422201638, -1.65075357738);
        constGeoCoord4 = new GeographicCoordinate(0, 0.674169883509, -1.57429052831);
        constGeoCoord5 = new GeographicCoordinate(0, 0.762398441406, -1.22959660552);
        constGeoCoord6 = new GeographicCoordinate(0, 0.762398109793, -1.22959787961);
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
        assertEquals("Same coordinate",
                PI/2, constGeoCoord1.bearingTo(constGeoCoord1), 0.01);
        assertEquals("Horizontally opposite side of globe",
                0, new GeographicCoordinate(0, 0, 0).bearingTo(new GeographicCoordinate(0, 0, PI)), 0.01);
        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
        assertEquals("http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates",
                4.36332, constGeoCoord5.bearingTo(constGeoCoord6), 0.01);
    }

    @Test
    public void bearingTo() throws Exception {
        assertEquals("Same coordinate",
                PI/2, constGeoCoord1.bearingTo(constGeoCoord1), 0.01);
        assertEquals("Horizontally opposite side of globe",
                0, new GeographicCoordinate(0, 0, 0).bearingTo(new GeographicCoordinate(0, 0, PI)), 0.01);
        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
        assertEquals("http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates",
                4.36332, constGeoCoord5.bearingTo(constGeoCoord6), 0.01);
    }

    @Test
    public void bearingTo3() throws Exception {
        assertEquals("Same coordinate",
                PI/2, constGeoCoord1.bearingTo(constGeoCoord1), 0.01);
        assertEquals("Horizontally opposite side of globe",
                0, new GeographicCoordinate(0, 0, 0).bearingTo(new GeographicCoordinate(0, 0, PI)), 0.01);
        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
        assertEquals("http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates",
                4.36332, constGeoCoord5.bearingTo(constGeoCoord6), 0.01);
    }

    @Test
    public void bearingTo4() throws Exception {
        assertEquals("Same coordinate",
                PI/2, constGeoCoord1.bearingTo(constGeoCoord1), 0.01);
        assertEquals("Horizontally opposite side of globe",
                0, new GeographicCoordinate(0, 0, 0).bearingTo(new GeographicCoordinate(0, 0, PI)), 0.01);
        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
        assertEquals("http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates",
                4.36332, constGeoCoord5.bearingTo(constGeoCoord6), 0.01);
    }

    @Test
    public void arcDistanceTest() throws Exception{
        GeographicCoordinate temp1 = new GeographicCoordinate(6371000,40,-73);
        GeographicCoordinate temp2 = new GeographicCoordinate(6371000,50,-70);
        assertEquals("http://www.movable-type.co.uk/scripts/latlong.html",1136000, temp1.arcDistance(temp2),3000);

    }

}