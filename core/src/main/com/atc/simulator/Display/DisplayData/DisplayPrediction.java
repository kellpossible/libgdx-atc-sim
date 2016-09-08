package com.atc.simulator.Display.DisplayData;

import com.atc.simulator.Display.Display;
import com.atc.simulator.Display.DisplayData.ModelInstanceProviders.PredictionModel;
import com.atc.simulator.Display.DisplayData.ModelInstanceProviders.VelocityModel;
import com.atc.simulator.Display.LayerManager;
import com.atc.simulator.flightdata.Prediction;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;
import java.util.HashMap;

/**
 * Represents a prediction of an aircraft's trajectory.
 * With display specific extensions.
 * @author Luke Frisken
 */
public class DisplayPrediction extends Prediction implements Disposable, DisplayRenderableProviderMultiplexer {
    private HashMap<String, DisplayRenderableProvider> models;
    private DisplayAircraft aircraft;
    private Display display;
    /**
     * Constructor DisplayPrediction creates a new DisplayPrediction instance.
     *
     * @param aircraft the aircraft this prediction belongs to
     * @param prediction of type Prediction
     */
    public DisplayPrediction(Display display, DisplayAircraft aircraft, Prediction prediction) {
        super(prediction.getAircraftID(), prediction.getPredictionTime(), prediction.getAircraftStates());
        this.aircraft = aircraft;
        models = new HashMap<String, DisplayRenderableProvider>();
        this.display = display;

        createModels();
    }

    private void createModels()
    {
        Camera perspectiveCamera = display.getCamera("perspective");
        models.put("PredictionLine", new PredictionModel(perspectiveCamera, this));
        models.put("VelocityLine", new VelocityModel(perspectiveCamera, aircraft));
    }

    public DisplayAircraft getAircraft()
    {
        return aircraft;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        for (DisplayRenderableProvider model : models.values())
        {
            model.dispose();
        }
    }

    @Override
    public Collection<DisplayRenderableProvider> getDisplayRenderableProviders() {
        return models.values();
    }

    /**
     * Update this prediction with new prediction values.
     * and update the model instances provided by this object.
     * @param newPrediction
     */
    public void update(Prediction newPrediction)
    {
        this.copyData(newPrediction);
        update();
    }

    /**
     * Call to update the instances provided by this multiplexer.
     */
    @Override
    public void update() {
        for (DisplayRenderableProvider model : models.values())
        {
            model.update();
        }
    }
}
