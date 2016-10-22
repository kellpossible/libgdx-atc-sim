package com.atc.simulator.display.View.ModelInstanceProviders;

import com.atc.simulator.display.DisplayCameraListener;
import com.atc.simulator.display.Model.DisplayAircraft;
import com.atc.simulator.display.View.DisplayRenderable.GDXDisplayRenderable;
import com.atc.simulator.display.View.DisplayRenderable.HiddenDisplayRenderable;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.EllipseShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * A model of the breadcrumb trail left behind by an aircraft on the display as it
 * travels.
 * @author Luke Frisken
 * Created by luke on 9/09/16.
 */
public class BreadCrumbModel extends SimpleDisplayRenderableProvider implements DisplayCameraListener {
    private DisplayAircraft aircraft;
    private static float dotSize = 0.0004f;


    /**
     * Constructor for BreadCrumbModel
     * @param camera the camera which will render this model
     * @param aircraft the aircraft to which this model belongs
     */
    public BreadCrumbModel(Camera camera, DisplayAircraft aircraft)
    {
        super(camera);
        this.aircraft = aircraft;
        update();
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update() {
        super.update();

        float scale = 1f;
        double depthAdjustment = 0.0;

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
            setDisplayRenderable(new HiddenDisplayRenderable());
            return;
        }

        if (track.size()%stepSize != 0 )
            return;

        track.get(track.size()-1);

        Array<Disposable> disposables = new Array<Disposable>();

        ModelCache modelCache = new ModelCache();
        modelCache.begin();


        int n = 1;
        for (int i = track.size()-1-stepSize; i > Math.max(0, track.size()-1-lookback); i-=stepSize)
        {
            float colorBrightness = 1.0f * ((float) Math.exp(-((double) n)/15.0f));
            float intermediateScale = (float) (scale * Math.exp(((double) -(n+3))/(lookback/2)));

            GeographicCoordinate position = track.get(i).getPosition();
            Vector3 modelDrawVector = position.getModelDrawVector(depthAdjustment);

            Vector3 normal = new Vector3(modelDrawVector).scl(-1).nor();

            Color newColor = new Color(colorBrightness, colorBrightness, colorBrightness, 1.0f);

            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();
            MeshPartBuilder builder = modelBuilder.part(
                    "breadcrumbs",
                    GL20.GL_TRIANGLES,
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                    new Material(ColorAttribute.createDiffuse(newColor)));

            EllipseShapeBuilder.build(builder, dotSize*intermediateScale, dotSize*intermediateScale, 20, modelDrawVector, normal);

            Model newModel = modelBuilder.end();
            ModelInstance modelInstance = new ModelInstance(newModel);
            modelCache.add(modelInstance);
            disposables.add(newModel);

            n++;
        }

        modelCache.end();
        disposables.add(modelCache);

        setDisplayRenderable(new GDXDisplayRenderable(modelCache, camera, disposables));
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
}