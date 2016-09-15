package com.atc.simulator.flightdata.DelayedWork;

/**
 * @author Luke Frisken
 */
public interface DelayedWorkBufferListener {
    void onItemAdded(DelayedWorkQueueItem newItem);
    void onItemReplaced(DelayedWorkQueueItem replacedItem, DelayedWorkQueueItem newItem);
    void onItemRemoved(DelayedWorkQueueItem removedItem);
}
