package com.atc.simulator.prediction_service.engine;

import com.atc.simulator.flightdata.Prediction;

/**
 * Listener interface for PredictionEngineThread
 * @author Luke Frisken
 */
public interface PredictionEngineListener {
    /**
     * Called by PredictionEngine on all its listeners
     * whenever a work item is overdue (taking longer
     * than the maximum latency).
     * @param workItem
     */
    public void onWorkItemOverdue(PredictionWorkItem workItem);

    /**
     * Called by PredictionEngine on all its listeners whenever
     * a prediction has completed.
     * @param prediction the completed prediction
     */
    public void onPredictionComplete(Prediction prediction);
}
