package com.atc.simulator.vectors;

import pythagoras.d.Vector3;

/**
 * Created by luke on 13/07/16.
 * Represents a map projection, capable of transforming from GeographicCoordinate
 * into its own cartesian space.
 *
 * @author Luke Frisken
 */
public abstract class Projection {
    public abstract Vector3 transformPositionTo(GeographicCoordinate geographicCoordinate);
    public abstract GeographicCoordinate transformPositionFrom(Vector3 projectionCoordinate);

    /**
     *
     * @param velocity velocity in the Spherical coordinate system
     * @param referencePosition reference position in Geographic coordinate system
     * @param referencePositionCartesian reference position in this projection's cartesian coordiant system
     * @return
     */
    public abstract Vector3 tranformVelocityTo(SphericalVelocity velocity,
                                               GeographicCoordinate referencePosition,
                                               Vector3 referencePositionCartesian);

    /**
     *
     * @param velocity velocity in this projection's cartesian coordinate system
     * @param referencePosition reference position in Geographic coordinate system
     * @param referencePositionCartesian reference position in this projection's cartesian coordiant system
     * @return
     */
    public abstract SphericalVelocity transformVelocityFrom(Vector3 velocity,
                                                            GeographicCoordinate referencePosition,
                                                            Vector3 referencePositionCartesian);
}
