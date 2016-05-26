package com.atc.simulator.navdata;

import com.atc.simulator.vectors.GeographicCoordinate;

/**
 * Created by luke on 10/04/16.
 *
 * @author Luke Frisken
 */
public class Beacon extends Waypoint {
    public Beacon(String icaoId, GeographicCoordinate position) {
        super(icaoId, position);
    }
}
