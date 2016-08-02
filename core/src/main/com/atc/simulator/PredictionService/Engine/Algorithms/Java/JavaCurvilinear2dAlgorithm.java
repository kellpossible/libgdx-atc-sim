package com.atc.simulator.PredictionService.Engine.Algorithms.Java;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.CircleSolver;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.GnomonicProjection;
import com.atc.simulator.vectors.SphericalVelocity;
import pythagoras.d.Circle;
import pythagoras.d.Matrix3;
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
        Vector3 currentPosition = projection.transformPositionTo(geographicPosition);
        currentPosition.z = 0;

        SphericalVelocity sphericalVelocity = state.getVelocity();

        Vector3 velocity = projection.tranformVelocityTo(sphericalVelocity, geographicPosition, currentPosition);
        velocity.z = 0;

        ArrayList<AircraftState> predictedStates = new ArrayList<AircraftState>();

        int dt = 5000;
        int totalDT = 0;
        int n = 24;
        int lookBack = Math.min(3, aircraftTrack.size()-1);

        Vector3 lookBackDirection = velocity.negate().normalize();

        System.out.println();
        System.out.println("NEW PREDICTION");
        System.out.println("Position: " + currentPosition);
        System.out.println("Size: " + aircraftTrack.size());
        System.out.println("Lookback Direction: " + lookBackDirection);
        System.out.println("Velocity: " + velocity);
        System.out.println("lookback: " + lookBack);

        double crossTrackErrorMax = Double.MIN_VALUE;

        Vector3 rVec = null;
        Vector3 centre = null;
        boolean useCircle = false;
        double w = 0;

        if (aircraftTrack.size() > 3)
        {
            //find centre of circle given 3 points
            Vector3 p1 = projection.transformPositionTo(aircraftTrack.get(aircraftTrack.size()-1).getPosition());
            Vector3 p2 = projection.transformPositionTo(aircraftTrack.get(aircraftTrack.size()-2).getPosition());
            Vector3 p3 = projection.transformPositionTo(aircraftTrack.get(aircraftTrack.size()-3).getPosition());

            Circle circle = CircleSolver.FromThreePoints(p1, p2, p3);

            System.out.println("CrossTrackError: " + crossTrackErrorMax);
            System.out.println("P1: " + p1);
            System.out.println("P2: " + p2);
            System.out.println("P3: " + p3);
            System.out.println("Circle (" + circle.x + "," + circle.y + "," + circle.radius + ")");

            if (circle.radius < 100000)
            {
                useCircle = true;
                centre = new Vector3(circle.x, circle.y, 0);
                rVec = currentPosition.subtract(centre);
                w = velocity.length()/circle.radius;
            }
        }

        if (useCircle)
        {
            for (int i = 0; i < n; i++)
            {
                totalDT += dt;

                Vector3 directionCheck = rVec.cross(velocity);
                double directionSign;
                if (directionCheck.z > 0.0) {
                    directionSign = 1.0;
                } else {
                    directionSign = -1.0;
                }

                double dphi = (totalDT/1000)*w;
                if (dphi > Math.PI)
                {
                    dphi = Math.PI;
                }

                Matrix3 rotation = new Matrix3().setToRotation(dphi*directionSign, new Vector3(0,0,1));
                Vector3 rVecRotated = rotation.transform(rVec);

                //make a linear prediction based on the current velocity
                Vector3 predictedPosition = new Vector3(centre.add(rVecRotated));
                GeographicCoordinate predictedGeographicPosition = projection.transformPositionFrom(predictedPosition);

                AircraftState predictedState = new AircraftState(
                        state.getAircraftID(),
                        startTime+totalDT,
                        predictedGeographicPosition,
                        sphericalVelocity,
                        0);
                predictedStates.add(predictedState);

            }
        }
        else {
            for (int i = 0; i < n; i++)
            {
                totalDT += dt;

                //make a linear prediction based on the current velocity
                Vector3 predictedPosition = currentPosition.add(velocity.mult(totalDT/1000));
                GeographicCoordinate predictedGeographicPosition = projection.transformPositionFrom(predictedPosition);

                AircraftState predictedState = new AircraftState(
                        state.getAircraftID(),
                        startTime+totalDT,
                        predictedGeographicPosition,
                        sphericalVelocity,
                        0);
                predictedStates.add(predictedState);

            }
        }



        Prediction prediction = new Prediction(state.getAircraftID(), startTime, predictedStates);
        return prediction;
    }

    private double linearCrossTrackError(Vector3 referencePoint, Vector3 referenceDirection, Vector3 point)
    {
        Vector3 to = point.subtract(referencePoint);
        Vector3 a1 = referenceDirection.mult(to.dot(referenceDirection));
        Vector3 a = point.subtract(referencePoint);
        Vector3 a2 = a.subtract(a1);
        return a2.length();
    }
}
