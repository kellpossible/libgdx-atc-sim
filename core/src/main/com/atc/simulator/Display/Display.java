package com.atc.simulator.Display;

import com.atc.simulator.Display.DisplayData.DisplayHud;
import com.atc.simulator.flightdata.DelayedWork.DelayedWorkQueueItem;
import com.atc.simulator.flightdata.DelayedWork.DelayedWorkQueueItemType;
import com.atc.simulator.flightdata.DelayedWork.DelayedWorker;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Timer;

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
    private HashMap<DisplayCameraListener, DelayedCameraListener> delayedCameraListenerHashMap;
    private DelayedWorker delayedWorker;
    private static final int WORK_UNITS_PER_FRAME = 100;
    private DisplayHud displayHud;

    /**
     * Constructor for Display
     */
    public Display()
    {
        cameras = new HashMap<String, Camera>();
        cameraListeners = new ArrayList<ObjectMap.Entry<DisplayCameraListener, Camera>>();
        delayedCameraListenerHashMap = new HashMap<DisplayCameraListener, DelayedCameraListener>();
        delayedWorker = new DelayedWorker(WORK_UNITS_PER_FRAME);
        displayHud = new DisplayHud();
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

    public DisplayHud getDisplayHud() {
        return displayHud;
    }


    private class DelayedCameraListener extends DelayedWorkQueueItemType implements DisplayCameraListener
    {
        private DisplayCameraListener originalListener;

        public DelayedCameraListener(DisplayCameraListener originalListener, int priority, int cost)
        {
            super(priority, cost);
            this.originalListener = originalListener;
        }

        @Override
        public void run(DelayedWorkQueueItem workItem) {
            Object data = workItem.getData();
            CameraUpdate cameraUpdate = (CameraUpdate) data;
            originalListener.onUpdate(cameraUpdate);
        }

        /**
         * Called when the camera has been updated.
         *
         * @param cameraUpdate the data for the camera update
         */
        @Override
        public void onUpdate(CameraUpdate cameraUpdate) {
            delayedWorker.addWorkItem(createWorkItem(cameraUpdate));
        }
    }

    public void addDelayedCameraListener(Camera camera, DisplayCameraListener listener, int priority, int cost)
    {
        DelayedCameraListener delayedCameraListener = new DelayedCameraListener(listener, priority, cost);
        delayedWorker.addWorkItemType(delayedCameraListener);
        delayedCameraListenerHashMap.put(listener, delayedCameraListener);
        addCameraListener(camera, delayedCameraListener);
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
     * @return boolean whether or not a camera listener was removed.
     */
    public boolean removeCameraListener(Camera camera, DisplayCameraListener listener)
    {
        DisplayCameraListener removeListener = listener;
        DelayedCameraListener delayedCameraListener = delayedCameraListenerHashMap.get(removeListener);

        if (delayedCameraListener != null)
        {
            delayedWorker.removeWorkItemType(delayedCameraListener);
            delayedCameraListenerHashMap.remove(removeListener);
            removeListener = delayedCameraListener.originalListener;
        }


        ArrayList<ObjectMap.Entry> removeEntries = new ArrayList<ObjectMap.Entry>();
        for (ObjectMap.Entry<DisplayCameraListener, Camera> entry : cameraListeners)
        {
            if (entry.key == removeListener && entry.value == camera)
            {
                removeEntries.add(entry);
            }
        }

        for(ObjectMap.Entry entry : removeEntries)
        {
            cameraListeners.remove(entry);
        }




        return removeEntries.size() > 0;
    }

    /**
     * Trigger an update event on a camera in the display.
     * @param cameraUpdate the data for the camera update
     */
    public void triggerCameraOnUpdate(DisplayCameraListener.CameraUpdate cameraUpdate)
    {
        for (ObjectMap.Entry<DisplayCameraListener, Camera> entry : cameraListeners)
        {
            if (entry.value == cameraUpdate.camera)
            {
                entry.key.onUpdate(cameraUpdate);
            }
        }
    }

    public void update()
    {
        delayedWorker.run();
    }

}
