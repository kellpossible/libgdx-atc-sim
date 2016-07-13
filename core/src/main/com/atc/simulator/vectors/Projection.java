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
    public abstract Vector3 transformTo(GeographicCoordinate geographicCoordinate);
    public abstract GeographicCoordinate transformFrom(Vector3 projectionCoordinate);
}
