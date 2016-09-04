package com.atc.simulator.flightdata;

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
     * @param aircraftIDs of type ArrayList<String>
     */
    void onSystemStateUpdate(SystemStateDatabase stateDatabase, ArrayList<String> aircraftIDs);
    void onNewAircraft(SystemStateDatabase stateDatabase, String aircraftID);
    void onRemoveAircraft(SystemStateDatabase stateDatabase, String aircraftID);
    void onUpdateAircraft(SystemStateDatabase stateDatabase, String aircraftID);

}
