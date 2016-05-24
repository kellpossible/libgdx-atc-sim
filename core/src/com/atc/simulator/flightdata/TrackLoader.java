package com.atc.simulator.flightdata;

import java.io.IOException;

/**
 * Created by luke on 8/04/16.
 * loads a track
 */
public abstract class TrackLoader {
    /**
     * Load a track.
     * @return
     */
    abstract Track load() throws IOException;
}
