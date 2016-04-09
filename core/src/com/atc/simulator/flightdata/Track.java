package com.atc.simulator.flightdata;

import com.atc.simulator.SphericalPosition;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by luke on 7/04/16.
 */
public class Track extends ArrayList<TrackEntry> {
    public static Track readFromCSVFile(FileHandle file)
    {
        Track track = new Track();


        double latitude;
        double longitude;
        Calendar time;

        String csv_string = file.readString();
        String[] lines = csv_string.split(System.getProperty("line.separator"));

        for (String line : lines)
        {

            List<String> line_values = Arrays.asList(line.split(","));
            try {
                time = ISO8601.toCalendar(line_values.get(0));

//                longitude = FastMath.toRadians(Double.parseDouble(line_values.get(1)));
//                latitude = FastMath.toRadians(Double.parseDouble(line_values.get(2)));
                longitude = Math.toRadians(Double.parseDouble(line_values.get(1)));
                latitude = Math.toRadians(Double.parseDouble(line_values.get(2)));
                SphericalPosition position = new SphericalPosition(0.99, latitude, longitude);
                track.add(
                        new TrackEntry(time, position)
                );

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return track;
    }


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
            TrackEntry prev_entry = this.get(i-jump);
            TrackEntry entry = this.get(i);
            System.out.println(entry.getPosition());
            Vector3 prev_pos = prev_entry.getPosition().getCartesianDrawVector();
            Vector3 pos = entry.getPosition().getCartesianDrawVector();
            System.out.println(prev_pos.len());
            System.out.println(pos.len());
            System.out.println(pos);
            builder.line(prev_pos, pos);
        }

        return modelBuilder.end();
    }

}
