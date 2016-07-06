package com.atc.simulator.PredictionService.Engine.Algorithms.Java;

import com.atc.simulator.PredictionService.Engine.Algorithms.PredictionAlgorithmType;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

import java.util.HashMap;

/**
 * Created by luke on 7/06/16.
 * @author Luke Frisken
 */
public abstract class JavaPredictionAlgorithm {
    private static HashMap<PredictionAlgorithmType, JavaPredictionAlgorithm> algorithmHashMap;

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
        algorithmHashMap = new HashMap<PredictionAlgorithmType, JavaPredictionAlgorithm>();
        algorithmHashMap.put(PredictionAlgorithmType.PASSTHROUGH, new JavaPassthroughAlgorithm());
        algorithmHashMap.put(PredictionAlgorithmType.LINEAR, new JavaLinearAlgorithm());
    }

    public static JavaPredictionAlgorithm getInstance(PredictionAlgorithmType type)
    {
        return algorithmHashMap.get(type);
    }
    /**
     * Method makePrediction ...
     *
     * @param aircraftTrack of type Track
     * @return Prediction
     */
    public abstract Prediction makePrediction(Track aircraftTrack);
}
