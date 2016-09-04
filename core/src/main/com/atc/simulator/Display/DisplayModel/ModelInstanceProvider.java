package com.atc.simulator.Display.DisplayModel;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by luke on 4/09/16.
 */
public interface ModelInstanceProvider {
    public ModelInstance getModelInstance();
    public void addModelInstanceListener(ModelInstanceListener listener);
    public void dispose();
}
