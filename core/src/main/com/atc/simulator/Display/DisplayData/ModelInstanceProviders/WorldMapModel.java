package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.Display.DisplayData.DisplayRenderable.GDXDisplayRenderable;
import com.atc.simulator.navdata.Countries;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * The background map for the display.
 * @author Luke Frisken
 */
public class WorldMapModel extends SimpleDisplayRenderableProvider {
    public WorldMapModel(Camera camera) {
        super(camera);
        update();
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update()
    {
        super.update();

        Countries countries = new Countries("assets/maps/countries.geo.json");
        Model newModel = countries.getModel();
        ModelInstance modelInstance = new ModelInstance(newModel);
        setDisplayRenderable(new GDXDisplayRenderable(modelInstance, getCamera(), newModel));
    }
}
