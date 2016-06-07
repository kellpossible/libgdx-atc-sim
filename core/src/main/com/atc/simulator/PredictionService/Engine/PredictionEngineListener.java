package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.flightdata.Prediction;

/**
 * Listener interface for PredictionEngineThread
 * @author Luke Frisken
 */
public interface PredictionEngineListener {
    public void onWorkItemOverdue(PredictionWorkItem workItem);
    public void onPredictionComplete(Prediction prediction);
}
