package com.atc.simulator.display.view.model_instance_providers;

import com.atc.simulator.display.model.DisplayTracks;
import com.atc.simulator.display.view.display_renderable.GDXDisplayRenderable;
import com.atc.simulator.display.view.display_renderable.HiddenDisplayRenderable;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
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
 * Red tracks from scenario to go in display.
 * @author Luke Frisken
 */
public class TracksModel extends SimpleDisplayRenderableProvider {
    private DisplayTracks myModel;

    /**
     * Constructor of TracksModel
     * @param model scenario to grab tracks from.
     */
    public TracksModel(Camera camera, DisplayTracks model)
    {
        super(camera);
        myModel = model;
        update();
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update()
    {
        double depthAdjustment = 0.01;

        super.update();

        if (!myModel.isVisible())
        {
            setDisplayRenderable(new HiddenDisplayRenderable());
            return;
        }
        ArrayList<Track> tracks = myModel.getScenarioTracks();

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "track",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.RED);

        for (Track track : tracks)
        {
            //jump, just in case we want to skip some elements (it was having trouble drawing the entire track)
            //for performance reasons.
            int jump = 1;
            Vector3 previousPositionDrawVector = track.get(0).getPosition().getModelDrawVector(depthAdjustment);
            for(int i = jump; i < track.size(); i+=jump)
            {
                AircraftState state = track.get(i);
                Vector3 positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
                builder.line(previousPositionDrawVector, positionDrawVector);
                previousPositionDrawVector = positionDrawVector;
            }
        }

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }
}
