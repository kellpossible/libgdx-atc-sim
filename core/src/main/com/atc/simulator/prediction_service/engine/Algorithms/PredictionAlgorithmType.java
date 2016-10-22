package com.atc.simulator.prediction_service.engine.Algorithms;

/**
 * Enumerator for the different types of algorithms available for use
 * in the prediction service.
 * @author Luke Frisken
 */
public enum PredictionAlgorithmType {
    PASSTHROUGH,
    PARALLEL_TEST,
    LINEAR,
    LINEAR2D,
    CURVILINEAR2D,
    CHRIS,
    LMLEASTSQUARES,
    LMLEASTSQUARESV3
}
