package com.atc.simulator.Display;

import com.atc.simulator.flightdata.Prediction;

/**
 * PredictionListener, simple interface that defines the ability for PredictionFeedClients to notify and update
 * listeners when new data arrives
 *
 * PUBLIC FEATURES:
 * // Methods
 *    onPredictionUpdate(Prediction)
 * MODIFIED:
 * @version 0.1, CC 28/05/16
 * @author    Chris Coleman, 7191375
 */
public interface PredictionListener {
    /**
     * When a client receives new information, it will call this method to notify listeners
     * of a new data
     * @param newPrediction the new prediction information
     */
    void onPredictionUpdate(Prediction newPrediction);
}
