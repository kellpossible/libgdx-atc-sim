package com.atc.simulator.flightdata;

import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

/**
 * Created by luke on 7/05/16.
 */
public class AircraftState {
    private String aircraftID;
    private GeographicCoordinate position;
    private SphericalVelocity velocity;
    private float heading;

    public AircraftState(String aircraftID, GeographicCoordinate position, SphericalVelocity velocity, float heading)
    {
        this.aircraftID = aircraftID;
        this.position = position;
        this.velocity = velocity;
        this.heading = heading;
    }

    public GeographicCoordinate getPosition() {
        return position;
    }

    public void setPosition(GeographicCoordinate position) {
        this.position = position;
    }

    public String getAircraftID() {
        return aircraftID;
    }

    public void setAircraftID(String aircraftID) {
        this.aircraftID = aircraftID;
    }

    public SphericalVelocity getVelocity() {
        return velocity;
    }

    public void setVelocity(SphericalVelocity velocity) {
        this.velocity = velocity;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }
}
