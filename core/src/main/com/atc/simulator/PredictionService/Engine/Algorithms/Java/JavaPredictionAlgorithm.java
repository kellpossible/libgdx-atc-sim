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

    public static JavaPredictionAlgorithm getInstance(PredictionAlgorithmType type)
    {
        switch(type)
        {
            case PASSTHROUGH:
                return new JavaPassthroughAlgorithm();
            case LINEAR:
                return new JavaLinearAlgorithm();
            case LINEAR2D:
                return new JavaLinear2dAlgorithm();
        }
        return null;
    }
    /**
     * Method makePrediction ...
     *
     * @param aircraftTrack of type Track
     * @return Prediction
     */
    public abstract Prediction makePrediction(Track aircraftTrack);
}
