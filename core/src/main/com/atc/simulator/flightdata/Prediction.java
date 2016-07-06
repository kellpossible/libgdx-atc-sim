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

    /**
     * Constructor Prediction creates a new Prediction instance.
     *
     * @param aircraftID of type String
     * @param time of type long. The time (in milliseconds since epoch) for the first predicted position.
     * @param predictedPositions of type ArrayList<GeographicCoordinate>
     */
    public Prediction(String aircraftID, long time, ArrayList<GeographicCoordinate> predictedPositions)
    {
        this.predictedPositions = predictedPositions;
        this.aircraftID = aircraftID;
        this.time = time;
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

}
