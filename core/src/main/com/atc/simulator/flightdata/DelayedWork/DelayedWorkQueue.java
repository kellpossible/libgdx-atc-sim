package com.atc.simulator.flightdata.DelayedWork;

import com.atc.simulator.flightdata.SortableOrderedQueue;

/**
 * Created by luke on 9/09/16.
 *
 * Whenever work is triggered for the consumer,
 * new workqueue items are added to the queue.
 *
 * each call of the run method first sorts the queue,
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

    @Override
    public void onItemAdded(DelayedWorkQueueItem newItem) {
        this.add(newItem);
    }

    @Override
    public void onItemReplaced(DelayedWorkQueueItem replacedItem, DelayedWorkQueueItem newItem) {
        this.remove(replacedItem);
    }

    @Override
    public void onItemRemoved(DelayedWorkQueueItem removedItem) {
        //this.remove(removedItem);
    }
}
