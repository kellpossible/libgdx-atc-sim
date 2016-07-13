package com.atc.simulator.DebugDataFeed.Scenarios;

import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;

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
    private static Scenario currentScenario = null;

    /**
     * Get the SystemState at a given time as represented by the Scenario.
     * Assumes that the time provided is within the startTime and endTime boundary
     *
     * @param time time of system state (in milliseconds since epoch)
     * @return system state
     */
    public abstract SystemState getState(long time);


    /**
     * Get the tracks that this scenario is based on.
     *
     * @return tracks this scenario is based on
     */
    public abstract ArrayList<Track> getTracks();

    /**
     * Get the start time of the Scenario
     *
     * @return start time (in milliseconds since epoch)
     */
    public abstract long getStartTime();


    /**
     * Get the end time of the scenario
     *
     * @return end time (in milliseconds since epoch)
     */
    public abstract long getEndTime();

    /**
     * The scenario's recommended update rate, in milliseconds, in order
     * to keep it syncronised.
     *
     * @return update rate (in milliseconds).
     */
    public abstract int getRecommendedUpdateRate();

    /**
     * Throw an error if the supplied time is outside the
     * boundary of the start time and the end time
     *
     * @param time time (in milliseconds since epoch) to check whether it is within the boundary
     */
    protected void checkStateTimeWithinBoundaries(long time)
    {
        if (time < getStartTime())
        {
            throw new IndexOutOfBoundsException("time is before start time");
        }
        if (time > getEndTime())
        {
            throw new IndexOutOfBoundsException("time is after end time");
        }
    }

    /**
     * Get the projection reference position of the scenario
     * @return
     */
    public abstract GeographicCoordinate getProjectionReference();


    /**
     * Get the current scenario instance
     * @return
     */
    public static Scenario getCurrentScenario()
    {
        return currentScenario;
    }

    /**
     * Set the current scenario instance
     * @param currentScenario
     * @return
     */
    public static void setCurrentScenario(Scenario currentScenario)
    {
        Scenario.currentScenario = currentScenario;
    }

}
