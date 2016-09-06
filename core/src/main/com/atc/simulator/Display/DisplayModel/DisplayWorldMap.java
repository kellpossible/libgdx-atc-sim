package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.navdata.Countries;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * The background map for the display.
 * @author Luke Frisken
 */
public class DisplayWorldMap extends SimpleModelInstanceProvider {
    public DisplayWorldMap() {
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
        model = countries.getModel();
        modelInstance = new ModelInstance(model);

        triggerOnInstanceUpdate(modelInstance);
    }
}
