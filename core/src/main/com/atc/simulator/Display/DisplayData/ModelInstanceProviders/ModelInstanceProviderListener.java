package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * A listener interface to allow objects to listen to ModelInstanceProviders.
 * @author Luke Frisken
 */
public interface ModelInstanceProviderListener {
    /**
     * Called by an instance provider to its listeners when it is being updated.
     * @param provider the provider who is updating
     * @param newInstance the new model instance as a result of the update
     */
    void onInstanceUpdate(ModelInstanceProvider provider, ModelInstance newInstance);

    /**
     * Called by an instance provider to its listeners when it is being disposed of.
     * @param provider the provider who is being disposed of
     */
    void onInstanceDispose(ModelInstanceProvider provider);
}
