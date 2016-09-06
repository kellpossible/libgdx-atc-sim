package com.atc.simulator.Display.DisplayModel;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * This interface represents any object which is able
 * to provide Model instances to RenderLayers in the
 * display.
 * @author Luke Frisken
 */
public interface ModelInstanceProvider {
    /**
     * Get the instance provided by this class
     * @return the instance provided by this class
     */
    ModelInstance getModelInstance();

    /**
     * Add a ModelInstanceListener listener to this class.
     * @param listener the listener to be added
     */
    void addModelInstanceListener(ModelInstanceListener listener);

    /**
     * Call to update the instance provided by this class.
     */
    void update();

    /**
     * Call to dispose of this class, and its resources.
     */
    void dispose();
}
