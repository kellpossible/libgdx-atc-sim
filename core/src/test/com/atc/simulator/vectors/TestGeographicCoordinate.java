package com.atc.simulator.vectors;

import org.junit.Assert;
import org.junit.Test;
import pythagoras.d.Vector3;

import static org.junit.Assert.*;

/**
 * Tests the Geogrpahic Coordinate Class
 * @author Adam Miritis
 */
public class TestGeographicCoordinate
{
    /**
     * Tests the creation of a Geographic Coordinate from degrees(Altitude, Latitude, Longitude).
     * @throws Exception
     */
    @Test public void fromDegrees() throws Exception
    {
        GeographicCoordinate t1 = GeographicCoordinate.fromDegrees(95,20,22);
        GeographicCoordinate t2 = GeographicCoordinate.fromDegrees(92.222,38.3,222.3);
        GeographicCoordinate t3 = GeographicCoordinate.fromDegrees(72.2,67.2232,22.3);

        Assert.assertEquals(1.65806,t1.getAltitude(),0.001);
        Assert.assertEquals(0.349066,t1.getLatitude(),0.001);
        Assert.assertEquals(0.383972,t1.getLongitude(),0.001);

        Assert.assertEquals(1.60957754, t2.getAltitude(),0.001);
        Assert.assertEquals(0.6684611,t2.getLatitude(),0.001);
        Assert.assertEquals(3.8798669,t2.getLongitude(),0.001);

        Assert.assertEquals(1.260128, t3.getAltitude(),0.001);
        Assert.assertEquals(1.173266174,t3.getLatitude(),0.001);
        Assert.assertEquals(0.3892084,t3.getLongitude(),0.001);
    }

    @Test public void fromCartesian() throws Exception
    {
        GeographicCoordinate t1 = GeographicCoordinate.fromCartesian(new Vector3(1,2,3));
        GeographicCoordinate t2 = GeographicCoordinate.fromCartesian(new Vector3(32.23,23,3.5));
        GeographicCoordinate t3 = GeographicCoordinate.fromCartesian(new Vector3(29.8,27.8,76.36));

        Assert.assertEquals(-0.9302,t1.getLatitude(),0.01);
        Assert.assertEquals(-2.0344,t1.getLongitude(),0.01);

        Assert.assertEquals(-0.0881,t2.getLatitude(),0.01);
        Assert.assertEquals(-2.5217,t2.getLongitude(),0.01);

        Assert.assertEquals(-1.080,t3.getLatitude(),0.01);
        Assert.assertEquals(-2.39097,t3.getLongitude(),0.01);
    }

    /**
     *
     * @throws Exception
     */
    @Test public void getRadius() throws Exception
    {
        GeographicCoordinate t1 = GeographicCoordinate.fromCartesian( new Vector3(1,2,3) );
        GeographicCoordinate t2 = GeographicCoordinate.fromCartesian( new Vector3(32.23,23,3.5) );
        GeographicCoordinate t3 = new GeographicCoordinate(95,20,22);

        Assert.assertEquals(3.7416,t1.getRadius(),0.01);
        Assert.assertEquals(39.74,t2.getRadius(),0.01);
        Assert.assertEquals(6371095.0,t3.getRadius(),0.01); // no radius
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

        t1.setLatitude(17.39372);
        t2.setLatitude(0);
        t3.setLatitude(20.28);

        Assert.assertEquals(95,t1.getAltitude(),0.001);
        Assert.assertEquals(92.222,t2.getAltitude(),0.001);
        Assert.assertEquals(72.2,t3.getAltitude(),0.001);
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
    @Test public void bearingTo() throws Exception
    {
        GeographicCoordinate t1 = new GeographicCoordinate(95,20,22);
        GeographicCoordinate t2 = new GeographicCoordinate(92.222,38.3,222.3);
        GeographicCoordinate t3 = new GeographicCoordinate(72.2,67.2232,22.3);

        Assert.assertEquals(4.208291210486665,t1.bearingTo(t2),0.01);
        Assert.assertEquals(3.518705,t2.bearingTo(t3),0.01);
        assertEquals(-0.9744,t3.bearingTo(t1),0.01);
    }
}