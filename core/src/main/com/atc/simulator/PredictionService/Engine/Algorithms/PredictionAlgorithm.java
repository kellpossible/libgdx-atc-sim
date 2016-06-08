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

    /**
     * for inducing a high load on this thread.
     * credit: http://stackoverflow.com/questions/382113/generate-cpu-load-in-java#answer-382212
     * @param milliseconds
     */
    protected static void spin(int milliseconds) {
        long sleepTime = milliseconds*1000000L; // convert to nanoseconds
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < sleepTime) {}
    }

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
