package com.atc.simulator.Display;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.*;

/**
 * Stores the layers, generates collection of instances for rendering.
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
     * Method getModelInstanceCameraBatches returns the renderInstances of this LayerManager object.
     *
     * @return the renderInstances (type Collection<ModelInstance>) of this LayerManager object.
     */
    public Collection<CameraBatch> getRenderInstances()
    {
        ArrayList<CameraBatch> cameraBatches = new ArrayList<CameraBatch>();
        for (RenderLayer layer: layers)
        {
            Collection<CameraBatch> layerCameraBatches = layer.getModelInstanceCameraBatches();
            if (layerCameraBatches != null)
            {
                cameraBatches.addAll(layerCameraBatches);
            }
        }

        return cameraBatches;
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
