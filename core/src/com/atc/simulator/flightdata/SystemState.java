package com.atc.simulator.flightdata;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Represents the state of a radar tracking/adsb/whatever aircraft tracking system at a given point in time.
 * @author Luke Frisken
 */
public class SystemState {
    private Calendar time;
    private ArrayList<AircraftState> aircraftStates;

    /**
     *
     * @param time
     * @param aircraftStates
     */
    public SystemState(Calendar time, ArrayList<AircraftState> aircraftStates)
    {
        this.aircraftStates = aircraftStates;
        this.time = time;
    }


    /**
     * Get the time for this state
     * @return
     */
    public Calendar getTime() {
        return time;
    }

    /**
     * Get the array of aircraft states for this system state.
     * @return
     */
    public ArrayList<AircraftState> getAircraftStates()
    {
        return this.aircraftStates;
    }
}
