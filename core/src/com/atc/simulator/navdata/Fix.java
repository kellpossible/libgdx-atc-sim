package com.atc.simulator.navdata;

import com.atc.simulator.vectors.GeographicCoordinate;

/**
 * Created by luke on 7/04/16.
 * A fix is the simplest form of navigational point, usually not representing or directly correlated
 * to a physical place.
 */
public class Fix extends Waypoint {
    public Fix(String icaoId, GeographicCoordinate position) {
        super(icaoId, position);
    }
}
