package com.atc.simulator.Display.DisplayData;

import com.atc.simulator.Display.Display;
import com.atc.simulator.Display.DisplayData.ModelInstanceProviders.*;
import com.atc.simulator.Display.LayerManager;
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
public class DisplayAircraft extends AircraftState implements Disposable, DisplayRenderableProviderMultiplexer {
    private Track track;
    private DisplayPrediction prediction = null;
    private HashMap<String, DisplayRenderableProvider> models;
    private Display display;
    private AircraftModel aircraftModel;
    private BreadCrumbModel breadCrumbModel;

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
        models = new HashMap<String, DisplayRenderableProvider>();
        createModels();


//        Vector3 screenPosition = cam.project(new Vector3(modelDrawVector));
//
//
//        String aircraftID = aircraftState.getAircraftID();
////                System.out.println("Screen Position: " + screenPosition + ", DisplayAircraft ID: " + aircraftID);
//
//        if (aircraftID.equals("QFA489"))
//        {
//            textPosition.x = screenPosition.x;
//            textPosition.y = screenPosition.y;
//        }
//
//        //if the aircraft is moving
//        if (velocity.length() > 0.00001)
//        {
//            GeographicCoordinate velocityEndPos = new GeographicCoordinate(position.add(velocity.mult(120))); //two minute velocity vector
//            aircraftStateVelocityModel = modelBuilder.createArrow(
//                    modelDrawVector,
//                    velocityEndPos.getModelDrawVector(depthAdjustment),
//                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
//                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//            aircraftStateVelocityModelInstance = new ModelInstance(aircraftStateVelocityModel);

    }

    /**
     * Call to update the gdxRenderableProviders provided by this multiplexer.
     */
    public void update()
    {
        this.copyData(track.getLatest());
        for (DisplayRenderableProvider model : models.values())
        {
            model.update();
        }
    }

    private void createModels()
    {
        Camera perspectiveCamera = display.getCamera("perspective");
        aircraftModel = new AircraftModel(perspectiveCamera, this);
        models.put("Aircraft", aircraftModel);
        display.addDelayedCameraListener(aircraftModel.getCamera(), aircraftModel, 1, 10);
//        AircraftInfoModel infoModel = new AircraftInfoModel(display.getCamera("ortho"), display, this);
//        display.addCameraListener(infoModel.getCamera(), infoModel);
//        models.put("AircraftInfo", infoModel);
        breadCrumbModel = new BreadCrumbModel(display.getCamera("perspective"), this);
        models.put("AircraftBreadcrumbs", breadCrumbModel);
        display.addDelayedCameraListener(breadCrumbModel.getCamera(), breadCrumbModel, 1, 20);

        models.put("PredictionLine", new PredictionModel(perspectiveCamera, this));
        models.put("VelocityLine", new VelocityModel(perspectiveCamera, this));
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
        for (DisplayRenderableProvider model : models.values())
        {
            model.dispose();
        }

//        AircraftInfoModel infoModel = (AircraftInfoModel) models.get("AircraftInfo");
//        display.removeCameraListener(infoModel.getCamera(), infoModel);

        display.removeCameraListener(aircraftModel.getCamera(), aircraftModel);
        display.removeCameraListener(breadCrumbModel.getCamera(), breadCrumbModel);
    }

    @Override
    public Collection<DisplayRenderableProvider> getDisplayRenderableProviders() {
        return models.values();
    }
}
