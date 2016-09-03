package com.atc.simulator.Display.DisplayModel.RenderLayers;

import com.atc.simulator.navdata.Countries;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the map background
 */
public class MapBackgroundRenderLayer extends RenderLayer {
    private Model model;
    private ModelInstance modelInstance;
    private ArrayList<ModelInstance> instances;

    public MapBackgroundRenderLayer(int priority, String name) {
        super(priority, name);

        Countries countries = new Countries("assets/maps/countries.geo.json");
        model = countries.getModel();
        modelInstance = new ModelInstance(model);
        instances = new ArrayList<ModelInstance>(1);
        instances.add(modelInstance);
    }
}
