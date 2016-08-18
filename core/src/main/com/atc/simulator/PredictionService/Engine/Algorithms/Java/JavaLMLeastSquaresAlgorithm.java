package com.atc.simulator.PredictionService.Engine.Algorithms.Java;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
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
 * Created by luke on 18/08/16.
 *
 * @author Luke Frisken
 */
public class JavaLMLeastSquaresAlgorithm extends JavaPredictionAlgorithm {
    GnomonicProjection projection = null;

    /**
     * Method makePrediction ...
     *
     * @param aircraftTrack of type Track
     * @return Prediction
     */
    @Override
    public Prediction makePrediction(Track aircraftTrack) {
        //TODO: probably need to move this into a singleton
        //basically have a cached version of the projection
        //because we don't want to have to generate it every time.
        if (projection == null)
        {
            GeographicCoordinate projectionReference = Scenario.getCurrentScenario().getProjectionReference();
            projection = new GnomonicProjection(projectionReference);

        }

        AircraftState state = aircraftTrack.getLatest();
        long startTime = state.getTime();
        GeographicCoordinate geographicPosition = state.getPosition();

        //transform position into a flat gnomonic projection coordinate system.
        Vector3 currentPosition = projection.transformPositionTo(geographicPosition);

        //set the z position to zero because we don't care about it, and
        //don't want to worry about it breaking stuff right now.
        currentPosition.z = 0;

        SphericalVelocity sphericalVelocity = state.getVelocity();

        //transform the velocity into a flat gnomonic projection coordinate system
        Vector3 velocity = projection.tranformVelocityTo(sphericalVelocity, geographicPosition, currentPosition);

        //same deal as with position z component
        velocity.z = 0;

        ArrayList<AircraftState> predictedStates = new ArrayList<AircraftState>();

        int dt = 5000;
        int totalDT = 0;
        int n = 24;

//        System.out.println();
//        System.out.println("NEW PREDICTION");
//        System.out.println("Position: " + currentPosition);
//        System.out.println("Size: " + aircraftTrack.size());
//        System.out.println("Velocity: " + velocity);

        Vector3 rVec = null;
        Vector3 centre = null;
        boolean useCircle = false;
        double w = 0;

        if (aircraftTrack.size() == 3)
        {
            //find centre of circle given 3 points
            Vector3 p1 = projection.transformPositionTo(aircraftTrack.get(aircraftTrack.size()-1).getPosition());
            Vector3 p2 = projection.transformPositionTo(aircraftTrack.get(aircraftTrack.size()-2).getPosition());
            Vector3 p3 = projection.transformPositionTo(aircraftTrack.get(aircraftTrack.size()-3).getPosition());

            Circle circle = CircleSolver.FromThreePoints(p1, p2, p3);

//            System.out.println("P1: " + p1);
//            System.out.println("P2: " + p2);
//            System.out.println("P3: " + p3);
//            System.out.println("Circle (" + circle.x + "," + circle.y + "," + circle.radius + ")");

            //check to see whether the radius is small enough for the aircraft to actually be turning.
            if (circle.radius < 100000)
            {
                useCircle = true;
                centre = new Vector3(circle.x, circle.y, 0);
                rVec = currentPosition.subtract(centre);

                //calculate the angular velocity
                w = velocity.length()/circle.radius;
            }
        }

        if (aircraftTrack.size() > 3)
        {
            int movingWindowSize = 10;
            int windowFromIndex = Math.max(aircraftTrack.size()-1-movingWindowSize, 0);
            ArrayList<AircraftState> movingWindow = new ArrayList<AircraftState>(aircraftTrack.subList(windowFromIndex, aircraftTrack.size()-1));
            ArrayList<Vector3> movingWindowPositions = new ArrayList<Vector3>(movingWindow.size());

            for(AircraftState aircraftState: movingWindow)
            {
                movingWindowPositions.add(projection.transformPositionTo(aircraftState.getPosition()));
            }
            //find centre of circle given 3 points

            //find centre of circle given 3 points
            Vector3 p1 = projection.transformPositionTo(aircraftTrack.get(aircraftTrack.size()-1).getPosition());
            Vector3 p2 = projection.transformPositionTo(aircraftTrack.get(aircraftTrack.size()-2).getPosition());
            Vector3 p3 = projection.transformPositionTo(aircraftTrack.get(aircraftTrack.size()-3).getPosition());

            Vector3 headPoint = movingWindowPositions.get(movingWindowPositions.size()-1);
            Vector3 middlePoint = movingWindowPositions.get((movingWindowPositions.size()-1)/2);
            Vector3 tailPoint = movingWindowPositions.get(0);

            Circle frontCircle = CircleSolver.FromThreePoints(p1, p2, p3);
            Circle betaCircle = CircleSolver.FromThreePoints(headPoint, middlePoint, tailPoint);
            Circle fitCircle = CircleSolver.LeastSquares(movingWindowPositions, betaCircle);

//            System.out.println("Moving Window Positions:");
//            for(Vector3 pos : movingWindowPositions)
//            {
//                System.out.println("[" + pos.x + "," + pos.y + "]");
//            }
//
//
//
//            System.out.println("P1: " + headPoint);
//            System.out.println("P2: " + middlePoint);
//            System.out.println("P3: " + tailPoint);
//            System.out.println("BetaCircle (" + betaCircle.x + "," + betaCircle.y + "," + betaCircle.radius + ")");
//            System.out.println("FitCircle (" + fitCircle.x + "," + fitCircle.y + "," + fitCircle.radius + ")");

            //check to see whether the radius is small enough for the aircraft to actually be turning.
            if (fitCircle.radius < 100000)
            {
                useCircle = true;
                centre = new Vector3(fitCircle.x, fitCircle.y, 0);
                rVec = currentPosition.subtract(centre);

                //calculate the angular velocity
                w = velocity.length()/fitCircle.radius;
            }
        }

        //use the circle prediction
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

                // calculate the distance around the circle given
                // the angular velocity. Only predict up to half a
                // circle maximum.
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
}
