package com.atc.simulator.display.view.model_instance_providers;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.display.model.DisplayAircraft;
import com.atc.simulator.display.view.display_renderable.GDXDisplayRenderable;
import com.atc.simulator.display.view.shapes.TrackLineMeshBuilder;
import com.atc.simulator.prediction_service.engine.algorithms.java.JavaLinearAlgorithm;
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

import java.util.ArrayList;

/**
 * Green velocity arrows
 * @author Luke Frisken
 */
public class VelocityModel extends SimpleDisplayRenderableProvider {
    private static final boolean stippledPredictions = ApplicationConfig.getBoolean("settings.display.stippled-predictions");
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

        Prediction prediction = new JavaLinearAlgorithm().makePrediction(aircraft.getTrack(), null);

        Color lightGreen = new Color(Color.rgba8888(0f, 1f, 0f, 1f));
        Color darkerGreen = new Color(Color.rgba8888(0f, 0.5f, 0.1f, 1f));

        ArrayList<AircraftState> states = prediction.getCentreTrack();

        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "prediction",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());

        if (stippledPredictions)
        {
            TrackLineMeshBuilder.buildStippled(builder, states, aircraft, depthAdjustment, lightGreen, darkerGreen);
        } else {
            builder.setColor(lightGreen);
            TrackLineMeshBuilder.build(builder, states, aircraft, depthAdjustment);
        }

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }
}
