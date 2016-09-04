package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.navdata.Countries;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

/**
 * Created by luke on 4/09/16.
 */
public class DisplayWorldMap implements ModelInstanceProvider {

    private Model model;
    private ModelInstance modelInstance;
    private ArrayList<ModelInstanceListener> modelInstanceListeners;

    public DisplayWorldMap() {
        Countries countries = new Countries("assets/maps/countries.geo.json");
        model = countries.getModel();
        modelInstance = new ModelInstance(model);
        modelInstanceListeners = new ArrayList<ModelInstanceListener>();
    }

    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    @Override
    public void addModelInstanceListener(ModelInstanceListener listener) {
        modelInstanceListeners.add(listener);
    }
}
