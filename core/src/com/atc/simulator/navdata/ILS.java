package com.atc.simulator.navdata;

import com.atc.simulator.GeographicCoordinate;

/**
 * Created by luke on 7/04/16.
 */
public class ILS extends DirectionalBeacon {
    public ILS(String icaoId, GeographicCoordinate position) {
        super(icaoId, position);
    }
}
