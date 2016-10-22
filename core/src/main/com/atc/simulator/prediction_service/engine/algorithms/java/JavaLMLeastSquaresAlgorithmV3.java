package com.atc.simulator.prediction_service.engine.algorithms.java;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.debug_data_feed.scenarios.Scenario;
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
public class JavaLMLeastSquaresAlgorithmV3 extends JavaPredictionAlgorithm {
    private static final double INTERPOLATION_TRANSITION_TIME = ApplicationConfig.getDouble("settings.prediction-service.prediction-engine.interpolation-transition-time");

    private class AlgorithmState {
        public int counter = 0;
        public Prediction.State lastState;
        public long lastStateTime;

        public AlgorithmState()
        {
            lastState = Prediction.State.STOPPED;
            lastStateTime = 0;
            counter = 0;
        }
    }


    GnomonicProjection projection = null;

    /**
     * Method makePrediction ...
     *
     * @param aircraftTrack of type Track
     * @return Prediction
     */
    @Override
    public Prediction makePrediction(Track aircraftTrack, Object algorithmState) {
        AlgorithmState as = (AlgorithmState) algorithmState;
        as.counter++;



        //TODO: probably need to move this into a singleton
        //basically have a cached version of the projection
        //because we don't want to have to generate it every time.
        if (projection == null)
        {
            GeographicCoordinate projectionReference = Scenario.getCurrentScenario().getProjectionReference();
            projection = new GnomonicProjection(projectionReference);

        }

        AircraftState state = aircraftTrack.getLatest();

        double stateTransition = 0.0;
        if (as.lastStateTime != 0)
        {
            double timeDiff = (state.getTime() - as.lastStateTime)/1000; //seconds
            stateTransition = Math.min(1.0, timeDiff/ INTERPOLATION_TRANSITION_TIME);
//            System.out.println(stateTransition);
        }

        long startTime = state.getTime();
        GeographicCoordinate geographicPosition = state.getPosition();
        int trackSize = aircraftTrack.size();

        //transform position into a flat gnomonic projection coordinate system.
        Vector3 currentPosition = projection.transformPositionTo(geographicPosition);

        double currentAltitude = geographicPosition.getAltitude();

        //set the z position to zero because we don't care about it, and
        //don't want to worry about it breaking stuff right now.
        currentPosition.z = 0;

        SphericalVelocity sphericalVelocity = state.getVelocity();

        if (sphericalVelocity.isNaN())
        {
            System.err.println("Invalid Velocity: " + sphericalVelocity + ". Setting to zero");
            sphericalVelocity.set(0.0, 0.0, 0.0);
        }

        //transform the velocity into a flat gnomonic projection coordinate system
        Vector3 velocity = projection.tranformVelocityTo(sphericalVelocity, geographicPosition, currentPosition);

        //same deal as with position z component
        velocity.z = 0;

        ArrayList<AircraftState> predictedStates = new ArrayList<AircraftState>();
        ArrayList<AircraftState> predictedStatesCentre = new ArrayList<AircraftState>();

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
        Vector3 centreCircleCentre = null;
        Vector3 centreCircleRVec = null;
        boolean useCircle = false;
        double w = 0;
        double wCentre = 0;

        // percentage of outer track, to make the inside track.
        double offsetAmount = 1.2 + (1-stateTransition)*5;

        if (trackSize == 3)
        {
            boolean continuous = continuousLine(aircraftTrack, 0, 2);
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
            if (circle.radius < 100000 && continuous)
            {
                useCircle = true;
                centre = new Vector3(circle.x, circle.y, 0);
                rVec = currentPosition.subtract(centre);

                centreCircleCentre = currentPosition.add(rVec.mult(-offsetAmount));
                centreCircleRVec = rVec.mult(offsetAmount);

                //calculate the angular velocity
                w = velocity.length()/circle.radius;
                wCentre = velocity.length()/(circle.radius*offsetAmount);
            }
        }

        if (trackSize > 3)
        {
            int movingWindowSize = 10;
            boolean continuous = continuousLine(aircraftTrack, trackSize-3, trackSize-1);
            int windowFromIndex = Math.max(aircraftTrack.size()-1-movingWindowSize, 0);
            ArrayList<AircraftState> movingWindow = new ArrayList<AircraftState>(aircraftTrack.subList(windowFromIndex, trackSize-1));
            ArrayList<Vector3> movingWindowPositions = new ArrayList<Vector3>(movingWindow.size());

            for(AircraftState aircraftState: movingWindow)
            {
                movingWindowPositions.add(projection.transformPositionTo(aircraftState.getPosition()));
            }
            //find centre of circle given 3 points

            //find centre of circle given 3 points
            Vector3 p1 = projection.transformPositionTo(aircraftTrack.get(trackSize-1).getPosition());
            Vector3 p2 = projection.transformPositionTo(aircraftTrack.get(trackSize-2).getPosition());
            Vector3 p3 = projection.transformPositionTo(aircraftTrack.get(trackSize-3).getPosition());

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
            if (fitCircle.radius < 100000 && continuous)
            {
                useCircle = true;
                centre = new Vector3(fitCircle.x, fitCircle.y, 0);
                rVec = currentPosition.subtract(centre);

                centreCircleCentre = currentPosition.add(rVec.mult(-offsetAmount));
                centreCircleRVec = rVec.mult(offsetAmount);

                //calculate the angular velocity
                w = velocity.length()/fitCircle.radius;
                wCentre = velocity.length()/(fitCircle.radius*offsetAmount);

            }
        }

        Prediction.State predictionState;

        //use the circle prediction
        if (useCircle)
        {
            Vector3 directionCheck = rVec.cross(velocity);
            double directionSign;
            if (directionCheck.z > 0.0) {
                directionSign = 1.0;
                predictionState = Prediction.State.LEFT_TURN;
            } else {
                directionSign = -1.0;
                predictionState = Prediction.State.RIGHT_TURN;
            }

            Vector3 previousPredictedPosition = currentPosition;
            Vector3 previousPredictionVelocityDir = velocity.normalize();
            Vector3 previousCentreVelocityDir = velocity.normalize();
            Vector3 previousCentrePredictedPosition = currentPosition;
            double speed = velocity.length();

            boolean linear = false;
            for (int i = 0; i < n; i++)
            {
                totalDT += dt;



                // calculate the distance around the circle given
                // the angular velocity. Only predict up to half a
                // circle maximum.
                double dphi = (totalDT/1000)*w;
                double dphiCentre = (totalDT/1000)*wCentre;
                if (dphi > Math.PI)
                {
                    linear = true;
                }

                Vector3 predictedPosition;
                Vector3 predictedCentrePosition;
                if (!linear)
                {
//                    System.out.println("RVEC: " + rVec);
//                    System.out.println("CentreRVEC: " + centreCircleRVec);
//                    System.out.println("cp: " + previousCentrePredictedPosition);
//                    System.out.println("p: " + previousPredictedPosition);

                    Matrix3 rotation = new Matrix3().setToRotation(dphi*directionSign, new Vector3(0,0,1));
                    Vector3 rVecRotated = rotation.transform(rVec);
                    predictedPosition = new Vector3(centre.add(rVecRotated));
                    previousPredictionVelocityDir = (new Vector3(0, 0, directionSign)).cross(rVecRotated).normalize();

                    Matrix3 centreRotation = new Matrix3().setToRotation(dphiCentre*directionSign, new Vector3(0,0,1));
                    Vector3 centreCircleRVecRotated = centreRotation.transform(centreCircleRVec);
                    predictedCentrePosition = new Vector3(centreCircleCentre.add(centreCircleRVecRotated));
                    previousCentreVelocityDir = (new Vector3(0, 0, directionSign)).cross(centreCircleRVecRotated).normalize();

                } else {
                    predictedPosition = previousPredictedPosition.add(previousPredictionVelocityDir.mult((dt/1000.0)*speed));
                    predictedCentrePosition = previousCentrePredictedPosition.add(previousCentreVelocityDir.mult((dt/1000.0)*speed));
                }


                //make a linear prediction based on the current velocity

                GeographicCoordinate predictedGeographicPosition = projection.transformPositionFrom(predictedPosition);
                predictedGeographicPosition.setAltitude(currentAltitude);
                GeographicCoordinate predictedCentreGeographicPosition = projection.transformPositionFrom(predictedCentrePosition);
                predictedCentreGeographicPosition.setAltitude(currentAltitude);
                previousPredictedPosition = predictedPosition;
                previousCentrePredictedPosition = predictedCentrePosition;

                checkPredictionPhysicallyPossible(geographicPosition, predictedGeographicPosition, totalDT/1000);
                checkPredictionPhysicallyPossible(geographicPosition, predictedCentreGeographicPosition, totalDT/1000);


                AircraftState predictedCentreState = new AircraftState(
                        state.getAircraftID(),
                        startTime+totalDT,
                        predictedCentreGeographicPosition,
                        sphericalVelocity,
                        0);

                AircraftState predictedState = new AircraftState(
                        state.getAircraftID(),
                        startTime+totalDT,
                        predictedGeographicPosition,
                        sphericalVelocity,
                        0);

                predictedStates.add(predictedState);
                predictedStatesCentre.add(predictedCentreState);

            }
        }
        else {
            predictionState = Prediction.State.STRAIGHT;
            for (int i = 0; i < n; i++)
            {
                totalDT += dt;

                //make a linear prediction based on the current velocity
                Vector3 predictedPosition = currentPosition.add(velocity.mult(totalDT/1000));
                GeographicCoordinate predictedGeographicPosition = projection.transformPositionFrom(predictedPosition);
                predictedGeographicPosition.setAltitude(currentAltitude);

                AircraftState predictedState = new AircraftState(
                        state.getAircraftID(),
                        startTime+totalDT,
                        predictedGeographicPosition,
                        sphericalVelocity,
                        0);

                checkPredictionPhysicallyPossible(geographicPosition, predictedGeographicPosition, totalDT/1000);
                predictedStates.add(predictedState);
                predictedStatesCentre.add(predictedState);

            }
        }

        Track rightTrack = new Track(predictedStates);

        Prediction linearPrediction = new JavaLinearAlgorithm().makePrediction(aircraftTrack);
        Track leftTrack = linearPrediction.getCentreTrack();

        Track centreTrack = new Track(predictedStatesCentre);

        if (leftTrack.size() != rightTrack.size())
        {
            try {
                throw new Exception("Tracks don't match sizes");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        for(int i = 0; i < leftTrack.size(); i++)
//        {
//            AircraftState leftState = leftTrack.get(i);
//            AircraftState rightState = rightTrack.get(i);
//
//            GeographicCoordinate leftPosition = leftState.getPosition();
//            GeographicCoordinate rightPosition = rightState.getPosition();
//
//            GeographicCoordinate centrePosition = new GeographicCoordinate(leftPosition.lerp(rightPosition, 0.5));
//
//            AircraftState centreState = new AircraftState(leftState);
//            centreState.setPosition(centrePosition);
//            centreTrack.add(centreState);
//        }

        if (as.lastState != predictionState)
        {
            as.lastState = predictionState;
            as.lastStateTime = state.getTime();
        }

        Prediction prediction = new Prediction(
                state.getAircraftID(),
                startTime,
                state,
                leftTrack,
                centreTrack,
                rightTrack,
                predictionState);
        return prediction;
    }

    /**
     * Get a new state object for this algorithm.
     *
     * @return
     */
    @Override
    public Object getNewStateObject() {
        return new AlgorithmState();
    }

    /**
     * Check to see whether the track has any reversals of direction
     * @param track
     * @param from
     * @param to
     * @return
     */
    private boolean continuousLine(Track track, int from, int to)
    {
        // if there are not enough points to do this calculation (requires 3 points in the track)
        if ((to-from) <= 1)
        {
            return false;
        }

        GeographicCoordinate p1, p2, p3;

        int n = 0;
        for (int i = from; i <= to && i < track.size(); i++)
        {
            if (n >= 2)
            {
                p1 = track.get(i-2).getPosition();
                p2 = track.get(i-1).getPosition();
                p3 = track.get(i).getPosition();

                if (p1.cartesianDistance(p3) < p1.cartesianDistance(p2))
                {
                    return false;
                }
            }
            n++;
        }
        return true;
    }


    /**
     * check to see whether the prediction is physically possible.
     * If not, print an error message.
     * @param originalPosition
     * @param predictedPosition
     * @param dt
     */
    private void checkPredictionPhysicallyPossible(GeographicCoordinate originalPosition, GeographicCoordinate predictedPosition, double dt)
    {
        double speed = predictedPosition.getCartesian().subtract(originalPosition.getCartesian()).length()/dt;
        if (speed > 400.0)
        {
            System.err.println("Unlikely average speed for prediction of " + speed + "m/s");
        }
    }
}
