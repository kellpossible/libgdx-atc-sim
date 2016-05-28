package com.atc.simulator.flightdata;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Prediction is a simple data type, storing a list of AircraftStates that form a future prediction made by the Prediction Engine
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    Prediction(String, Calendar) - Creates a new prediction with ID, time and empty position array
 * // Methods
 *    String getAircraftID() - Returns the ID of the aircraft of which this prediction is for
 *    Calendar getPredictionTime() - Returns the time that the data was sent by the aircraft of which this prediction is for
 *    void addPosToPrediction(GeographicCoordinate) - Adds a new Position to the end of the array
 *    ArrayList<GeographicCoordinate> getListOfPositions() - Returns the whole array of positions
 *
 * COLLABORATORS:
 *    java.util.ArrayList
 *    java.util.Calendar;
 *
 * MODIFIED:
 * @version 1.0, CC 28/05/16
 * @author    Chris Coleman, 7191375
 */
public class Prediction {
    private ArrayList<GeographicCoordinate> predictedPositions; //Array List of positional predictions
    private String aircraftID;
    private Calendar time;

    /**
     * Constructor, instantiates a ArrayList of AircraftStates
     */
    public Prediction(String aID, Calendar time)
    {
        predictedPositions = new ArrayList<GeographicCoordinate>();
        aircraftID = aID;
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
    public Calendar getPredictionTime(){return time;}

    /**
     * Adds a new position to the prediction list
     * @param pos : The Position that has been predicted
     */
    public void addPosToPrediction(GeographicCoordinate pos){predictedPositions.add(pos);}

    /**
     * Returns all the states in the prediction
     * @return predictedStates : The full list of states that have been predicted
     */
    public ArrayList<GeographicCoordinate> getListOfPositions(){return predictedPositions;}

}
