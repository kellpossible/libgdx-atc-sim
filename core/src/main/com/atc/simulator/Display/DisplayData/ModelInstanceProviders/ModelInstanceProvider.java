package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;

/**
 * This interface represents any object which is able
 * to provide Model instances to RenderLayers in the
 * display.
 * @author Luke Frisken
 */
public interface ModelInstanceProvider extends Disposable {
    /**
     * Get the instance provided by this class
     * @return the instance provided by this class
     */
    ModelInstance getModelInstance();

    /**
     * Add a ModelInstanceProviderListener listener to this class.
     * @param listener the listener to be added
     */
    void addModelInstanceListener(ModelInstanceProviderListener listener);

    /**
     * Call to update the instance provided by this class.
     */
    void update();
}
