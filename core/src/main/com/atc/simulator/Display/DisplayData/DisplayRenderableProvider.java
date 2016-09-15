package com.atc.simulator.Display.DisplayData;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;

/**
 * This interface represents any object which is able
 * to provide Model gdxRenderableProviders to RenderLayers in the
 * display.
 * @author Luke Frisken
 */
public interface DisplayRenderableProvider extends Disposable {
    /**
     * Get the instance provided by this class
     * @return the instance provided by this class
     */
    DisplayRenderable getDisplayRenderable();

    /**
     * Add a DisplayRenderableProviderListener listener to this class.
     * @param listener the listener to be added
     */
    void addDisplayRenderableProviderListener(DisplayRenderableProviderListener listener);

    /**
     * Remove a DisplayRenderableProviderListener listener from this class.
     * @param listener the listener to be removed
     */
    void removeDisplayRenderableProviderListener(DisplayRenderableProviderListener listener);

    /**
     * Call to update the instance provided by this class.
     */
    void update();
}
