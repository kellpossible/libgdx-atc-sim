package com.atc.simulator.PredictionService;

import com.atc.simulator.DebugDataFeed.DataPlaybackListener;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.flightdata.Track;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luke on 7/06/16.
 * @author Luke Frisken
 */
public class SystemStateDatabase implements DataPlaybackListener {
    private HashMap<String, Track> tracks;
    private ArrayList<SystemStateDatabaseListener> listeners;

    public SystemStateDatabase()
    {
        tracks = new HashMap<String, Track>();
        listeners = new ArrayList<SystemStateDatabaseListener>();
    }

    /**
     * submit an update to this state database
     * @param aircraftState
     */
    public void update(AircraftState aircraftState)
    {
        String aircraftID = aircraftState.getAircraftID();
        Track track = tracks.get(aircraftID);
        if (track != null)
        {
            //todo: obviously this assumes that this aircraft state is sequential/after previous update times.
            //which may well not be the case if it is coming from different sources
            track.add(aircraftState);
        } else {
            track = new Track();
            track.add(aircraftState);
            tracks.put(aircraftID, track);
        }
        ArrayList<String> aircraftIDs = new ArrayList<String>();
        aircraftIDs.add(aircraftID);
        triggerOnSystemStateUpdate(aircraftIDs);
    }

    /**
     * not implemented yet
     * @param aircraftStates
     */
    public void update(AircraftState[] aircraftStates)
    {

    }

    /**
     * Get an aircraft as referenced by its ID
     * @param aircraftID
     * @return
     */
    public Track getTrack(String aircraftID)
    {
        return tracks.get(aircraftID);
    }


    /**
     * TODO: remove this and implement the debugdatafeedclient listener interface
     * This method gets called when there is a system update, and gets
     * passed the new system state
     *
     * @param systemState the updated system state
     */
    @Override
    public void onSystemUpdate(SystemState systemState) {
        //could just use basic array for better performance here
        ArrayList<String> aircraftIDs = new ArrayList<String>(systemState.getAircraftStates().size());
        for (AircraftState aircraftState : systemState.getAircraftStates())
        {
            this.update(aircraftState);
            aircraftIDs.add(aircraftState.getAircraftID());
        }
    }

    /**
     * Add a SystemStateDatabaseListener to this SystemStateDatabase
     * @param listener
     * @return
     */
    public boolean addListener(SystemStateDatabaseListener listener)
    {
        if (!listeners.contains(listener))
        {
            return listeners.add(listener);
        } else {
            System.err.println("ERROR: cannot add listener, already exists");
            return false;
        }
    }

    /**
     * Remove a SystemStateDatabaseListener from this SystemStateDatabase
     * @param listener
     * @return
     */
    public boolean removeListener(SystemStateDatabaseListener listener)
    {
        return listeners.remove(listener);
    }

    /**
     * Trigger the onSystemStateUpdate event for the listeners
     * @param aircraftIDs
     */
    private void triggerOnSystemStateUpdate(ArrayList<String> aircraftIDs)
    {
        for (SystemStateDatabaseListener listener : listeners)
        {
            listener.onSystemStateUpdate(aircraftIDs);
        }
    }
}
