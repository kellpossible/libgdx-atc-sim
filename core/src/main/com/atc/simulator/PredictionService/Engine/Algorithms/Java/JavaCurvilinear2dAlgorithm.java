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
 * Created by luke on 13/07/16.
 */
public class JavaCurvilinear2dAlgorithm extends JavaPredictionAlgorithm {
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

        AircraftState state = aircraftTrack.getLatest();
        long startTime = state.getTime();
        GeographicCoordinate geographicPosition = state.getPosition();
        Vector3 position = projection.transformPositionTo(geographicPosition);

        SphericalVelocity sphericalVelocity = state.getVelocity();

        Vector3 velocity = projection.tranformVelocityTo(sphericalVelocity, geographicPosition, position);

        ArrayList<AircraftState> predictedStates = new ArrayList<AircraftState>();

        int dt = 5000;
        int totalDT = 0;
        int n = 24;
        int lookBack = Math.min(5, aircraftTrack.size()-1);

        Vector3 lookBackDirection = velocity.negate().normalize();

        System.out.println();
        System.out.println("NEW PREDICTION");
        System.out.println("Position: " + position);
        System.out.println("Size: " + aircraftTrack.size());
        System.out.println("Lookback Direction: " + lookBackDirection);
        System.out.println("Velocity: " + velocity);
        System.out.println("lookback: " + lookBack);

        for (int i = aircraftTrack.size()-1-lookBack; i < aircraftTrack.size()-1; i++)
        {
            AircraftState lookBackState = aircraftTrack.get(i);
            GeographicCoordinate lookBackPosition = lookBackState.getPosition();
            Vector3 lookBackPosition2d = projection.transformPositionTo(lookBackPosition);

            double crossTrackError = linearCrossTrackError(position, lookBackDirection, lookBackPosition2d);
            System.out.println("CrossTrackError: " + crossTrackError);
            System.out.println("lookbackpos: " + lookBackPosition2d);



        }

        for (int i = 0; i < n; i++)
        {
            totalDT += dt;

            //make a linear prediction based on the current velocity
            Vector3 predictedPosition = position.add(velocity.mult(totalDT/1000));
            GeographicCoordinate predictedGeographicPosition = projection.transformPositionFrom(predictedPosition);

            AircraftState predictedState = new AircraftState(
                    state.getAircraftID(),
                    startTime+totalDT,
                    predictedGeographicPosition,
                    sphericalVelocity,
                    0);
            predictedStates.add(predictedState);

        }

        Prediction prediction = new Prediction(state.getAircraftID(), startTime, predictedStates);
        return prediction;
    }

    private double linearCrossTrackError(Vector3 referencePoint, Vector3 referenceDirection, Vector3 point)
    {
        Vector3 a1 = referenceDirection.mult(point.dot(referenceDirection));
        Vector3 a = point.subtract(referencePoint);
        Vector3 a2 = a.subtract(a1);
        return a2.length();
    }
}
