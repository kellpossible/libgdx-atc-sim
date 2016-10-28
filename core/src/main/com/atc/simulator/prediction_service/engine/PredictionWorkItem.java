package com.atc.simulator.prediction_service.engine;

import com.atc.simulator.prediction_service.engine.algorithms.PredictionAlgorithmType;
import com.atc.simulator.prediction_service.engine.workers.PredictionWorkerThread;
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
    private Object algorithmState;
    private Prediction prediction;
    private boolean started;
    private boolean completed;
    private PredictionWorkerThread worker;
    private PredictionAlgorithmType algorithmType;

    /**
     * Creates an empty prediction work item, for use specifically
     * in the sorter.
     */
    public PredictionWorkItem()
    {
        this(null, null, null, null);
    }

    /**
     * Constructor for PredictionWorkItem
     * @param aircraftID
     * @param aircraftTrack
     * @param algorithmType the type of algorithm to be used to perform this work
     * @param algorithmState the state object for the algorithm, which is associated with the aircraftID.
     */
    public PredictionWorkItem(
            String aircraftID,
            Track aircraftTrack,
            PredictionAlgorithmType algorithmType,
            Object algorithmState)
    {
        this.aircraftID = aircraftID;
        this.aircraftTrack = aircraftTrack;
        this.algorithmType = algorithmType;
        this.algorithmState = algorithmState;

        started = false;
        completed = false;
        worker = null;

    }

    /**
     * Compare the priorities of two PredictionWorkItem s for the use in the
     * PredictionEngineThread priority queue
     * TODO: implment this!! before sorting the priorities
     * @param i0
     * @param i1
     * @return int based on priority
     */
    @Override
    public int compare(PredictionWorkItem i0, PredictionWorkItem i1) {
        return 0;//if it was created earlier then it should be higher
    }

    /**
     * Tell this object that work has begun on its contents,
     * being done by the worker specified.
     * @param worker the worker which has started working on this work item.
     */
    public void setWorkStarted(PredictionWorkerThread worker)
    {
        if (started == false && this.worker == null)
        {
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

    }

    /**
     * Get the id of the aircraft who's track is being used for the
     * prediction in this work item.
     * @return
     */
    public String getAircraftID() {
        return aircraftID;
    }


    /**
     * Get the track which is being used for the prediction in this work item.
     * @return
     */
    public Track getTrack() {
        return aircraftTrack;
    }


    /**
     * Get the prediction created by and belonging to this work item.
     * @return
     */
    public Prediction getPrediction() {
        return prediction;
    }


    /**
     * Whether or not processing has started on this work item.
     * @return
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Whether or not processing has completed on this work item.
     * @return
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Get the worker currently assigned to this work item.
     * @return
     */
    public PredictionWorkerThread getWorker() {
        return worker;
    }

    /**
     * Get the type of algorithm being used for the prediction in this work item.
     * @return
     */
    public PredictionAlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    /**
     * Get the state object for the algorithm which is associated with the aircraft
     * in this work item.
     * @return
     */
    public Object getAlgorithmState()
    {
        return algorithmState;
    }
}
