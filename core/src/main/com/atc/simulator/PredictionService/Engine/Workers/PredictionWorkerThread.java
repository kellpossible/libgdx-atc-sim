package com.atc.simulator.PredictionService.Engine.Workers;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.PredictionService.Engine.Algorithms.Java.JavaPredictionAlgorithm;
import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.PredictionService.Engine.PredictionWorkItem;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * A worker who can work on PredictionWorkItem s that the PredictionEngineThread gives to it.
 * Has at least one internal thread.
 *
 * Could be subclassed to create opencl workers.
 *
 * @author Luke Frisken
 */
public abstract class PredictionWorkerThread implements RunnableThread{
    protected static final boolean enableTimer = ApplicationConfig.getBoolean("settings.debug.worker-timer");
    protected static final boolean enableDebugPrint = ApplicationConfig.getBoolean("settings.debug.print-worker");

    protected PredictionEngineThread predictionEngine;
    private Thread thread;
    protected String threadName;
    protected boolean continueThread;
    protected int workerID;

    public PredictionWorkerThread(int workerID, PredictionEngineThread predictionEngine) {
        this.workerID = workerID;
        thread = null;
        this.predictionEngine = predictionEngine;
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
     * Join this thread.
     */
    @Override
    public void join() throws InterruptedException {

    }


}
