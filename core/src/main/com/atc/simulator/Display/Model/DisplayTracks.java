package com.atc.simulator.Display.Model;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.Display.View.ModelInstanceProviders.TracksModel;
import com.atc.simulator.flightdata.Track;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

/**
 *  Represents a list of tracks present in a scenario
 *
 * Created by Chris on 20/09/2016.
 */
public class DisplayTracks implements Disposable {
    ArrayList<Track> scenarioTracks;
    TracksModel myView;
    boolean visibleModel = true;

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
     * Set the view that is displaying this Track Model
     * @param view The view we will use to display this Model
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
