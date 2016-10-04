package com.atc.simulator.flightdata;

/**
 * A real time source, based on the system clock
 * @author Luke Frisken
 */
public class RealTimeSource implements TimeSource {

    /**
     * Get the current time in milliseconds since epoch
     *
     * @return
     */
    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}