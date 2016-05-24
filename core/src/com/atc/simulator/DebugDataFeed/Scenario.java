package com.atc.simulator.DebugDataFeed;

import com.atc.simulator.flightdata.SystemState;

import java.util.Calendar;

/**
 * Created by luke on 24/05/16.
 */
public abstract class Scenario {
    private Calendar startTime;
    private Calendar endTime;

    public abstract SystemState getState(Calendar time);

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
}
