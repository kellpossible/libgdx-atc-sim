package com.atc.simulator.PredictionService.Engine.Algorithms.Java;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;

/**
 * Created by luke on 6/07/16.
 */
public class JavaLinearAlgorithm extends JavaPredictionAlgorithm {
    /** @see JavaPredictionAlgorithm#makePrediction(Track) */
    @Override
    public Prediction makePrediction(Track aircraftTrack) {
        AircraftState state = aircraftTrack.getLatest();
        GeographicCoordinate position = state.getPosition();
        SphericalVelocity velocity = state.getVelocity();

        Prediction prediction = new Prediction(state.getAircraftID(), state.getTime());
    }
}
