package com.atc.simulator.prediction_service.engine.algorithms.java;

import com.atc.simulator.debug_data_feed.scenarios.Scenario;
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
    public Prediction makePrediction(Track aircraftTrack, Object algorithmState) {
        if (projection == null)
        {
            GeographicCoordinate projectionReference = Scenario.getCurrentScenario().getProjectionReference();
            projection = new GnomonicProjection(projectionReference);

        }

        AircraftState state = aircraftTrack.getLatest();
        long startTime = state.getTime();

        GeographicCoordinate geographicPosition = state.getPosition();
        Vector3 currentPosition = projection.transformPositionTo(geographicPosition);

        SphericalVelocity sphericalVelocity = state.getVelocity();

        Vector3 currentVelocity = projection.tranformVelocityTo(sphericalVelocity, geographicPosition, currentPosition);


        ArrayList<AircraftState> predictedStates = new ArrayList<AircraftState>(); //an array to store the predicted states

        if(aircraftTrack.size() < 3) //If there's only 1 or 2 states, we can't find acceleration
        {//So we will steal Luke's JavaLinearAlgorithm algorithm

            int dt = 5000;
            int totalDT = 0;
            int n = 24;

            for (int i = 0; i < n; i++)
            {
                totalDT += dt;

                //make a linear prediction based on the current velocity
                Vector3 predictedPosition = currentPosition.add(currentVelocity.mult(totalDT/1000));
                GeographicCoordinate predictedGeographicPosition = projection.transformPositionFrom(predictedPosition);

                AircraftState predictedState = new AircraftState(
                        state.getAircraftID(),
                        startTime+totalDT,
                        predictedGeographicPosition,
                        sphericalVelocity,
                        0);
                predictedStates.add(predictedState);

            }

            Track predictedTrack = new Track(predictedStates);
            Prediction prediction = new Prediction(
                    state.getAircraftID(),
                    startTime,
                    state,
                    predictedTrack,
                    predictedTrack,
                    predictedTrack,
                    Prediction.State.STRAIGHT);
            return prediction;
        }
        else //otherwise, we can try my algorithm!!
        {
            //This will be acceleration in radians / second^2 I guess?
                        //and it can be made into a lot less lines... just neater this way for now x
            Vector3 newPos = new Vector3(currentPosition);
            AircraftState oldState = aircraftTrack.get(aircraftTrack.size() - 2);
            SphericalVelocity oldVelocity = oldState.getVelocity();
            double oldTime = oldState.getTime();
            double dt = startTime - oldTime;



            Vector3 acceleration = currentVelocity.subtract(oldVelocity).mult(1.0/dt);

            System.out.println("Acceleration:" + acceleration.length());

            for (int numPredictions = 1; numPredictions != 24; numPredictions++)
            {
                Vector3 predictedPosition = newPos.add(
                        currentVelocity.mult(numPredictions*5).add(
                                acceleration.mult(0.5*numPredictions*5*numPredictions*5)
                        )
                );

                GeographicCoordinate predictedGeographicPosition = projection.transformPositionFrom(predictedPosition);


                AircraftState predictedState = new AircraftState(
                        state.getAircraftID(),
                        startTime+(numPredictions*5),
                        predictedGeographicPosition,
                        sphericalVelocity,
                        0);
                predictedStates.add(predictedState);
            }
            Track predictedTrack = new Track(predictedStates);
            Prediction prediction = new Prediction(
                    state.getAircraftID(),
                    startTime,
                    state,
                    predictedTrack,
                    predictedTrack,
                    predictedTrack,
                    Prediction.State.STRAIGHT);
            return prediction;

        }
    }

    /**
     * Get a new state object for this algorithm.
     *
     * @return
     */
    @Override
    public Object getNewStateObject() {
        return null;
    }

    private double linearCrossTrackError(Vector3 referencePoint, Vector3 referenceDirection, Vector3 point)
    {
        Vector3 a1 = referenceDirection.mult(point.dot(referenceDirection));
        Vector3 a = point.subtract(referencePoint);
        Vector3 a2 = a.subtract(a1);
        return a2.length();
    }
}
