package com.atc.simulator.PredictionService.Engine.Algorithms;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.ArrayList;

/**
 * Created by luke on 8/06/16.
 */
public class PassthroughPredictionAlgorithm extends PredictionAlgorithm {
    @Override
    public Prediction makePrediction(Track aircraftTrack) {
        AircraftState state = aircraftTrack.getLatest();
        ArrayList<GeographicCoordinate> predictionPositions = new ArrayList<GeographicCoordinate>();
        //Add the current position
        predictionPositions.add(state.getPosition());

        //Make a very simple prediction
        double tempAlt = state.getPosition().getAltitude();
        double tempLat = state.getPosition().getLatitude()+0.5;
        double tempLon = state.getPosition().getLongitude()+0.5;
        //Add it to the prediction
        predictionPositions.add(new GeographicCoordinate(tempAlt,tempLat,tempLon));
        return new Prediction(state.getAircraftID(), state.getTime(), predictionPositions);
    }
}
