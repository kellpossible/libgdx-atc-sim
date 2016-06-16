package com.atc.simulator.PredictionService.Engine.Workers;

import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.PredictionService.Engine.PredictionWorkItem;

/**
 * Created by luke on 8/06/16.
 */
public class OpenCLPredictionWorkerThread extends PredictionWorkerThread{
    public OpenCLPredictionWorkerThread(int workerID, PredictionEngineThread predictionEngine) {
        super(workerID, predictionEngine);
        threadName = "OpenCLPredictionWorkerThread " + workerID;
    }

    private void makeNewPrediction(PredictionWorkItem workItem)
    {

    }

    @Override
    public void run() {
        PredictionWorkItem workItem = null;
        while (continueThread)
        {
            if(workItem != null) {

                makeNewPrediction(workItem);

                if(enableDebugPrint){ System.out.println(threadName + " finished a prediction"); }
            } else {
                workItem = predictionEngine.startWorkItem(this);
                if(enableDebugPrint){ System.out.println(threadName + " starting on a new work item"); }
            }
        }
    }
}
