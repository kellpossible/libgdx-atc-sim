package com.atc.simulator.DebugDataFeed;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.SystemState;

import java.util.ArrayList;

/**
 * Created by luke on 24/05/16.
 */
public class DataPlaybackThread implements Runnable{
    private ArrayList<DataPlaybackListener> listeners;
    private int updateRate;
    private Scenario scenario;
    private String threadName;
    private Thread thread;

    public DataPlaybackThread(Scenario scenario, int updateRate)
    {
        listeners = new ArrayList<DataPlaybackListener>();
        this.updateRate = updateRate;
        this.scenario = scenario;
        threadName = "DataPlayback";
    }

    public void addListener(DataPlaybackListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(DataPlaybackListener listener) {
        listeners.remove(listener);
    }

    /**
     * Trigger an onSystemState event on all the DataPlaybackListener listeners to this thread
     * @param systemState
     */
    private void triggerOnSystemUpdate(SystemState systemState)
    {
        for(DataPlaybackListener listener: listeners)
        {
            listener.onSystemUpdate(systemState);
        }
    }

    @Override
    public void run() {

    }

    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}
