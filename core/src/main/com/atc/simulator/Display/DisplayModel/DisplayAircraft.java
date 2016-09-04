package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

/**
 * Created by luke on 4/09/16.
 */
public class DisplayAircraft implements ModelInstanceProvider {
    private String aircraftID;
    private Track track;
    private ArrayList<ModelInstanceListener> listeners;
    private DisplayPrediction prediction;

    private ModelInstance modelInstance = null;
    private Model model = null;

    public DisplayAircraft(Track track)
    {
        listeners = new ArrayList<ModelInstanceListener>();
        this.aircraftID = track.getLatest().getAircraftID();
        this.track = track;

    }

    public void setPrediction(DisplayPrediction newPrediction)
    {
        prediction = newPrediction;
    }

    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    @Override
    public void addModelInstanceListener(ModelInstanceListener listener) {
        listeners.add(listener);
    }

    @Override
    public void dispose() {

    }

    public String getAircraftID()
    {
        return aircraftID;
    }

    public AircraftState getCurrentState()
    {
        return track.getLatest();
    }
}
