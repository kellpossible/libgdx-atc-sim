package com.atc.simulator.flightdata;

import com.atc.simulator.SphericalPosition;

import java.util.Calendar;

/**
 * Created by luke on 7/04/16.
 */
public class TrackEntry {
    private Calendar time;
    private SphericalPosition position;

    public TrackEntry(Calendar time, SphericalPosition position)
    {
        this.time = time;
        this.position = position;

    }

    public SphericalPosition getPosition() {
        return position;
    }

    public void setPosition(SphericalPosition position) {
        this.position = position;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

}
