package com.atc.simulator.PredictionService.Engine.Algorithms;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.ArrayList;

/**
 * Created by luke on 8/06/16.
 *
 * A dummy prediction algorithm which just takes the input positions/times,
 * and passes them back out as a prediction.
 *
 */
public class PassthroughPredictionAlgorithm extends PredictionAlgorithm {
    private static final boolean enableTimer = ApplicationConfig.getInstance().getBoolean("settings.debug.algorithm-timer");

    @Override
    public Prediction makePrediction(Track aircraftTrack) {
        long start1=0, start2=0; //needed for the timer
        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }

        AircraftState state = aircraftTrack.getLatest();
        ArrayList<GeographicCoordinate> predictionPositions = new ArrayList<GeographicCoordinate>();
        //Add the current position
//        predictionPositions.add(state.getPosition());

        //Make a very simple prediction
//        double tempAlt = state.getPosition().getAltitude();
//        double tempLat = state.getPosition().getLatitude()+0.5;
//        double tempLon = state.getPosition().getLongitude()+0.5;
        //Add it to the prediction
//        predictionPositions.add(new GeographicCoordinate(tempAlt,tempLat,tempLon));
        Prediction prediction = new Prediction(state.getAircraftID(), state.getTime(), predictionPositions);
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println("PassthroughPredictionAlgorithm work time: " + (((double) diff)/1000000.0) + " ms");
        }
        return prediction;
    }
}
