package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by luke on 5/09/16.
 */
public class DisplayTracks extends SimpleModelInstanceProvider {
    private Scenario scenario;
    public DisplayTracks(Scenario scenario)
    {
        this.scenario = scenario;
        update();
    }

    @Override
    public void update()
    {
        double depthAdjustment = 0.01;

        super.update();

        ArrayList<Track> tracks = scenario.getTracks();

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

        model = modelBuilder.end();
        modelInstance = new ModelInstance(model);


        triggerOnInstanceUpdate(modelInstance);
    }
}
