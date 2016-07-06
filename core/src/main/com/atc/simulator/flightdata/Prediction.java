package com.atc.simulator.flightdata;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Prediction is a simple data type, storing a list of AircraftStates that form a future prediction made by the Prediction Engine
 *
 * @author    Chris Coleman, Luke Frisken
 */
public class Prediction {
    private ArrayList<GeographicCoordinate> predictedPositions; //Array List of positional predictions
    private String aircraftID;
    private long time;
    private int dt;

    /**
     * Constructor Prediction creates a new Prediction instance.
     *
     * @param aircraftID of type String
     * @param time of type long. The time (in milliseconds since epoch) for the first predicted position.
     * @param predictedPositions of type ArrayList<GeographicCoordinate>
     * @param dt of type int. The change in time between predicted positions
     */
    public Prediction(String aircraftID, long time, ArrayList<GeographicCoordinate> predictedPositions, int dt)
    {
        this.predictedPositions = predictedPositions;
        this.aircraftID = aircraftID;
        this.time = time;
        this.dt = dt;
    }

    /**
     * Gets the ID for the plane being predicted
     * @return aircraftID : The ID of the aircraft
     */
    public String getAircraftID(){return aircraftID;}

    /**
     * Gets the timestamp for when the aircraft information was sent
     * @return time : The ID of the aircraft
     */
    public long getPredictionTime(){return time;}

    /**
     * Returns all the states in the prediction
     * @return predictedStates : The full list of states that have been predicted
     */
    public ArrayList<GeographicCoordinate> getPredictedPositions() {
        return predictedPositions;
    }

    /**
     * Returns delta t, the change in time between predicted positions.
     * @return the dt (type int) of this Prediction object.
     */
    public int getDt() {
        return dt;
    }
}
