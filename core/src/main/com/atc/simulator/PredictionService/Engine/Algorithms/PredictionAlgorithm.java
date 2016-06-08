package com.atc.simulator.PredictionService.Engine.Algorithms;

import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

import java.util.HashMap;

/**
 * Created by luke on 7/06/16.
 * @author Luke Frisken
 */
public abstract class PredictionAlgorithm {
    private static HashMap<PredictionAlgorithmType, PredictionAlgorithm> algorithmHashMap;

    static {
        algorithmHashMap = new HashMap<PredictionAlgorithmType, PredictionAlgorithm>();
        algorithmHashMap.put(PredictionAlgorithmType.PASSTHROUGH, new PassthroughPredictionAlgorithm());
    }

    public static PredictionAlgorithm getInstance(PredictionAlgorithmType type)
    {
        return algorithmHashMap.get(type);
    }
    public abstract Prediction makePrediction(Track aircraftTrack);
}
