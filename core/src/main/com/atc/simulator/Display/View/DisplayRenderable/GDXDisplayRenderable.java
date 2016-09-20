package com.atc.simulator.Display.View.DisplayRenderable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

/**
 * A libGDX renderable provider DisplayRenderable
 * @see RenderableProvider
 * @author Luke Frisken
 */
public class GDXDisplayRenderable extends DisplayRenderable implements RenderableProvider{
    private Array<Disposable> disposables;
    private RenderableProvider renderable;

    /**
     *
     * @param renderable libgdx renderable provider
     * @param disposables items which need disposing upon disposing of this renderable
     */
    public GDXDisplayRenderable(RenderableProvider renderable, Camera camera, Array<Disposable> disposables)
    {
        super(camera);
        this.disposables = disposables;
        this.renderable = renderable;
    }

    /**
     *
     * @param renderable libgdx renderable provider
     * @param disposable item which needs disposing upon disposing of this renderable
     */
    public GDXDisplayRenderable(RenderableProvider renderable, Camera camera, Disposable disposable)
    {
        super(camera);
        disposables = new Array<Disposable>();
        disposables.add(disposable);
        this.renderable = renderable;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        for (Disposable disposable : this.disposables)
        {
            disposable.dispose();
        }
    }

    /**
     * Returns {@link Renderable} instances. Renderables are obtained from the provided {@link Pool} and added to the provided
     * array. The Renderables obtained using {@link Pool#obtain()} will later be put back into the pool, do not store them
     * internally. The resulting array can be rendered via a {@link com.badlogic.gdx.graphics.g3d.ModelBatch}.
     *
     * @param renderables the output array
     * @param pool        the pool to obtain Renderables from
     */
    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        renderable.getRenderables(renderables, pool);
    }
}
