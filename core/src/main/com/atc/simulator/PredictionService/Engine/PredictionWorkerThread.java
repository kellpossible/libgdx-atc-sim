package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * A worker who can work on PredictionWorkItem s that the PredictionEngineThread gives to it.
 * Has at least one internal thread.
 *
 * Could be subclassed to create opencl workers.
 *
 * @author Luke Frisken
 */
public class PredictionWorkerThread implements RunnableThread{
    private PredictionEngineThread predictionEngine;
    private ArrayBlockingQueue<PredictionWorkItem> todoQueue;
    private Thread thread;
    private String threadName;
    private boolean continueThread;
    private int workerID;

    public PredictionWorkerThread(int workerID, PredictionEngineThread predictionEngine) {
        this.workerID = workerID;
        thread = null;
        this.predictionEngine = predictionEngine;
        todoQueue = new ArrayBlockingQueue<PredictionWorkItem>(10);
        threadName =  "PredictionWorkerThread " + workerID;
        continueThread = false;
    }

    /**
     * Start this thread
     */
    @Override
    public void start() {
        continueThread = true;
        if (thread == null)
        {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

    /**
     * Kill this thread asynchronously
     */
    @Override
    public void kill() {

    }

    /**
     * Private method that will create a new prediction and send it to the PredictionFeedServerThread
     */
    private void makeNewPrediction(PredictionWorkItem workItem)
    {
        Track aircraftTrack = workItem.getTrack();
        AircraftState state = aircraftTrack.getLatest();
        ArrayList<GeographicCoordinate> predictionPositions = new ArrayList<GeographicCoordinate>();
        //Add the current position
        predictionPositions.add(state.getPosition());

        //Make a very simple prediction
        double tempAlt = state.getPosition().getAltitude();
        double tempLat = state.getPosition().getLatitude()+0.5;
        double tempLon = state.getPosition().getLongitude()+0.5;
        //Add it to the prediction
        predictionPositions.add(new GeographicCoordinate(tempAlt,tempLat,tempLon));

        //Add the ID and Time, mark the work item as complete, tell the prediction engine
        workItem.complete(new Prediction(state.getAircraftID(), state.getTime(), predictionPositions));
        predictionEngine.completeWorkItem(workItem);
        System.out.println(threadName + " finished a prediction");
    }

    /**
     * Join this thread.
     */
    @Override
    public void join() throws InterruptedException {

    }

    @Override
    public void run() {
        while (continueThread)
        {
            if(!todoQueue.isEmpty()) {
                makeNewPrediction(todoQueue.poll());
            } else {
                PredictionWorkItem newWorkItem = predictionEngine.startWorkItem(this);
                System.out.println(threadName + " starting on a new work item");
                todoQueue.add(newWorkItem);
            }
        }
    }
}
