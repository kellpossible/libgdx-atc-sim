package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.Display.DisplayCameraListener;
import com.atc.simulator.Display.DisplayData.DisplayAircraft;
import com.atc.simulator.Display.DisplayData.DisplayRenderable;
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
 * @author Luke Frisken
 * Created by luke on 9/09/16.
 */
public class BreadCrumbModel extends SimpleDisplayRenderableProvider implements DisplayCameraListener {
    private DisplayAircraft aircraft;


    public BreadCrumbModel(Camera camera, DisplayAircraft aircraft)
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

        float scale = 1f;
        double depthAdjustment = -0.01;

        PerspectiveCamera camera = (PerspectiveCamera) getCamera();
        scale = camera.fieldOfView/2;

        if (scale > 2f)
        {
            scale = 2;
        }

        int stepSize = 1;
        int lookback = 20;

        Track track = aircraft.getTrack();

        if (track.size() < 1+stepSize)
        {
            setDisplayRenderable(new DisplayRenderable());
            return;
        }

        if (track.size()%stepSize != 0 )
            return;

        track.get(track.size()-1);

        ModelCache modelCache = new ModelCache();
        modelCache.begin();


        int n = 1;
        for (int i = track.size()-1-stepSize; i > Math.max(0, track.size()-1-lookback); i-=stepSize)
        {
            float colorBrightness = 1.0f * ((float) Math.exp(-((double) n)/15.0f));
            float intermediateScale = (float) (scale * Math.exp(((double) -(n+3))/(lookback/2)));

            GeographicCoordinate position = track.get(i).getPosition();
            Vector3 modelDrawVector = position.getModelDrawVector(depthAdjustment);

            Color newColor = new Color(colorBrightness, colorBrightness, colorBrightness, 1.0f);

            ModelBuilder modelBuilder = new ModelBuilder();
            Model newModel = modelBuilder.createSphere(
                    0.0005f, 0.0005f, 0.0005f, 5, 5,
                    new Material(ColorAttribute.createDiffuse(newColor)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            ModelInstance modelInstance = new ModelInstance(newModel);
            modelInstance.transform.setToScaling(intermediateScale, intermediateScale, intermediateScale);
            modelInstance.calculateTransforms();
            modelInstance.transform.setTranslation(modelDrawVector.x, modelDrawVector.y, modelDrawVector.z);
            modelCache.add(modelInstance);

            n++;
        }

        modelCache.end();

        setDisplayRenderable(new DisplayRenderable(modelCache, camera));
    }

    @Override
    public void onUpdate(Camera camera, UpdateType updateType) {
        switch (updateType)
        {
            case RESIZE:
                update();
                break;
        }
    }
}