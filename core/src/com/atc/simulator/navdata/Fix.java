package com.atc.simulator.navdata;

import com.atc.simulator.SphericalPosition;

/**
 * Created by luke on 7/04/16.
 */
public class Fix extends NavObject {
    public Fix(String ICAO_id, SphericalPosition position) {
        super(ICAO_id, position);
    }
}
