package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.navdata.Countries;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

/**
 * Created by luke on 4/09/16.
 */
public class DisplayWorldMap extends SimpleModelInstanceProvider {
    public DisplayWorldMap() {
        Countries countries = new Countries("assets/maps/countries.geo.json");
        model = countries.getModel();
        modelInstance = new ModelInstance(model);
    }
}
