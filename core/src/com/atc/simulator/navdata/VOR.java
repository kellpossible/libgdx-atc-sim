package com.atc.simulator.navdata;

import com.atc.simulator.vectors.GeographicCoordinate;

/**
 * Created by luke on 7/04/16.
 * A Nav Object which represents a VHF Omni Directional Radio Range ground station
 * https://en.wikipedia.org/wiki/VHF_omnidirectional_range
 */
public class VOR extends DirectionalBeacon {
    public VOR(String icaoId, GeographicCoordinate position) {
        super(icaoId, position);
    }
}
