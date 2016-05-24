package com.atc.simulator.flightdata;

import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.badlogic.gdx.files.FileHandle;
import pythagoras.d.Vector3;
import sun.nio.cs.StandardCharsets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by luke on 24/05/16.
 * Read in the track data from a CSV file generated by xplage
 * (http://www.chriskern.net/code/xplaneToGoogleEarth.html)
 * @author Luke Frisken
 */
public class SimulatorTrackLoader extends TrackLoader {
    private String filePath;

    /**
     * @param filePath
     */
    public SimulatorTrackLoader(String filePath)
    {
        this.filePath = filePath;
    }

    @Override
    public Track load() throws IOException {
        return readFromCSVFile(this.filePath);
    }

    /**
     * Read the CSV File
     * @param filePath the path of the file to read
     * @return
     */
    public static Track readFromCSVFile(String filePath) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        Track track = new Track();


        double latitude;
        double longitude;
        Calendar time;

        String csv_string = new String(encoded, java.nio.charset.StandardCharsets.UTF_8);
        String[] lines = csv_string.split(System.getProperty("line.separator"));

        GeographicCoordinate previousPosition = null;

        for (String line : lines)
        {

            List<String> line_values = Arrays.asList(line.split(","));
            try {
                time = ISO8601.toCalendar(line_values.get(0));

                //Funnily enough, the latitude and longitude are in the oposite order
                //to what you usually find.
                longitude = Math.toRadians(Double.parseDouble(line_values.get(1)));
                latitude = Math.toRadians(Double.parseDouble(line_values.get(2)));
                GeographicCoordinate position = new GeographicCoordinate(0, latitude, longitude); //TODO: implement altitude properly

                double heading = Double.NaN;
                SphericalVelocity velocity;

                if (previousPosition != null)
                {
                    heading = previousPosition.bearingTo(position); //TODO: pretty sure this is broken although who knows what x-plane's heading is putting out (true or magnetic?)
                    velocity = new SphericalVelocity(position.subtract(previousPosition)); //TODO: fails hard due to precision error
                } else {
                    velocity = new SphericalVelocity(0,0,0);
                }

                AircraftState state = new AircraftState("DEBUG", position, velocity, heading); //TODO: implement velocity and heading properly
                track.add(
                        new TrackEntry(time, state)
                );

                previousPosition = position;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return track;
    }
}
