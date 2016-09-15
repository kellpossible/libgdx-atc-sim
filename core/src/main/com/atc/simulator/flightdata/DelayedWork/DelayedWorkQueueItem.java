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
public class DelayedWorkQueueItem implements Comparator<DelayedWorkQueueItem>{
    private int priority;
    private int cost;
    private DelayedWorkQueueItemType type;
    private Object data;

    public DelayedWorkQueueItem(Object data, int priority, int cost, DelayedWorkQueueItemType type)
    {
        this.data = data;
        this.priority = priority;
        this.cost = cost;
        this.type = type;
    }

    public DelayedWorkQueueItemType getType()
    {
        return type;
    }

    public int compare(DelayedWorkQueueItem item1, DelayedWorkQueueItem item2)
    {
        return Integer.compare(item1.getPriority(), item2.getPriority());
    }

    public boolean equals(Object item)
    {
        return this == item;
    }

    public int getPriority() {
        return priority;
    }

    public int getCost() {
        return cost;
    }

    public void run()
    {
        type.run(this);
    }

    public Object getData()
    {
        return data;
    }

}
