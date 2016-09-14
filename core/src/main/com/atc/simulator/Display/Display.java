package com.atc.simulator.Display;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Object model for the display
 * @author Luke Frisken
 */
public class Display {
    private LayerManager layerManager;
    private HashMap<String, Camera> cameras;
    private ArrayList<ObjectMap.Entry<DisplayCameraListener, Camera>> cameraListeners;

    /**
     * Constructor for Display
     */
    public Display()
    {
        cameras = new HashMap<String, Camera>();
        cameraListeners = new ArrayList<ObjectMap.Entry<DisplayCameraListener, Camera>>();
    }

    /**
     * Get the layer manager
     * @return the layer manager
     */
    public LayerManager getLayerManager() {
        return layerManager;
    }

    /**
     * Set the display's layer manager
     * @param layerManager the display's layermanager
     */
    public void setLayerManager(LayerManager layerManager) {
        this.layerManager = layerManager;
    }

    /**
     * Add a camera to the display
     * @param cameraName name to reference the camera by
     * @param camera camera object
     */
    public void addCamera(String cameraName, Camera camera)
    {
        cameras.put(cameraName, camera);
    }

    /**
     * get a camera, as referenced by the name it was added with.
     * @param cameraName name referring to a camera in the display.
     * @return camera associated with cameraName provided.
     */
    public Camera getCamera(String cameraName)
    {
        return cameras.get(cameraName);
    }

    public void addDelayedCameraListener(Camera camera, DisplayCameraListener listener, int priority, int cost)
    {

    }

    /**
     * add a camera listener to this display.
     * @param camera the camera being listened too by the listener
     * @param listener listener of type DisplayCameraListener
     */
    public void addCameraListener(Camera camera, DisplayCameraListener listener)
    {
        ObjectMap.Entry entry = new ObjectMap.Entry<DisplayCameraListener, Camera>();
        entry.key = listener;
        entry.value = camera;
        cameraListeners.add(entry);
    }

    /**
     * Remove a camera listener from this display.
     * @param camera camera that the listener was listening to.
     * @param listener listener to remove.
     */
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

    /**
     * Trigger an update event on a camera in the display.
     * @param camera camera that was updated
     * @param updateType type of update
     */
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
