package com.atc.simulator.prediction_service.engine.algorithms.java;

import com.atc.simulator.flightdata.Prediction;

/**
 * Created by luke on 24/10/16.
 *
 * represents the current state of the algorithm.
 *
 * @author Luke Frisken
 */
public class AlgorithmState {
    private Prediction.State currentState;
    private long lastStateTime;

    /**
     * Constructor for algorithm state
     */
    public AlgorithmState()
    {
        currentState = Prediction.State.STOPPED;
        lastStateTime = 0;
    }

    /**
     * Set a new current state for the algorithm
     * @param newState the new state to set this to
     * @param newStateTime the time that the new state was set
     */
    public void setState(Prediction.State newState, long newStateTime)
    {
        currentState = newState;
        lastStateTime = newStateTime;
    }

    /**
     * @return the current state of the algorithm
     */
    public Prediction.State getCurrentState()
    {
        return currentState;
    }

    public long getLastStateTime()
    {
        return lastStateTime;
    }
}