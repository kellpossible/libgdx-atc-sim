package com.atc.simulator.flightdata;

import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.Calendar;

/**
 * Created by luke on 7/04/16.
 * Represents the state of an aircraft at a particular point in time.
 */
public class TrackEntry {
    private Calendar time;
    private GeographicCoordinate position;

    /**
     * @param time the point in time this entry represents
     * @param position the geographical position of this entry
     */
    public TrackEntry(Calendar time, GeographicCoordinate position)
    {
        this.time = time;
        this.position = position;

    }

    /**
     * Get the geographical position of this track entry
     * @return
     */
    public GeographicCoordinate getPosition() {
        return position;
    }

    /**
     * Set the geographical position of this track entry
     * @param position
     */
    public void setPosition(GeographicCoordinate position) {
        this.position = position;
    }

    /**
     * Get the time this track entry represents.
     * @return
     */
    public Calendar getTime() {
        return time;
    }

    /**
     * Set the time this track entry represents.
     * @param time
     */
    public void setTime(Calendar time) {
        this.time = time;
    }

}
