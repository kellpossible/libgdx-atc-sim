package IntegrationTesting;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.Display.PredictionListener;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import pythagoras.d.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    /**
     * Private class that combines the Times and Positions of each data point being simulated.
     * This is for storage, and ease of use, in the HashMap, below.
     */
    private class dataPoint{
        public long timeStamp; //Time stamp of the data point
        public GeographicCoordinate pos;//Geographic Position of the data point
    }


    private ArrayBlockingQueue<Prediction> newPredictionQueue; //Queue to store predictions
    private Map<String, ArrayList<dataPoint>> actualDataValues; //HashMaps: Find planeID, then you can iterate through times and positions

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
        actualDataValues = new HashMap<String, ArrayList<dataPoint>>(); //and the nested HashMaps for the Scenario's tracks

        for(Track temp: scenario.getTracks()) //For every track in the scenario:
        {
            ArrayList<dataPoint> tempList = new ArrayList<dataPoint>(); //create a temporary list
            for(int stateNum = 0; stateNum < temp.size(); stateNum++)   //and loop through all the supplied AircraftStates
            {
                dataPoint tempData = new dataPoint();
                tempData.pos = temp.get(stateNum).getPosition();
                tempData.timeStamp = temp.get(stateNum).getTime();
                tempList.add(tempData);
            }
            actualDataValues.put(temp.get(0).getAircraftID(), tempList); //And store that temporary Map with the PlaneID as a Key
        }
        System.out.println("We have " + actualDataValues.size() + " states to test Accuracy Testing");
        for(String name: actualDataValues.keySet())
        {
            System.out.println("Respectively " + actualDataValues.get(name).size() + " positions to test");
        }
        /* Uncomment to print out all time stamps stored in the map
        System.out.println("------------------------------------------");
        for(String name: actualDataValues.keySet())
        {
            for(long time : actualDataValues.get(name).keySet())
            {
                System.out.println(time);
            }
        }
        System.out.println("------------------------------------------");
        */
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
     *   3a. If prediction time stamps match data, compare
     *   3b.  else, linear interpolate to find corresponding data point
     *   4. **Some calculations in here?
     *   5. **Store in a document?
     */
    public void run()
    {
        while(continueThread)
        {
            //See if new predictions have been made
            Prediction predictionUnderTest = newPredictionQueue.poll();
            if(predictionUnderTest != null)
            {
                //Collect and print the PlaneID
                String planeID = predictionUnderTest.getAircraftID();
                System.out.print(planeID);
                //Remove the list of predictions
                ArrayList<AircraftState> predictionStates = predictionUnderTest.getAircraftStates();

                //if the actual data contains the plane (otherwise we've had something go wrong here)
                if(actualDataValues.containsKey(planeID))
                {
                    //For now: Loop over the first 5 predictions
                    for (int i = 0; i < 5; i++)
                    {
                        GeographicCoordinate actualCoord = null;

                        long predTime = predictionStates.get(i).getTime();
                        System.out.print(", "+predTime+": ");
                        for(int j=0; j<actualDataValues.get(planeID).size(); j++)
                        {
                           //If we have a matching timeStamp, store the coordinate
                            if(predTime == actualDataValues.get(planeID).get(j).timeStamp)
                            {
                                actualCoord = actualDataValues.get(planeID).get(j).pos;
                                break;
                            }
                            //If we've passed the moment, find the linear interpolation of the last two positions
                            //todo :  Make this a heap neater.. I feel I'm making do too much work here :/
                            else if(predTime < actualDataValues.get(planeID).get(j).timeStamp)
                            {
                                double interpolant = (double)(predTime - actualDataValues.get(planeID).get(j-1).timeStamp)
                                        / (double)(actualDataValues.get(planeID).get(j).timeStamp-actualDataValues.get(planeID).get(j-1).timeStamp);
                                actualCoord = new GeographicCoordinate(
                                        actualDataValues.get(planeID).get(j-1).pos.getCartesian().lerp(
                                                predictionStates.get(i).getPosition().getCartesian(),interpolant));
                                break;
                                //Vector3
                            }
                        }
                        if(actualCoord != null)
                        {
                            System.out.print( Math.abs(Math.abs(actualCoord.getLatitude())-Math.abs(predictionStates.get(i).getPosition().getLatitude())));
                        }
                    }
                }
                System.out.println();
            }
            else{try{Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}}
       }

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
