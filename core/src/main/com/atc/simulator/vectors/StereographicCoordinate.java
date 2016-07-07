package com.atc.simulator.vectors;

import pythagoras.d.Vector3;

/**
 * Created by luke on 7/07/16.
 *
 * Represents a coordinate in the stereographic projection:
 * https://en.wikipedia.org/wiki/Stereographic_projection
 *
 * @author Luke Frisken
 */
public class StereographicCoordinate extends Vector3 {
    private GeographicCoordinate projectionReference;

    /**
     * Convert a GeographicCoordinate into a StereographicCoordinate based on its projection reference
     * @param geographicCoordinate the coordinate to be converted
     * @param projectionReference the reference point of the stereographic coordinate system
     */
    public StereographicCoordinate(GeographicCoordinate geographicCoordinate, GeographicCoordinate projectionReference)
    {
        this.projectionReference = projectionReference;
    }

    /**
     * Method getProjectionReference returns the projectionReference of this StereographicCoordinate object
     *
     * @return the projectionReference (type GeographicCoordinate) of this StereographicCoordinate object.
     */
    public GeographicCoordinate getProjectionReference() {
        return projectionReference;
    }
}
