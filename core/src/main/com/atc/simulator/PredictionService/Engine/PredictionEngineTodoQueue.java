package com.atc.simulator.PredictionService.Engine;

import com.atc.simulator.flightdata.SortableOrderedQueue;

import java.util.Collections;

/**
 * Created by luke on 7/06/16.
 * @author Luke Frisken
 */
public class PredictionEngineTodoQueue extends SortableOrderedQueue<PredictionWorkItem> {
    private static final PredictionWorkItem SORTER = new PredictionWorkItem();

    /**
     * Sort the queue using the Comparator interface in T
     * The queue is orded as high at tail and low at head.
     * TODO: some gains in performance could be made here by reducing to a single loop
     */
    @Override
    protected void doSort() {
        Collections.sort(this, SORTER);
        Collections.reverse(this);
    }
}
