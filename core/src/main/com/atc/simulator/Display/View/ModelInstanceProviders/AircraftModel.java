package com.atc.simulator.Display.View.ModelInstanceProviders;

import com.atc.simulator.Display.Model.Display;
import com.atc.simulator.Display.Model.DisplayAircraft;
import com.atc.simulator.Display.View.DisplayRenderableProvider;
import com.atc.simulator.Display.View.DisplayRenderableProviderMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by luke on 20/09/16.
 *
 * @author Luke Frisken
 */
public class AircraftModel implements Disposable, DisplayRenderableProviderMultiplexer {
    private Display display;
    private DisplayAircraft aircraft;
    private HashMap<String, DisplayRenderableProvider> models;
    private AircraftDotModel aircraftDotModel;
    private BreadCrumbModel breadCrumbModel;


    /**
     * Constructor for AircraftModel
     * @param display
     * @param aircraft
     */
    public AircraftModel(Display display, DisplayAircraft aircraft)
    {
        this.display = display;
        this.aircraft = aircraft;
        models = new HashMap<String, DisplayRenderableProvider>();
        createModels();
    }

    /**
     * Get the instance providers provided by this multiplexer
     * @return
     */
    @Override
    public Collection<DisplayRenderableProvider> getDisplayRenderableProviders() {
        return models.values();
    }

    /**
     * Create models
     */
    private void createModels()
    {
        Camera perspectiveCamera = display.getCamera("perspective");
        aircraftDotModel = new AircraftDotModel(perspectiveCamera, aircraft);
        models.put("Aircraft", aircraftDotModel);
        display.addDelayedCameraListener(aircraftDotModel.getCamera(), aircraftDotModel, 1, 10);
//        AircraftInfoModel infoModel = new AircraftInfoModel(display.getCamera("ortho"), display, this);
//        display.addCameraListener(infoModel.getCamera(), infoModel);
//        models.put("AircraftInfo", infoModel);
        breadCrumbModel = new BreadCrumbModel(display.getCamera("perspective"), aircraft);
        models.put("AircraftBreadcrumbs", breadCrumbModel);
        display.addDelayedCameraListener(breadCrumbModel.getCamera(), breadCrumbModel, 1, 20);

        models.put("PredictionLine", new PredictionModel(perspectiveCamera, aircraft));
        models.put("VelocityLine", new VelocityModel(perspectiveCamera, aircraft));
    }

    /**
     * Call to update the gdxRenderableProviders provided by this multiplexer.
     */
    public void update()
    {
        for (DisplayRenderableProvider model : models.values())
        {
            model.update();
        }
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

//        AircraftInfoModel infoModel = (AircraftInfoModel) models.get("AircraftInfo");
//        display.removeCameraListener(infoModel.getCamera(), infoModel);

        display.removeCameraListener(aircraftDotModel.getCamera(), aircraftDotModel);
        display.removeCameraListener(breadCrumbModel.getCamera(), breadCrumbModel);
    }
}
