package com.atc.simulator.Display.DisplayData;

import com.atc.simulator.Display.DisplayData.RenderLayers.RenderLayer;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.*;

/**
 * @author Luke Frisken
 */
public class LayerManager {
    private PriorityQueue<RenderLayer> layers;

    /**
     * Constructor LayerManager creates a new LayerManager instance.
     */
    public LayerManager()
    {
        layers = new PriorityQueue<RenderLayer>();
    }

    /**
     * Adds a new layer to the display
     *
     * @param renderLayer of type RenderLayer
     */
    public void addRenderLayer(RenderLayer renderLayer)
    {
        layers.add(renderLayer);
    }

    /**
     * Method getRenderInstances returns the renderInstances of this LayerManager object.
     *
     * @return the renderInstances (type Collection<ModelInstance>) of this LayerManager object.
     */
    public Collection<ModelInstance> getRenderInstances()
    {
        ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();
        for (RenderLayer layer: layers)
        {
            Collection<ModelInstance> layerInstances = layer.getRenderInstances();
            if (layerInstances != null)
            {
                instances.addAll(layerInstances);
            }
        }

        return instances;
    }

    /**
     * Call to dispose of this class, and its resources.
     */
    public void dispose()
    {
        for (RenderLayer layer : layers)
        {
            layer.dispose();
        }
    }

}
