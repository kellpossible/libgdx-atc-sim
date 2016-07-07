package com.atc.simulator.vectors;

import pythagoras.d.Vector3;

/**
 * Created by luke on 7/07/16.
 */
public class StereographicCoordinate extends Vector3 {
    private GeographicCoordinate projectionReference;

    /**
     * Convert a GeographicCoordinate into a gnomic coordinate
     * @param geographicCoordinate
     * @param projectionReference the reference point of the gnomonic coordinate system
     */
    public StereographicCoordinate(GeographicCoordinate geographicCoordinate, GeographicCoordinate projectionReference)
    {

    }
}
