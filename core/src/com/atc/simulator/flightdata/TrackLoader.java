package com.atc.simulator.flightdata;

import java.io.IOException;

/**
 * Created by luke on 8/04/16.
 * loads a track
 *
 * @author Luke Frisken
 */
public abstract class TrackLoader {
    /**
     * Load a track.
     * @return
     */
    public abstract Track load() throws IOException;
}
