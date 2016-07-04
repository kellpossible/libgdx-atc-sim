package com.atc.simulator.DebugDataFeed.Scenarios;

import com.atc.simulator.flightdata.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by luke on 24/05/16.
 *
 * Represents a scenario of a single track heading from Melbourne International (YMML)
 * to Canberra International (YSCB).
 *
 * @author Luke Frisken
 */
public class YMMLtoYSCBScenario extends Scenario {
    private Track track;
    private long startTime;
    private long endTime;

    /**
     * Field lastStateIndex
     * The index of the last state that was returned by getState
     * Used for optimisation to avoid having to loop through the
     * entire track each time the getState method gets called.
     * Makes the assumption that whatever is calling this class
     * is incrementing time, with time moving forward.
     */
    private int lastStateIndex;

    /**
     * Field trackUpdateRate
     * The update rate of this track is 1000 millisconds
     */
    private final int trackUpdateRate = 1000;

    /**
     * Constructor for the Scenario
     */
    public YMMLtoYSCBScenario()
    {
        SimulatorTrackLoader trackLoader = new SimulatorTrackLoader("assets/flight_data/YMMLtoYSCB/YMML2YSCB_track.csv");
        try {
            track = trackLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //base the start and end times from the first and the track entries.
        startTime = track.get(0).getTime();
        endTime = track.get(track.size()-1).getTime();
        lastStateIndex = 0;

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
        checkStateTimeWithinBoundaries(time);

        /* loop through track entries,
        starting with the entry which was obtained last with this method.
         */

        AircraftState prevState = track.get(lastStateIndex);
        long prevStateTime = prevState.getTime();
        for(int i = lastStateIndex; i<track.size();i++)
        {
            AircraftState state = track.get(i);
            long stateTime = state.getTime();

            /* compare the entry time with the time this method is aiming to get
            if the time of this entry is now after the desired time, this track entry
            is recognised as the closest to the desired time, and so it is returned.
             */
            if (time < stateTime)
            {
                lastStateIndex = i-1; //update the index with this track entry index.
                ArrayList<AircraftState> aircraftStates = new ArrayList<AircraftState>();
                aircraftStates.add(prevState);
                return new SystemState(prevStateTime, aircraftStates);
            }

            prevState = state;
            prevStateTime = stateTime;
        }

        return null;
    }

    /**
     * Get the tracks that this scenario is based on.
     *
     * @return tracks this scenario is based on
     */
    @Override
    public ArrayList<Track> getTracks() {
        ArrayList<Track> tracks = new ArrayList<Track>();
        tracks.add(track);
        return tracks;
    }

    /**
     * Get the start time of the Scenario
     *
     * @return start time (in milliseconds since epoch)
     */
    @Override
    public long getStartTime() {
        return startTime;
    }

    /**
     * Get the end time of the scenario
     *
     * @return end time (in milliseconds since epoch)
     */
    @Override
    public long getEndTime() {
        return endTime;
    }

    /**
     * The scenario's recommended update rate, in milliseconds, in order
     * to keep it syncronised.
     *
     * @return update rate (in milliseconds).
     */
    @Override
    public int getRecommendedUpdateRate() {
        return 3000;
    }
}
