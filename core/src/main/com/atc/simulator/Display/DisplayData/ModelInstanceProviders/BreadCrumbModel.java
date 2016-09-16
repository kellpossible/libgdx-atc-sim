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

import java.util.ArrayList;

/**
 * A model of the breadcrumb trail left behind by an aircraft on the display as it
 * travels.
 * @author Luke Frisken
 * Created by luke on 9/09/16.
 */
public class BreadCrumbModel extends SimpleDisplayRenderableProvider implements DisplayCameraListener {
    private DisplayAircraft aircraft;
    private ArrayList<Model> models;
    private ModelCache modelCache;


    /**
     * Constructor for BreadCrumbModel
     * @param camera the camera which will render this model
     * @param aircraft the aircraft to which this model belongs
     */
    public BreadCrumbModel(Camera camera, DisplayAircraft aircraft)
    {
        super(camera);
        this.aircraft = aircraft;
        models = new ArrayList<Model>();
        update();
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


        modelCache = new ModelCache();
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
            models.add(newModel);

            n++;
        }

        modelCache.end();

        setDisplayRenderable(new DisplayRenderable(modelCache, camera));
    }

    /**
     * Called when the camera has been updated.
     * @param cameraUpdate the data for the camera update
     */
    @Override
    public void onUpdate(CameraUpdate cameraUpdate) {
        switch (cameraUpdate.updateType)
        {
            case ZOOM:
//                System.out.println("lock length" + updateLock.getQueueLength());
                update();
                break;
        }
    }


    /**
     * Call to dispose of this class, and its resources.
     */
    @Override
    public void dispose()
    {
        if (modelCache != null)
        {
            modelCache.dispose();
            modelCache = null;
        }

        for(Model model : models)
        {
            model.dispose();
        }

        models.clear();
        super.dispose();
    }
}