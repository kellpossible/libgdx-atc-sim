package com.atc.simulator.Display.DisplayData;

import com.atc.simulator.Display.BitmapText.BitmapText;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * @author Luke Frisken
 */
public class DisplayRenderable {
    private Object renderable;
    private RenderableType type;
    private Camera camera;

    public enum RenderableType {
        MODELINSTANCE,
        DISPLAYTEXT
    }

    public DisplayRenderable(ModelInstance instance, Camera camera)
    {
        renderable = instance;
        type = RenderableType.MODELINSTANCE;
        this.camera = camera;
    }

    public DisplayRenderable(BitmapText text)
    {
        renderable = text;
        type = RenderableType.DISPLAYTEXT;
    }

    public RenderableType getType()
    {
        return type;
    }

    public BitmapText getDisplayText()
    {
        checkType(RenderableType.DISPLAYTEXT);
        return (BitmapText) renderable;
    }

    public ModelInstance getModelInstance()
    {
        checkType(RenderableType.MODELINSTANCE);
        return (ModelInstance) renderable;
    }

    public Camera getCamera()
    {
        return camera;
    }


    private void checkType(RenderableType shouldBe)
    {
        if (type != shouldBe) {
            throw new ClassCastException("Cannot cast " + renderable.getClass() + "of type " + type.name() +
                    " to a " + shouldBe.name());
        }

    }
}
