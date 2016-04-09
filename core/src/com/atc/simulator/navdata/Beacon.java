package com.atc.simulator.navdata;

import com.atc.simulator.GeographicCoordinate;

/**
 * Created by luke on 10/04/16.
 */
public class Beacon extends Waypoint {
    public Beacon(String icaoId, GeographicCoordinate position) {
        super(icaoId, position);
    }
}
