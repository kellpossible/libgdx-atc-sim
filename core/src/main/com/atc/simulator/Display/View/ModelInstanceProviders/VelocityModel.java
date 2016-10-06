package com.atc.simulator.Display.View.ModelInstanceProviders;

import com.atc.simulator.Display.Model.DisplayAircraft;
import com.atc.simulator.Display.View.DisplayRenderable.GDXDisplayRenderable;
import com.atc.simulator.PredictionService.Engine.Algorithms.Java.JavaLinearAlgorithm;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.vectors.GeographicCoordinate;
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
 * Green velocity arrows
 * @author Luke Frisken
 */
public class VelocityModel extends SimpleDisplayRenderableProvider {
    private DisplayAircraft aircraft;

    /**
     * Constructor VelocityModel creates a new VelocityModel instance.
     *
     * @param aircraft of type AircraftModel
     */
    public VelocityModel(Camera camera, DisplayAircraft aircraft)
    {
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

        ModelBuilder modelBuilder = new ModelBuilder();

        GeographicCoordinate position = aircraft.getPosition();
        double depthAdjustment = -0.02;

        Prediction prediction = new JavaLinearAlgorithm().makePrediction(aircraft.getTrack());

        Color lightGreen = new Color(Color.rgba8888(0f, 1f, 0f, 1f));
        Color darkerGreen = new Color(Color.rgba8888(0f, 0.5f, 0.1f, 1f));

        ArrayList<Vector3> lightGreenLines = new ArrayList<Vector3>();
        ArrayList<Vector3> darkerGreenLines = new ArrayList<Vector3>();

        ArrayList<AircraftState> states = prediction.getCentreTrack();


        //start of prediction line is the current aircraft position.
        AircraftState state = null;
        Vector3 positionDrawVector = null;
        lightGreenLines.add(aircraft.getPosition().getModelDrawVector(depthAdjustment));
        state = states.get(0);
        positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
        lightGreenLines.add(positionDrawVector);

        for(int i = 1; i+1 < states.size(); i+=2)
        {
            state = states.get(i);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            lightGreenLines.add(positionDrawVector);

            state = states.get(i+1);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            lightGreenLines.add(positionDrawVector);
        }

        for(int i = 0; i+1 < states.size(); i+=2)
        {
            state = states.get(i);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            darkerGreenLines.add(positionDrawVector);

            state = states.get(i+1);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            darkerGreenLines.add(positionDrawVector);
        }

        //start of prediction line is the current aircraft position.
        lightGreenLines.add(aircraft.getPosition().getModelDrawVector(depthAdjustment));
        for(int i = 0; i < states.size(); i+=2)
        {
            state = states.get(i);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            lightGreenLines.add(positionDrawVector);
        }

        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "prediction",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(lightGreen);

        for (int i = 0; i+1 < lightGreenLines.size(); i+=2)
        {
            Vector3 p1 = lightGreenLines.get(i);
            Vector3 p2 = lightGreenLines.get(i+1);
            builder.line(p1, p2);
        }

        builder.setColor(darkerGreen);

        for (int i = 0; i+1 < darkerGreenLines.size(); i+=2)
        {
            Vector3 p1 = darkerGreenLines.get(i);
            Vector3 p2 = darkerGreenLines.get(i+1);
            builder.line(p1, p2);
        }



        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }
}
