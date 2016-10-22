package com.atc.simulator.flightdata.system_state_database;

import java.util.ArrayList;

/**
 * listener interface for SystemStateDatabase
 * @author Luke Frisken
 */
public interface SystemStateDatabaseListener {
    /**
     * This method is called by the SystemStateDataBase on its listeners
     * whenever the SystemStateDatabase receives updated information.
     *
     * @param stateDatabase database which triggered this event
     * @param aircraftIDs   of type ArrayList&lt;String&gt;
     */
    void onSystemStateUpdate(SystemStateDatabase stateDatabase, ArrayList<String> aircraftIDs);

    /**
     * This method is called by the SystemStateDataBase on its listeners
     * whenever the SystemStateDatabase creates a new aircraft.
     *
     * @param stateDatabase database which triggered this event
     * @param aircraftID of type String
     */
    void onNewAircraft(SystemStateDatabase stateDatabase, String aircraftID);

    /**
     * This method is called by the SystemStateDataBase on its listeners
     * whenever the SystemStateDatabase removes an aircraft.
     *
     * @param stateDatabase database which triggered this event
     * @param aircraftID of type String
     */
    void onRemoveAircraft(SystemStateDatabase stateDatabase, String aircraftID);

    /**
     * This method is called by the SystemStateDataBase on its listeners
     * whenever the SystemStateDatabase receives an update to an aircraft.
     *
     * @param stateDatabase database which triggered this event
     * @param aircraftID of type String
     */
    void onUpdateAircraft(SystemStateDatabase stateDatabase, String aircraftID);

}
