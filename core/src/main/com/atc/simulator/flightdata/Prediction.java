package com.atc.simulator.flightdata;
import java.util.ArrayList;

/**
 * Prediction is a simple data type, storing a list of AircraftStates that form a future prediction made by the Prediction Engine
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    Prediction()
 * // Methods
 *    addState(AircraftState state) - Add a new state to the internal list
 *    getListOfStates() - Returns the full ArrayList of States
 *
 * COLLABORATORS:
 *    java.util.ArrayList
 *
 * MODIFIED:
 * @version 1.0, CC 25/05/16
 * @author    Chris Coleman, 7191375
 */
public class Prediction {
    private ArrayList<AircraftState> predictedStates; //Array List of positional predictions

    /**
     * Constructor, instantiates a ArrayList of AircraftStates
     */
    public Prediction(){predictedStates = new ArrayList<AircraftState>();}

    /**
     * Add a new state to the end of the prediction list
     * @param state : The Coordinate/Position that has been predicted
     */
    public void addState(AircraftState state){predictedStates.add(state);}

    /**
     * Returns all the states in the prediction
     * @return predictedStates : The full list of states that have been predicted
     */
    public ArrayList<AircraftState> getListOfStates(){return predictedStates;}

}
