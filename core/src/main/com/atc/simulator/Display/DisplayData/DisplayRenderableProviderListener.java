package com.atc.simulator.Display.DisplayData;

import com.atc.simulator.Display.DisplayData.DisplayRenderableProvider;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * A listener interface to allow objects to listen to ModelInstanceProviders.
 * @author Luke Frisken
 */
public interface DisplayRenderableProviderListener {
    /**
     * Called by an instance provider to its listeners when it is being updated.
     * @param provider the provider who is updating
     * @param displayRenderable the new model instance as a result of the update
     */
    void onDisplayRenderableUpdate(DisplayRenderableProvider provider, DisplayRenderable displayRenderable);

    /**
     * Called by an instance provider to its listeners when it is being disposed of.
     * @param provider the provider who is being disposed of
     */
    void onDisplayRenderableDispose(DisplayRenderableProvider provider);
}