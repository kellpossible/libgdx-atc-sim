package com.atc.simulator.flightdata;

/**
 * @author Luke Frisken
 */
public interface TimeSource {
    /**
     * Get the current time in milliseconds since epoch
     * @return
     */
    long getCurrentTime();
}
