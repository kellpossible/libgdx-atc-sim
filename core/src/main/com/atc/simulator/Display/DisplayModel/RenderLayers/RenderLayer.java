package com.atc.simulator.Display.DisplayModel.RenderLayers;

import com.atc.simulator.Display.DisplayModel.ModelInstanceListener;
import com.atc.simulator.Display.DisplayModel.ModelInstanceProvider;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luke on 4/09/16.
 */
public class RenderLayer implements Comparable, Iterable<ModelInstance>, ModelInstanceListener {
    HashMap<ModelInstanceProvider, ModelInstance> instances;
    protected int priority;
    private String name;


    public RenderLayer(int priority, String name)
    {
        instances = new HashMap<ModelInstanceProvider, ModelInstance>();
        this.priority = priority;
        this.name = name;
    }

    @Override
    public void onInstanceUpdate(ModelInstanceProvider provider, ModelInstance newInstance) {
        instances.put(provider, newInstance);
    }

    public void addInstance(ModelInstanceProvider provider, ModelInstance newInstance)
    {
        instances.put(provider, newInstance);
    }

    @Override
    public Iterator<ModelInstance> iterator() {
        return instances.values().iterator();
    }


    /**
     * Get the name of this render layer
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the priority of this render layer
     * @return
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * Implementation of the compareTo interface,
     * used by the DisplayModel to sort the render layers.
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        RenderLayer other = (RenderLayer) o;

        return Integer.compare(this.priority, other.priority);
    }
}
