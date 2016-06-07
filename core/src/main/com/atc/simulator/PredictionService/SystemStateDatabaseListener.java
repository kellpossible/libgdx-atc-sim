package com.atc.simulator.PredictionService;

import java.util.ArrayList;

/**
 * Created by luke on 7/06/16.
 * @author Luke Frisken
 */
public interface SystemStateDatabaseListener {
    public void onSystemStateUpdate(ArrayList<String> aircraftIDs);
}
