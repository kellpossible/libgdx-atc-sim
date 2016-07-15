package com.atc.simulator.PredictionService.Engine.Algorithms.Java;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.GnomonicProjection;
import com.atc.simulator.vectors.SphericalVelocity;
import pythagoras.d.Vector3;

import java.util.ArrayList;

/**
 * Attempt at boshing an acceleration through 3 changes in position
 * Uses Luke's linear algorithm for anything less than 3 poitns
 * Created by Chris on 15/07/2016.
 */
public class JavaChrisAlgorithm1 extends JavaPredictionAlgorithm {

    GnomonicProjection projection = null;

    /**
     * Method makePrediction ...
     *
     * @param aircraftTrack of type Track
     * @return Prediction
     */
    @Override
    public Prediction makePrediction(Track aircraftTrack) {
        if (projection == null)
        {
            GeographicCoordinate projectionReference = Scenario.getCurrentScenario().getProjectionReference();
            projection = new GnomonicProjection(projectionReference);

        }
        if(aircraftTrack.size() < 3) //If there's only 1 or 2 states, we can't find acceleration
        {//So we will steal Luke's JavaLinearAlgorithm algorithm
            AircraftState state = aircraftTrack.getLatest();
            long startTime = state.getTime();
            GeographicCoordinate position = state.getPosition();
            SphericalVelocity velocity = state.getVelocity();
            ArrayList<AircraftState> predictedStates = new ArrayList<AircraftState>();

            int dt = 5000;
            int totalDT = 0;
            int n = 24;

            for (int i = 0; i < n; i++)
            {
                totalDT += dt;

                //make a linear prediction based on the current velocity
                GeographicCoordinate predictedPosition = new GeographicCoordinate(
                        position.add(velocity.mult(totalDT/1000))
                );

                AircraftState predictedState = new AircraftState(
                        state.getAircraftID(),
                        startTime+totalDT,
                        predictedPosition,
                        velocity,
                        0);
                predictedStates.add(predictedState);

            }

            Prediction prediction = new Prediction(state.getAircraftID(), startTime, predictedStates);
            return prediction;
        }
        else //otherwise, we can try my algorithm!!
        {
            //I don't trust Luke's velocity, so I'm doing it myself... it'll be a bit slow maybe?
            //Get the three latest positions
            AircraftState newestState = aircraftTrack.getLatest();
            AircraftState previousState = aircraftTrack.get(aircraftTrack.size() - 2);
            AircraftState oldestState = aircraftTrack.get(aircraftTrack.size() - 3);
            //Find the changes in Time
            double deltaTimeNew = (newestState.getTime() - previousState.getTime())/1000;
            double deltaTimeOld = (previousState.getTime() - oldestState.getTime())/1000;
            //Find the changes in positions
            double deltaLatNew = newestState.getPosition().getLatitude() - previousState.getPosition().getLatitude();
            double deltaLatOld = previousState.getPosition().getLatitude() - oldestState.getPosition().getLatitude();
            double deltaLongNew = newestState.getPosition().getLongitude() - previousState.getPosition().getLongitude();
            double deltaLongOld = previousState.getPosition().getLongitude() - oldestState.getPosition().getLongitude();
            //double deltaAltNew = newestState.getPosition().getAltitude() - previousState.getPosition().getAltitude();
            //double deltaAltOld = previousState.getPosition().getAltitude() - oldestState.getPosition().getAltitude();

            //Accel is velocity changes over average time
            double doubleDeltaLat = (deltaLatNew - deltaLatOld) / ( (deltaTimeNew+deltaTimeOld)/2 );
            double doubleDeltaLong = (deltaLongNew - deltaLongOld) / ( (deltaTimeNew+deltaTimeOld)/2 );

        }
    }

    private double linearCrossTrackError(Vector3 referencePoint, Vector3 referenceDirection, Vector3 point)
    {
        Vector3 a1 = referenceDirection.mult(point.dot(referenceDirection));
        Vector3 a = point.subtract(referencePoint);
        Vector3 a2 = a.subtract(a1);
        return a2.length();
    }
}
