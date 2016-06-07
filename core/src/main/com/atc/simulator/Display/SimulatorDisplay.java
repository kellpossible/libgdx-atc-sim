package com.atc.simulator.Display;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.DebugDataFeed.DataPlaybackListener;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.*;
import com.atc.simulator.navdata.Countries;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class SimulatorDisplay extends ApplicationAdapter implements DataPlaybackListener, PredictionListener {
    private SpriteBatch batch;
	private PerspectiveCamera cam;
    private Model earthTextureModel;
    private ModelInstance earthTextureInstance;
    private ModelBatch modelBatch;
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

    private Model countriesModel;
    private ModelInstance countriesModelInstance;


    public SimulatorDisplay(Scenario scenario)
    {
        this.scenario = scenario;
        systemStateUpdateQueue = new ArrayBlockingQueue<SystemState>(100);
        predictionUpdateQueue = new ArrayBlockingQueue<Prediction>(300);
        aircraftStateModels = new HashMap<String, Model>();
        aircraftStateModelInstances = new HashMap<String, ModelInstance>();
        aircraftStateVelocityModels = new HashMap<String, Model>();
        aircraftStateVelocityModelInstances = new HashMap<String, ModelInstance>();
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
    @Override
    public void onPredictionUpdate(Prediction prediction) {
        predictionUpdateQueue.add(prediction);
        ApplicationConfig.debugPrint("print-display", "Holy shit, the display got data from the Engine!");
    }


    private class MyCameraController extends CameraInputController {

		public MyCameraController(Camera camera) {
			super(camera);
            this.pinchZoomFactor = 20f;
			zoom(0f);
		}

		@Override
		public boolean zoom(float amount)
		{
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

    private void generateCountriesModel()
    {
        Countries countries = new Countries("assets/maps/countries.geo.json");
        countriesModel = countries.getModel();
        countriesModelInstance = new ModelInstance(countriesModel);
    }

	@Override
	public void create () {
		assets = new AssetManager();
//		assets.load("flight_data/CallibrateMap/CallibrateMap.csv", Track.class);
//        assets.load("assets/models/planet.g3db", Model.class);
		assets.finishLoading();

        generateCountriesModel();
        generateTracksModel();





        modelBatch = new ModelBatch();
		batch = new SpriteBatch();

		cam = new PerspectiveCamera(40, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		cam.position.set(0f, 0f, 0f);
//		Vector3 firstPos = track.get(0).getPosition().getCartesianDrawVector();
//		cam.lookAt(firstPos.x, firstPos.y, firstPos.z);
		cam.lookAt(1, 0, 0);
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

    private void pollPredictionUpdateQueue()
    {
        Prediction prediction = predictionUpdateQueue.poll();

        while (prediction != null)
        {

            prediction = predictionUpdateQueue.poll();
        }
    }

    //todo: to improve draw performance this should go into the callers thread, and out of the display thread. Only push models into the queue instead.
    private void pollSystemUpdateQueue()
    {
        SystemState systemState = systemStateUpdateQueue.poll();

        while (systemState != null)
        {
//            System.out.println("System Update");
            ArrayList<String> aircraftIDs = new ArrayList<String>(aircraftStateModels.keySet());
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

                String aircraftID = aircraftState.getAircraftID();
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

        for (ModelInstance instance: aircraftStateModelInstances.values()){
            modelBatch.render(instance);
        }

        for (ModelInstance instance: aircraftStateVelocityModelInstances.values()){
            modelBatch.render(instance);
        }

        modelBatch.render(tracksModelInstance);

		modelBatch.end();
	}

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

    @Override
    public void resize(int width, int height)
    {
        float aspectRatio = (float) width / (float) height;
        cam.viewportWidth = 2f * aspectRatio;
        cam.viewportHeight = 2f;
        cam.update();

    }
}
