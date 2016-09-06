package com.atc.simulator.Display;

import com.atc.simulator.Display.DisplayData.ModelInstanceProviders.ModelInstanceProviderListener;
import com.atc.simulator.Display.DisplayData.ModelInstanceProviders.ModelInstanceProvider;
import com.atc.simulator.Display.DisplayData.ModelInstanceProviders.ModelInstanceProviderMultiplexer;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.*;

/**
 *
 * @author Luke Frisken
 */
public class RenderLayer implements Comparable, Iterable<ModelInstance>, ModelInstanceProviderListener {
    HashMap<ModelInstanceProvider, ModelInstance> instances;
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
        instances = new HashMap<ModelInstanceProvider, ModelInstance>();
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
     * @param newInstance the new model instance as a result of the update
     */
    @Override
    public void onInstanceUpdate(ModelInstanceProvider provider, ModelInstance newInstance) {
        if (newInstance == null)
        {
            throw new NullPointerException(provider.getClass().getName() + " provided a null model instance");
        }
        instances.put(provider, newInstance);
    }

    /**
     * Called by an instance provider to its listeners when it is being disposed of.
     * @param provider the provider who is being disposed of
     */
    @Override
    public void onInstanceDispose(ModelInstanceProvider provider) {
        instances.remove(provider);
    }

    /**
     * Add a new instance provider to this render layer.
     *
     * @param provider of type ModelInstanceProvider
     */
    public void addInstanceProvider(ModelInstanceProvider provider)
    {
        provider.addModelInstanceListener(this);
        instances.put(provider, provider.getModelInstance());
    }

    /**
     * Add new instance provider multiplexer to this render layer.
     *
     * @param multiplexer of type ModelInstanceProviderMultiplexer
     */
    public void addInstanceProvider(ModelInstanceProviderMultiplexer multiplexer)
    {
        for(ModelInstanceProvider provider : multiplexer.getInstanceProviders())
        {
            addInstanceProvider(provider);
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
     * @param o
     * @return
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
        for (ModelInstanceProvider provider : instances.keySet())
        {
            provider.dispose();
        }
    }
}
