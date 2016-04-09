package com.atc.simulator.flightdata;

import com.atc.simulator.GeographicCoordinate;

import java.util.Calendar;

/**
 * Created by luke on 7/04/16.
 * Represents the state of an aircraft at a particular point in time.
 */
public class TrackEntry {
    private Calendar time;
    private GeographicCoordinate position;

    public TrackEntry(Calendar time, GeographicCoordinate position)
    {
        this.time = time;
        this.position = position;

    }

    public GeographicCoordinate getPosition() {
        return position;
    }

    public void setPosition(GeographicCoordinate position) {
        this.position = position;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

}
