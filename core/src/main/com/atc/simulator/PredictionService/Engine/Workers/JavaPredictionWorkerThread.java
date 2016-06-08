package com.atc.simulator.PredictionService.Engine.Workers;

import com.atc.simulator.PredictionService.Engine.Algorithms.Java.JavaPredictionAlgorithm;
import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.PredictionService.Engine.PredictionWorkItem;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by luke on 8/06/16.
 */
public class JavaPredictionWorkerThread extends PredictionWorkerThread {
    protected ArrayBlockingQueue<PredictionWorkItem> todoQueue;

    public JavaPredictionWorkerThread(int workerID, PredictionEngineThread predictionEngine)
    {
        super(workerID, predictionEngine);
        todoQueue = new ArrayBlockingQueue<PredictionWorkItem>(10);
        threadName = "JavaPredictionWorkerThread " + workerID;
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
        JavaPredictionAlgorithm algorithm = JavaPredictionAlgorithm.getInstance(workItem.getAlgorithmType());
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

    @Override
    public void run() {
        while (continueThread)
        {
            if(!todoQueue.isEmpty()) {

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
