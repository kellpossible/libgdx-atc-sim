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

    /**
     * Constructor for DelayedWorkQueueItem
     * @param data data associated with the work item
     * @param priority priority of the work item
     * @param cost cost of the work item to complete in "work points"
     * @param type type associated with this work item, the source of this work item.
     */
    public DelayedWorkQueueItem(Object data, int priority, int cost, DelayedWorkQueueItemType type)
    {
        this.data = data;
        this.priority = priority;
        this.cost = cost;
        this.type = type;
    }

    /**
     * Method getType returns the type of this DelayedWorkQueueItem object.
     * @return the type (type DelayedWorkQueueItemType) of this DelayedWorkQueueItem object.
     */
    public DelayedWorkQueueItemType getType()
    {
        return type;
    }

    /**
     * Compare two work items by their priority
     * @param item1
     * @param item2
     * @return
     */
    public int compare(DelayedWorkQueueItem item1, DelayedWorkQueueItem item2)
    {
        return Integer.compare(item1.getPriority(), item2.getPriority());
    }

    /**
     * Whether or not two work items are the same
     * @param item item to compare to this one with == operator
     * @return
     */
    public boolean equals(Object item)
    {
        return this == item;
    }

    /**
     * Get the priority of this work item
     * @return priority (higher number is higher priority)
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Get the cost of this work item
     * @return the cost in "work points"
     */
    public int getCost() {
        return cost;
    }

    /**
     * Run this work item's run method provided by its type.
     */
    public void run()
    {
        type.run(this);
    }

    /**
     * Get the data associated with this work item.
     * @return
     */
    public Object getData()
    {
        return data;
    }

}
