package com.atc.simulator.flightdata.delayed_work;

/**
 * A listener who listens for changes to a DelayedWorkBuffer
 * @author Luke Frisken
 */
public interface DelayedWorkBufferListener {
    /**
     * Called when a new item is added to the work buffer
     * @param item item that has been added to the work buffer
     */
    void onItemAdded(DelayedWorkQueueItem item);

    /**
     * Called when an item has been replaced in the work buffer
     * @param replacedItem item that has just been replaced
     * @param item item that has just replaced the replacedItem
     */
    void onItemReplaced(DelayedWorkQueueItem replacedItem, DelayedWorkQueueItem item);

    /**
     * Called when an item has been removed from the work buffer
     * @param item item that has just been removed
     */
    void onItemRemoved(DelayedWorkQueueItem item);
}
