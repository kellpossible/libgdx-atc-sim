package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import java.util.Collection;

/**
 * A bit of a hack to allow multiple model instance providers
 * to be passed to a render layer by a single object.
 * @author Luke Frisken
 */
public interface ModelInstanceProviderMultiplexer {
    /**
     * Get the instance providers provided by this multiplexer
     * @return
     */
    Collection<ModelInstanceProvider> getInstanceProviders();

    /**
     * Call to update the instances provided by this multiplexer.
     */
    void update();
}
