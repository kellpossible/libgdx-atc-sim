package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.PredictionService.Engine.Algorithms.PredictionAlgorithm;
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
     * for inducing a high load on this thread.
     * credit: http://stackoverflow.com/questions/382113/generate-cpu-load-in-java#answer-382212
     * @param milliseconds
     */
    private static void spin(int milliseconds) {
        long sleepTime = milliseconds*1000000L; // convert to nanoseconds
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < sleepTime) {}
    }

    /**
     * Private method that will create a new prediction and send it to the PredictionFeedServerThread
     */
    private void makeNewPrediction(PredictionWorkItem workItem)
    {
//        spin(50);
        Track aircraftTrack = workItem.getTrack();
        PredictionAlgorithm algorithm = PredictionAlgorithm.getInstance(workItem.getAlgorithmType());
        Prediction prediction = algorithm.makePrediction(aircraftTrack);
        workItem.complete(prediction);
        predictionEngine.completeWorkItem(workItem);
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
                long start1 = System.nanoTime();
                // maybe add here a call to a return to remove call up time, too.
                // Avoid optimization
                long start2 = System.nanoTime();
                makeNewPrediction(todoQueue.poll());
                long stop = System.nanoTime();
                long diff = stop - 2*start2 + start1;
                ApplicationConfig.debugPrint("print-worker", threadName + " work time: " + (((double) diff)/1000000.0) + " ms");
                ApplicationConfig.debugPrint("print-worker", threadName + " finished a prediction");
            } else {
                PredictionWorkItem newWorkItem = predictionEngine.startWorkItem(this);
                ApplicationConfig.debugPrint("print-worker", threadName + " starting on a new work item");
                todoQueue.add(newWorkItem);
            }
        }
    }
}
