package com.atc.simulator.display.view.model_instance_providers;

import com.atc.simulator.display.DisplayCameraListener;
import com.atc.simulator.display.model.DisplayAircraft;
import com.atc.simulator.display.view.display_renderable.GDXDisplayRenderable;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.EllipseShapeBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * Green dot representing the aircraft
 * @author Luke Frisken
 */
public class AircraftDotModel extends SimpleDisplayRenderableProvider implements DisplayCameraListener {
    private DisplayAircraft aircraft;
    private static float dotSize = 0.0004f;


    public AircraftDotModel(Camera camera, DisplayAircraft aircraft)
    {
        super(camera);
        this.aircraft = aircraft;
        update();


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

    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update() {
        super.update();

        GeographicCoordinate position = aircraft.getPosition();

        float scale = 1f;

        PerspectiveCamera camera = (PerspectiveCamera) getCamera();
        scale = camera.fieldOfView/2;

        if (scale > 2f)
        {
            scale = 2;
        }

        double depthAdjustment = -0.05;
        Vector3 modelDrawVector = position.getModelDrawVector(depthAdjustment);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "aircraftDot",
                GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material(ColorAttribute.createDiffuse(new Color(0, 1, 0, 1))));

        Vector3 normal = new Vector3(modelDrawVector).scl(-1).nor();

        EllipseShapeBuilder.build(builder, dotSize*scale, dotSize*scale, 20, modelDrawVector, normal);

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);

//        ModelBuilder modelBuilder = new ModelBuilder();
//        Model newModel = modelBuilder.createSphere(
//                0.0005f, 0.0005f, 0.0005f, 7, 7,
//                new Material(ColorAttribute.createDiffuse(new Color(0, 1, 0, 1))),
//                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//        ModelInstance modelInstance = new ModelInstance(newModel);
//        modelInstance.transform.setToScaling(scale, scale, scale);
//        modelInstance.calculateTransforms();
//        modelInstance.transform.setTranslation(modelDrawVector.x, modelDrawVector.y, modelDrawVector.z);

        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }

    @Override
    public void onUpdate(CameraUpdate cameraUpdate) {
        switch (cameraUpdate.updateType)
        {
            case ZOOM:
                update();
//                System.out.println("finished" + aircraft.getAircraftID());
                break;
        }
    }
}
