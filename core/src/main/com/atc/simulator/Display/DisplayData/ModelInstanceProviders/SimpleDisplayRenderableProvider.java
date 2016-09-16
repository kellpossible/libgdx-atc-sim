package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.Display.DisplayData.DisplayRenderable;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProvider;
import com.atc.simulator.Display.DisplayData.DisplayRenderableProviderListener;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

/**
 * A simple implementation of MOdelInstanceProvider, providing a single instance.
 * @author Luke Frisken
 */
public abstract class SimpleDisplayRenderableProvider implements DisplayRenderableProvider {
    private ArrayList<DisplayRenderableProviderListener> modelInstanceListeners = new ArrayList<DisplayRenderableProviderListener>();
    private DisplayRenderable renderable;
    private Model model;
    private Camera camera;

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
        if (model != null)
        {
            model.dispose();
        }
        triggerOnInstanceDispose();
    }

    /**
     * Call to update the instance provided by this class.
     */
    public void update()
    {
        dispose();
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

    public void triggerOnInstanceDispose()
    {
        for (DisplayRenderableProviderListener listener : modelInstanceListeners)
        {
            listener.onDisplayRenderableDispose(this);
        }
    }

    public void setDisplayRenderable(DisplayRenderable newRenderable)
    {
        setDisplayRenderable(newRenderable, true);
    }

    public void setDisplayRenderable(DisplayRenderable newRenderable, boolean triggerUpdate)
    {
        renderable = newRenderable;

        if (triggerUpdate)
        {
            triggerOnRenderableUpdate(renderable);
        }
    }

    //todo remove setModel, because it's superceded by setrenderable
    protected ModelInstance setModel(Model newModel)
    {
        return setModel(newModel, true);
    }

    protected ModelInstance setModel(Model newModel, boolean triggerUpdate)
    {
        ModelInstance instance = new ModelInstance(newModel);
        model = newModel;
        setDisplayRenderable(new DisplayRenderable(instance, camera), triggerUpdate);
        return instance;
    }

    public Model getModel()
    {
        return model;
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
