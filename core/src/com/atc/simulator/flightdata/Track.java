package com.atc.simulator.flightdata;

import com.atc.simulator.vectors.GeographicCoordinate;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by luke on 7/04/16.
 * Represents a continuous track of an aircraft as it flies through the air, with regular
 * TrackEntry's representing the state of the aircraft for each point in time.
 */
public class Track extends ArrayList<TrackEntry> {

    /**
     * Generate a GL_LINES model of the track
     * @return
     */
    public Model getModel(){
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "track",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.RED);
        int jump = 1;
        for(int i = jump; i < this.size(); i+=jump)
        {
            TrackEntry previousEntry = this.get(i-jump);
            TrackEntry entry = this.get(i);
            AircraftState state = entry.getAircraftState();
            AircraftState previousState = previousEntry.getAircraftState();
            System.out.println(state.getPosition());
            Vector3 prev_pos = previousState.getPosition().getCartesianDrawVector();
            Vector3 pos = state.getPosition().getCartesianDrawVector();
            System.out.println(prev_pos.len());
            System.out.println(pos.len());
            System.out.println(pos);
            builder.line(prev_pos, pos);
        }

        return modelBuilder.end();
    }

}
