package com.atc.simulator.flightdata.DelayedWork;

import java.util.HashMap;

/**
 * @author Luke Frisken
 */
public class DelayedWorker {
    private HashMap<DelayedWorkQueueConsumer, DelayedWorkBuffer> workBufferMap;
    private DelayedWorkQueue workQueue;

    public DelayedWorker()
    {
        workBufferMap = new HashMap<DelayedWorkQueueConsumer, DelayedWorkBuffer>();
        workQueue = new DelayedWorkQueue();
    }

    public void addConsumer(DelayedWorkQueueConsumer consumer)
    {
        workBufferMap.put(consumer, new DelayedWorkBuffer(1, workQueue));
    }

    public void removeConsumer(DelayedWorkQueueConsumer consumer)
    {
        workBufferMap.remove(consumer);
    }

    public void addWorkItem(DelayedWorkQueueItem item)
    {
        DelayedWorkBuffer buffer = workBufferMap.get(item.getConsumer());
        buffer.add(item);
    }

}
