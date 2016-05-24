package com.atc.simulator.DebugDataFeed.Scenarios;

import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.flightdata.Track;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
     * Assumes that the time provided is within the startTime and endTime boundary
     * @param time time of system state
     * @return system state
     */
    public abstract SystemState getState(Calendar time);


    /**
     * Get the tracks that this scenario is based on.
     * @return tracks this scenario is based on
     */
    public abstract ArrayList<Track> getTracks();

    /**
     * Get the start time of the Scenario
     * @return start time
     */
    public abstract Calendar getStartTime();


    /**
     * Get the end time of the scenario
     * @return end time
     */
    public abstract Calendar getEndTime();

    /**
     * The scenario's recommended update rate, in milliseconds, in order
     * to keep it syncronised.
     *
     * @return update rate (in milliseconds).
     */
    public abstract int recommendedUpdateRate();

    /**
     * Throw an error if the supplied time is outside the
     * boundary of the start time and the end time
     * @param time time to check whether it is within the boundary
     */
    protected void checkStateTimeWithinBoundaries(Calendar time)
    {
        if (time.compareTo(getStartTime()) < 0)
        {
            throw new IndexOutOfBoundsException("time is before start time");
        }
        if (time.compareTo(getEndTime()) > 0)
        {
            throw new IndexOutOfBoundsException("time is after end time");
        }
    }

}
