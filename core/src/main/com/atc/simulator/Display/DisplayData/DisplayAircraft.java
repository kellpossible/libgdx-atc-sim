package com.atc.simulator.Display.DisplayData;

import com.atc.simulator.Display.DisplayData.ModelInstanceProviders.*;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Aircraft to be displayed in the display.
 * @author Luke Frisken
 */
public class DisplayAircraft extends AircraftState implements Disposable, ModelInstanceProviderMultiplexer {
    private Track track;
    private DisplayPrediction prediction = null;
    private HashMap<String, ModelInstanceProvider> models;

    private DisplayAircraft(AircraftState aircraftState)
    {
        super(aircraftState.getAircraftID(),
                aircraftState.getTime(),
                aircraftState.getPosition(),
                aircraftState.getVelocity(),
                aircraftState.getHeading());
    }

    public DisplayAircraft(Track track)
    {
        this(track.getLatest());
        this.track = track;
        models = new HashMap<String, ModelInstanceProvider>();
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
     * Call to update the instances provided by this multiplexer.
     */
    public void update()
    {
        this.copyData(track.getLatest());
        for (ModelInstanceProvider model : models.values())
        {
            model.update();
        }
    }

    private void createModels()
    {
        models.put("Aircraft", new AircraftModel(this));
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
        for (ModelInstanceProvider model : models.values())
        {
            model.dispose();
        }
    }

    @Override
    public Collection<ModelInstanceProvider> getInstanceProviders() {
        return models.values();
    }
}
