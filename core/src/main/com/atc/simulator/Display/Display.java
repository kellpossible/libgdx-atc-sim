package com.atc.simulator.Display;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luke on 8/09/16.
 */
public class Display {
    private LayerManager layerManager;
    private HashMap<String, Camera> cameras;
    private ArrayList<ObjectMap.Entry<DisplayCameraListener, Camera>> cameraListeners;

    public Display()
    {
        cameras = new HashMap<String, Camera>();
        cameraListeners = new ArrayList<ObjectMap.Entry<DisplayCameraListener, Camera>>();
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

    public void addCameraListener(Camera camera, DisplayCameraListener listener)
    {
        ObjectMap.Entry entry = new ObjectMap.Entry<DisplayCameraListener, Camera>();
        entry.key = listener;
        entry.value = camera;
        cameraListeners.add(entry);
    }

    public void removeCameraListener(Camera camera, DisplayCameraListener listener)
    {
        ArrayList<ObjectMap.Entry> removeEntries = new ArrayList<ObjectMap.Entry>();
        for (ObjectMap.Entry<DisplayCameraListener, Camera> entry : cameraListeners)
        {
            if (entry.key == listener && entry.value == camera)
            {
                removeEntries.add(entry);
            }
        }

        for(ObjectMap.Entry entry : removeEntries)
        {
            cameraListeners.remove(entry);
        }
    }

    public void triggerCameraOnUpdate(Camera camera, DisplayCameraListener.UpdateType updateType)
    {
        for (ObjectMap.Entry<DisplayCameraListener, Camera> entry : cameraListeners)
        {
            if (entry.value == camera)
            {
                entry.key.onUpdate(camera, updateType);
            }
        }
    }

}
