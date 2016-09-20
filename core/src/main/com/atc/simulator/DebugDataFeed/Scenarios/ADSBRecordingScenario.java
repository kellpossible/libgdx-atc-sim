package com.atc.simulator.DebugDataFeed.Scenarios;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pythagoras.d.Vector3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.lang.Math;

/**
 * A scenario which is based on a recording of ADSB data in a JSON database file.
 *
 * @author Luke Frisken
 */
public class ADSBRecordingScenario extends Scenario {
    private HashMap<String, Track> tracksDictionary; //map a track to the aircraft id
    private ArrayList<Track> tracks;
    private LinkedHashMap<Long, SystemState> systemStates;
    private int recommendedUpdateRate;
    private long startTime = 0, endTime = 0;
    private GeographicCoordinate projectionReference;

    /**
     * Constructor ADSBRecordingScenario creates a new ADSBRecordingScenario instance.
     */
    public ADSBRecordingScenario(String filePath)
    {
        tracksDictionary = new HashMap<String, Track>();
        systemStates = new LinkedHashMap<Long, SystemState>();
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

        JsonArray projectionReferenceJS = object.get("projection_reference").getAsJsonArray();
        double projectionReferenceLatitude = projectionReferenceJS.get(0).getAsDouble();
        double projectionReferenceLongitude = projectionReferenceJS.get(1).getAsDouble();

        projectionReference = new GeographicCoordinate(
                0,
                Math.toRadians(projectionReferenceLatitude),
                Math.toRadians(projectionReferenceLongitude));

        JsonArray systemStatesJS = object.get("system_states").getAsJsonArray();

        startTime = Long.MAX_VALUE;
        endTime = Long.MIN_VALUE;

        for (JsonElement systemStateElement: systemStatesJS) {
            JsonObject systemStateJS = systemStateElement.getAsJsonObject();

            long systemStateTime = ISO8601.toCalendar(systemStateJS.get("time").getAsString()).getTimeInMillis();

            JsonArray aircraftStatesJS = systemStateJS.get("aircraft_states").getAsJsonArray();

            ArrayList<AircraftState> aircraftStates = new ArrayList<AircraftState>();

            for (JsonElement aircraftStateElement : aircraftStatesJS) {
                JsonObject aircraftStateJS = aircraftStateElement.getAsJsonObject();
                String aircraftID = aircraftStateJS.get("mode_s_code").getAsString();

//                if (!callsign.equals("QFA7373"))
//                {
//                    continue;
//                }

                long aircraftStateTime = ISO8601.toCalendar(aircraftStateJS.get("time").getAsString()).getTimeInMillis();

                if (aircraftStateTime > endTime)
                {
                    endTime = aircraftStateTime;
                }

                if (aircraftStateTime < startTime)
                {
                    startTime = aircraftStateTime;
                }


                double altitude = aircraftStateJS.get("altitude").getAsDouble();
                double latitude = Math.toRadians(aircraftStateJS.get("latitude").getAsDouble());
                double longitude = Math.toRadians(aircraftStateJS.get("longitude").getAsDouble());
                double speed = aircraftStateJS.get("speed").getAsDouble();

                GeographicCoordinate position = new GeographicCoordinate(altitude, latitude, longitude);

                double heading = aircraftStateJS.get("heading").getAsDouble();



                Track track = tracksDictionary.get(aircraftID);
                if (track == null) {
                    track = new Track();
                    tracksDictionary.put(aircraftID, track);
                }
                SphericalVelocity velocity;
                SphericalVelocity previousNonZeroVelocity;

                //hack to get the velocities by differentiating the positions.
                if (track.size() == 0)
                {
                    velocity = new SphericalVelocity(0,0,0);
                } else {
                    AircraftState previousState = track.get(track.size() - 1);
                    GeographicCoordinate previousPosition = previousState.getPosition();
                    long previousTime = previousState.getTime();


                    //calculate the velocity based on the change in position over time.
                    double dt = (aircraftStateTime-previousTime)/1000.0;

                    if (dt < 0.1)
                    {
                        continue; //exclude records within 0.1 seconds of each other. (probably duplicates)
                    } else {
                        Vector3 dPos = position.subtract(previousPosition);
                        double storeX = dPos.x;

                        Vector3 cartesionPrevPos = previousPosition.getCartesian();

//                        System.out.println("position1geo" + position);
                        Vector3 cartesionPos = new SphericalCoordinate(position).getCartesian();
//                        System.out.println("cartesianpos" + cartesionPos);
                        Vector3 cartesianDPos = cartesionPos.subtract(cartesionPrevPos);
//                        System.out.println("Cartesion DPos: " + cartesianDPos);
                        Vector3 cartesianVelocity = cartesianDPos.normalize().mult(speed);
                        Vector3 cartesianVelocityPos = cartesionPos.add(cartesianVelocity);
                        SphericalCoordinate sphericalVelocityPos = SphericalCoordinate.fromCartesian(cartesianVelocityPos);
                        dPos = new SphericalCoordinate(sphericalVelocityPos.subtract(position));



//                        double speedAngle = speed/position.x;
//
//                        System.out.println("SpeedAngle" + speedAngle);
//                        System.out.println("Speed" + speed);
//                        System.out.println(dPos);
////
//                        dPos.x = 0;
//                        dPos = dPos.normalize();
//                        dPos = dPos.mult(speedAngle);
//                        dPos.x = storeX;

//                        System.out.println("dpos" + dPos);
                        //hack until I can figure out how to calculate the velocity correctly
                        velocity = new SphericalVelocity(dPos);
                    }
                }

                //TODO: callsign might not actually be the best thing to use for aircraft ID, looks like there might be duplicates?
                //mode_s_code seems to get used more instead.
                AircraftState aircraftState = new AircraftState(aircraftID, aircraftStateTime, position, velocity, heading);
                track.add(aircraftState);



                aircraftStates.add(aircraftState);
            }

            systemStates.put(systemStateTime, new SystemState(systemStateTime, aircraftStates));

        }

        tracks = new ArrayList<Track>(tracksDictionary.values());

    }

    /**
     * Get the SystemState at a given time as represented by the Scenario.
     * Assumes that the time provided is within the startTime and endTime boundary
     *
     * @param time time of system state (in milliseconds since epoch)
     * @return system state
     */
    @Override
    public SystemState getState(long time) {
        checkStateTimeWithinBoundaries(time);

//        System.out.println("getStateTime" + time.getTimeInMillis());
//        System.out.println("startTime" + startTime.getTimeInMillis());

        long closestTime = 0;
        long closestTimeDiff = Long.MAX_VALUE;
        for (long comparisonTime: systemStates.keySet())
        {
            long timeDiff = Math.abs(comparisonTime - time);
            if (timeDiff < closestTimeDiff)
            {
                closestTimeDiff = timeDiff;
                closestTime = comparisonTime;
            }
        }

//        System.out.println("startTime" + startTime.getTimeInMillis());

        if (closestTime != 0)
        {
//            SystemState tempSystemState = systemStates.get(closestTime);

            ArrayList<AircraftState> newStates = new ArrayList<AircraftState>();
//            for(AircraftState aircraftState: tempSystemState.getAircraftStates())
//            {
//                Track track = tracksDictionary.get(aircraftState.getAircraftID());
//                newStates.add(track.lerp(time));
//            }

            for (Track track : tracksDictionary.values())
            {
                if (track.timeWithinBounds(time))
                {
                    newStates.add(track.lerp(time));
                }

            }

            return new SystemState(time, newStates);

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
     * @return start time (in milliseconds since epoch)
     */
    @Override
    public long getStartTime() {
        return startTime;
    }

    /**
     * Get the end time of the scenario
     *
     * @return end time (in milliseconds since epoch)
     */
    @Override
    public long getEndTime() {
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

    /**
     * Get the projection reference position of the scenario
     *
     * @return
     */
    @Override
    public GeographicCoordinate getProjectionReference() {
        return projectionReference;
    }
}
