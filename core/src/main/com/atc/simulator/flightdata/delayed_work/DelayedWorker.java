package com.atc.simulator.flightdata.delayed_work;

import java.util.HashMap;

/**
 * This class is designed to be run periodically using the run method in order
 * to work on a backlog of work that has been placed in the workQueue. It operates
 * on a set budget of "work points" each time the run method is called. This is to prevent
 * too much work being done in any given frame that the run method is called, causing a loss
 * in framerate.
 * @see #run()
 * @author Luke Frisken
 */
public class DelayedWorker {
    private HashMap<DelayedWorkQueueItemType, DelayedWorkBuffer> workBufferMap;
    private DelayedWorkQueue workQueue;
    private int budgetPerFrame;
    private static final int WORK_BUFFER_SIZE = 1;

    /**
     * Constructor for DelayedWorker
     * @param budgetPerFrame number of work points to process per frame.
     */
    public DelayedWorker(int budgetPerFrame)
    {
        workBufferMap = new HashMap<DelayedWorkQueueItemType, DelayedWorkBuffer>();
        workQueue = new DelayedWorkQueue();
        this.budgetPerFrame = budgetPerFrame;
    }

    /**
     * Process items in the workQueue
     */
    public void run() {
        DelayedWorkQueueItem workItem = workQueue.poll();
        int expenditure = 0;

        while(workItem != null && expenditure < budgetPerFrame)
        {
            expenditure += workItem.getCost();

            DelayedWorkQueueItemType workItemType = workItem.getType();
            DelayedWorkBuffer delayedWorkBuffer = workBufferMap.get(workItemType);

            //check to see that the item hasn't been removed yet from the workBufferMap,
            //as it may have been removed when a cameralistener was deleted, when
            //an aircraft was deleted.
            if (delayedWorkBuffer != null) {
                delayedWorkBuffer.remove(workItem, false);
                workItem.run();
            }

            workItem = workQueue.poll();
        }
    }

    /**
     * Add a new type of work item.
     * @param workItemType the new type of work item to be added
     */
    public void addWorkItemType(DelayedWorkQueueItemType workItemType)
    {
        workBufferMap.put(workItemType, new DelayedWorkBuffer(WORK_BUFFER_SIZE, workQueue));
    }

    /**
     * Remove a work item type
     * @param workItemType the work item type to be removed
     */
    public void removeWorkItemType(DelayedWorkQueueItemType workItemType)
    {
        workBufferMap.remove(workItemType);
    }

    /**
     * Add a new work item to the queue.
     * @param item
     */
    public void addWorkItem(DelayedWorkQueueItem item)
    {
        if (item == null)
        {
            System.err.println("Invalid work item!");
        }
        DelayedWorkBuffer buffer = workBufferMap.get(item.getType());
        buffer.add(item);
    }

}
