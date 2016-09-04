package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by luke on 4/09/16.
 */
public class DisplayPrediction extends Prediction implements ModelInstanceProvider {
    private ArrayList<ModelInstanceListener> modelInstanceListeners = new ArrayList<ModelInstanceListener>();
    private ModelInstance modelInstance;
    private Model model;

    /**
     * Constructor DisplayPrediction creates a new DisplayPrediction modelInstance.
     *
     * @param aircraft       of type DisplayAircraft.
     * @param time           of type long. The time (in milliseconds since epoch) for the first predicted position.
     * @param aircraftStates of type ArrayList<AircraftState>
     */
    public DisplayPrediction(DisplayAircraft aircraft, long time, ArrayList<AircraftState> aircraftStates) {
        super(aircraft.getAircraftID(), time, aircraftStates);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "prediction",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.YELLOW);

        ArrayList<AircraftState> states = this.getAircraftStates();

        Vector3 previousPositionDrawVector = aircraft.getCurrentState().getPosition().getModelDrawVector();
        for(int i = 0; i < states.size(); i++)
        {
            AircraftState state = states.get(i);
            Vector3 positionDrawVector = state.getPosition().getModelDrawVector();
            builder.line(previousPositionDrawVector, positionDrawVector);
            previousPositionDrawVector = positionDrawVector;
        }

        model = modelBuilder.end();
        modelInstance = new ModelInstance(model);
    }

    public DisplayPrediction(DisplayAircraft aircraft, Prediction prediction)
    {
        this(aircraft, prediction.getPredictionTime(), prediction.getAircraftStates());
    }

    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    @Override
    public void addModelInstanceListener(ModelInstanceListener listener) {
        listener.onInstanceUpdate(this, modelInstance);
        modelInstanceListeners.add(listener);
    }

    @Override
    public void dispose() {
        model.dispose();
        for (ModelInstanceListener listener : modelInstanceListeners)
        {
            listener.onInstanceDispose(this);
        }
    }
}
