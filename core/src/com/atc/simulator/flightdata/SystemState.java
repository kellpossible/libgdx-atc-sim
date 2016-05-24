package com.atc.simulator.flightdata;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by luke on 24/05/16.
 */
public class SystemState {
    private Calendar time;
    private ArrayList<TrackEntry> trackEntries;

    public SystemState()
    {
        trackEntries = new ArrayList<TrackEntry>();
    }


    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public ArrayList<TrackEntry> getTrackEntries() {
        return trackEntries;
    }

    public void setTrackEntries(ArrayList<TrackEntry> trackEntries) {
        this.trackEntries = trackEntries;
    }
}
