package com.atc.simulator.Display.DisplayData.DisplayRenderable;

import com.atc.simulator.Display.BitmapText.BitmapText;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Luke Frisken
 */
public abstract class DisplayRenderable implements Disposable {
    private Camera camera;

    /**
     * Create a hidden/empty display renderable
     */
    public DisplayRenderable()
    {
        this.camera = null;
    }

    public DisplayRenderable(Camera camera)
    {
        this.camera = camera;
    }

    public Camera getCamera()
    {
        return camera;
    }
}
