package com.atc.simulator.Display;

import com.atc.simulator.DebugDataFeed.DataPlaybackListener;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.flightdata.*;
import com.atc.simulator.vectors.GeographicCoordinate;
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
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class SimulatorDisplay extends ApplicationAdapter implements DataPlaybackListener {
    private SpriteBatch batch;
	private PerspectiveCamera cam;
    private Model earthTextureModel;
    private ModelInstance earthTextureInstance;
    private ModelBatch modelBatch;
    private Environment environment;
    private MyCameraController camController;
    private AssetManager assets;
    private Track track;
    private Model trackModel;
    private ModelInstance trackModelInstance;
    private Scenario scenario;
    private ArrayBlockingQueue<SystemState> systemStateUpdateQueue;

    private Model systemStateModel = null;
    private ModelInstance systemStateModelInstance = null;
    private Model systemStateVelocityModel = null;
    private ModelInstance systemStateVelocityModelInstance = null;


    public SimulatorDisplay(Scenario scenario)
    {
        this.scenario = scenario;
        systemStateUpdateQueue = new ArrayBlockingQueue<SystemState>(100);
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

	@Override
	public void create () {
		assets = new AssetManager();
//		assets.load("flight_data/CallibrateMap/CallibrateMap.csv", Track.class);
        track = scenario.getTracks().get(0);

        assets.load("assets/models/planet.g3db", Model.class);
		assets.finishLoading();

//		track = assets.get("flight_data/CallibrateMap/CallibrateMap.csv", Track.class);
		trackModel = track.getModel();
        trackModelInstance = new ModelInstance(trackModel);
		//trackModelInstance.transform.setToScaling(-1f,1f,1f);



        modelBatch = new ModelBatch();
		batch = new SpriteBatch();

		cam = new PerspectiveCamera(40, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		cam.position.set(0f, 0f, 0f);
		Vector3 firstPos = track.get(0).getAircraftState().getPosition().getCartesianDrawVector();
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


        System.out.println("Finished loading");
        earthTextureModel = assets.get("assets/models/planet.g3db", Model.class);


		earthTextureInstance = new ModelInstance(earthTextureModel);
        earthTextureInstance.transform.setToScaling(-1f,1f,1f);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 10f, 10f, 10f, 1f));
        //environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camController = new MyCameraController(cam);
        Gdx.input.setInputProcessor(camController);
    }

    private void pollSystemUpdateQueue()
    {
        SystemState systemState = systemStateUpdateQueue.poll();

        if (systemState == null)
        {
            return;
        } else {
            System.out.println("System Update");
            AircraftState aircraftState = systemState.getAircraftStates().get(0);
            GeographicCoordinate position = aircraftState.getPosition();
            SphericalVelocity velocity = aircraftState.getVelocity();
            System.out.println("Position: " + position.toString());
            System.out.println("Velocity: " + velocity);
            System.out.println("Heading: " + Math.toDegrees(aircraftState.getHeading()));
            System.out.println(ISO8601.fromCalendar(systemState.getTime()));
            Vector3 modelDrawVector = position.getModelDrawVector();

            ModelBuilder modelBuilder = new ModelBuilder();

            if (systemStateModel != null )
            {
                systemStateModel.dispose();
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
            systemStateModel = modelBuilder.createSphere(
                    0.001f, 0.001f, 0.001f, 10, 10,
                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            systemStateModelInstance = new ModelInstance(systemStateModel);
            systemStateModelInstance.transform.setTranslation(modelDrawVector.x, modelDrawVector.y, modelDrawVector.z);


            GeographicCoordinate velocityEndPos = new GeographicCoordinate(position.add(velocity.mult(120))); //two minute velocity vector
            systemStateVelocityModel = modelBuilder.createArrow(
                    modelDrawVector,
                    velocityEndPos.getModelDrawVector(),
                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            systemStateVelocityModelInstance = new ModelInstance(systemStateVelocityModel);
            systemStateModelInstance.transform.setTranslation(modelDrawVector.x, modelDrawVector.y, modelDrawVector.z);
        }
    }

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();
		modelBatch.begin(cam);
		modelBatch.render(earthTextureInstance);
        modelBatch.render(trackModelInstance);

        pollSystemUpdateQueue();
        if ( systemStateModelInstance != null ) {
            modelBatch.render(systemStateModelInstance);
            modelBatch.render(systemStateVelocityModelInstance);
        }

		modelBatch.end();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		earthTextureModel.dispose();
        trackModel.dispose();
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
