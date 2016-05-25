package com.atc.simulator.flightdata;

import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.Calendar;

/**
 * Created by luke on 7/04/16.
 * Represents the state of an aircraft at a particular point in time.
 */
public class TrackEntry {
    private Calendar time;
    private AircraftState aircraftState;

    /**
     * @param time the point in time this entry represents
     * @param aircraftState the state of the aircraft in this track entry
     */
    public TrackEntry(Calendar time, AircraftState aircraftState)
    {
        this.time = time;
        this.aircraftState = aircraftState;

    }


    /**
     * Get the time this track entry represents.
     * @return
     */
    public Calendar getTime() {
        return time;
    }

    /**
     * Set the time this track entry represents.
     * @param time
     */
    public void setTime(Calendar time) {
        this.time = time;
    }

    /**
     * Get the state of the aircraft referred to in this track entry
     * @return
     */
    public AircraftState getAircraftState() {
        return aircraftState;
    }

    /**
     * Set the state of the aircraft referred to in this track entry
     * @param aircraftState
     */
    public void setAircraftState(AircraftState aircraftState) {
        this.aircraftState = aircraftState;
    }
}
