package com.atc.simulator.debug_data_feed;

import com.atc.simulator.flightdata.SystemState;

/**
 * Created by luke on 24/05/16.
 *
 * @author Luke Frisken
 */
public interface DataPlaybackListener {
    /**
     * This method gets called when there is a system update, and gets
     * passed the new system state
     * @param systemState the updated system state
     */
    void onSystemUpdate(SystemState systemState);
}
