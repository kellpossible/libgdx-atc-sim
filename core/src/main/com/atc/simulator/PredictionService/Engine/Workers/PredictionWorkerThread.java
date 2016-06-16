package com.atc.simulator.PredictionService.Engine.Workers;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.PredictionService.Engine.Algorithms.PredictionAlgorithm;
import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.PredictionService.Engine.PredictionWorkItem;
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
    private static final boolean enableTimer = ApplicationConfig.getInstance().getBoolean("settings.debug.worker-timer");
    private static final boolean enableDebugPrint = ApplicationConfig.getInstance().getBoolean("settings.debug.print-worker");

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
        long start1=0, start2=0;
        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        Track aircraftTrack = workItem.getTrack();
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println(threadName + " getTrack " + (((double) diff)/1000000.0) + " ms");
        }


        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        //get the algorithm type
        PredictionAlgorithm algorithm = PredictionAlgorithm.getInstance(workItem.getAlgorithmType());
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println(threadName + " getAlgorithmInstance " + (((double) diff)/1000000.0) + " ms");
        }


        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        //Make a prediction using the algorithm
        Prediction prediction = algorithm.makePrediction(aircraftTrack);
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println(threadName + " makePrediction " + (((double) diff)/1000000.0) + " ms");
        }

        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        //tell the PredictionEngine that this work item is complete.
        //TODO: I have a feeling the class design could be improved a lot here...
        workItem.complete(prediction);
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println(threadName + " workItemComplete " + (((double) diff)/1000000.0) + " ms");
        }

        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        predictionEngine.completeWorkItem(workItem);
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println(threadName + " completeWorkItem " + (((double) diff)/1000000.0) + " ms");
        }
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

                //make a new prediction every time there is an item in the todoQueue
                makeNewPrediction(todoQueue.poll());

                if(enableDebugPrint){ System.out.println(threadName + " finished a prediction"); }
            } else {
                PredictionWorkItem newWorkItem = predictionEngine.startWorkItem(this);
                if(enableDebugPrint){ System.out.println(threadName + " starting on a new work item"); }
                todoQueue.add(newWorkItem);
            }
        }
    }
}
