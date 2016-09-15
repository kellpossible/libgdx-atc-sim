package com.atc.simulator.flightdata.DelayedWork;

import java.util.Comparator;

/**
 * Created by luke on 9/09/16.
 *
 * Generated in the delayedworkqueue when work needs to get done
 * on a given workqueueconsumer.
 *
 *
 * @author Luke Frisken
 */
public abstract class DelayedWorkQueueItem implements Comparator<DelayedWorkQueueItem>{
    private int priority;
    private int cost;
    private DelayedWorkQueueConsumer consumer;

    public DelayedWorkQueueItem(int priority, int cost, DelayedWorkQueueConsumer consumer)
    {
        this.priority = priority;
        this.cost = cost;
        this.consumer = consumer;
    }

    public DelayedWorkQueueConsumer getConsumer()
    {
        return consumer;
    }

    public int compare(DelayedWorkQueueItem item1, DelayedWorkQueueItem item2)
    {
        return Integer.compare(item1.getPriority(), item2.getPriority());
    }

    public boolean equals(Object item)
    {
        if (item instanceof DelayedWorkQueueItem)
        {
            return compare(this, (DelayedWorkQueueItem) item) == 0;
        } else {
            return false;
        }
    }

    public int getPriority() {
        return priority;
    }

    public int getCost() {
        return cost;
    }

}
