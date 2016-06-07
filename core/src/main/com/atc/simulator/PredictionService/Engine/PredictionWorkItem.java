package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

import java.util.Calendar;
import java.util.Comparator;

/**
 * A self contained item of work for a PredictionWorker to work on.
 * @author Luke Frisken
 */
public class PredictionWorkItem implements Comparator<PredictionWorkItem>{
    protected String aircraftID;
    protected Track aircraftTrack;
    protected Prediction prediction;
    protected Calendar timeCreated;
    protected Calendar timeStarted;
    protected Calendar timeCompleted;
    protected boolean started;
    protected boolean completed;
    protected PredictionWorker worker;

    public PredictionWorkItem(String aircraftID, Track aircraftTrack)
    {
        this.aircraftID = aircraftID;
        this.aircraftTrack = aircraftTrack;
        timeCreated = Calendar.getInstance();
        timeStarted = null;
        timeCompleted = null;
        started = false;
        completed = false;
        worker = null;
    }

    /**
     * Compare the priorities of two PredictionWorkItem s for the use in the
     * PredictionEngine priority queue
     * @param i0
     * @param i1
     * @return
     */
    @Override
    public int compare(PredictionWorkItem i0, PredictionWorkItem i1) {
        return i0.timeCreated.compareTo(i1.timeCreated)*-1;//if it was created earlier then it should be higher
    }
}
