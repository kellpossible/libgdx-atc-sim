package com.atc.simulator.flightdata;

import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

import java.util.Calendar;

/**
 * Created by luke on 7/05/16.
 * Represents the state of an aircraft
 * @author Luke Frisken
 */
public class AircraftState {
    private String aircraftID;
    private long time;
    private GeographicCoordinate position;
    private SphericalVelocity velocity;
    private double heading;

    /**
     * Constructor for AircraftState
     * @param aircraftID the id of the aircraft
     * @param time the time that this aircraft state represents
     * @param position the position of the aircraft
     * @param velocity the velocity of the aircraft
     * @param heading the heading of the aircraft with respect to true north
     */
    public AircraftState(String aircraftID,
                         long time,
                         GeographicCoordinate position,
                         SphericalVelocity velocity,
                         double heading)
    {
        this.aircraftID = aircraftID;
        this.time = time;
        this.position = position;
        this.velocity = velocity;
        this.heading = heading;
    }

    /**
     * Create a copy of an {@link AircraftState} (not a deep copy though)
     * @param aircraftState state to copy
     */
    public AircraftState(AircraftState aircraftState)
    {
        this.copyData(aircraftState);
    }

    /**
     * Linearly interpolate between this state and another state.
     * when t = 0.0 it is entirely this state
     * when t = 1.0 it is entirely the other state
     * @param other other state to interpolate with
     * @param t factor between 0.0 and 1.0
     * @return the interpolated state
     */
    public AircraftState lerp(AircraftState other, double t)
    {
        GeographicCoordinate newPosition = new GeographicCoordinate(this.getPosition().linearIntepolate(other.getPosition(), t));
        SphericalVelocity newVelocity = new SphericalVelocity(this.getVelocity().lerp(other.getVelocity(), t));

        long newTime = this.getTime() + (long)(((double) (other.getTime() - this.getTime())) * t);

        double headingDiff = other.getHeading() - this.heading;
        double newHeading = this.heading + headingDiff * t;

        return new AircraftState(aircraftID, newTime, newPosition, newVelocity, newHeading);
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


    /**
     * Method getTime returns the time that this AircraftState represents
     * @return the time (type Calendar) of this AircraftState object.
     */
    public long getTime() {
        return time;
    }

    /**
     * Method setTime sets the time that this AircraftState represents
     * @param time the time of this AircraftState object.
     *
     */
    public void setTime(long time) {
        this.time = time;
    }


    /**
     * Shallow copy state data from another AircraftState object.
     * @param other aircraft state to copy from
     */
    public void copyData(AircraftState other)
    {
        aircraftID = other.aircraftID;
        time = other.time;
        position = other.position;
        velocity = other.velocity;
        heading = other.heading;
    }
}
