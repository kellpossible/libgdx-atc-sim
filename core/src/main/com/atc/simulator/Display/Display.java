package com.atc.simulator.Display;

import com.badlogic.gdx.graphics.Camera;

import java.util.HashMap;

/**
 * Created by luke on 8/09/16.
 */
public class Display {
    private LayerManager layerManager;
    private HashMap<String, Camera> cameras;

    public Display()
    {
        cameras = new HashMap<String, Camera>();
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }

    public void setLayerManager(LayerManager layerManager) {
        this.layerManager = layerManager;
    }

    public void addCamera(String cameraName, Camera camera)
    {
        cameras.put(cameraName, camera);
    }

    public Camera getCamera(String cameraName)
    {
        return cameras.get(cameraName);
    }

}
