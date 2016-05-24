package com.atc.simulator.flightdata;

import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.ArrayList;

/**
 * Prediction is a simple data type that the Prediction Engine can use to pass messages to the Server.
 * The main components of a prediction are:
 *      - Plane ID - String
 *      - Positions - array of positions (current, and future)
 *
 *
 * @
 * PUBLIC FEATURES:
 * // Constructors
 *    Prediction()
 * // Methods
 *    sendPredictionToServer(String aircraftID, GeographicCoordinate[] predictions)  - Encodes a Prediction and stores in internal buffer
 *    run() - Thread of checking buffer and removing first element
 *    killThread() - flags that the Server has been finished with, and to stop all threads running (clearing of buffer and accepting clients)
 *
 * COLLABORATORS:
 *    java.util.ArrayList
 *    vectors.GeographicCoordinate
 *    PredictionFeedServe.PredictionMessage
 *
 * MODIFIED:
 * @version 0.2, CC 21/05/16, Merged Encoder and Server
 * @author    Chris Coleman, 7191375
 */
public class Prediction {
    private String PlaneID; //ID of Plane that data is for
    private ArrayList<GeographicCoordinate> predictionPositions; //Array List of positional predictions

    /**
     * Constructor, instantiates a ArrayList of coordinate positions
     */
    public Prediction(){predictionPositions = new ArrayList<GeographicCoordinate>();}

    /**
     * Set the ID of the Plane that is being predicted
     * @param newID : The ID of the Aircraft being predicted
     */
    public void setPlaneID(String newID){PlaneID = newID;}

    /**
     * Add a new coordinate to the end of the prediction list
     * @param pos : The Coordinate/Position that has been predicted
     */
    public void addPosition(GeographicCoordinate pos){predictionPositions.add(pos);}

    /**
     * Get the ID of the Plane that is being predicted
     * @return PlaneID : The ID of the Aircraft being predicted
     */
    public String getPlaneID(){return PlaneID;}

    /**
     * Add a new coordinate to the end of the prediction list
     * @return predictionPositions : The full list of positions that have been predicted
     */
    public ArrayList<GeographicCoordinate> getPositionList(){return predictionPositions;}

}
