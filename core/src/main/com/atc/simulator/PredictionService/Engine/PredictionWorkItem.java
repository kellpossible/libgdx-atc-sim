package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.PredictionService.Engine.Algorithms.PredictionAlgorithmType;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

import java.util.Comparator;

/**
 * A self contained item of work for a PredictionWorkerThread to work on.
 * @author Luke Frisken
 */
public class PredictionWorkItem implements Comparator<PredictionWorkItem>{
    private String aircraftID;
    private Track aircraftTrack;
    private Prediction prediction;


    /**
     * Special note on the time tracking:
     * These are for debugging purposes.
     * They need to be removed for production.
     * Each System.currentTimeMillis() call is about 16ms,
     * which adds up to 48ms per Work Item. The impact of this can
     * be reduced by having more workers, but the currentTimeMillis is
     * likely to be a blocking call, so the more workers, the longer it takes.
     *
     * 48ms * 2000 aircraft = 32seconds to process the input from 2000 aircraft.
     */
    private boolean trackTime;
    private long timeCreated;
    private long timeStarted;
    private long timeCompleted;


    private boolean started;
    private boolean completed;
    private PredictionWorkerThread worker;
    private PredictionAlgorithmType algorithmType;


    public PredictionWorkItem()
    {
        this(null, null, null, false);
    }

    public PredictionWorkItem(
            String aircraftID,
            Track aircraftTrack,
            PredictionAlgorithmType algorithmType,
            boolean trackTime)
    {
        this.aircraftID = aircraftID;
        this.aircraftTrack = aircraftTrack;
        this.algorithmType = algorithmType;
        started = false;
        completed = false;
        worker = null;
        this.trackTime = trackTime;

        if (trackTime)
        {
            timeCreated = System.currentTimeMillis();
        }

    }

    /**
     * Compare the priorities of two PredictionWorkItem s for the use in the
     * PredictionEngineThread priority queue
     * TODO: implment this!! before sorting the priorities
     * @param i0
     * @param i1
     * @return
     */
    @Override
    public int compare(PredictionWorkItem i0, PredictionWorkItem i1) {
        return 0;//if it was created earlier then it should be higher
    }

    public void startWorking(PredictionWorkerThread worker)
    {
        if (started == false && this.worker == null)
        {
            if (trackTime)
            {
                timeStarted = System.currentTimeMillis();
            }
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
        completed = true;

        if (trackTime)
        {
            timeCompleted = System.currentTimeMillis();
        }
    }

    public String getAircraftID() {
        return aircraftID;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public Track getTrack() {
        return aircraftTrack;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public long getTimeStarted() {
        return timeStarted;
    }

    public long getTimeCompleted() {
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

    public boolean isTrackingTime() {
        return trackTime;
    }
}
