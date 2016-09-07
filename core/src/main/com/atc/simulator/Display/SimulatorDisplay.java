package com.atc.simulator.Display;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.DebugDataFeed.DataPlaybackListener;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.Display.DisplayData.DisplayAircraft;
import com.atc.simulator.Display.DisplayData.DisplayPrediction;
import com.atc.simulator.Display.DisplayData.ModelInstanceProviders.TracksModel;
import com.atc.simulator.Display.DisplayData.ModelInstanceProviders.WorldMapModel;
import com.atc.simulator.flightdata.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * The DisplayModel for the Simulator
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
    private ModelBatch modelBatch;
    private SpriteBatch spriteBatch;
    private Environment environment;
    private MyCameraController camController;
    private AssetManager assets;
    private Scenario scenario;
    private ArrayBlockingQueue<SystemState> systemStateUpdateQueue;
    private ArrayBlockingQueue<Prediction> predictionUpdateQueue;

    private SystemStateDatabase stateDatabase;
    private LayerManager layerManager;
    private WorldMapModel map;
    private RenderLayer mapLayer;
    private TracksModel tracks;
    private RenderLayer tracksLayer;
    private AircraftDatabase aircraftDatabase;
    private RenderLayer aircraftLayer;
    private RenderLayer predictionLayer;

    Vector2 textPosition;

    private SystemState currentSystemState = null;


    /**
     * private class for storing DisplayAircraft associated with the SystemStateDatabase,
     * and to keep them in sync with the systemstatedatabase.
     *
     * May have been better to subclass SystemStateDatabase...
     *
     * @author Luke Frisken
     * Created on 6/09/16
     */
    private class AircraftDatabase extends HashMap<String, DisplayAircraft> implements SystemStateDatabaseListener
    {
        /**
         * This method is called by the SystemStateDataBase on its listeners
         * whenever the SystemStateDatabase receives updated information.
         *
         * @param stateDatabase
         * @param aircraftIDs   of type ArrayList<String>
         */
        @Override
        public void onSystemStateUpdate(SystemStateDatabase stateDatabase, ArrayList<String> aircraftIDs) {

        }

        @Override
        public void onNewAircraft(SystemStateDatabase stateDatabase, String aircraftID) {
            DisplayAircraft newAircraft = new DisplayAircraft(stateDatabase.getTrack(aircraftID));
            aircraftLayer.addDisplayRenderableProvider(newAircraft);
            this.put(aircraftID, newAircraft);
        }

        @Override
        public void onRemoveAircraft(SystemStateDatabase stateDatabase, String aircraftID) {
            this.get(aircraftID).dispose();
            this.remove(aircraftID);
        }

        @Override
        public void onUpdateAircraft(SystemStateDatabase stateDatabase, String aircraftID) {
            this.get(aircraftID).update();
        }
    }

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
        layerManager = new LayerManager();
        stateDatabase = new SystemStateDatabase();
        aircraftDatabase = new AircraftDatabase();
        stateDatabase.addListener(aircraftDatabase);

    }



    /**
     * This method gets called when there is a system update, and gets
     * passed the new system state
     * @param systemState the updated system state
     */
    @Override
    public void onSystemUpdate(SystemState systemState) {
        systemStateUpdateQueue.add(systemState);
    }

    /**
     * When a client receives new information, it will call this method to notify listeners
     * of a new data
     * @param newPrediction the new prediction information
     */
    @Override
    public void onPredictionUpdate(Prediction newPrediction) {
        predictionUpdateQueue.add(newPrediction);
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
     * Method create,
     * put any initialization stuff in here that relies on the opengl context
     * being available.
     */
    @Override
	public void create () {
	    textPosition = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		assets = new AssetManager();
//		assets.load("flight_data/CallibrateMap/CallibrateMap.csv", Track.class);
//        assets.load("assets/models/planet.g3db", DisplayModel.class);
		assets.finishLoading();

        spriteBatch = new SpriteBatch();

        //load the bitmap font
//        font = new BitmapFont(new FileHandle("assets/fonts/DejaVu_Sans_Mono_12.fnt"));

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
//        earthTextureModel = assets.get("assets/models/planet.g3db", DisplayModel.class);


//		earthTextureInstance = new ModelInstance(earthTextureModel);
//        earthTextureInstance.transform.setToScaling(-1f,1f,1f);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 10f, 10f, 10f, 1f));
        //environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camController = new MyCameraController(cam);
        Gdx.input.setInputProcessor(camController);


        //Set up all the RenderLayers
        mapLayer = new RenderLayer(10, "map");
        layerManager.addRenderLayer(mapLayer);
        map = new WorldMapModel();
        mapLayer.addDisplayRenderableProvider(map);

        if (showTracks)
        {
            tracksLayer = new RenderLayer(9, "tracks");
            layerManager.addRenderLayer(tracksLayer);
            tracks = new TracksModel(scenario);
            tracksLayer.addDisplayRenderableProvider(tracks);
        }

        predictionLayer = new RenderLayer(8, "predictions");
        layerManager.addRenderLayer(predictionLayer);

        aircraftLayer = new RenderLayer(7, "aircraft");
        layerManager.addRenderLayer(aircraftLayer);
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

            DisplayAircraft aircraft = aircraftDatabase.get(aircraftID);

            if (aircraft != null)
            {
                DisplayPrediction displayPrediction = aircraft.getPrediction();
                if (displayPrediction != null)
                {
                    displayPrediction.dispose();
                }
                displayPrediction = new DisplayPrediction(aircraft, prediction);
                predictionLayer.addDisplayRenderableProvider(displayPrediction);
                aircraft.setPrediction(displayPrediction);
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
            stateDatabase.systemStateUpdate(systemState);
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

        pollSystemUpdateQueue();
        pollPredictionUpdateQueue();

        //Render all the instances in the layerManager.
        Collection<ModelInstance> instances = layerManager.getRenderInstances();
        for(ModelInstance instance : instances)
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
