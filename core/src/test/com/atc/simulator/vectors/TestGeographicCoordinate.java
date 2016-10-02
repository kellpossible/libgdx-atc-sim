package com.atc.simulator.vectors;

import org.junit.Assert;
import pythagoras.d.Vector3;
import org.junit.Before;
import org.junit.Test;

import static java.lang.Math.PI;
import static org.junit.Assert.*;

/**
 * @author Chris Coleman
 * @author Luke Frisken
 * @author Adam Miritis
 */
public class TestGeographicCoordinate {
    @Before
    public void before() throws Exception {
    }

    /**
     * Method: GeographicCoordinate(GeographicCoordinate)
     * Test whether the copy constructors work
     */
    @Test
    public void testCopyConstructor1() throws Exception {
        GeographicCoordinate coord1 = GeographicCoordinate.fromDegrees(100, -30, -23);
        GeographicCoordinate copy = new GeographicCoordinate(coord1);
        assertEquals(0, coord1.distance(copy), 0.0001);
    }

    /**
     * Method: GeographicCoordinate(GeographicCoordinate)
     * Test whether the copy constructors work
     */
    @Test
    public void testCopyConstructor2() throws Exception {
        GeographicCoordinate coord1 = GeographicCoordinate.fromDegrees(-100, 58, 160);
        GeographicCoordinate copy = new GeographicCoordinate(coord1);
        assertEquals(0, coord1.distance(copy), 0.0001);
    }


    /**
     * Method: fromCartesian()
     * Test whether converting to and from cartesian keeps the coordinate information as the original
     */
    @Test
    public void fromCartesian1() throws Exception {
        GeographicCoordinate coord1 = GeographicCoordinate.fromDegrees(100, -30, -23);
        Vector3 cart1 = coord1.getCartesian();
        GeographicCoordinate coord2 = GeographicCoordinate.fromCartesian(cart1);

        //accurate to within 0.2 meters
        assertEquals(0, coord2.distance(coord1), 0.2);
    }

//    @Test
//    public void bearingTo1() throws Exception {
//        assertEquals("Same coordinate",
//                PI/2, constGeoCoord1.bearingTo(constGeoCoord1), 0.01);
//        assertEquals("Horizontally opposite side of globe",
//                0, new GeographicCoordinate(0, 0, 0).bearingTo(new GeographicCoordinate(0, 0, PI)), 0.01);
//        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
//                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
//        assertEquals("http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates",
//                4.36332, constGeoCoord5.bearingTo(constGeoCoord6), 0.01);
//    }
//
//    @Test
//    public void bearingTo() throws Exception {
//        assertEquals("Same coordinate",
//                PI/2, constGeoCoord1.bearingTo(constGeoCoord1), 0.01);
//        assertEquals("Horizontally opposite side of globe",
//                0, new GeographicCoordinate(0, 0, 0).bearingTo(new GeographicCoordinate(0, 0, PI)), 0.01);
//        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
//                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
//        assertEquals("http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates",
//                4.36332, constGeoCoord5.bearingTo(constGeoCoord6), 0.01);
//    }
//
//    @Test
//    public void bearingTo3() throws Exception {
//        assertEquals("Same coordinate",
//                PI/2, constGeoCoord1.bearingTo(constGeoCoord1), 0.01);
//        assertEquals("Horizontally opposite side of globe",
//                0, new GeographicCoordinate(0, 0, 0).bearingTo(new GeographicCoordinate(0, 0, PI)), 0.01);
//        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
//                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
//        assertEquals("http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates",
//                4.36332, constGeoCoord5.bearingTo(constGeoCoord6), 0.01);
//    }
//
//    @Test
//    public void bearingTo4() throws Exception {
//        assertEquals("Same coordinate",
//                PI/2, constGeoCoord1.bearingTo(constGeoCoord1), 0.01);
//        assertEquals("Horizontally opposite side of globe",
//                0, new GeographicCoordinate(0, 0, 0).bearingTo(new GeographicCoordinate(0, 0, PI)), 0.01);
//        assertEquals("http://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/",
//                1.68441726, constGeoCoord3.bearingTo(constGeoCoord4), 0.01);
//        assertEquals("http://gis.stackexchange.com/questions/29239/calculate-bearing-between-two-decimal-gps-coordinates",
//                4.36332, constGeoCoord5.bearingTo(constGeoCoord6), 0.01);
//    }

    @Test
    public void arcDistanceTest2() throws Exception
    {
        GeographicCoordinate pos1 = GeographicCoordinate.fromDegrees(0, 38, 148);
        GeographicCoordinate pos2 = GeographicCoordinate.fromDegrees(0, 38, 149);

        double d = pos1.arcDistance(pos2);
        assertEquals(87620.0, d, 10);
    }

    /**
     *
     * Method: arcDistance(SphericalCoordinate other)
     *
     */
    @Test
    public void arcDistanceTest3() throws Exception
    {
        GeographicCoordinate pos1 = GeographicCoordinate.fromDegrees(0, -40, 120);
        GeographicCoordinate pos2 = GeographicCoordinate.fromDegrees(0, -45, 170);

        double d = pos1.arcDistance(pos2);
        assertEquals(4071000, d, 1000);
    }

    /**
     *
     * @throws Exception
     */
    @Test public void arcDistance() throws Exception
    {

    }

    /**
     * Tests the creation of a Geographic Coordinate from degrees(Altitude, Latitude, Longitude).
     * @throws Exception
     */
    @Test public void fromDegrees() throws Exception
    {
        GeographicCoordinate t1 = GeographicCoordinate.fromDegrees(95,20,22);
        GeographicCoordinate t2 = GeographicCoordinate.fromDegrees(92.222,38.3,222.3);
        GeographicCoordinate t3 = GeographicCoordinate.fromDegrees(72.2,67.2232,22.3);

        Assert.assertEquals(95,t1.getAltitude(),0.001);
        Assert.assertEquals(0.349066,t1.getLatitude(),0.001);
        Assert.assertEquals(0.383972,t1.getLongitude(),0.001);

        Assert.assertEquals(92.222, t2.getAltitude(),0.001);
        Assert.assertEquals(0.6684611,t2.getLatitude(),0.001);
        Assert.assertEquals(3.8798669,t2.getLongitude(),0.001);

        Assert.assertEquals(72.2, t3.getAltitude(),0.001);
        Assert.assertEquals(1.173266174,t3.getLatitude(),0.001);
        Assert.assertEquals(0.3892084,t3.getLongitude(),0.001);
    }

    /**
     * Tests returning the Latitude
     * @throws Exception
     */
    @Test public void getLatitude() throws Exception
    {
        GeographicCoordinate t1 = new GeographicCoordinate(95,20,22);
        GeographicCoordinate t2 = new GeographicCoordinate(92.222,38.3,222.3);
        GeographicCoordinate t3 = new GeographicCoordinate(72.2,67.2232,22.3);

        Assert.assertEquals(20,t1.getLatitude(),0.001);
        Assert.assertEquals(38.3,t2.getLatitude(),0.001);
        Assert.assertEquals(67.2232,t3.getLatitude(),0.001);
    }

    /**
     * Tests getting a new latitude.
     * @throws Exception
     */
    @Test public void setLatitude() throws Exception
    {
        GeographicCoordinate t1 = new GeographicCoordinate(95,20,22);
        GeographicCoordinate t2 = new GeographicCoordinate(92.222,38.3,222.3);
        GeographicCoordinate t3 = new GeographicCoordinate(72.2,67.2232,22.3);

        t1.setLatitude(17.39372);
        t2.setLatitude(0);
        t3.setLatitude(20.28);

        Assert.assertEquals(17.39372,t1.getLatitude(),0.001);
        Assert.assertEquals(0,t2.getLatitude(),0.001);
        Assert.assertEquals(20.28,t3.getLatitude(),0.001);
    }
    /**
     * Tests returning the Longitude
     * @throws Exception
     */
    @Test public void getLongitude() throws Exception
    {
        GeographicCoordinate t1 = new GeographicCoordinate(95,20,22);
        GeographicCoordinate t2 = new GeographicCoordinate(92.222,38.3,222.3);
        GeographicCoordinate t3 = new GeographicCoordinate(72.2,67.2232,22.3);

        Assert.assertEquals(22,t1.getLongitude(),0.001);
        Assert.assertEquals(222.3,t2.getLongitude(),0.001);
        Assert.assertEquals(22.3,t3.getLongitude(),0.001);
    }

    /**
     * Tests setting a new longitude
     * @throws Exception
     */
    @Test public void setLongitude() throws Exception
    {
        GeographicCoordinate t1 = new GeographicCoordinate(95,20,22);
        GeographicCoordinate t2 = new GeographicCoordinate(92.222,38.3,222.3);
        GeographicCoordinate t3 = new GeographicCoordinate(72.2,67.2232,22.3);

        t1.setLongitude(17.39372);
        t2.setLongitude(0);
        t3.setLongitude(20.28);

        Assert.assertEquals(17.39372,t1.getLongitude(),0.001);
        Assert.assertEquals(0,t2.getLongitude(),0.001);
        Assert.assertEquals(20.28,t3.getLongitude(),0.001);
    }
    /**
     * Tests returning the Altitude
     * @throws Exception
     */
    @Test public void getAltitude() throws Exception
    {
        GeographicCoordinate t1 = new GeographicCoordinate(95,20,22);
        GeographicCoordinate t2 = new GeographicCoordinate(92.222,38.3,222.3);
        GeographicCoordinate t3 = new GeographicCoordinate(72.2,67.2232,22.3);

        Assert.assertEquals(95,t1.getAltitude(),0.001);
        Assert.assertEquals(92.222,t2.getAltitude(),0.001);
        Assert.assertEquals(72.2,t3.getAltitude(),0.001);
    }

    /**
     * Tests the setting of altitude
     * @throws Exception
     */
    @Test public void setAltitude() throws Exception
    {
        GeographicCoordinate t1 = new GeographicCoordinate(95,20,22);
        GeographicCoordinate t2 = new GeographicCoordinate(92.222,38.3,222.3);
        GeographicCoordinate t3 = new GeographicCoordinate(72.2,67.2232,22.3);

        Assert.assertEquals(95,t1.getAltitude(),0.001);
        Assert.assertEquals(92.222,t2.getAltitude(),0.001);
        Assert.assertEquals(72.2,t3.getAltitude(),0.001);

        t1.setLatitude(17.39372);
        t2.setLatitude(0);
        t3.setLatitude(20.28);

        Assert.assertEquals(95,t1.getAltitude(),0.001);
        Assert.assertEquals(92.222,t2.getAltitude(),0.001);
        Assert.assertEquals(72.2,t3.getAltitude(),0.001);

        t1.setAltitude(17.39372);
        t2.setAltitude(0);
        t3.setAltitude(20.28);

        Assert.assertEquals(17.39372,t1.getAltitude(),0.001);
        Assert.assertEquals(0,t2.getAltitude(),0.001);
        Assert.assertEquals(20.28,t3.getAltitude(),0.001);
    }

    /**
     * Tests converting the Geographical coordinate to a string.
     * Please note, needed the name change from toString to toStringTest to satisfy Java and remove it throwing an error for returning void on toString.
     * @throws Exception
     */
    @Test public void toStringTest() throws Exception
    {
        GeographicCoordinate t1 = new GeographicCoordinate(95,20,22);
        GeographicCoordinate t2 = new GeographicCoordinate(92.222,38.3,222.3);
        GeographicCoordinate t3 = new GeographicCoordinate(72.2,67.2232,22.3);

        Assert.assertEquals("[95.0, 1145.9155902616465, 1260.5071492878112] (degrees)",t1.toString());
        Assert.assertEquals("[92.22200000006706, 2194.4283553510527, 12736.851785758201] (degrees)",t2.toString());
        Assert.assertEquals("[72.20000000018626, 3851.605645363836, 1277.6958831417357] (degrees)",t3.toString());
    }

    /**
     * Tests bearing from one geographical coordinate to another.
     * @throws Exception
     */
    @Test public void bearingTo2() throws Exception
    {
        GeographicCoordinate t1 = new GeographicCoordinate(95,20,22);
        GeographicCoordinate t2 = new GeographicCoordinate(92.222,38.3,222.3);
        GeographicCoordinate t3 = new GeographicCoordinate(72.2,67.2232,22.3);

        Assert.assertEquals(4.208291210486665,t1.bearingTo(t2),0.01);
        Assert.assertEquals(3.518705,t2.bearingTo(t3),0.01);
        assertEquals(-0.9744,t3.bearingTo(t1),0.01);
    }

}