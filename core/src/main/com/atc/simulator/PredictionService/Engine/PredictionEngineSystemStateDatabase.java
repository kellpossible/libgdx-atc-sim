package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.flightdata.SystemStateDatabase.SystemStateDatabase;
import com.atc.simulator.flightdata.SystemStateDatabase.SystemStateDatabaseListener;
import com.atc.simulator.flightdata.TimeSource;
import com.sun.istack.internal.Nullable;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luke on 7/10/16.
 *
 * @author Luke Frisken
 */
public class PredictionEngineSystemStateDatabase extends SystemStateDatabase implements SystemStateDatabaseListener {
    private HashMap<String, Object> algorithmStateMap;

    /**
     * Constructor
     * @param timeSource
     */
    public PredictionEngineSystemStateDatabase(TimeSource timeSource) {
        super(timeSource);
        algorithmStateMap = new HashMap<String, Object>();

        //listen to itself.
        this.addListener(this);
    }

    /**
     * Returns the object used to store algorithm state
     * associated with any given aircraft track.
     * @param aircraftID aircraft track to get state for
     * @return Object containing the algorithm state
     */
    @Nullable
    public Object getAlgorithmState(String aircraftID)
    {
        return algorithmStateMap.get(aircraftID);
    }

    /**
     * Checks whether a given aircraft track has an algorithm state associated
     * with it yet.
     * @param aircraftID aircraft track to check
     * @return boolean whether or not the track with that ID has an algorithm state associated with it.
     */
    public boolean hasAlgorithmState(String aircraftID)
    {
        return algorithmStateMap.containsKey(aircraftID);
    }

    /**
     * Set the algorithm state to be associated with a track with the given aircraft ID.
     * @param aircraftID
     * @param algorithmState
     */
    public void setAlgorithmState(String aircraftID, Object algorithmState)
    {
        algorithmStateMap.put(aircraftID, algorithmState);
    }

    /**
     * This method is called by the SystemStateDataBase on its listeners
     * whenever the SystemStateDatabase receives updated information.
     *
     * @param stateDatabase database which triggered this event
     * @param aircraftIDs   of type ArrayList<String>
     */
    @Override
    public void onSystemStateUpdate(SystemStateDatabase stateDatabase, ArrayList<String> aircraftIDs) {

    }

    /**
     * This method is called by the SystemStateDataBase on its listeners
     * whenever the SystemStateDatabase creates a new aircraft.
     *
     * @param stateDatabase database which triggered this event
     * @param aircraftID    of type String
     */
    @Override
    public void onNewAircraft(SystemStateDatabase stateDatabase, String aircraftID) {

    }

    /**
     * This method is called by the SystemStateDataBase on its listeners
     * whenever the SystemStateDatabase removes an aircraft.
     *
     * @param stateDatabase database which triggered this event
     * @param aircraftID of type String
     */
    public void onRemoveAircraft(SystemStateDatabase stateDatabase, String aircraftID)
    {
        algorithmStateMap.remove(aircraftID);
    }

    /**
     * This method is called by the SystemStateDataBase on its listeners
     * whenever the SystemStateDatabase receives an update to an aircraft.
     *
     * @param stateDatabase database which triggered this event
     * @param aircraftID    of type String
     */
    @Override
    public void onUpdateAircraft(SystemStateDatabase stateDatabase, String aircraftID) {

    }
}
