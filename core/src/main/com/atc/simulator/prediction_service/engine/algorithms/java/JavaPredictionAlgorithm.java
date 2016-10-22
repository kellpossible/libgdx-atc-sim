package com.atc.simulator.prediction_service.engine.algorithms.java;

import com.atc.simulator.prediction_service.engine.algorithms.PredictionAlgorithmType;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

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
            case CURVILINEAR2D:
                return new JavaCurvilinear2dAlgorithm();
            case CHRIS:
                return new JavaChrisAlgorithm1();
            case LMLEASTSQUARES:
                return new JavaLMLeastSquaresAlgorithm();
            case LMLEASTSQUARESV3:
                return new JavaLMLeastSquaresAlgorithmV3();

        }
        return null;
    }
    /**
     * Method makePrediction ...
     *
     * @param aircraftTrack of type Track
     * @return Prediction
     */
    public abstract Prediction makePrediction(Track aircraftTrack, Object algorithmState);

    public Prediction makePrediction(Track aircraftTrack)
    {
        return makePrediction(aircraftTrack, getNewStateObject());
    }

    /**
     * Get a new state object for this algorithm.
     * @return
     */
    public abstract Object getNewStateObject();
}
