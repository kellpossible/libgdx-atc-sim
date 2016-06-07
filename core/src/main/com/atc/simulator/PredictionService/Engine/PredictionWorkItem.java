package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

import java.util.Calendar;
import java.util.Comparator;

/**
 * A self contained item of work for a PredictionWorkerThread to work on.
 * @author Luke Frisken
 */
public class PredictionWorkItem implements Comparator<PredictionWorkItem>{
    private String aircraftID;
    private Track aircraftTrack;
    private Prediction prediction;
    private Calendar timeCreated;
    private Calendar timeStarted;
    private Calendar timeCompleted;
    private boolean started;
    private boolean completed;
    private PredictionWorkerThread worker;
    private PredictionAlgorithmType algorithmType;

    public PredictionWorkItem()
    {
        this(null, null, null);
    }

    public PredictionWorkItem(String aircraftID, Track aircraftTrack, PredictionAlgorithmType algorithmType)
    {
        this.aircraftID = aircraftID;
        this.aircraftTrack = aircraftTrack;
        this.algorithmType = algorithmType;
        timeCreated = Calendar.getInstance();
        timeStarted = null;
        timeCompleted = null;
        started = false;
        completed = false;
        worker = null;
    }

    /**
     * Compare the priorities of two PredictionWorkItem s for the use in the
     * PredictionEngineThread priority queue
     * @param i0
     * @param i1
     * @return
     */
    @Override
    public int compare(PredictionWorkItem i0, PredictionWorkItem i1) {
        return i0.getTimeCreated().compareTo(i1.getTimeCreated())*-1;//if it was created earlier then it should be higher
    }

    public void startWorking(PredictionWorkerThread worker)
    {
        if (timeStarted == null && this.worker == null)
        {
            timeStarted = Calendar.getInstance();
            started = true;
            this.worker = worker;
        }else {
            System.err.println("ERROR: Cannot start working on an item which is already being worked");
        }
    }

    /**
     * Complete the work item by providing a prediction
     * @param prediction
     */
    public void complete(Prediction prediction)
    {
        this.prediction = prediction;
        timeCompleted = Calendar.getInstance();
        completed = true;
    }

    public String getAircraftID() {
        return aircraftID;
    }

    public Calendar getTimeCreated() {
        return timeCreated;
    }

    public Track getTrack() {
        return aircraftTrack;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public Calendar getTimeStarted() {
        return timeStarted;
    }

    public Calendar getTimeCompleted() {
        return timeCompleted;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isCompleted() {
        return completed;
    }

    public PredictionWorkerThread getWorker() {
        return worker;
    }

    public PredictionAlgorithmType getAlgorithmType() {
        return algorithmType;
    }
}
