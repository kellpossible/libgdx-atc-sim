package com.atc.simulator.flightdata;

/**
 * Created by luke on 9/09/16.
 *
 * Generated in the delayedworkqueue when work needs to get done
 * on a given workqueueconsumer.
 *
 *
 * @author Luke Frisken
 */
public abstract class DelayedWorkQueueItem {
    private int priority;
    private int cost;

    public DelayedWorkQueueItem(int priority, int cost)
    {
        this.priority = priority;
        this.cost = cost;
    }
}
