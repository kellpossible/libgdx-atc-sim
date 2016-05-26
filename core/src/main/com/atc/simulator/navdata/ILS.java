package com.atc.simulator.navdata;

import com.atc.simulator.vectors.GeographicCoordinate;

/**
 * Created by luke on 7/04/16.
 *
 * @author Luke Frisken
 */
public class ILS extends DirectionalBeacon {
    public ILS(String icaoId, GeographicCoordinate position) {
        super(icaoId, position);
    }
}
