package com.atc.simulator.display.model;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.debug_data_feed.scenarios.Scenario;
import com.atc.simulator.display.view.ModelInstanceProviders.TracksModel;
import com.atc.simulator.flightdata.Track;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

/**
 *  Represents a list of tracks present in a scenario
 *
 * Created by Chris on 20/09/2016.
 */
public class DisplayTracks implements Disposable {
    private static final boolean showTracks = ApplicationConfig.getBoolean("settings.display.show-tracks-default");

    private ArrayList<Track> scenarioTracks;
    private TracksModel myView;
    private boolean visibleModel = showTracks;

    /**
     * Constructor for {@link DisplayTracks}
     * @param tracks
     */
    public DisplayTracks(Scenario tracks)
    {
        scenarioTracks = new ArrayList<Track>();
        for(Track temp : tracks.getTracks())
            scenarioTracks.add(temp);
    }

    /**
     * Return the list of Tracks stored in this model
     * @return all of the tracks stored for this scenario
     */
    public ArrayList<Track> getScenarioTracks(){return scenarioTracks;}

    /**
     * Set the view that is displaying this Track model
     * @param view The view we will use to display this model
     */
    public void setMyView(TracksModel view){myView = view;}

    /**
     * Toggles the visibility of the track
     */
    public void toggleTrackVisibility()
    {
        visibleModel = !visibleModel;
        myView.update();
    }
    /**
     * Return the current visibility of the Track Model
     */
    public boolean isVisible(){return visibleModel;}

    /**
     * Dispose of all the resources
     */
    @Override
    public void dispose() {
        myView.dispose();
    }
}
