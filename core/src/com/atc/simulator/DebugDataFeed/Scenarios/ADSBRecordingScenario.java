package com.atc.simulator.DebugDataFeed.Scenarios;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

/**
 * Created by luke on 26/05/16.
 *
 * @author Luke Frisken
 */
public class ADSBRecordingScenario extends Scenario {
    private HashMap<String, Track> tracksDictionary; //map a track to the aircraft id
    private ArrayList<Track> tracks;
    private LinkedHashMap<Calendar, SystemState> systemStates;
    private int recommendedUpdateRate;
    private Calendar startTime, endTime;

    /**
     * Constructor ADSBRecordingScenario creates a new ADSBRecordingScenario instance.
     */
    public ADSBRecordingScenario(String filePath)
    {
        tracksDictionary = new HashMap<String, Track>();
        try {
            readFromJsonFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void readFromJsonFile(String filePath) throws IOException, ParseException {
        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        String jsonString = new String(encoded, java.nio.charset.StandardCharsets.UTF_8);
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(jsonString).getAsJsonObject();
        recommendedUpdateRate = (int) (object.get("update_rate").getAsDouble() * 1000);

        JsonArray systemStatesJS = object.get("system_states").getAsJsonArray();

        int i = 0;
        int finalStateIndex = systemStatesJS.size()-1;

        for (JsonElement systemStateElement: systemStatesJS) {
            JsonObject systemStateJS = systemStateElement.getAsJsonObject();

            Calendar systemStateTime = ISO8601.toCalendar(systemStateJS.get("time").getAsString());

            if (i == 0)
            {
                startTime = systemStateTime;
            } else if (i == finalStateIndex) {
                endTime = systemStateTime;
            }

            JsonArray aircraftStatesJS = systemStateJS.get("aircraft_states").getAsJsonArray();

            ArrayList<AircraftState> aircraftStates = new ArrayList<AircraftState>();

            for (JsonElement aircraftStateElement : aircraftStatesJS) {
                JsonObject aircraftStateJS = aircraftStateElement.getAsJsonObject();
                String callsign = aircraftStateJS.get("callsign").getAsString();
                Calendar aircraftStateTime = ISO8601.toCalendar(aircraftStateJS.get("time").getAsString());
                double altitude = aircraftStateJS.get("altitude").getAsDouble();
                double latitude = aircraftStateJS.get("latitude").getAsDouble();
                double longitude = aircraftStateJS.get("longitude").getAsDouble();

                GeographicCoordinate position = new GeographicCoordinate(altitude, latitude, longitude);
                SphericalVelocity velocity = new SphericalVelocity(0,0,0);

                double heading = aircraftStateJS.get("heading").getAsDouble();

                AircraftState aircraftState = new AircraftState(callsign, aircraftStateTime, position, velocity, heading);

                Track track = tracksDictionary.get(callsign);
                if (track == null) {
                    track = new Track();
                    tracksDictionary.put(callsign, track);
                }
                track.add(aircraftState);

                aircraftStates.add(aircraftState);
            }

            systemStates.put(systemStateTime, new SystemState(systemStateTime, aircraftStates));

            i++;
        }

        tracks = new ArrayList<Track>(tracksDictionary.values());

    }

    /**
     * Get the SystemState at a given time as represented by the Scenario.
     * Assumes that the time provided is within the startTime and endTime boundary
     *
     * @param time time of system state
     * @return system state
     */
    @Override
    public SystemState getState(Calendar time) {
        Calendar closestTime = null;
        long closestTimeDiff = Long.MAX_VALUE;
        for (Calendar comparisonTime: systemStates.keySet())
        {
            long timeDiff = Math.abs(comparisonTime.getTimeInMillis() - time.getTimeInMillis());
            if (timeDiff < closestTimeDiff)
            {
                closestTimeDiff = timeDiff;
                closestTime = comparisonTime;
            }
        }

        if (closestTime != null)
        {
            return systemStates.get(closestTime);
        }

        return null;
    }

    /**
     * Get the tracks that this scenario is based on.
     *
     * @return tracks this scenario is based on
     */
    @Override
    public ArrayList<Track> getTracks() {
        return tracks;
    }

    /**
     * Get the start time of the Scenario
     *
     * @return start time
     */
    @Override
    public Calendar getStartTime() {
        return startTime;
    }

    /**
     * Get the end time of the scenario
     *
     * @return end time
     */
    @Override
    public Calendar getEndTime() {
        return endTime;
    }

    /**
     * The scenario's recommended update rate, in milliseconds, in order
     * to keep it syncronised.
     *
     * @return update rate (in milliseconds).
     */
    @Override
    public int getRecommendedUpdateRate() {
        return recommendedUpdateRate;
    }
}
