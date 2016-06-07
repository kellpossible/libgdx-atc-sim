package com.atc.simulator.PredictionService;

/**
 * Created by luke on 7/06/16.
 * @author Luke Frisken
 */
public interface SystemStateDatabaseListener {
    public void systemStateUpdated(String[] aircraftIDs);
}
