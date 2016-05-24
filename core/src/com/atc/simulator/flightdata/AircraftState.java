package com.atc.simulator.flightdata;

import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

/**
 * Created by luke on 7/05/16.
 * Represents the state of an aircraft
 * @author Luke Frisken
 */
public class AircraftState {
    private String aircraftID;
    private GeographicCoordinate position;
    private SphericalVelocity velocity;
    private double heading;

    /**
     * Constructor for AircraftState
     * @param aircraftID the id of the aircraft
     * @param position the position of the aircraft
     * @param velocity the velocity of the aircraft
     * @param heading the heading of the aircraft with respect to true north
     */
    public AircraftState(String aircraftID, GeographicCoordinate position, SphericalVelocity velocity, double heading)
    {
        this.aircraftID = aircraftID;
        this.position = position;
        this.velocity = velocity;
        this.heading = heading;
    }

    /**
     * Method getPosition returns the position of this AircraftState object.
     * @return the position (type GeographicCoordinate) of this AircraftState object.
     */
    public GeographicCoordinate getPosition() {
        return position;
    }

    /**
     * Method setPosition sets the position of the aircraft represented by this AircraftState
     * @param position the position of the aircraft represented by this AircraftState
     *
     */
    public void setPosition(GeographicCoordinate position) {
        this.position = position;
    }

    /**
     * Method getAircraftID returns the aircraftID of the aircraft represented by this AircraftState.
     * @return the aircraftID (type String) of of the aircraft represented by this AircraftState.
     */
    public String getAircraftID() {
        return aircraftID;
    }

    /**
     * Method setAircraftID sets the aircraftID of the aircraft represented by this AircraftState.
     * @param aircraftID the aircraftID of the aircraft represented by this AircraftState.
     */
    public void setAircraftID(String aircraftID) {
        this.aircraftID = aircraftID;
    }

    /**
     * Method getVelocity returns the velocity of the aircraft represented by this AircraftState
     * @return the velocity (type SphericalVelocity) of the aircraft represented by this AircraftState
     */
    public SphericalVelocity getVelocity() {
        return velocity;
    }

    /**
     * Method setVelocity sets the velocity of the aircraft represented by this AircraftState
     * @param velocity the velocity of the aircraft represented by this AircraftState
     *
     */
    public void setVelocity(SphericalVelocity velocity) {
        this.velocity = velocity;
    }

    /**
     * Method getHeading returns the heading of the aircraft represented by this AircraftState
     * @return the heading (type float) of the aircraft represented by this AircraftState
     */
    public double getHeading() {
        return heading;
    }

    /**
     * Method setHeading sets the heading of the aircraft represented by this AircraftState
     * @param heading the heading of the aircraft represented by this AircraftState
     *
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }
}
