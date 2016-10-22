package integration_testing;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.debug_data_feed.scenarios.Scenario;
import com.atc.simulator.display.PredictionFeedClientThread;
import com.atc.simulator.display.PredictionListener;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Prediction;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.GnomonicProjection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import pythagoras.d.Vector3;

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
 * Listens in on the debug_data_feed and PredictionEngine to receive updates, and then compares the predicted future states with
 * the actual values in order to analyse the accuracy and pros/cons of the implemented Prediction Algorithms.
 * Stores results in a .txt file with filenames the date/time the current simulation was run.
 * (All results files are stored in our project's workspace, in a Libgdx_ATC_Simulator/Results/Accuracy/ directory
 *
 * @author Chris Coleman
 * @author Luke Frisken
 */
public class TestAccuracy implements PredictionListener, RunnableThread {
    public static final boolean SAVE_JSON = ApplicationConfig.getBoolean("settings.testing.save-json");
    public static final boolean SAVE_CSV = ApplicationConfig.getBoolean("settings.testing.save-csv");
    private GnomonicProjection projection;
    private JsonObject jsonAccuracyTest;
    private JsonArray jsonPredictions;
    private String csvFileName;
    private String jsonFileName;
    private boolean jsonFirstWrite = true;
    /**
     * Private class that combines the Times and Positions of each data point being simulated.
     * This is for storage, and ease of use, in the HashMap, below.
     */
    private class dataPoint{
        public long timeStamp; //Time stamp of the data point
        public GeographicCoordinate pos;//Geographic Position of the data point
    }

    private ArrayBlockingQueue<Prediction> newPredictionQueue; //Queue to store jsonPredictions
    private Map<String, ArrayList<dataPoint>> actualDataValues; //HashMaps: Find planeID, then you can iterate through times and positions

    private static final int predictionsToCompare = 24; //Number of points from the prediction to store
    private static final String threadName = "IntAccuracyTest";
    private Thread thread;
    private Boolean continueThread = true;

    private File csvResultsFile; //File that will store results
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
        jsonAccuracyTest = new JsonObject();
        jsonPredictions = new JsonArray();
        jsonAccuracyTest.add("jsonPredictions", jsonPredictions);

        projection = new GnomonicProjection(scenario.getProjectionReference()); // projection for x y coordinates
        newPredictionQueue = new ArrayBlockingQueue<Prediction>(400); //Create a queue to store jsonPredictions
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

        String algorithmName = ApplicationConfig.getString("settings.prediction-service.prediction-engine.algorithm-type");
        String baseFileName = "./Results/Accuracy/" + algorithmName + "_" + new SimpleDateFormat("MM-dd_HH-mm-ss").format(new Date());

        //Create a new file for results with the date/time as filename
        csvFileName = baseFileName + ".csv";
        csvResultsFile = new File(csvFileName);
        jsonFileName = baseFileName + ".json";
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
            //Step1: See if there are new jsonPredictions to test
            Prediction predictionUnderTest = newPredictionQueue.poll();
            if(predictionUnderTest != null)
            {
                JsonObject jsonPrediction = new JsonObject();
                jsonPrediction.addProperty("plane-id", predictionUnderTest.getAircraftID());
                jsonPrediction.addProperty("state", predictionUnderTest.getPredictionState().toString());
                jsonPrediction.addProperty("time", predictionUnderTest.getAircraftState().getTime());

                singleTestString = "";
                //Store PlaneID for use in the HashMap
                String planeID = predictionUnderTest.getAircraftID();
                singleTestString += planeID +", ";
                singleTestString += predictionUnderTest.getPredictionState().toString() + ", ";

                //time that the prediction was made
                singleTestString += predictionUnderTest.getPredictionTime() + ", ";

                //x and y coordinates of the aircraft at the time the prediction was made
                GeographicCoordinate currentPosition = predictionUnderTest.getAircraftState().getPosition();
                Vector3 pos = projection.transformPositionTo(currentPosition);
                singleTestString += pos.x + ", ";
                singleTestString += pos.y + ", ";

                JsonArray jsonCurrentPosition = new JsonArray();
                jsonCurrentPosition.add(pos.x);
                jsonCurrentPosition.add(pos.y);
                jsonPrediction.add("current-position", jsonCurrentPosition);

                JsonArray jsonPredictionTrack = new JsonArray();

                //Remove the list of jsonPredictions
                ArrayList<AircraftState> predictionStates = predictionUnderTest.getCentreTrack();

                //Check that our Scenario contains this Plane (sanity, if this fails we have messed up)
                if(actualDataValues.containsKey(planeID))
                {
                    //todo: this just loops the first 5, our algorithm can change this
                    int numToCompare = Math.min(predictionsToCompare, predictionStates.size());
                    for (int i = 0; i < numToCompare; i++)
                    {
                        JsonObject jsonPredictionItem = new JsonObject();

                        GeographicCoordinate actualCoord = null;
                        //Get the timeStamp for the prediction we're testing
                        long predTime = predictionStates.get(i).getTime();
                        if(i == 0)
                            singleTestString += predTime + ", ";

                        jsonPredictionItem.addProperty("time", predTime);
                        JsonArray jsonPredictionItemPos = new JsonArray();

                        Vector3 itemPos = projection.transformPositionTo(predictionStates.get(i).getPosition());
                        jsonPredictionItemPos.add(itemPos.x);
                        jsonPredictionItemPos.add(itemPos.y);

                        jsonPredictionItem.add("position", jsonPredictionItemPos);

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
                                actualCoord = new GeographicCoordinate(actualDataValues.get(planeID).get(j-1).pos.linearIntepolate(actualDataValues.get(planeID).get(j).pos, interpolant));
                                break;
                            }
                        }
                        if(actualCoord != null)
                        {
                            //Store the distance between our predicted and the interpolated 'actual' positions
                            actualCoord.setAltitude(0); //take altitude out of the equation
                            Vector3 cartesianActualCoord = actualCoord.getCartesian();
                            GeographicCoordinate predictionCoord = predictionStates.get(i).getPosition();
                            predictionCoord.setAltitude(0); //take altitude out of the equation
                            Vector3 cartesianPredictionCoord = predictionCoord.getCartesian();
                            double distance = cartesianActualCoord.distance(cartesianPredictionCoord);

                            if (distance == Double.NaN)
                            {
                                System.err.println("distance is NaN");
                                continue;
                            }
//                            distance = Math.abs(actualCoord.arcDistance(predictionStates.get(i).getPosition()));
                            singleTestString += distance+", ";
                            jsonPredictionItem.addProperty("error-distance", distance);
                            jsonPredictionTrack.add(jsonPredictionItem);
                        }



                    }

                } else {
                    System.err.println("Scenario does not contain this plane: " + planeID);
                }

                jsonPrediction.add("prediction-track", jsonPredictionTrack);

                jsonPredictions.add(jsonPrediction);

                if (SAVE_CSV) {
                    //And write the newly formed string to our File
                    try{resultsWriter = new PrintStream(new FileOutputStream(csvResultsFile,true));
                        resultsWriter.println(singleTestString);
                        resultsWriter.close();}catch(IOException e){System.err.println("Error printing to FILE, Accuracy Test");}
                }

                if (SAVE_JSON) {
                    try {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String outString = gson.toJson(jsonPrediction);
                        RandomAccessFile out = new RandomAccessFile(jsonFileName, "rw");
                        long length  = out.length();
                        if (jsonFirstWrite) {
                            out.writeBytes("[\n");
                            jsonFirstWrite = false;
                        } else {
                            //seek back to overwrite the old "]" close bracket and newline
                            //with a comma and a newline
                            out.seek(length-2);
                            out.writeBytes(",\n");
                        }
                        out.writeBytes(outString);
                        out.writeBytes("\n]");
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

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
