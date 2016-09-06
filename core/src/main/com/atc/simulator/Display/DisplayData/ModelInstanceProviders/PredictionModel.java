package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.Display.DisplayData.DisplayAircraft;
import com.atc.simulator.Display.DisplayData.DisplayPrediction;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * A prediction to be displayed in the display.
 * @author Luke Frisken
 */
public class PredictionModel implements ModelInstanceProvider {
    private ArrayList<ModelInstanceProviderListener> modelInstanceListeners = new ArrayList<ModelInstanceProviderListener>();
    private ModelInstance modelInstance;
    private Model model;
    private DisplayPrediction prediction;

    public PredictionModel(DisplayPrediction prediction) {
        this.prediction = prediction;
        update();
    }

    /**
     * Get the instance provided by this class
     * @return the instance provided by this class
     */
    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    /**
     * Add a ModelInstanceProviderListener listener to this class.
     * @param listener the listener to be added
     */
    @Override
    public void addModelInstanceListener(ModelInstanceProviderListener listener) {
        listener.onInstanceUpdate(this, modelInstance);
        modelInstanceListeners.add(listener);
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update() {
        DisplayAircraft aircraft = prediction.getAircraft();

        if (model != null)
        {
            model.dispose();
        }
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "prediction",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.YELLOW);

        ArrayList<AircraftState> states = prediction.getAircraftStates();

        //start of prediction line is the current aircraft position.
        Vector3 previousPositionDrawVector = aircraft.getPosition().getModelDrawVector();
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

    /**
     * Call to dispose of this class, and its resources.
     */
    @Override
    public void dispose() {
        model.dispose();
        for (ModelInstanceProviderListener listener : modelInstanceListeners)
        {
            listener.onInstanceDispose(this);
        }
    }
}
