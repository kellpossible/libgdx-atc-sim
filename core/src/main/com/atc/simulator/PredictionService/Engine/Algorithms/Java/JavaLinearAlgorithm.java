package com.atc.simulator.PredictionService.Engine.Algorithms.Java;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import pythagoras.d.Matrix3;
import pythagoras.d.Vector3;

import java.util.ArrayList;

/**
 * Created by luke on 6/07/16.
 *
 * Predicts position based on linear angular velocity
 * and rotating the position vector using that angular
 * velocity.
 *
 *
 * If we are to introduce a different shape than a sphere,
 * this will need to use some sort of numeric integration
 * which tries to keep the cartesian velocity constant.
 */
public class JavaLinearAlgorithm extends JavaPredictionAlgorithm {
    /** @see JavaPredictionAlgorithm#makePrediction(Track) */
    @Override
    public Prediction makePrediction(Track aircraftTrack) {
        AircraftState state = aircraftTrack.getLatest();
        long startTime = state.getTime();
        GeographicCoordinate position = state.getPosition();
        Vector3 cartesianPosition = position.getCartesian();
        SphericalVelocity velocity = state.getVelocity();


        Vector3 angularVelocity = velocity.getCartesianAngularVelocity(position);
        double dangle = angularVelocity.length();
        Vector3 angleAxis = angularVelocity.normalize();

        ArrayList<AircraftState> predictedStates = new ArrayList<AircraftState>();

        int dt = 5000;
        int totalDT = 0;
        int n = 24;

        for (int i = 0; i < n; i++)
        {
            totalDT += dt;

            Matrix3 rotation = new Matrix3().setToRotation(dangle*totalDT/1000, angleAxis);
            Vector3 cartesianPredictedPosition = rotation.transform(cartesianPosition);

            //make a linear prediction based on the current velocity
            GeographicCoordinate predictedPosition = GeographicCoordinate.fromCartesian(cartesianPredictedPosition);

            AircraftState predictedState = new AircraftState(
                    state.getAircraftID(),
                    startTime+totalDT,
                    predictedPosition,
                    velocity,
                    0);
            predictedStates.add(predictedState);

        }

        Track predictedTrack = new Track(predictedStates);
        Prediction prediction = new Prediction(state.getAircraftID(), startTime, null, predictedTrack, null);
        return prediction;
    }
}
