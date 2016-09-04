package com.atc.simulator.Display.DisplayModel;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

/**
 * Created by luke on 5/09/16.
 */
public abstract class SimpleModelInstanceProvider implements ModelInstanceProvider {
    private ArrayList<ModelInstanceListener> modelInstanceListeners = new ArrayList<ModelInstanceListener>();
    protected ModelInstance modelInstance;
    protected Model model;

    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    @Override
    public void addModelInstanceListener(ModelInstanceListener listener) {
        modelInstanceListeners.add(listener);
        listener.onInstanceUpdate(this, modelInstance);
    }

    @Override
    public void dispose() {
        model.dispose();
        for (ModelInstanceListener listener : modelInstanceListeners)
        {
            listener.onInstanceDispose(this);
        }
    }

    public void update()
    {
        if (model != null)
        {
            model.dispose();
        }
    }

    public void triggerOnInstanceUpdate(ModelInstance updatedInstance)
    {
        for (ModelInstanceListener listener : modelInstanceListeners)
        {
            listener.onInstanceUpdate(this, updatedInstance);
        }
    }
}
