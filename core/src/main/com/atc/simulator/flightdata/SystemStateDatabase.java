package com.atc.simulator.flightdata;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a very simple placeholder SystemStateDatabase, which just keeps track of all items it receives,
 * and notifies its listeners whenever it receives an update.
 *
 * In the future it will probably want its own thread to cull items which have been sitting in the database
 * for too long without getting used. (e.g. aircraft that have moved to other sectors)
 *
 * @author Luke Frisken
 */
public class SystemStateDatabase {
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
            triggerOnNewAircraft(aircraftID);
        }
        ArrayList<String> aircraftIDs = new ArrayList<String>();
        aircraftIDs.add(aircraftID);
        triggerOnSystemStateUpdate(aircraftIDs);
        triggerOnUpdateAircraft(aircraftID);
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
     * Get a copy of the track
     * @param aircraftID
     * @return
     */
    public Track copyTrack(String aircraftID)
    {
        Track track = tracks.get(aircraftID);
        Track newTrack = new Track();
        newTrack.addAll(track);
        return newTrack;
    }


    /**
     * Note, Uros: This method gets called from the debugdatafeed client when debugdatafeed client
     * receives an update from the display.
     *
     * This method gets called when there is a system update, and gets
     * passed the new system state
     *
     * @param systemState the updated system state
     */
    public void systemStateUpdate(SystemState systemState) {
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
            listener.onSystemStateUpdate(this, aircraftIDs);
        }
    }

    private void triggerOnNewAircraft(String aircraftID)
    {
        for (SystemStateDatabaseListener listener : listeners)
        {
            listener.onNewAircraft(this, aircraftID);
        }
    }

    private void triggerOnUpdateAircraft(String aircraftID)
    {
        for (SystemStateDatabaseListener listener : listeners)
        {
            listener.onUpdateAircraft(this, aircraftID);
        }
    }
}
