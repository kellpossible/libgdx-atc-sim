package com.atc.simulator.navdata;

import com.atc.simulator.vectors.GeographicCoordinate;

/**
 * Created by luke on 7/04/16.
 * Represents an airport navigational reference position.
 */
public class Airport extends Waypoint {

    public Airport(String icaoId, GeographicCoordinate position) {
        super(icaoId, position);
    }
}
