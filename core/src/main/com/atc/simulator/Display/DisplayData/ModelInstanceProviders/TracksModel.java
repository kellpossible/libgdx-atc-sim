package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Red tracks from scenario to go in display.
 * @author Luke Frisken
 */
public class TracksModel extends ModelInstanceDisplayRenderableProvider {
    private Scenario scenario;

    /**
     * Cnstructor of TracksModel
     * @param scenario scenario to grab tracks from.
     */
    public TracksModel(Camera camera, Scenario scenario)
    {
        super(camera);
        this.scenario = scenario;
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

        Model newModel = modelBuilder.end();
        setModel(newModel);
    }
}
