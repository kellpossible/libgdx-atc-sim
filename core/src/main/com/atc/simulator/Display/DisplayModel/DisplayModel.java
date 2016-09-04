package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.Display.DisplayModel.RenderLayers.RenderLayer;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.*;

/**
 * Created by luke on 4/09/16.
 */
public class DisplayModel {
    private PriorityQueue<RenderLayer> layers;

    public DisplayModel()
    {
        layers = new PriorityQueue<RenderLayer>();
    }

    public void addRenderLayer(RenderLayer renderLayer)
    {
        layers.add(renderLayer);
    }

    public Collection<ModelInstance> getRenderInstances()
    {
        ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();
        for (RenderLayer layer: layers)
        {
            instances.addAll(layer.getRenderInstances());
        }

        return instances;
    }

    public void dispose()
    {
        for (RenderLayer layer : layers)
        {
            layer.dispose();
        }
    }

}
