package com.atc.simulator.prediction_service.engine;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.prediction_service.engine.algorithms.java.JavaPredictionAlgorithm;
import com.atc.simulator.prediction_service.engine.algorithms.PredictionAlgorithmType;
import com.atc.simulator.prediction_service.engine.workers.JavaPredictionWorkerThread;
import com.atc.simulator.prediction_service.engine.workers.PredictionWorkerThread;
import com.atc.simulator.prediction_service.PredictionFeedServerThread;
import com.atc.simulator.flightdata.system_state_database.SystemStateDatabase;
import com.atc.simulator.flightdata.system_state_database.SystemStateDatabaseListener;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* Basic engine, will receive system states, break them into AircraftStates, create predictions and push to the server

* @author    Chris Coleman, 7191375, Luke Frisken
* TODO: implement the PredictionEngineListener functionality
*/
public class PredictionEngineThread implements RunnableThread, SystemStateDatabaseListener{
    private static final boolean enableTimer = ApplicationConfig.getBoolean("settings.debug.engine-timer");
    private static final boolean enableDebugPrintQueues = ApplicationConfig.getBoolean("settings.debug.print-queues");
    private static final PredictionAlgorithmType algorithmType = (PredictionAlgorithmType) ApplicationConfig.getEnum("settings.prediction-service.prediction-engine.algorithm-type", PredictionAlgorithmType.class);

    private PredictionEngineTodoQueue todoQueue;
    private PredictionEngineSystemStateDatabase systemStateDatabase;
    private ArrayList<PredictionWorkerThread> workerPool;
    private ArrayList<PredictionWorkItem> todoList;
    private ArrayList<PredictionWorkItem> previousTodoList;

    private final Lock todoQueueLock = new ReentrantLock();
    private final Condition todoQueueChangedCondition = todoQueueLock.newCondition();

    //Internal settings
    private PredictionFeedServerThread predictionFeedServer;
    //Thread definitions
    private boolean continueThread = true;  //Simple flag that dictates whether the Server threads will keep looping
    private Thread thread;
    private final String threadName = "PredictionEngineThread";
    private int numberOfWorkers;
    private final int todoQueueMaxSize = 500;
    private final long maxLatency = 400;


    /**
     * Constructor, connect to the server and create a new arrayList
     * @param predictionFeedServer : The server I need to connect to
     */
    public PredictionEngineThread(
            PredictionFeedServerThread predictionFeedServer,
            PredictionEngineSystemStateDatabase systemStateDatabase,
            int numberOfWorkers)
    {
        this.numberOfWorkers = numberOfWorkers;
        this.predictionFeedServer = predictionFeedServer;
        todoQueue = new PredictionEngineTodoQueue();

        //populate the worker pool with workers
        workerPool = new ArrayList<PredictionWorkerThread>();
        for (int i = 0; i<numberOfWorkers; i++)
        {
            workerPool.add(new JavaPredictionWorkerThread(i, this));
        }
        this.systemStateDatabase = systemStateDatabase;

        todoList = new ArrayList<PredictionWorkItem>();
        previousTodoList = new ArrayList<PredictionWorkItem>();
    }

    /**
     * Waits for a new work item to be available, and when available
     * marks it as started, and then returns it to the worker who
     * requested it.
     * @param worker worker who is requesting this work item
     * @return
     */
    public PredictionWorkItem startWorkItem(PredictionWorkerThread worker)
    {
        //there is a potential here for orphan threads to be created that never get killed...
        PredictionWorkItem workItem = todoQueue.take();
        workItem.setWorkStarted(worker);
        return  workItem;
    }

    /**
     * Tell the PredictionEngineThread that a work item has been
     * completed by one of its workers.
     * @param workItem
     */
    public void completeWorkItem(PredictionWorkItem workItem)
    {
        Prediction prediction = workItem.getPrediction();
        long start1=0, start2=0; //required for timer
        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        predictionFeedServer.sendPrediction(prediction); //send prediction to the display using the server
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println(threadName + " sendPrediction " + (((double) diff)/1000000.0) + " ms");
        }

        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        synchronized (todoList)
        {
            todoList.remove(workItem); //remove this workItem from the todoList
        }

        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println(threadName + " removeWorkItemtodoList " + (((double) diff)/1000000.0) + " ms");
        }
    }

    /**
     * currently this does nothing, in the future it could be used
     * to reprioritize items in the queue.
     */
    public void run() {
        long lastTime = System.currentTimeMillis();
        long currentTime = 0;
        while (continueThread)
        {
            try {
                /*
                Check to see whether items are staying on the todo list,
                indicating that they have been around longer than maxLatency,
                the maximum allowable latency for this work item.
                 */
                currentTime = System.currentTimeMillis();
                if ((currentTime - lastTime) > (maxLatency-5))
                {
                    synchronized (todoList)
                    {
                        for (PredictionWorkItem item : todoList)
                        {
                            if (previousTodoList.contains(item))
                            {
//                                System.err.println("ERROR: "
//                                        + threadName
//                                        + " item "
//                                        + item
//                                        + " is taking too long to complete (>"
//                                        + (currentTime-lastTime)
//                                        + "ms)and is exceeding latency constraints");
                            }

                            previousTodoList.clear();
                            previousTodoList.addAll(todoList);
                        }
                    }
                }

                if (todoQueue.size() > todoQueueMaxSize)
                {
                    System.err.println("ERROR: "
                            + threadName
                            + " todoQueue is exceeding maximum allowable size ("
                            + todoQueueMaxSize
                            + ")");
                }
                Thread.sleep(maxLatency); //sleep for maxLatency between checks of the todoList
            } catch (InterruptedException e) {
                System.err.println("ERROR: Timer interrupted, ignore latency warnings");
                e.printStackTrace();
            }
            lastTime = currentTime;

        }
    }

    /**
     * Small method called too kill the server's threads when the have update through
     */
    public void kill()
    {
        continueThread = false;
        for (PredictionWorkerThread worker: workerPool)
        {
            worker.kill();
        }
    }

     /**
      * Join this thread.
      */
     @Override
     public void join() throws InterruptedException {
         for (PredictionWorkerThread worker: workerPool)
         {
             worker.join();
         }
         thread.join();
     }

     /**
     * Start this thread
     */
    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this, threadName);
            thread.start();
        }

        for (PredictionWorkerThread worker: workerPool)
        {
            worker.start();
        }
    }

    /**
     * This method is called by the SystemStateDataBase on its listeners
     * whenever the SystemStateDatabase receives updated information.
     *
     * @param stateDatabase
     * @param aircraftIDs   of type ArrayList&lt;String&gt;
     */
    @Override
    public void onSystemStateUpdate(SystemStateDatabase stateDatabase, ArrayList<String> aircraftIDs) {
        for (String aircraftID: aircraftIDs)
        {
            Track aircraftTrack = stateDatabase.copyTrack(aircraftID);

            // get the algorithm state from the system state database, and create a new one if it doesn't exist yet
            Object algorithmState = systemStateDatabase.getAlgorithmState(aircraftID);
            if (algorithmState == null)
            {
                algorithmState = JavaPredictionAlgorithm.getInstance(algorithmType).getNewStateObject();
                systemStateDatabase.setAlgorithmState(aircraftID, algorithmState);
            }

            PredictionWorkItem workItem = new PredictionWorkItem(
                    aircraftID,
                    aircraftTrack,
                    algorithmType,
                    algorithmState
                    );
            //TODO: make a seperate buffer for this so it doesn't block while todoQueue is being reordered?
            if(enableDebugPrintQueues){System.out.println(threadName + " Adding to queue which has a current size of " + todoQueue.size());}

            todoQueueLock.lock();
            try {
                todoQueue.add(workItem); //TODO: make this an addinorder call
                todoQueueChangedCondition.signalAll();
            } finally {
                todoQueueLock.unlock();
            }

            synchronized (todoList)
            {
                todoList.add(workItem);
            }
        }
    }

    @Override
    public void onNewAircraft(SystemStateDatabase stateDatabase, String aircraftID) {

    }

    @Override
    public void onRemoveAircraft(SystemStateDatabase stateDatabase, String aircraftID) {

    }

    @Override
    public void onUpdateAircraft(SystemStateDatabase stateDatabase, String aircraftID) {

    }
}
