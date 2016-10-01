package IntegrationTesting;

import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.Display.PredictionListener;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Integration Testing - Accuracy
 *
 * Listens in on the DebugDataFeed and PredictionEngine to receive updates, and then compares the predicted future states with
 * the actual values in order to analyse the accuracy and pros/cons of the implemented Prediction Algorithms.
 * Stores results in a .txt file with filenames the date/time the current simulation was run.
 * (All results files are stored in our project's workspace, in a Libgdx_ATC_Simulator/Results/Accuracy/ directory
 *
 * Modified by Chris on 10/09/2016.
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

    private static final int predictionsToCompare = 24; //Number of points from the prediction to store
    private static final String threadName = "IntAccuracyTest";
    private Thread thread;
    private Boolean continueThread = true;

    private File resultsFile; //File that will store results
    private PrintStream resultsWriter;//Writer for the file
    /**
     * Constructor
     * Instantiate the Prediction Receiving queue and the HashMap for the Scenario's tracks.
     * Then takes the supplied scenario and removes the tracks, and stores them under each PlaneID
     *
     * Once this is complete, generate a new results file using the current time (MM-dd HH-mm-ss) and have that ready to be written
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

        //Create a new file for results with the date/time as filename
        String fileName = "Results/Accuracy/" + new SimpleDateFormat("MM-dd HH-mm-ss").format(new Date())+".txt";
        resultsFile = new File(fileName);
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
     * Threaded function, waits for a new prediction to be fed in and then:
     *  1. Stores the Plane's ID
     *  2. Removes and loops through all the states making up the prediction
     *  3. Finds the matching, or bordering, states from the Scenario data via timeStamps
     *  4a. If matching times, uses the actual data position
     *  4b. otherwise, it will interpolate a position between the two bordering positions
     *  5. Using Greater-Circle Arc Distance, finds the distance between prediction and actual data points
     *  6. Writes the planeID, times and distance errors in a file
     */
    public void run()
    {
        while(continueThread)
        {
            String singleTestString;
            //Step1: See if there are new predictions to test
            Prediction predictionUnderTest = newPredictionQueue.poll();
            if(predictionUnderTest != null)
            {

                singleTestString = "";
                //Store PlaneID for use in the HashMap
                String planeID = predictionUnderTest.getAircraftID();
                singleTestString += planeID +", ";

                //Remove the list of predictions
                ArrayList<AircraftState> predictionStates = predictionUnderTest.getCentreTrack();

                //Check that our Scenario contains this Plane (sanity, if this fails we have messed up)
                if(actualDataValues.containsKey(planeID))
                {
                    //todo: this just loops the first 5, our algorithm can change this
                    int numToCompare = Math.min(predictionsToCompare, predictionStates.size());
                    for (int i = 0; i < numToCompare; i++)
                    {
                        GeographicCoordinate actualCoord = null;
                        //Get the timeStamp for the prediction we're testing
                        long predTime = predictionStates.get(i).getTime();
                        if(i == 0)
                            singleTestString += predTime + ", ";

                        for(int j=0; j<actualDataValues.get(planeID).size(); j++)
                        {
                           //If we have a matching timeStamp, store the coordinate
                            if(predTime == actualDataValues.get(planeID).get(j).timeStamp)
                            {
                                actualCoord = actualDataValues.get(planeID).get(j).pos;
                                break;
                            }
                            //If we've passed the moment, find the linear interpolation of the last two positions
                            else if(predTime < actualDataValues.get(planeID).get(j).timeStamp)
                            {
                                //Interpolant: how far (in %) the prediction timeStamp is between the two closest actual data times
                                float interpolant = Math.abs((predTime - actualDataValues.get(planeID).get(j-1).timeStamp))
                                        / Math.abs((actualDataValues.get(planeID).get(j).timeStamp-actualDataValues.get(planeID).get(j-1).timeStamp));
                                //Obtain the interpolated 'actual' coordinate and break out of the loop
//                                actualCoord = new GeographicCoordinate(actualDataValues.get(planeID).get(j-1).pos.linearIntepolate
//                                        (actualDataValues.get(planeID).get(j).pos, interpolant));
                                actualCoord = new GeographicCoordinate(actualDataValues.get(planeID).get(j-1).pos.lerp(actualDataValues.get(planeID).get(j).pos, interpolant));
                                break;
                            }
                        }
                        if(actualCoord != null)
                        {
                            //Store the distance between our predicted and the interpolated 'actual' positions
                            double distance = Math.abs(actualCoord.arcDistance(predictionStates.get(i).getPosition()));
                            singleTestString += distance+", ";
                        }

                    }

                }
                //And write the newly formed string to our File
                try{resultsWriter = new PrintStream(new FileOutputStream(resultsFile,true));
                        resultsWriter.println(singleTestString);
                        resultsWriter.close();}catch(IOException e){System.err.println("Error printing to FILE, Accuracy Test");}
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
