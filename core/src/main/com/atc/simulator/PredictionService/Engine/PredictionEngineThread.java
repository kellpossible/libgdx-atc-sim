package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.PredictionService.Engine.Algorithms.PredictionAlgorithmType;
import com.atc.simulator.PredictionService.Engine.Workers.JavaPredictionWorkerThread;
import com.atc.simulator.PredictionService.Engine.Workers.PredictionWorkerThread;
import com.atc.simulator.PredictionService.PredictionFeedServerThread;
import com.atc.simulator.PredictionService.SystemStateDatabase;
import com.atc.simulator.PredictionService.SystemStateDatabaseListener;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* Basic engine, will receive system states, break them into AircraftStates, create predictions and push to the server
*
* @
* PUBLIC FEATURES:
* // Constructors
*    Engine(PredictionFeedServerThread)
* // Methods
*    onSystemUpdate() - Listener Override, adds new AircraftStates to the Buffer
*    run() - Thread of checking buffer and passing messages to server
*    kill() - Clears the flag that the client thread runs off, letting the thread finish gracefully
*    start() - Start new Thread
*    makeNewPrediction(AircraftState) - Create a new prediction for the AircraftState given
*
* MODIFIED:
* @version 0.1, CC 29/05/16, Initial creation/beginning of a general framework
* @author    Chris Coleman, 7191375
* TODO: implement the PredictionEngineListener functionality
*/
public class PredictionEngineThread implements RunnableThread, SystemStateDatabaseListener{
    private static final boolean enableTimer = ApplicationConfig.getInstance().getBoolean("settings.debug.engine-timer");
    private static final boolean enableDebugPrintQueues = ApplicationConfig.getInstance().getBoolean("settings.debug.print-queues");

    private PredictionEngineTodoQueue todoQueue; //TODO: make this threadsafe and possibly use a seperate buffer
    private SystemStateDatabase systemStateDatabase;
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
            SystemStateDatabase systemStateDatabase,
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
        workItem.startWorking(worker);
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
        long start1=0, start2=0;
        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        predictionFeedServer.sendPrediction(prediction);
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
        todoList.remove(workItem);
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
                currentTime = System.currentTimeMillis();
                if ((currentTime - lastTime) > (maxLatency-5))
                {
                    for (PredictionWorkItem item : todoList)
                    {
                        if (previousTodoList.contains(item))
                        {
                            System.err.println("ERROR: "
                                    + threadName
                                    + " item "
                                    + item
                                    + " is taking too long to complete (>"
                                    + (currentTime-lastTime)
                                    + "ms)and is exceeding latency constraints");
                        }

                        previousTodoList.clear();
                        previousTodoList.addAll(todoList);
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
                Thread.sleep(maxLatency);
            } catch (InterruptedException e) {
                System.err.println("ERROR: Timer interrupted, ignore latency warnings");
                e.printStackTrace();
            }
            lastTime = currentTime;

        }
    }

    /**
     * Small method called too kill the server's threads when the have run through
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
      * Interface implementation for SystemStateDatabaseListener
      * gets called whenever the SystemStateDatabase receives an update.
      * @param aircraftIDs
      */
     @Override
     public void onSystemStateUpdate(ArrayList<String> aircraftIDs) {
         for (String aircraftID: aircraftIDs)
         {
             Track aircraftTrack = systemStateDatabase.getTrack(aircraftID); //TODO: make this a deep copy
             PredictionWorkItem workItem = new PredictionWorkItem(
                     aircraftID,
                     aircraftTrack,
                     PredictionAlgorithmType.PASSTHROUGH);
             //TODO: make a seperate buffer for this so it doesn't block while todoQueue is being reordered?
             if(enableDebugPrintQueues){System.out.println(threadName + " Adding to queue which has a current size of " + todoQueue.size());}

             todoQueueLock.lock();
             try {
                 todoQueue.add(workItem); //TODO: make this an addinorder call
                 todoQueueChangedCondition.signalAll();
             } finally {
                 todoQueueLock.unlock();
             }

             todoList.add(workItem);

         }
     }
 }
