package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.Display.DisplayModel.RenderLayers.RenderLayer;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.*;

/**
 * Model for the Display MVC.
 * @author Luke Frisken
 */
public class DisplayModel {
    private PriorityQueue<RenderLayer> layers;

    /**
     * Constructor DisplayModel creates a new DisplayModel instance.
     */
    public DisplayModel()
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
     * Method getRenderInstances returns the renderInstances of this DisplayModel object.
     *
     * @return the renderInstances (type Collection<ModelInstance>) of this DisplayModel object.
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
