package com.atc.simulator.Display.View.ModelInstanceProviders;

import com.atc.simulator.Display.Model.Display;
import com.atc.simulator.Display.DisplayCameraListener;
import com.atc.simulator.Display.Model.DisplayAircraft;
import com.atc.simulator.Display.View.DisplayRenderable.GDXDisplayRenderable;
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

        //        Vector3 screenPosition = cam.project(new Vector3(modelDrawVector));
//
//
//        String aircraftID = aircraftState.getAircraftID();
////                System.out.println("Screen Position: " + screenPosition + ", AircraftModel ID: " + aircraftID);
//
//        if (aircraftID.equals("QFA489"))
//        {
//            textPosition.x = screenPosition.x;
//            textPosition.y = screenPosition.y;
//        }
//
//        //if the aircraft is moving
//        if (velocity.length() > 0.00001)
//        {
//            GeographicCoordinate velocityEndPos = new GeographicCoordinate(position.add(velocity.mult(120))); //two minute velocity vector
//            aircraftStateVelocityModel = modelBuilder.createArrow(
//                    modelDrawVector,
//                    velocityEndPos.getModelDrawVector(depthAdjustment),
//                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
//                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//            aircraftStateVelocityModelInstance = new ModelInstance(aircraftStateVelocityModel);

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
        ModelInstance modelInstance = new ModelInstance(newModel);

        GeographicCoordinate position = aircraft.getPosition();
        Vector3 modelDrawVector = position.getModelDrawVector(depthAdjustment);

//        modelInstance.transform.setToRotation(getCamera().up, 30);
//        modelInstance.calculateTransforms();
//        modelInstance.transform.setToScaling(size, size, size);
//        modelInstance.calculateTransforms();
//        modelInstance.transform.setTranslation(modelDrawVector.x, modelDrawVector.y, modelDrawVector.z);


        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }

    @Override
    public void onUpdate(CameraUpdate cameraUpdate) {
        update();
    }
}