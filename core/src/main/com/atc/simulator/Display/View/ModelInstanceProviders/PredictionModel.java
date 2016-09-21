package com.atc.simulator.Display.View.ModelInstanceProviders;

import com.atc.simulator.Display.Model.Display;
import com.atc.simulator.Display.Model.DisplayAircraft;
import com.atc.simulator.Display.Model.DisplayPrediction;
import com.atc.simulator.Display.View.DisplayRenderable.GDXDisplayRenderable;
import com.atc.simulator.Display.View.DisplayRenderable.HiddenDisplayRenderable;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.badlogic.gdx.graphics.Camera;
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
public class PredictionModel extends SimpleDisplayRenderableProvider {
    private DisplayAircraft aircraft;
    private Display display;

    /**
     * Constructor for PredictionModel
     * @param camera that will be rendering this
     * @param aircraft associated with this prediction
     * @param display that will be rendering this
     */
    public PredictionModel(Camera camera, DisplayAircraft aircraft, Display display) {
        super(camera);
        this.aircraft = aircraft;
        this.display = display;
        update();
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update()
    {
        switch(display.getPredictionDisplayMethod())
        {
            case WIREFRAME:
                updateWireframes();
                break;
            case GRADIENT:
                updateGradient();
                break;
            case NONE:
                updateHidden();
                break;
            default:
                updateHidden();
                break;
        }
    }

    public void updateHidden()
    {
        super.update();
        setDisplayRenderable(new HiddenDisplayRenderable());
        return;
    }

    /**
     * Render the wireframe version of the prediction
     */
    public void updateGradient()
    {
        super.update();

        DisplayPrediction prediction = aircraft.getPrediction();

        if (prediction == null)
        {
            setDisplayRenderable(new HiddenDisplayRenderable());
            return;
        }


        DisplayAircraft aircraft = prediction.getAircraft();
        Material matWhite = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "rightPredictionTrack",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.YELLOW);

        Track leftTrack = prediction.getLeftTrack();
        Track centreTrack = prediction.getCentreTrack();
        Track rightTrack = prediction.getRightTrack();

        //start of prediction line is the current aircraft position.
        Vector3 previousPositionDrawVector = aircraft.getPosition().getModelDrawVector();
        for(int i = 0; i < rightTrack.size(); i++)
        {
            AircraftState state = rightTrack.get(i);
            Vector3 positionDrawVector = state.getPosition().getModelDrawVector();
            builder.line(previousPositionDrawVector, positionDrawVector);
            previousPositionDrawVector = positionDrawVector;
        }

        builder = modelBuilder.part(
                "predictionArea",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());


        //for how to do textured version:
        //http://stackoverflow.com/questions/21161456/building-a-box-with-texture-on-one-side-in-libgdx-performance

        builder.setColor(Color.YELLOW);
        Vector3 prevLeftPosition = null;
        Vector3 prevRightPosition = null;
        Vector3 prevCentrePosition = null;

        for(int i = 0; i < prediction.size(); i++)
        {
            Vector3 leftPosition = leftTrack.get(i).getPosition().getModelDrawVector();
            Vector3 centrePosition = centreTrack.get(i).getPosition().getModelDrawVector();
            Vector3 rightPosition = rightTrack.get(i).getPosition().getModelDrawVector();

            if(i > 0)
            {
                Vector3 normal = new Vector3(leftPosition).scl(-1).nor();
                builder.triangle(leftPosition, prevLeftPosition, centrePosition);
                builder.triangle(rightPosition, prevRightPosition, centrePosition);
            }

            prevLeftPosition = leftPosition;
            prevCentrePosition = centrePosition;
            prevRightPosition = rightPosition;

        }

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }

    /**
     * Render the wireframe version of the prediction
     */
    public void updateWireframes()
    {
        super.update();

        DisplayPrediction prediction = aircraft.getPrediction();

        if (prediction == null)
        {
            setDisplayRenderable(new HiddenDisplayRenderable());
            return;
        }


        DisplayAircraft aircraft = prediction.getAircraft();
        Material matWhite = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "rightPredictionTrack",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.YELLOW);

        Track leftTrack = prediction.getLeftTrack();
        Track centreTrack = prediction.getCentreTrack();
        Track rightTrack = prediction.getRightTrack();

        //start of prediction line is the current aircraft position.
        Vector3 previousPositionDrawVector = aircraft.getPosition().getModelDrawVector();
        for(int i = 0; i < rightTrack.size(); i++)
        {
            AircraftState state = rightTrack.get(i);
            Vector3 positionDrawVector = state.getPosition().getModelDrawVector();
            builder.line(previousPositionDrawVector, positionDrawVector);
            previousPositionDrawVector = positionDrawVector;
        }

        builder = modelBuilder.part(
                "predictionArea",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());


        //for how to do textured version:
        //http://stackoverflow.com/questions/21161456/building-a-box-with-texture-on-one-side-in-libgdx-performance

        builder.setColor(Color.YELLOW);
        Vector3 prevLeftPosition = null;
        Vector3 prevRightPosition = null;
        Vector3 prevCentrePosition = null;

        for(int i = 0; i < prediction.size(); i++)
        {
            Vector3 leftPosition = leftTrack.get(i).getPosition().getModelDrawVector();
            Vector3 centrePosition = centreTrack.get(i).getPosition().getModelDrawVector();
            Vector3 rightPosition = rightTrack.get(i).getPosition().getModelDrawVector();

            if(i > 0)
            {
                Vector3 normal = new Vector3(leftPosition).scl(-1).nor();
                builder.triangle(leftPosition, prevLeftPosition, centrePosition);
                builder.triangle(rightPosition, prevRightPosition, centrePosition);
            }

            prevLeftPosition = leftPosition;
            prevCentrePosition = centrePosition;
            prevRightPosition = rightPosition;

        }

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }
}


