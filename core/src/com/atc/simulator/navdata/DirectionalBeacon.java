package com.atc.simulator.navdata;

import com.atc.simulator.vectors.GeographicCoordinate;

/**
 * Created by luke on 7/04/16.
 * A Waypoint which represents a directional navigation beacon
 */
public class DirectionalBeacon extends Waypoint {
    public DirectionalBeacon(String icaoId, GeographicCoordinate position) {
        super(icaoId, position);
    }
}
