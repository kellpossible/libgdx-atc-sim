package com.atc.simulator.flightdata.DelayedWork;

import com.atc.simulator.Display.DisplayCameraListener;
import com.badlogic.gdx.graphics.Camera;

/**
 * Created by luke on 9/09/16.
 *
 *
 * @author Luke Frisken
 */
public abstract class DelayedWorkQueueItemType {
    private int priority;
    private int cost;

    public DelayedWorkQueueItemType(int priority, int cost)
    {
        this.priority = priority;
        this.cost = cost;
    }

    public abstract void run(DelayedWorkQueueItem workItem);

    protected DelayedWorkQueueItem createWorkItem(Object data)
    {
        return new DelayedWorkQueueItem(data, this.priority, this.cost, this);
    }
}
