package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.Display.DisplayData.DisplayRenderable.DisplayRenderable;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProvider;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProviderListener;
import com.badlogic.gdx.graphics.Camera;

import java.util.ArrayList;

/**
 * A simple implementation of MOdelInstanceProvider, providing a single instance.
 * @author Luke Frisken
 */
public abstract class SimpleDisplayRenderableProvider implements DisplayRenderableProvider {
    private ArrayList<DisplayRenderableProviderListener> modelInstanceListeners = new ArrayList<DisplayRenderableProviderListener>();
    private DisplayRenderable renderable;
    private Camera camera;

    /**
     * Constructor for {@link SimpleDisplayRenderableProvider}
     * @param camera camera rendering renderables provided by this provider
     */
    public SimpleDisplayRenderableProvider(Camera camera)
    {
        this.camera = camera;
    }

    /**
     * Get the instance provided by this class
     * @return the instance provided by this class
     */
    @Override
    public DisplayRenderable getDisplayRenderable() {
        return renderable;
    }

    /**
     * Add a DisplayRenderableProviderListener listener to this class.
     * @param listener the listener to be added
     */
    @Override
    public void addDisplayRenderableProviderListener(DisplayRenderableProviderListener listener) {
        modelInstanceListeners.add(listener);
        listener.onDisplayRenderableUpdate(this, renderable);
    }

    /**
     * Remove a DisplayRenderableProviderListener listener from this class.
     * @param listener the listener to be removed
     */
    @Override
    public void removeDisplayRenderableProviderListener(DisplayRenderableProviderListener listener) {
        modelInstanceListeners.remove(listener);
//        listener.onDisplayRenderableUpdate(this, renderable);
    }

    /**
     * Call to dispose of this class, and its resources.
     */
    @Override
    public void dispose() {
        if (renderable != null)
        {
            renderable.dispose();
        }
        triggerOnInstanceDispose();
    }

    /**
     * Call to update the instance provided by this class.
     */
    public void update()
    {
        dispose(); //any update will need to dispose of its models etc
    }

    /**
     * Trigger an onDisplayRenderableUpdate event for this class' listeners.
     * @param renderable the new, updated instance
     */
    public void triggerOnRenderableUpdate(DisplayRenderable renderable)
    {
        for (DisplayRenderableProviderListener listener : modelInstanceListeners)
        {
            listener.onDisplayRenderableUpdate(this, renderable);
        }
    }

    /**
     * Trigger a dispose event on this class's listeners
     */
    public void triggerOnInstanceDispose()
    {
        for (DisplayRenderableProviderListener listener : modelInstanceListeners)
        {
            listener.onDisplayRenderableDispose(this);
        }
    }

    /**
     * Set the renderable for this class
     * @param newRenderable new renderable to set
     */
    public void setDisplayRenderable(DisplayRenderable newRenderable)
    {
        setDisplayRenderable(newRenderable, true);
    }

    /**
     * Set the renderable for this class
     * @param newRenderable new renderable to set
     * @param triggerUpdate whether or not to trigger a {@link #triggerOnRenderableUpdate(DisplayRenderable)}
     */
    public void setDisplayRenderable(DisplayRenderable newRenderable, boolean triggerUpdate)
    {
        renderable = newRenderable;

        if (triggerUpdate)
        {
            triggerOnRenderableUpdate(renderable);
        }
    }

    /**
     * Get the camera being used to draw the model gdxRenderableProviders provided by this provider.
     * @return
     */
    public Camera getCamera()
    {
        return camera;
    }
}
