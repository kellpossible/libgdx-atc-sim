package com.atc.simulator.display.view.model_instance_providers;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.display.model.Display;
import com.atc.simulator.display.DisplayCameraListener;
import com.atc.simulator.display.model.DisplayHud;
import com.atc.simulator.display.view.display_renderable.GDXDisplayRenderable;
import com.atc.simulator.display.view.vector_text.HersheyFont;
import com.atc.simulator.display.view.vector_text.HersheyText;
import com.atc.simulator.flightdata.TimeSource;
import com.badlogic.gdx.Gdx;
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

import java.util.Calendar;

/**
 * @author Luke Frisken
 * Created by luke on 8/09/16.
 */
public class HudModel extends SimpleDisplayRenderableProvider implements DisplayCameraListener {
    private static final String algorithmType = ApplicationConfig.getString("settings.prediction-service.prediction-engine.algorithm-type");
    private HersheyFont font;
    private DisplayHud displayHud;
    /**
     * Cnstructor of TracksModel
     * @param camera the camera used to draw this model
     * @param displayHud the data for the display hud
     */
    public HudModel(Camera camera, DisplayHud displayHud)
    {
        super(camera);
        font = new HersheyFont(HersheyFont.CharacterSet.SIMPLEX);
        this.displayHud = displayHud;
        update();
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update()
    {
        super.update();

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "aircraft_info",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.WHITE);

        float crossHairSize = displayHud.getCrossHairSize();
        float frameRateSize = 20f;
        Vector3 frameRateScale = new Vector3(frameRateSize, -frameRateSize, frameRateSize);

        Vector3 centre = new Vector3(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);

        HersheyText fpsText = new HersheyText("FPS: " + Gdx.graphics.getFramesPerSecond(),
                font, new Vector3(0, 20, 0), frameRateScale, 0f);
        fpsText.buildMesh(builder);

        HersheyText nInstancesText = new HersheyText("Instances: " + displayHud.getNumInstances(),
                font, new Vector3(0, 50, 0), frameRateScale, 0f);
        nInstancesText.buildMesh(builder);

        Display display = displayHud.getDisplay();

        TimeSource timeSource = display.getTimeSource();
        long currentTime = timeSource.getCurrentTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);

        HersheyText timeText = new HersheyText("Time: " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND),
                font, new Vector3(0, 80, 0), frameRateScale, 0f);
        timeText.buildMesh(builder);

        HersheyText displayMethodText = new HersheyText("display Method: " + display.getPredictionDisplayMethod().name(),
                font, new Vector3(0, 110, 0), frameRateScale, 0f);
        displayMethodText.buildMesh(builder);

        HersheyText algorithmTypeText = new HersheyText("Algorithm: " + algorithmType,
                font, new Vector3(0, 140, 0), frameRateScale, 0f);
        algorithmTypeText.buildMesh(builder);

        Vector3 crossHairTop = new Vector3(centre).add(new Vector3(0f, crossHairSize, 0f));
        Vector3 crossHairBottom = new Vector3(centre).add(new Vector3(0f, -crossHairSize, 0f));
        Vector3 crossHairLeft = new Vector3(centre).add(new Vector3(-crossHairSize, 0f, 0f));
        Vector3 crossHairRight = new Vector3(centre).add(new Vector3(crossHairSize, 0f, 0f));

        builder.line(crossHairBottom, crossHairTop);
        builder.line(crossHairLeft, crossHairRight);
//        builder.line(new Vector3(0, 0, -1), new Vector3(0, 0, 1));

        Model newModel = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(newModel);

        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }

    @Override
    public void onUpdate(CameraUpdate cameraUpdate) {
        update();
    }
}
