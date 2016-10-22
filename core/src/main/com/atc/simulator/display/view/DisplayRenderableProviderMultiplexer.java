package com.atc.simulator.display.view;

import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;

/**
 * A bit of a hack to allow multiple model instance providers
 * to be passed to a render layer by a single object.
 * @author Luke Frisken
 */
public interface DisplayRenderableProviderMultiplexer extends Disposable {
    /**
     * Get the instance providers provided by this multiplexer
     * @return
     */
    Collection<DisplayRenderableProvider> getDisplayRenderableProviders();

    /**
     * Call to update the gdxRenderableProviders provided by this multiplexer.
     */
    void update();
}
