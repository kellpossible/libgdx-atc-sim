package com.atc.simulator.Display.DisplayData;

import com.atc.simulator.Display.BitmapText.BitmapText;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;

/**
 * @author Luke Frisken
 */
public class DisplayRenderable {
    private Object renderable;
    private RenderableType type;
    private Camera camera;

    public enum RenderableType {
        GDX_RENDERABLE_PROVIDER,
        DISPLAYTEXT,
        HIDDEN
    }

    public DisplayRenderable()
    {
        renderable = null;
        type = RenderableType.HIDDEN;
        this.camera = null;
    }

    public DisplayRenderable(RenderableProvider gdxRenderableProvider, Camera camera)
    {
        renderable = gdxRenderableProvider;
        type = RenderableType.GDX_RENDERABLE_PROVIDER;
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

    public RenderableProvider getRenderableProvider()
    {
        checkType(RenderableType.GDX_RENDERABLE_PROVIDER);
        return (RenderableProvider) renderable;
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
