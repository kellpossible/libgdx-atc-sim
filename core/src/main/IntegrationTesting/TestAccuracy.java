package IntegrationTesting;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.Display.PredictionListener;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.io.PrintWriter;
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
public class TestAccuracy implements PredictionListener, RunnableThread {
    private static final boolean enableDebugPrint = ApplicationConfig.getInstance().getBoolean("settings.debug.print-predictionfeedserver");

    private ArrayBlockingQueue<Prediction> newPredictionQueue; //Queue to store predictions
    private HashMap<String, HashMap<Long, GeographicCoordinate>> actualDataValues; //Nested HashMaps: Find planeID, then Time, get position as GeoCoord

    private static final String threadName = "IntAccuracyTest";
    private Thread thread;
    private Boolean continueThread = true;

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
        System.out.println("We have " + actualDataValues.size() + " aircraft for the Accuracy Testing");
    }

    /**
     * This method gets called when a new prediction is received by the {@link PredictionFeedClientThread}
     * @param prediction The newly generated prediction
     */
    @Override
    public void onPredictionUpdate(Prediction prediction) {
        newPredictionQueue.add(prediction);
    }

    /****************************Thread Related activities************************/
    /**
     * If this thread has no started yet, start one up and raise the flag to keep looping
     */
    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

    /**
     * Threaded Loop function
     *   1. Wait for a prediction to be present in the Queue
     *   2. Remove it and store the PlaneID
     *   3. For each prediction step's coordinate, compare to the stored Track data
     *   4. **Some calculations in here?
     *   5. **Store in a document?
     */
    public void run()
    {
        while(continueThread)
        {try{
            Prediction predictionUnderTest = newPredictionQueue.take();
            String planeID = predictionUnderTest.getAircraftID();
            ArrayList<AircraftState> predictionStates = predictionUnderTest.getAircraftStates();
            System.out.print(planeID + ",");
            for(int i = 0; i < Math.min(predictionStates.size(),5); i++)
            {
                //PredictedLat - ActualLat
                System.out.print(predictionStates.get(i).getPosition().getLatitude()- actualDataValues.get(planeID).get(predictionStates.get(i).getTime()).getLatitude());
                System.out.print(",");
            }
            System.out.println();

        }catch (InterruptedException e){e.printStackTrace();}}

    }

    /**
     * Small method called too kill the server's threads when the have run through
     */
    public void kill()
    {
        continueThread = false;
    }

    /**
     * Join this thread.
     */
    @Override
    public void join() throws InterruptedException {
        thread.join();
    }



}
