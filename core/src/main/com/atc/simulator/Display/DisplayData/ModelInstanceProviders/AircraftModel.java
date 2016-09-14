package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.Display.DisplayCameraListener;
import com.atc.simulator.Display.DisplayData.DisplayAircraft;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * Green dot representing the aircraft
 * @author Luke Frisken
 */
public class AircraftModel extends SimpleDisplayRenderableProvider implements DisplayCameraListener {
    private DisplayAircraft aircraft;


    public AircraftModel(Camera camera, DisplayAircraft aircraft)
    {
        super(camera);
        this.aircraft = aircraft;
        update();


//        Vector3 screenPosition = cam.project(new Vector3(modelDrawVector));
//
//
//        String aircraftID = aircraftState.getAircraftID();
////                System.out.println("Screen Position: " + screenPosition + ", DisplayAircraft ID: " + aircraftID);
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

        double depthAdjustment = -0.01;
        Vector3 modelDrawVector = position.getModelDrawVector(depthAdjustment);

        ModelBuilder modelBuilder = new ModelBuilder();
        Model newModel = modelBuilder.createSphere(
                0.0005f, 0.0005f, 0.0005f, 7, 7,
                new Material(ColorAttribute.createDiffuse(new Color(0, 1, 0, 1))),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ModelInstance modelInstance = setModel(newModel, false);
        modelInstance.transform.setToScaling(scale, scale, scale);
        modelInstance.calculateTransforms();
        modelInstance.transform.setTranslation(modelDrawVector.x, modelDrawVector.y, modelDrawVector.z);

        triggerOnRenderableUpdate(getDisplayRenderable());
    }

    @Override
    public void onUpdate(Camera camera, UpdateType updateType) {
        switch (updateType)
        {
            case ZOOM:
//                System.out.println("updating" + aircraft.getAircraftID());
                update();
//                System.out.println("finished" + aircraft.getAircraftID());
                break;
        }
    }
}