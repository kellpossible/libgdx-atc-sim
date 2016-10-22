package com.atc.simulator.flightdata.delayed_work;

import com.atc.simulator.flightdata.SortableOrderedQueue;

/**
 * Created by luke on 9/09/16.
 *
 * Whenever work is triggered for the consumer,
 * new workqueue items are added to the queue.
 *
 * each call of the update method first sorts the queue,
 * according to priority.
 * then proceeds to execute the jobs/items in the queue
 * up until an allotted size has been reached, it then returns.
 *
 * @author Luke Frisken
 */
public class DelayedWorkQueue extends SortableOrderedQueue<DelayedWorkQueueItem> implements DelayedWorkBufferListener {
    /**
     * Sort the queue using the Comparator interface in T
     * The queue is orded as high at tail and low at head.
     */
    @Override
    protected void sort() {

    }

    /**
     * Called when a new item is added to the work buffer
     * @param item item that has been added to the work buffer
     */
    @Override
    public void onItemAdded(DelayedWorkQueueItem item) {
        this.add(item);
    }

    /**
     * Called when an item has been replaced in the work buffer
     * @param replacedItem item that has just been replaced
     * @param item item that has just replaced the replacedItem
     */
    @Override
    public void onItemReplaced(DelayedWorkQueueItem replacedItem, DelayedWorkQueueItem item) {
        this.remove(replacedItem);
        this.add(item);
    }

    /**
     * Called when an item has been removed from the work buffer
     * @param item item that has just been removed
     */
    @Override
    public void onItemRemoved(DelayedWorkQueueItem item) {
//        this.remove(item);
    }
}
