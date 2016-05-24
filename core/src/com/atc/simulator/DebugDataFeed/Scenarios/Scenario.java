package com.atc.simulator.DebugDataFeed.Scenarios;

import com.atc.simulator.flightdata.SystemState;

import java.util.Calendar;

/**
 * Created by luke on 24/05/16.
 * Represents a scenerio for playback in the DebugDataFeed.
 *
 * @author Luke Frisken
 */
public abstract class Scenario {

    /**
     * Get the SystemState at a given time as represented by the Scenario.
     * @param time
     * @return
     */
    public abstract SystemState getState(Calendar time);

    /**
     * Get the start time of the Scenario
     * @return
     */
    public abstract Calendar getStartTime();


    /**
     * Get the end time of the scenario
     * @return
     */
    public abstract Calendar getEndTime();

}
