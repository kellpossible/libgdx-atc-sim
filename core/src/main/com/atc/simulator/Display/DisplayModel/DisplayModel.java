package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.Display.DisplayModel.RenderLayers.RenderLayer;

import java.util.PriorityQueue;

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


}
