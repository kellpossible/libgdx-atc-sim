package com.atc.simulator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class ATCSimulator extends ApplicationAdapter {
	SpriteBatch batch;
	public PerspectiveCamera cam;

	public Model model;
	public ModelInstance instance;
	public ModelBatch modelBatch;
    public Environment environment;
	public MyCameraController camController;
    public AssetManager assets;


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
        modelBatch = new ModelBatch();
		batch = new SpriteBatch();

		cam = new PerspectiveCamera(40, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		cam.position.set(0f, 0f, 0f);
		cam.lookAt(1f, 0, 0);
		cam.near = 0.01f;
		cam.far = 2f;
		cam.update();

//		ModelBuilder modelBuilder = new ModelBuilder();
//		model = modelBuilder.createSphere(5f, 5f, 5f, 64, 64,
//				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
//				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//
        assets = new AssetManager();
        assets.load("models/planet.g3db", Model.class);
        assets.finishLoading();
        System.out.println("Finished loading");
        model = assets.get("models/planet.g3db", Model.class);


		instance = new ModelInstance(model);
        instance.transform.setToScaling(-1f,1f,1f);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 10f, 10f, 10f, 1f));
        //environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camController = new MyCameraController(cam);
        Gdx.input.setInputProcessor(camController);
    }

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();
		modelBatch.begin(cam);
		modelBatch.render(instance);
		modelBatch.end();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
		model.dispose();
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
