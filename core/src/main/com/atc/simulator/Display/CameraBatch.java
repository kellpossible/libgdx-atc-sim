package com.atc.simulator.Display;

import com.atc.simulator.Display.DisplayData.DisplayRenderableProvider;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by luke on 8/09/16.
 */
public class CameraBatch extends HashMap<DisplayRenderableProvider, ModelInstance>
{
    private Camera camera;
    public CameraBatch(Camera camera)
    {
        super();
        this.camera = camera;
    }

    public Camera getCamera()
    {
        return camera;
    }

    public Collection<ModelInstance> instances()
    {
        return values();
    }
}
