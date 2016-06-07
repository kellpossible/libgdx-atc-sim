package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.flightdata.Prediction;

/**
 * Listener interface for PredictionEngine
 * @author Luke Frisken
 */
public interface PredictionEngineListener {
    public void workItemOverdue(PredictionWorkItem workItem);
    public void predictionComplete(Prediction prediction);
}
