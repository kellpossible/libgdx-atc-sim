package com.atc.simulator.Display;

import com.atc.simulator.Display.DisplayData.DisplayRenderableProvider;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by luke on 8/09/16.
 */
public class CameraBatch extends HashMap<DisplayRenderableProvider, RenderableProvider>
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

    public Collection<RenderableProvider> gdxRenderableProviders()
    {
        return values();
    }
}
