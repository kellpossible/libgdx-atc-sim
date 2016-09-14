package com.atc.simulator.flightdata;

import com.atc.simulator.Display.DisplayCameraListener;
import com.badlogic.gdx.graphics.Camera;

/**
 * Created by luke on 9/09/16.
 *
 * DelayedWorkQueueItems call the run method on this class.
 * DelayedWorkQueueItems are generated from instances of this
 * class being present in the DelayedWorkQueue's list of consumers.
 *
 * @author Luke Frisken
 */
public abstract class DelayedWorkQueueConsumer {
    private int priority;
    private int cost;

    public DelayedWorkQueueConsumer(int priority, int cost)
    {
        this.priority = priority;
        this.cost = cost;
    }

    public abstract void run();
}
