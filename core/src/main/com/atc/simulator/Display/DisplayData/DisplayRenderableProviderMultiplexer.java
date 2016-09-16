package com.atc.simulator.Display.DisplayData;

import com.atc.simulator.Display.DisplayData.DisplayRenderableProvider;

import java.util.Collection;

/**
 * A bit of a hack to allow multiple model instance providers
 * to be passed to a render layer by a single object.
 * @author Luke Frisken
 */
public interface DisplayRenderableProviderMultiplexer {
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
