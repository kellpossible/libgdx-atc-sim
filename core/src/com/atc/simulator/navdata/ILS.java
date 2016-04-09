package com.atc.simulator.navdata;

import com.atc.simulator.SphericalPosition;

/**
 * Created by luke on 7/04/16.
 */
public class ILS extends DirectionalBeacon {
    public ILS(String ICAO_id, SphericalPosition position) {
        super(ICAO_id, position);
    }
}
