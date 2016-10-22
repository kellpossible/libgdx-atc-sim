package com.atc.simulator.display.view.DisplayRenderable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;

/**
 * An object which is renderable by the display
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
