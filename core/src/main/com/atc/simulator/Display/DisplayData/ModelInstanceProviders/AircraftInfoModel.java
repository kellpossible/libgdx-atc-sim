package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.Display.Display;
import com.atc.simulator.Display.DisplayCameraListener;
import com.atc.simulator.Display.DisplayData.DisplayAircraft;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by luke on 8/09/16.
 */
public class AircraftInfoModel extends SimpleDisplayRenderableProvider implements DisplayCameraListener {
    private Display display;
    private DisplayAircraft aircraft;
    /**
     * Cnstructor of TracksModel
     * @param camera the camera used to draw this model
     * @param display the display this model is being rendered on.
     */
    public AircraftInfoModel(Camera camera, Display display, DisplayAircraft aircraft)
    {
        super(camera);
        this.display = display;
        this.aircraft = aircraft;
        update();
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update()
    {
        double depthAdjustment = -0.01;

        super.update();

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "aircraft_info",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.GREEN);

        float size = 1000f;
//        builder.line(new Vector3(0, 0, 0), new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0));
//        builder.line(new Vector3(0, 0, -1), new Vector3(0, 0, 1));

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = setModel(newModel, false);

        GeographicCoordinate position = aircraft.getPosition();
        Vector3 modelDrawVector = position.getModelDrawVector(depthAdjustment);

//        modelInstance.transform.setToRotation(getCamera().up, 30);
//        modelInstance.calculateTransforms();
//        modelInstance.transform.setToScaling(size, size, size);
//        modelInstance.calculateTransforms();
//        modelInstance.transform.setTranslation(modelDrawVector.x, modelDrawVector.y, modelDrawVector.z);


        triggerOnRenderableUpdate(getDisplayRenderable());
    }

    @Override
    public void onUpdate(Camera camera, UpdateType updateType) {
        update();
    }
}