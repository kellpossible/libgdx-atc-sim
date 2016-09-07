package com.atc.simulator.Display.DisplayData;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.concurrent.ExecutionException;

/**
 * @author Luke Frisken
 */
public class DisplayRenderable {
    private Object renderable;
    private RenderableType type;

    public enum RenderableType {
        MODELINSTANCE,
        DISPLAYTEXT
    }

    public DisplayRenderable(ModelInstance instance)
    {
        renderable = instance;
        type = RenderableType.MODELINSTANCE;
    }

    public DisplayRenderable(DisplayText text)
    {
        renderable = text;
        type = RenderableType.DISPLAYTEXT;
    }

    public RenderableType getType()
    {
        return type;
    }

    public DisplayText getDisplayText()
    {
        checkType(RenderableType.DISPLAYTEXT);
        return (DisplayText) renderable;
    }

    public ModelInstance getModelInstance()
    {
        checkType(RenderableType.MODELINSTANCE);
        return (ModelInstance) renderable;
    }


    private void checkType(RenderableType shouldBe)
    {
        if (type != shouldBe) {
            throw new ClassCastException("Cannot cast " + renderable.getClass() + "of type " + type.name() +
                    " to a " + shouldBe.name());
        }

    }
}
