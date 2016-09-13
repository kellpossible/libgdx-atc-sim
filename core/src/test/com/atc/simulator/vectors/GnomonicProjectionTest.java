package com.atc.simulator.vectors;

import org.junit.Assert;
import org.junit.Test;
import pythagoras.d.Vector3;

import static org.junit.Assert.*;

/**
 * Created by Adam on 13/09/2016.
 * For conversion. Using http://keisan.casio.com/exec/system/1359534351
 */
public class GnomonicProjectionTest {

    //Geographic -> Cartesian
    @Test
    public void transformPositionTo() throws Exception {

        GeographicCoordinate Coord1 = new GeographicCoordinate(3.75297841, 0.77125725,3.21252475); // expecting 5,15,30
        GnomonicProjection t1 = new GnomonicProjection(Coord1);

        Assert.assertEquals(new Vector3(5,15,30), t1.transformPositionTo(Coord1));
    }

    //Cartesian -> Geographic
    @Test
    public void transformPositionFrom() throws Exception {

    }

    @Test
    public void tranformVelocityTo() throws Exception {

    }

    @Test
    public void transformVelocityFrom() throws Exception {

    }

}