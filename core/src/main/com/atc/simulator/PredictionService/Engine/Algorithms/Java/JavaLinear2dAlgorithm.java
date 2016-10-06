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
 *
 * @author Luke Frisken
 */
public class JavaLinear2dAlgorithm extends JavaPredictionAlgorithm {
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
        Vector3 position = projection.transformPositionTo(geographicPosition);

        SphericalVelocity sphericalVelocity = state.getVelocity();

        Vector3 velocity = projection.tranformVelocityTo(sphericalVelocity, geographicPosition, position);



        ArrayList<AircraftState> predictedStates = new ArrayList<AircraftState>();

        int dt = 5000;
        int totalDT = 0;
        int n = 24;

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

        Track predictionTrack = new Track(predictedStates);
        Prediction prediction = new Prediction(
                state.getAircraftID(),
                startTime,
                state,
                predictionTrack,
                predictionTrack,
                predictionTrack,
                Prediction.State.STRAIGHT);
        return prediction;
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
}
