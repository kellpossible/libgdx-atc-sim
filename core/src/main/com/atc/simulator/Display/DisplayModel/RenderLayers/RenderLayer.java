package com.atc.simulator.Display.DisplayModel.RenderLayers;

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
public class RenderLayer implements Comparable, Iterable<ModelInstance> {
    ArrayList<ModelInstanceProvider> instanceProviders;
    protected int priority;
    private String name;


    private final class ModelInstanceIterator implements Iterator<ModelInstance>
    {
        private Iterator<ModelInstanceProvider> providerIterator;
        public ModelInstanceIterator() {
            providerIterator = instanceProviders.iterator();
        }
        @Override
        public boolean hasNext() {
            return providerIterator.hasNext();
        }

        @Override
        public ModelInstance next() {
            return providerIterator.next().getModelInstance();
        }

        @Override
        public void remove() {
            providerIterator.remove();
        }
    }

    @Override
    public Iterator<ModelInstance> iterator() {
        return new ModelInstanceIterator();
    }

    public RenderLayer(int priority, String name)
    {
        instanceProviders = new ArrayList<ModelInstanceProvider>();
        this.priority = priority;
        this.name = name;
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

    public void addProvider(ModelInstanceProvider provider)
    {
        instanceProviders.add(provider);
    }
}
