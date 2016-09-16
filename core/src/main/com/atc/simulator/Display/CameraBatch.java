package com.atc.simulator.Display;

import com.atc.simulator.Display.DisplayData.DisplayRenderableProvider;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;

import java.util.Collection;
import java.util.HashMap;

/**
 * Represents a collection of renderable providers to be rendered with the same camera.
 * @author Luke Frisken
 */
public class CameraBatch extends HashMap<DisplayRenderableProvider, RenderableProvider>
{
    private Camera camera;

    /**
     * Constructor for CameraBatch
     * @param camera the camera that will be rendering the objects in the batch.
     */
    public CameraBatch(Camera camera)
    {
        super();
        this.camera = camera;
    }

    /**
     * Get the camera that will be rendering the objects in the batch
     * @return
     */
    public Camera getCamera()
    {
        return camera;
    }

    /**
     * Get the values contained in this batch.
     * @return
     */
    public Collection<RenderableProvider> gdxRenderableProviders()
    {
        return values();
    }
}
