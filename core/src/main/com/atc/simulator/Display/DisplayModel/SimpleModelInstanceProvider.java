package com.atc.simulator.Display.DisplayModel;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

/**
 * A simple implementation of MOdelInstanceProvider, providing a single instance.
 * @author Luke Frisken
 */
public abstract class SimpleModelInstanceProvider implements ModelInstanceProvider {
    private ArrayList<ModelInstanceListener> modelInstanceListeners = new ArrayList<ModelInstanceListener>();
    protected ModelInstance modelInstance;
    protected Model model;

    /**
     * Get the instance provided by this class
     * @return the instance provided by this class
     */
    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    /**
     * Add a ModelInstanceListener listener to this class.
     * @param listener the listener to be added
     */
    @Override
    public void addModelInstanceListener(ModelInstanceListener listener) {
        modelInstanceListeners.add(listener);
        listener.onInstanceUpdate(this, modelInstance);
    }

    /**
     * Call to dispose of this class, and its resources.
     */
    @Override
    public void dispose() {
        model.dispose();
        for (ModelInstanceListener listener : modelInstanceListeners)
        {
            listener.onInstanceDispose(this);
        }
    }

    /**
     * Call to update the instance provided by this class.
     */
    public void update()
    {
        if (model != null)
        {
            model.dispose();
        }
    }

    /**
     * Trigger an onInstanceUpdate event for this class' listeners.
     * @param updatedInstance the new, updated instance
     */
    public void triggerOnInstanceUpdate(ModelInstance updatedInstance)
    {
        for (ModelInstanceListener listener : modelInstanceListeners)
        {
            listener.onInstanceUpdate(this, updatedInstance);
        }
    }
}
