package com.atc.simulator.Display.Model;

import com.atc.simulator.Display.View.DisplayRenderableProvider;
import com.atc.simulator.Display.View.DisplayRenderableProviderMultiplexer;
import com.atc.simulator.Display.View.ModelInstanceProviders.*;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;
import java.util.HashMap;

/**
 * Represents an aircraft...
 * With display specific extensions.
 * @author Luke Frisken
 */
public class DisplayAircraft extends AircraftState implements Disposable {
    private Track track;
    private DisplayPrediction prediction = null;
    private Display display;
    private AircraftModel model;

    private DisplayAircraft(Display display, AircraftState aircraftState)
    {
        super(aircraftState.getAircraftID(),
                aircraftState.getTime(),
                aircraftState.getPosition(),
                aircraftState.getVelocity(),
                aircraftState.getHeading());
        this.display = display;
    }

    public DisplayAircraft(Display display, Track track)
    {
        this(display, track.getLatest());
        this.track = track;
        model = new AircraftModel(display, this);

    }

    public DisplayRenderableProviderMultiplexer getRenderable() {
        return model;
    }

    /**
     * Call to update the gdxRenderableProviders provided by this multiplexer.
     */
    public void update()
    {
        this.copyData(track.getLatest());
        model.update();
    }



    /**
     * Method setPrediction sets the current prediction associated with this aircraft.
     *
     * @param newPrediction the new prediction to be associated with this aircraft
     *
     */
    public void setPrediction(DisplayPrediction newPrediction)
    {
        prediction = newPrediction;
    }

    /**
     * Get the prediction associated with this aircraft.
     * @return PredictionModel
     */
    public DisplayPrediction getPrediction()
    {
        return prediction;
    }

    public Track getTrack()
    {
        return track;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        model.dispose();
    }
}
