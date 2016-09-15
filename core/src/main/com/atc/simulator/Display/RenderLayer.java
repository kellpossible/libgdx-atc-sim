package com.atc.simulator.Display;

import com.atc.simulator.Display.DisplayData.DisplayRenderable;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProvider;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProviderListener;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProviderMultiplexer;
import com.badlogic.gdx.graphics.Camera;

import java.util.*;

/**
 * Represents a layer in the display, a collection of items which
 * get rendered together. Either due to draw order, or shader requirements.
 * @author Luke Frisken
 */
class RenderLayer implements Comparable, DisplayRenderableProviderListener {
    private HashMap<Camera, CameraBatch> cameraBatches;

    protected int priority;
    private String name;
    private boolean visible;




    /**
     * Default constructor for RenderLayer
     * @param priority priority/order of buildMesh layer.
     * @param name name of buildMesh layer
     */
    public RenderLayer(int priority, String name)
    {
        cameraBatches = new HashMap<Camera, CameraBatch>();
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
     * Get the camera batches for the model gdxRenderableProviders in this layer
     * @return collection of model gdxRenderableProviders
     */
    public Collection<CameraBatch> getModelInstanceCameraBatches()
    {
        if (visible)
        {
            return cameraBatches.values();
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
//            throw new NullPointerException(provider.getClass().getName() + " provided a null model instance");
        }
        storeDisplayRenderable(provider, displayRenderable);
    }

    /**
     * Store a DisplayRenderable and it's provider in the appropriate
     * location.
     *
     * @param provider the provider of the renderab
     * @param renderable of type DisplayRenderable
     */
    private void storeDisplayRenderable(DisplayRenderableProvider provider, DisplayRenderable renderable)
    {
        switch (renderable.getType())
        {
            case DISPLAYTEXT:
                renderable.getDisplayText();
                break;
            case GDX_RENDERABLE_PROVIDER:
                storeModelInstance(provider, renderable);
                break;
        }
    }

    /**
     * Removea a DisplayRenderable and it's provider from the appropriate
     * locations.
     *
     * @param provider the provider of the renderab
     * @param renderable of type DisplayRenderable
     */
    private void removeDisplayRenderable(DisplayRenderableProvider provider, DisplayRenderable renderable)
    {
        switch (renderable.getType())
        {
            case DISPLAYTEXT:
                //TODO
                break;
            case GDX_RENDERABLE_PROVIDER:
                removeModelInstance(provider, renderable);
                break;
        }
    }

    private void storeModelInstance(DisplayRenderableProvider provider, DisplayRenderable renderable)
    {
        Camera camera = renderable.getCamera();
        CameraBatch batch = cameraBatches.get(camera);
        if (batch == null)
        {
            batch = new CameraBatch(camera);
            cameraBatches.put(camera, batch);
        }

        batch.put(provider, renderable.getRenderableProvider());
    }

    private void removeModelInstance(DisplayRenderableProvider provider, DisplayRenderable renderable)
    {
        Camera camera = renderable.getCamera();
        CameraBatch batch = cameraBatches.get(camera);
        if (batch != null)
        {
            batch.remove(provider);
        }
    }

    /**
     * Called by an instance provider to its listeners when it is being disposed of.
     * @param provider the provider who is being disposed of
     */
    @Override
    public void onDisplayRenderableDispose(DisplayRenderableProvider provider) {
        for (CameraBatch cameraBatch : cameraBatches.values())
        {
            cameraBatch.remove(provider);
        }

    }

    /**
     * Add a new instance provider to this buildMesh layer.
     *
     * @param provider of type DisplayRenderableProvider
     */
    public void addDisplayRenderableProvider(DisplayRenderableProvider provider)
    {
        provider.addDisplayRenderableProviderListener(this);
        DisplayRenderable renderable = provider.getDisplayRenderable();

        storeDisplayRenderable(provider, renderable);

    }

    /**
     * Add new instance provider multiplexer to this buildMesh layer.
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
     * removes a display renderable provider
     *
     * @param provider of type DisplayRenderableProvider
     */
    public void removeDisplayRenderableProvider(DisplayRenderableProvider provider)
    {
        provider.removeDisplayRenderableProviderListener(this);
        DisplayRenderable renderable = provider.getDisplayRenderable();

        removeDisplayRenderable(provider, renderable);

    }

    /**
     * removes a display renderable provider multiplexor
     *
     * @param multiplexer of type DisplayRenderableProviderMultiplexer
     */
    public void removeDisplayRenderableProvider(DisplayRenderableProviderMultiplexer multiplexer)
    {
        for(DisplayRenderableProvider provider : multiplexer.getDisplayRenderableProviders())
        {
            removeDisplayRenderableProvider(provider);
        }
    }


    /**
     * Get the name of this buildMesh layer
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the priority of this buildMesh layer
     * @return int
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * Implementation of the compareTo interface,
     * used by the LayerManager to sort the buildMesh layers.
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
        for (CameraBatch cameraBatch : cameraBatches.values())
        {
            for (DisplayRenderableProvider provider : cameraBatch.keySet())
            {
                provider.dispose();
            }
        }

    }
}
