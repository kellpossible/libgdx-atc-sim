package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.Display.DisplayData.DisplayAircraft;
import com.atc.simulator.Display.DisplayData.DisplayPrediction;
import com.atc.simulator.Display.DisplayData.DisplayRenderable.DisplayRenderable;
import com.atc.simulator.Display.DisplayData.DisplayRenderable.GDXDisplayRenderable;
import com.atc.simulator.Display.DisplayData.DisplayRenderable.HiddenDisplayRenderable;
import com.atc.simulator.flightdata.AircraftState;
import com.badlogic.gdx.graphics.Camera;
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
 * A prediction to be displayed in the display.
 * @author Luke Frisken
 */
public class PredictionModel extends SimpleDisplayRenderableProvider {
    DisplayAircraft aircraft;
    public PredictionModel(Camera camera, DisplayAircraft aircraft) {
        super(camera);
        this.aircraft = aircraft;
        update();
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update()
    {
        super.update();

        DisplayPrediction prediction = aircraft.getPrediction();

        if (prediction == null)
        {
            setDisplayRenderable(new HiddenDisplayRenderable());
            return;
        }


        DisplayAircraft aircraft = prediction.getAircraft();
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

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }
}


