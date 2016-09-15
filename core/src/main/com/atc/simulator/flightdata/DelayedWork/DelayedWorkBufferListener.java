package com.atc.simulator.flightdata.DelayedWork;

/**
 * @author Luke Frisken
 */
public interface DelayedWorkBufferListener {
    void onItemAdded(DelayedWorkQueueItem item);
    void onItemReplaced(DelayedWorkQueueItem replacedItem, DelayedWorkQueueItem item);
    void onItemRemoved(DelayedWorkQueueItem item);
}
