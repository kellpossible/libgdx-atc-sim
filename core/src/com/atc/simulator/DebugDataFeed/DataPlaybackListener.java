package com.atc.simulator.DebugDataFeed;

import com.atc.simulator.flightdata.SystemState;

/**
 * Created by luke on 24/05/16.
 */
public interface DataPlaybackListener {
    /**
     * This method gets called when there is a system update, and gets
     * passed the new system state
     * @param systemState the updated system state
     */
    void onSystemUpdate(SystemState systemState);
}
