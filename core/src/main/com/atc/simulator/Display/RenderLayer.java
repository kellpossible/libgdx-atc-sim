package com.atc.simulator.Display;

import com.atc.simulator.Display.DisplayData.DisplayRenderable;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProvider;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProviderListener;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProviderMultiplexer;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.*;

/**
 *
 * @author Luke Frisken
 */
class RenderLayer implements Comparable, Iterable<ModelInstance>, DisplayRenderableProviderListener {
    HashMap<DisplayRenderableProvider, ModelInstance> instances;
    protected int priority;
    private String name;
    private boolean visible;


    /**
     * Default constructor for RenderLayer
     * @param priority priority/order of render layer.
     * @param name name of render layer
     */
    public RenderLayer(int priority, String name)
    {
        instances = new HashMap<DisplayRenderableProvider, ModelInstance>();
        this.priority = priority;
        this.name = name;
        visible = true;
    }

    /**
     * Check to see whether or not this layer is set to visible.
     * @return boolean visible
     */
    public boolean isVisible()
    {
        return visible;
    }

    /**
     * Set the visibility of this RenderLayer
     * @param visible boolean status of visibility
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    /**
     * Get the instances in this layer
     * @return collection of model instances
     */
    public Collection<ModelInstance> getRenderInstances()
    {
        if (visible)
        {
            return instances.values();
        } else {
            return null;
        }

    }

    /**
     * Called by an instance provider to its listeners when it is being updated.
     * @param provider the provider who is updating
     * @param displayRenderable the new model instance as a result of the update
     */
    @Override
    public void onDisplayRenderableUpdate(DisplayRenderableProvider provider, DisplayRenderable displayRenderable) {
        if (displayRenderable == null)
        {
            throw new NullPointerException(provider.getClass().getName() + " provided a null model instance");
        }
        storeDisplayRenderable(provider, displayRenderable);
    }

    /**
     * Store a DisplayRenderable and it's provider in the appropriate
     * location.
     *
     * @param provider the provider of the renderable
     * @param renderable the renderable to store, associated with its provider.
     */
    private void storeDisplayRenderable(DisplayRenderableProvider provider, DisplayRenderable renderable)
    {
        switch (renderable.getType())
        {
            case DISPLAYTEXT:
                renderable.getDisplayText();
                break;
            case MODELINSTANCE:
                instances.put(provider, renderable.getModelInstance());
                break;
        }
    }

    /**
     * Called by an instance provider to its listeners when it is being disposed of.
     * @param provider the provider who is being disposed of
     */
    @Override
    public void onDisplayRenderableDispose(DisplayRenderableProvider provider) {
        instances.remove(provider);
    }

    /**
     * Add a new instance provider to this render layer.
     *
     * @param provider of type DisplayRenderableProvider
     */
    public void addDisplayRenderableProvider(DisplayRenderableProvider provider)
    {
        provider.addModelInstanceListener(this);
        DisplayRenderable renderable = provider.getDisplayRenderable();

        storeDisplayRenderable(provider, renderable);

    }

    /**
     * Add new instance provider multiplexer to this render layer.
     *
     * @param multiplexer of type DisplayRenderableProviderMultiplexer
     */
    public void addDisplayRenderableProvider(DisplayRenderableProviderMultiplexer multiplexer)
    {
        for(DisplayRenderableProvider provider : multiplexer.getDisplayRenderableProviders())
        {
            addDisplayRenderableProvider(provider);
        }
    }

    /**
     * Implementation of iterable interface
     * @return Iterator<ModelInstance>
     */
    @Override
    public Iterator<ModelInstance> iterator() {
        return instances.values().iterator();
    }


    /**
     * Get the name of this render layer
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the priority of this render layer
     * @return int
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * Implementation of the compareTo interface,
     * used by the LayerManager to sort the render layers.
     * @param o object to compare to
     * @return -1 if o is greater than this, 0 if same, and 1 if o less than this.
     */
    @Override
    public int compareTo(Object o) {
        RenderLayer other = (RenderLayer) o;

        return Integer.compare(this.priority, other.priority);
    }

    /**
     * Call to dispose of this class, and its resources.
     */
    public void dispose()
    {
        for (DisplayRenderableProvider provider : instances.keySet())
        {
            provider.dispose();
        }
    }
}
