package com.atc.simulator.flightdata.DelayedWork;

/**
 * Created by luke on 9/09/16.
 *
 * A source of work items (a kind of template) for the {@link DelayedWorker}
 *
 * @author Luke Frisken
 */
public abstract class DelayedWorkQueueItemType {
    private int priority;
    private int cost;

    /**
     * Constructor for {@link DelayedWorkQueueItemType}
     * @param priority priority for work items created using this type (higher is higher priority)
     * @param cost cost in "work points" for work items created using this type
     */
    public DelayedWorkQueueItemType(int priority, int cost)
    {
        this.priority = priority;
        this.cost = cost;
    }

    /**
     * The method which is called when the work items that are created using this type
     * need to be processed.
     * @param workItem work item being processed.
     */
    public abstract void run(DelayedWorkQueueItem workItem);

    /**
     * Create a new work item based on this this type
     * @param data data associated with the work item and required to run it.
     * @return the new work item
     */
    protected DelayedWorkQueueItem createWorkItem(Object data)
    {
        return new DelayedWorkQueueItem(data, this.priority, this.cost, this);
    }
}
