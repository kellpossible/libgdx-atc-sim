package com.atc.simulator.display.model;

import com.atc.simulator.flightdata.Prediction;
import com.badlogic.gdx.utils.Disposable;

/**
 * Represents a prediction of an aircraft's trajectory.
 * With display specific extensions.
 * @author Luke Frisken
 */
public class DisplayPrediction extends Prediction implements Disposable {
    private DisplayAircraft aircraft;
    private Display display;
    /**
     * Constructor DisplayPrediction creates a new DisplayPrediction instance.
     *
     * @param aircraft the aircraft this prediction belongs to
     * @param prediction of type Prediction
     */
    public DisplayPrediction(Display display, DisplayAircraft aircraft, Prediction prediction) {
        super(prediction);
        this.aircraft = aircraft;
        this.display = display;
    }

    public DisplayAircraft getAircraft()
    {
        return aircraft;
    }


    /**
     * Update this prediction with new prediction values.
     * and update the model gdxRenderableProviders provided by this object.
     * @param newPrediction
     */
    public void update(Prediction newPrediction)
    {
        this.copyData(newPrediction);
        aircraft.update();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {

    }
}
