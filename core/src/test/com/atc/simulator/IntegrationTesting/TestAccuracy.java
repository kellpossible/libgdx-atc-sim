package com.atc.simulator.IntegrationTesting;

import com.atc.simulator.DebugDataFeed.DataPlaybackListener;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.Display.PredictionListener;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Integration Testing - Accuracy
 *
 * Listens in on the DebugDataFeed and PredictionEngine to receive updates, and then compares the predicted future states with
 * the actual values in order to analyse the accuracy and pros/cons of the implemented Prediction Algorithms
 *
 * Created by Chris on 31/08/2016.
 */
public class TestAccuracy implements DataPlaybackListener, PredictionListener {
    private ArrayBlockingQueue<Prediction> newPredictionQueue; //Queue to store predictions
    private HashMap<String, HashMap<Long, GeographicCoordinate>> actualDataValues; //Nested HashMaps: Find planeID, then Time, get position as GeoCoord

    /**
     * Constructor
     * Instantiate the Prediction Receiving queue and the HashMap for the Scenario's tracks.
     * Then takes the supplied scenario and removes plane ID's/Times/Coordinates to fill in the HashMap
     *
     * @param scenario the scenario being run and tested on
     */
    public TestAccuracy(Scenario scenario)
    {
        newPredictionQueue = new ArrayBlockingQueue<Prediction>(400); //Create a queue to store predictions
        actualDataValues = new HashMap<String, HashMap<Long, GeographicCoordinate>>(); //and the nested HashMaps for the Scenario's tracks

        for(Track temp: scenario.getTracks()) //For every track in the scenario:
        {
            HashMap<Long, GeographicCoordinate> tempMap = new HashMap<Long, GeographicCoordinate>(); //create a temporary HashMap (internal)
            for(int stateNum = 0; stateNum < temp.size(); stateNum++)   //and loop through all the supplied AircraftStates
                tempMap.put(temp.get(stateNum).getTime(), temp.get(stateNum).getPosition());//storing the times and Coordinates

            actualDataValues.put(temp.get(0).getAircraftID(), tempMap); //And store that temporary Map with the PlaneID as a Key
        }
    }

    /**
     * This method gets called when a new prediction is received by the {@link PredictionFeedClientThread}
     * @param prediction The newly generated prediction
     */
    @Override
    public void onPredictionUpdate(Prediction prediction) {
        newPredictionQueue.add(prediction);
    }


}
