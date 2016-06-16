package com.atc.simulator.PredictionService;

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
    public void onSystemStateUpdate(ArrayList<String> aircraftIDs);
}
