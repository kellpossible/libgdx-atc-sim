package com.atc.simulator.PredictionService.Engine.Algorithms.Java;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.GnomonicProjection;
import com.atc.simulator.vectors.SphericalCoordinate;
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

        AircraftState state = aircraftTrack.getLatest();
        long startTime = state.getTime();
        GeographicCoordinate currentPosition = state.getPosition();
        SphericalVelocity currentVelocity = state.getVelocity();
        ArrayList<AircraftState> predictedStates = new ArrayList<AircraftState>(); //an array to store the predicted states

        if(aircraftTrack.size() < 3) //If there's only 1 or 2 states, we can't find acceleration
        {//So we will steal Luke's JavaLinearAlgorithm algorithm



            int totalDT = 0;


            for (int i = 0; i < 24; i++)
            {
                totalDT += 5000;

                //make a linear prediction based on the current velocity
                GeographicCoordinate predictedPosition = new GeographicCoordinate(
                        currentPosition.add(currentVelocity.mult(totalDT/1000))
                );

                AircraftState predictedState = new AircraftState(
                        state.getAircraftID(),
                        startTime+totalDT,
                        predictedPosition,
                        currentVelocity,
                        0);
                predictedStates.add(predictedState);

            }

            Prediction prediction = new Prediction(state.getAircraftID(), startTime, predictedStates);
            return prediction;
        }
        else //otherwise, we can try my algorithm!!
        {
            //This will be acceleration in radians / second^2 I guess?
                        //and it can be made into a lot less lines... just neater this way for now x
            SphericalCoordinate newPos = new SphericalCoordinate(currentPosition);
            AircraftState oldState = aircraftTrack.get(aircraftTrack.size() - 2);
            SphericalVelocity oldVelocity = oldState.getVelocity();
            double oldTime = oldState.getTime();

            double accelR = (currentVelocity.getDR() - oldVelocity.getDR()) / (startTime-oldTime);
            double accelTheta = (currentVelocity.getDTheta() - oldVelocity.getDTheta()) / (startTime-oldTime);
            double accelPhi = (currentVelocity.getDPhi() - oldVelocity.getDPhi()) / (startTime-oldTime);


            for (int numPredictions = 1; numPredictions != 24; numPredictions++)
            {
                SphericalCoordinate predictedPosition = new SphericalCoordinate(
                        newPos.getR() + + (currentVelocity.getDR() * (numPredictions*5)) + (0.5 * accelR * (numPredictions*5) * (numPredictions*5)),
                        newPos.getTheta() + (currentVelocity.getDTheta() * (numPredictions*5)) + (0.5 * accelTheta * (numPredictions*5) * (numPredictions*5)), //s2 = s1 + ut + .5a(t^2)
                        newPos.getPhi() + (currentVelocity.getDTheta() * (numPredictions*5)) + (0.5 * accelPhi * (numPredictions*5) * (numPredictions*5)) //s2 = s1 + ut + .5a(t^2)
                );

                AircraftState predictedState = new AircraftState(
                        state.getAircraftID(),
                        startTime+(numPredictions*5),
                        new GeographicCoordinate(predictedPosition),
                        currentVelocity,
                        0);
                predictedStates.add(predictedState);
            }
            Prediction prediction = new Prediction(state.getAircraftID(), startTime, predictedStates);
            return prediction;

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
