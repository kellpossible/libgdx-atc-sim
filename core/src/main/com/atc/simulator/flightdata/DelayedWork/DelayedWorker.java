package com.atc.simulator.flightdata.DelayedWork;

import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;

/**
 * @author Luke Frisken
 */
public class DelayedWorker {
    private HashMap<DelayedWorkQueueItemType, DelayedWorkBuffer> workBufferMap;
    private DelayedWorkQueue workQueue;
    private int budgetPerFrame;
    private static final int WORK_BUFFER_SIZE = 1;

    public DelayedWorker(int budgetPerFrame)
    {
        workBufferMap = new HashMap<DelayedWorkQueueItemType, DelayedWorkBuffer>();
        workQueue = new DelayedWorkQueue();
        this.budgetPerFrame = budgetPerFrame;
    }

    public void run() {
        DelayedWorkQueueItem workItem = workQueue.poll();
        int expenditure = 0;

        while(workItem != null && expenditure < budgetPerFrame)
        {
            expenditure += workItem.getCost();

            DelayedWorkQueueItemType consumer = workItem.getType();
            workBufferMap.get(consumer).remove(workItem, false);
            workItem.run();

            workItem = workQueue.poll();
        }
    }

    public void addConsumer(DelayedWorkQueueItemType consumer)
    {
        workBufferMap.put(consumer, new DelayedWorkBuffer(WORK_BUFFER_SIZE, workQueue));
    }

    public void removeConsumer(DelayedWorkQueueItemType consumer)
    {
        workBufferMap.remove(consumer);
    }

    public void addWorkItem(DelayedWorkQueueItem item)
    {
        DelayedWorkBuffer buffer = workBufferMap.get(item.getType());
        buffer.add(item);
    }

}
