package com.atc.simulator.Display;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.DebugDataFeed.DataPlaybackListener;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.*;
import com.atc.simulator.navdata.Countries;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * The Display for the Simulator
 *
 *  - Uses LibGDX
 *  - Is a bit of a hack
 *  - might need redesigning
 *
 * @author Luke Frisken
 */
public class SimulatorDisplay extends ApplicationAdapter implements DataPlaybackListener, PredictionListener {
    private static final boolean enableDebugPrint = ApplicationConfig.getInstance().getBoolean("settings.debug.print-display");
    private static final boolean showTracks = ApplicationConfig.getInstance().getBoolean("settings.display.show-tracks");

	private PerspectiveCamera cam;
    private Model earthTextureModel;
    private ModelInstance earthTextureInstance;
    private ModelBatch modelBatch;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private Environment environment;
    private MyCameraController camController;
    private AssetManager assets;
    private Model tracksModel;
    private ModelInstance tracksModelInstance;
    private Scenario scenario;
    private ArrayBlockingQueue<SystemState> systemStateUpdateQueue;
    private ArrayBlockingQueue<Prediction> predictionUpdateQueue;

    private HashMap<String, Model> aircraftStateModels = null;
    private HashMap<String, ModelInstance> aircraftStateModelInstances = null;
    private HashMap<String, Model> aircraftStateVelocityModels = null;
    private HashMap<String, ModelInstance> aircraftStateVelocityModelInstances = null;
    private HashMap<String, Model> aircraftPredictionModels = null;
    private HashMap<String, ModelInstance> aircraftPredictionModelInstances = null;
    private HashMap<String, Prediction> predictions = null;

    Vector2 textPosition;

    private Model countriesModel;
    private ModelInstance countriesModelInstance;

    private SystemState currentSystemState = null;


    /**
     * Constructor for the SimulatorDisplay
     *
     * @param scenario the scenario going to be displayed.
     */
    public SimulatorDisplay(Scenario scenario)
    {
        this.scenario = scenario;
        systemStateUpdateQueue = new ArrayBlockingQueue<SystemState>(100);
        predictionUpdateQueue = new ArrayBlockingQueue<Prediction>(300);
        aircraftStateModels = new HashMap<String, Model>();
        aircraftStateModelInstances = new HashMap<String, ModelInstance>();
        aircraftStateVelocityModels = new HashMap<String, Model>();
        aircraftStateVelocityModelInstances = new HashMap<String, ModelInstance>();
        aircraftPredictionModels = new HashMap<String, Model>();
        aircraftPredictionModelInstances = new HashMap<String, ModelInstance>();
        predictions = new HashMap<String, Prediction>();
    }



    /**
     * This method gets called when there is a system update, and gets
     * passed the new system state
     *
     * @param systemState the updated system state
     */
    @Override
    public void onSystemUpdate(SystemState systemState) {
        systemStateUpdateQueue.add(systemState);
    }

    /**
     * This method gets called when a new prediction is received by the {@link PredictionFeedClientThread}
     * @param prediction
     */
    @Override
    public void onPredictionUpdate(Prediction prediction) {
        predictionUpdateQueue.add(prediction);
    }


    /**
     * Private camera controller class to allow us to zoom and pan the map.
     */
    private class MyCameraController extends CameraInputController {

		public MyCameraController(Camera camera) {
			super(camera);
            this.pinchZoomFactor = 20f;
			zoom(0f);
		}

		@Override
		public boolean zoom(float amount)
		{
		    //some magic numbers for zooming and panning.
			float newFieldOfView = cam.fieldOfView - (amount * (cam.fieldOfView/15f));
			if (newFieldOfView < 0.01 || newFieldOfView > 179)
			{
				return false;
			}

			cam.fieldOfView = newFieldOfView;
			cam.update();

			this.rotateAngle = cam.fieldOfView;
			return true;
		}

		@Override
		protected boolean process (float deltaX, float deltaY, int button) {
			return super.process(-deltaX*1.8f, -deltaY, button);
		}
	}

    /**
     * Generate a model with all the tracks in a scenario in it
     * drawn as red lines.
     */
    private void generateTracksModel()
    {
        ArrayList<Track> tracks = scenario.getTracks();

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "track",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.RED);

        for (Track track : tracks)
        {
            //jump, just in case we want to skip some elements (it was having trouble drawing the entire track)
            //for performance reasons.
            int jump = 1;
            Vector3 previousPositionDrawVector = track.get(0).getPosition().getModelDrawVector();
            for(int i = jump; i < track.size(); i+=jump)
            {
                AircraftState state = track.get(i);
                Vector3 positionDrawVector = state.getPosition().getModelDrawVector();
                builder.line(previousPositionDrawVector, positionDrawVector);
                previousPositionDrawVector = positionDrawVector;
            }
        }

        tracksModel = modelBuilder.end();
        tracksModelInstance = new ModelInstance(tracksModel);
    }

    /**
     * Generate the vector lines model of the country borders.
     */
    private void generateCountriesModel()
    {
        Countries countries = new Countries("assets/maps/countries.geo.json");
        countriesModel = countries.getModel();
        countriesModelInstance = new ModelInstance(countriesModel);
    }

	@Override
	public void create () {
	    textPosition = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		assets = new AssetManager();
//		assets.load("flight_data/CallibrateMap/CallibrateMap.csv", Track.class);
//        assets.load("assets/models/planet.g3db", Model.class);
		assets.finishLoading();

        generateCountriesModel();
        generateTracksModel();

        spriteBatch = new SpriteBatch();

        //load the bitmap font
        font = new BitmapFont(new FileHandle("assets/fonts/DejaVu_Sans_Mono_12.fnt"));

        modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(40, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		cam.position.set(0f, 0f, 0f);
//		Vector3 firstPos = track.get(0).getPosition().getCartesianDrawVector();
//		cam.lookAt(firstPos.x, firstPos.y, firstPos.z);


        //make the camera look at the projection reference (as it is likely the center of whatever's going on
        // in the scenario
        Vector3 lookAt = scenario.getProjectionReference().getCartesianDrawVector();
		cam.lookAt(lookAt);
		cam.near = 0.01f;
		cam.far = 2f;
		cam.update();

//		ModelBuilder modelBuilder = new ModelBuilder();
//		earthTextureModel = modelBuilder.createSphere(5f, 5f, 5f, 64, 64,
//				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
//				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//


//        System.out.println("Finished loading");
//        earthTextureModel = assets.get("assets/models/planet.g3db", Model.class);


//		earthTextureInstance = new ModelInstance(earthTextureModel);
//        earthTextureInstance.transform.setToScaling(-1f,1f,1f);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 10f, 10f, 10f, 1f));
        //environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camController = new MyCameraController(cam);
        Gdx.input.setInputProcessor(camController);
    }

    /**
     * Process any new predictions which have been placed in the prediction update queue
     * by the {@link PredictionFeedClientThread}
     */
    private void pollPredictionUpdateQueue()
    {
        if (currentSystemState == null)
        {
            return;
        }

        Prediction prediction = predictionUpdateQueue.poll();

        //poll the predictionUpdateQueue until it is empty (returning null)
        while (prediction != null)
        {
            String aircraftID = prediction.getAircraftID();
            AircraftState aircraftState = currentSystemState.getAircraftState(aircraftID);
            if (aircraftState != null) {
                //remove/dispose the model and instance if it already exists (to create a new one)
                if (aircraftPredictionModels.containsKey(aircraftID))
                {
                    Model model = aircraftPredictionModels.remove(aircraftID);
                    model.dispose();
                }
                if (aircraftPredictionModelInstances.containsKey(aircraftID))
                {
                    ModelInstance modelInstance = aircraftPredictionModelInstances.remove(aircraftID);
                }

                ArrayList<AircraftState> states = prediction.getAircraftStates();

                ModelBuilder modelBuilder = new ModelBuilder();
                modelBuilder.begin();
                MeshPartBuilder builder = modelBuilder.part(
                        "track",
                        GL20.GL_LINES,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                        new Material());
                builder.setColor(Color.YELLOW);

                Vector3 previousPositionDrawVector = aircraftState.getPosition().getModelDrawVector();
                for(int i = 0; i < states.size(); i++)
                {
                    AircraftState state = states.get(i);
                    Vector3 positionDrawVector = state.getPosition().getModelDrawVector();
                    builder.line(previousPositionDrawVector, positionDrawVector);
                    previousPositionDrawVector = positionDrawVector;
                }

                Model model = modelBuilder.end();
                ModelInstance instance = new ModelInstance(model);
                aircraftPredictionModels.put(aircraftID, model);
                aircraftPredictionModelInstances.put(aircraftID, instance);

            }

            prediction = predictionUpdateQueue.poll();
        }
    }

    //todo: to improve draw performance this should go into the callers thread, and out of the display thread. Only push models into the queue instead.

    /**
     * process any items which have been placed in the system update queue by
     * the {@link com.atc.simulator.DebugDataFeed.DataPlaybackThread}
     */
    private void pollSystemUpdateQueue()
    {
        SystemState systemState = systemStateUpdateQueue.poll();
        currentSystemState = systemState;

        while (systemState != null)
        {
//            System.out.println("System Update");
            ArrayList<String> aircraftIDs = new ArrayList<String>(aircraftStateModels.keySet());
            //check to see whether every aircraftID of every model currently being drawn can
            //be found in the new system state, if not, then remove the model and its instance.
            for (String aircraftID : aircraftIDs)
            {
                boolean found = false;
                for (AircraftState aircraftState : systemState.getAircraftStates())
                {
                    if (aircraftID.equals(aircraftState.getAircraftID()))
                    {
                        found = true;
                    }
                }

                if (!found)
                {
                    aircraftStateModels.get(aircraftID).dispose();
                    aircraftStateModels.remove(aircraftID);
                    aircraftStateModelInstances.remove(aircraftID);

                    Model mdl = aircraftStateVelocityModels.get(aircraftID);
                    if (mdl != null) {
                        mdl.dispose();
                    }
                    aircraftStateVelocityModels.remove(aircraftID);
                    aircraftStateVelocityModelInstances.remove(aircraftID);
                }
            }

            //for every aircraft in the current system state
            for (AircraftState aircraftState : systemState.getAircraftStates())
            {
                GeographicCoordinate position = aircraftState.getPosition();
                SphericalVelocity velocity = aircraftState.getVelocity();
//                System.out.println("Position: " + position.toString());
//                System.out.println("Velocity: " + velocity);
//                System.out.println("Heading: " + Math.toDegrees(aircraftState.getHeading()));
//                System.out.println(ISO8601.fromCalendar(systemState.getTime()));

                double depthAdjustment = -0.01;
                Vector3 modelDrawVector = position.getModelDrawVector(depthAdjustment);

                Vector3 screenPosition = cam.project(new Vector3(modelDrawVector));


                String aircraftID = aircraftState.getAircraftID();
//                System.out.println("Screen Position: " + screenPosition + ", Aircraft ID: " + aircraftID);

                if (aircraftID.equals("QFA489"))
                {
                    textPosition.x = screenPosition.x;
                    textPosition.y = screenPosition.y;
                }
//
//                System.out.println(aircraftID);
//                System.out.println(position);

                ModelBuilder modelBuilder = new ModelBuilder();

                Model aircraftStateModel = aircraftStateModels.get(aircraftID);
                ModelInstance aircraftStateModelInstance;
                if (aircraftStateModel != null)
                {
                    aircraftStateModel.dispose();
                    aircraftStateModels.remove(aircraftID);
                }


//            modelBuilder.begin();
//            MeshPartBuilder builder = modelBuilder.part(
//                    "system_state",
//                    GL20.GL_POINTS,
//                    VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
//                    new Material());
//            builder.setColor(Color.GREEN);
//            builder.vertex(position.getModelDrawVector().x, position.getModelDrawVector().y, position.getModelDrawVector().z);

//            systemStateModel = modelBuilder.end();

                // create a model for the aircraft ( a little green sphere )
                aircraftStateModel = modelBuilder.createSphere(
                        0.0005f, 0.0005f, 0.0005f, 10, 10,
                        new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                aircraftStateModelInstance = new ModelInstance(aircraftStateModel);
                aircraftStateModelInstance.transform.setTranslation(modelDrawVector.x, modelDrawVector.y, modelDrawVector.z);

                aircraftStateModelInstances.put(aircraftID, aircraftStateModelInstance);
                aircraftStateModels.put(aircraftID, aircraftStateModel);

                Model aircraftStateVelocityModel = aircraftStateVelocityModels.get(aircraftID);
                ModelInstance aircraftStateVelocityModelInstance;
                if (aircraftStateVelocityModel != null)
                {
                    aircraftStateVelocityModel.dispose();
                    aircraftStateVelocityModels.remove(aircraftID);
                }

                //if the aircraft is moving
                if (velocity.length() > 0.00001)
                {
                    GeographicCoordinate velocityEndPos = new GeographicCoordinate(position.add(velocity.mult(120))); //two minute velocity vector
                    aircraftStateVelocityModel = modelBuilder.createArrow(
                            modelDrawVector,
                            velocityEndPos.getModelDrawVector(depthAdjustment),
                            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    aircraftStateVelocityModelInstance = new ModelInstance(aircraftStateVelocityModel);

                    //for some strange reason this isn't required...!
//            aircraftStateVelocityModelInstance.transform.setTranslation(modelDrawVector.x, modelDrawVector.y, modelDrawVector.z);

                    aircraftStateVelocityModels.put(aircraftID, aircraftStateVelocityModel);
                    aircraftStateVelocityModelInstances.put(aircraftID, aircraftStateVelocityModelInstance);
                } else {
                    aircraftStateVelocityModels.remove(aircraftID);
                    aircraftStateVelocityModelInstances.remove(aircraftID);
                }
            }

            systemState = systemStateUpdateQueue.poll();
        }
    }

    /**
     * The render method for the display.
     */
	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();
		modelBatch.begin(cam);
//		modelBatch.render(earthTextureInstance);
        modelBatch.render(countriesModelInstance);

        pollSystemUpdateQueue();
        pollPredictionUpdateQueue();

        //render the aircraft tracks
        if (showTracks)
        {
            modelBatch.render(tracksModelInstance);
        }

        //render the aircraft
        for (ModelInstance instance: aircraftStateModelInstances.values()){
            modelBatch.render(instance);
        }

//        for (ModelInstance instance: aircraftStateVelocityModelInstances.values()){
//            modelBatch.render(instance);
//        }

        //render the predictions
        for (ModelInstance instance: aircraftPredictionModelInstances.values())
        {
            modelBatch.render(instance);
        }

		modelBatch.end();

        spriteBatch.begin();
//        font.draw(spriteBatch, "Hello World", textPosition.x, textPosition.y);
        spriteBatch.end();
	}


    /**
     * Dispose of objects which have been allocated outside of java
     */
	@Override
	public void dispose () {
		modelBatch.dispose();
//		earthTextureModel.dispose();
        tracksModel.dispose();
        countriesModel.dispose();

        for (Model model: aircraftStateModels.values()){
            model.dispose();
        }

        for (Model model: aircraftStateVelocityModels.values()){
            model.dispose();
        }
	}

    /**
     * Allow resizing of the display window.
     * Compensate the camera for the changed window dimensions.
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height)
    {
        float aspectRatio = (float) width / (float) height;
        cam.viewportWidth = 2f * aspectRatio;
        cam.viewportHeight = 2f;
        cam.update();

    }
}
