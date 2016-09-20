package com.atc.simulator.flightdata;

import java.util.ArrayList;

/**
 * Prediction is a simple data type, storing a list of AircraftStates that form a future prediction made by the Prediction Engine
 *
 * @author    Chris Coleman, Luke Frisken
 */
public class Prediction {
    /**
     * Array List of positional predictions on the left boundary of the prediction area
     */
    private Track leftTrack;

    /**
     * Array List of positional predictions in the centre of the prediction area
     */
    private Track centreTrack;

    /**
     * Array List of positional predictions on the right boundary of the prediction area
     */
    private Track rightTrack;

    private String aircraftID;
    private long time;

    /**
     * Constructor Prediction creates a new Prediction instance.
     *
     * @param aircraftID of type String
     * @param time of type long. The time (in milliseconds since epoch) for the first predicted position.
     * @param leftTrack of type ArrayList<AircraftState>
     * @param centreTrack of type ArrayList<AircraftState>
     * @param rightTrack of type ArrayList<AircraftState>
     */
    public Prediction(String aircraftID,
                      long time,
                      Track leftTrack,
                      Track centreTrack,
                      Track rightTrack)
    {
        this.leftTrack = leftTrack;
        this.centreTrack = centreTrack;
        this.rightTrack = rightTrack;
        this.aircraftID = aircraftID;
        this.time = time;
    }

    /**
     * Create a copy of a prediction (not a deep copy)
     * @param prediction prediction to copy
     */
    public Prediction(Prediction prediction)
    {
        this.copyData(prediction);
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
     * Returns all the states in the prediction of the line which determines the
     * left boundary of the prediction area
     * @return leftTrack
     */
    public Track getLeftTrack() {
        return leftTrack;
    }

    /**
     * Returns all the states in the prediction of the line which lies in the centre
     * of the prediction area.
     * @return centreTrack
     */
    public Track getCentreTrack() {
        return centreTrack;
    }

    /**
     * Returns all the states in the prediction of the line which determines the
     * right boundary of the prediction area
     * @return rightTrack
     */
    public Track getRightTrack() {
        return rightTrack;
    }

    /**
     * Whether or not this prediction has a left track
     * @see #getLeftTrack()
     * @return boolean
     */
    public boolean hasLeftTrack()
    {
        return leftTrack != null;
    }

    /**
     * Whether or not this prediction has a left track
     * @see #getLeftTrack()
     * @return boolean
     */
    public boolean hasCentreTrack()
    {
        return centreTrack != null;
    }

    /**
     * Whether or not this prediction has a left track
     * @see #getLeftTrack()
     * @return boolean
     */
    public boolean hasRightTrack()
    {
        return rightTrack != null;
    }

    /**
     * Shallow copy prediction data from another prediction
     * @param other prediction to copy data from.
     */
    public void copyData(Prediction other)
    {
        aircraftID = other.aircraftID;
        time = other.time;
        this.leftTrack = other.leftTrack;
        this.centreTrack = other.centreTrack;
        this.rightTrack = other.rightTrack;
    }

}
