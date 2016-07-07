package com.atc.simulator.vectors;

import pythagoras.d.Vector3;

/**
 * Created by luke on 7/07/16.
 *
 * Represents a coordinate in the gnomonic projection
 * https://en.wikipedia.org/wiki/Gnomonic_projection
 *
 * @author Luke Frisken
 */
public class GnomonicCoordinate extends Vector3 {
    private GeographicCoordinate projectionReference;
    /**
     * Convert a GeographicCoordinate into a GnomonicCoordinate based on its projection reference
     * @param geographicCoordinate the coordinate we are converting
     * @param projectionReference the reference point of the gnomonic coordinate system
     */
    public GnomonicCoordinate(GeographicCoordinate geographicCoordinate, GeographicCoordinate projectionReference)
    {
        this.projectionReference = projectionReference;
    }

    /**
     * Method getProjectionReference returns the projectionReference of this GnomonicCoordinate object.
     *
     * @return the projectionReference (type GeographicCoordinate) of this GnomonicCoordinate object.
     */
    public GeographicCoordinate getProjectionReference() {
        return projectionReference;
    }
}
