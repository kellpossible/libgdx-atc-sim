package com.atc.simulator.Display.Model;

import com.atc.simulator.Display.LayerManager;
import com.atc.simulator.Display.RenderLayer;
import com.atc.simulator.Display.View.ModelInstanceProviders.*;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
import com.badlogic.gdx.utils.Disposable;

/**
 * Represents an aircraft...
 * With display specific extensions.
 * @author Luke Frisken
 */
public class DisplayAircraft extends AircraftState implements Disposable {
    private Track track;
    private DisplayPrediction prediction = null;
    private Display display;
    private AircraftModel aircraftModel;
    private PredictionModel predictionModel;
    private LayerManager layerManager;
    private RenderLayer aircraftLayer;
    private RenderLayer predictionLayer;

    /**
     * Constructor for DisplayAircraft
     * @param display
     * @param track
     */
    public DisplayAircraft(Display display, Track track, LayerManager layerManager)
    {
        super(track.getLatest());
        this.display = display;
        this.track = track;
        aircraftModel = new AircraftModel(display, this);
        predictionModel = new PredictionModel(display.getCamera("perspective"), this, display);
        this.layerManager = layerManager;
        aircraftLayer = layerManager.getRenderLayer("aircraft");
        predictionLayer = layerManager.getRenderLayer("prediction");

        predictionLayer.addDisplayRenderableProvider(predictionModel);
        aircraftLayer.addDisplayRenderableProvider(aircraftModel);

    }

    /**
     * Call to update the gdxRenderableProviders provided by this multiplexer.
     */
    public void update()
    {
//        System.out.println("Updating Aircraft");
        this.copyData(track.getLatest());
        aircraftModel.update();
        predictionModel.update();
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

    /**
     * Get the track associated with this aircraft
     * @return
     */
    public Track getTrack()
    {
        return track;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        aircraftLayer.removeDisplayRenderableProvider(aircraftModel);
        predictionLayer.removeDisplayRenderableProvider(predictionModel);
        aircraftModel.dispose();
        predictionModel.dispose();
    }
}
