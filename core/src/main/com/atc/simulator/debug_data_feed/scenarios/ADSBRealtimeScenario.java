package com.atc.simulator.debug_data_feed.scenarios;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.ArrayList;

/**
 * Created by luke on 5/10/16.
 */
public class ADSBRealtimeScenario extends Scenario {
    private static final ArrayList<AircraftState> emptyStateList = new ArrayList<AircraftState>();
    private static final ArrayList<Track> emptyTrackList = new ArrayList<Track>();
    private GeographicCoordinate projectionReference;

    public ADSBRealtimeScenario(GeographicCoordinate projectionReference)
    {
        this.projectionReference = projectionReference;
    }

    /**
     * Get the SystemState at a given time as represented by the Scenario.
     * Assumes that the time provided is within the startTime and endTime boundary
     *
     * @param time time of system state (in milliseconds since epoch)
     * @return system state
     */
    @Override
    public SystemState getState(long time) {
        return new SystemState(System.currentTimeMillis(), emptyStateList);
    }

    /**
     * Get the tracks that this scenario is based on.
     *
     * @return tracks this scenario is based on
     */
    @Override
    public ArrayList<Track> getTracks() {
        //return an empty track list
        return emptyTrackList;
    }

    /**
     * Get the start time of the Scenario
     *
     * @return start time (in milliseconds since epoch)
     */
    @Override
    public long getStartTime() {
        return 0;
    }

    /**
     * Get the end time of the scenario
     *
     * @return end time (in milliseconds since epoch)
     */
    @Override
    public long getEndTime() {
        return Integer.MAX_VALUE;
    }

    /**
     * The scenario's recommended update rate, in milliseconds, in order
     * to keep it syncronised.
     *
     * @return update rate (in milliseconds).
     */
    @Override
    public int getRecommendedUpdateRate() {
        return 500;
    }

    /**
     * Get the projection reference position of the scenario
     *
     * @return Geographical Coordinate based on reference position
     */
    @Override
    public GeographicCoordinate getProjectionReference() {
        return projectionReference;
    }
}
