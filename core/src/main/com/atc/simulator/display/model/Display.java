package com.atc.simulator.display.model;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.display.DisplayCameraListener;
import com.atc.simulator.display.LayerManager;
import com.atc.simulator.flightdata.delayed_work.DelayedWorkQueueItem;
import com.atc.simulator.flightdata.delayed_work.DelayedWorkQueueItemType;
import com.atc.simulator.flightdata.delayed_work.DelayedWorker;
import com.atc.simulator.flightdata.TimeSource;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Object model for the display
 * @author Luke Frisken
 */
public class Display {
    private PredictionDisplayMethod predictionDisplayMethod =
            (PredictionDisplayMethod) ApplicationConfig.getEnum("settings.display.prediction-display-method",
                    PredictionDisplayMethod.class);
    private LayerManager layerManager;
    private HashMap<String, Camera> cameras;
    private ArrayList<ObjectMap.Entry<DisplayCameraListener, Camera>> cameraListeners;
    private HashMap<DisplayCameraListener, DelayedCameraListener> delayedCameraListenerHashMap;
    private DelayedWorker delayedWorker;
    private static final int WORK_UNITS_PER_FRAME = 100;
    private DisplayHud displayHud;
    private TimeSource timeSource;
    private DisplayTracks displayTracks;

    /**
     * Constructor for display
     */
    public Display(TimeSource timeSource)
    {
        this.timeSource = timeSource;
        cameras = new HashMap<String, Camera>();
        cameraListeners = new ArrayList<ObjectMap.Entry<DisplayCameraListener, Camera>>();
        delayedCameraListenerHashMap = new HashMap<DisplayCameraListener, DelayedCameraListener>();
        delayedWorker = new DelayedWorker(WORK_UNITS_PER_FRAME);
    }

    public void cyclePredictionDisplayMethod()
    {
        PredictionDisplayMethod[] displayMethods = PredictionDisplayMethod.values();
        for (int i = 0; i < displayMethods.length; i++)
        {
            if(displayMethods[i] == predictionDisplayMethod)
            {
                if ((i+1) == displayMethods.length)
                {
                    predictionDisplayMethod = displayMethods[0];
                } else {
                    predictionDisplayMethod = displayMethods[i+1];
                }
                return;
            }
        }

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

    /**
     * Get the display's {@link DisplayHud}
     * @return the hud which belongs to this display
     */
    public DisplayHud getDisplayHud() {
        return displayHud;
    }

    /**
     * Set the display hud
     * @param displayHud the new value for displayHud
     */
    public void setDisplayHud(DisplayHud displayHud)
    {
        this.displayHud = displayHud;
    }


    /**
     * Get the Scenario's Tracks
     * @return the Tracks Model being used in this display
     */
    public DisplayTracks getDisplayTracks(){return this.displayTracks;}
    /**
     * Set the Track display Model
     * @param displayTrack the new Model for displayTracks
     */
    public void setDisplayTracks(DisplayTracks displayTrack)
    {
        this.displayTracks = displayTrack;
    }

    /**
     * Get the display's time source
     * @return TimeSource the display's time source
     */
    public TimeSource getTimeSource() {
        return timeSource;
    }

    /**
     * Get the current prediction display method as per {@link PredictionDisplayMethod}
     * @return display method currently employed
     */
    public PredictionDisplayMethod getPredictionDisplayMethod() {
        return predictionDisplayMethod;
    }

    /**
     * Set the prediction display method as per {@link PredictionDisplayMethod}
     */
    public void setPredictionDisplayMethod(PredictionDisplayMethod predictionDisplayMethod) {
        this.predictionDisplayMethod = predictionDisplayMethod;
    }

    /**
     * A {@link DelayedWorkQueueItemType} which implements display camera listener
     * in order to be able to put updates to models due to changes to the camera into
     * the {@link DelayedWorker}'s queue so that it can be processed gradually
     * rather than bogging down the framerate and doing an update each time the camera changes
     * (which while zooming could be once every frame).
     */
    private class DelayedCameraListener extends DelayedWorkQueueItemType implements DisplayCameraListener
    {
        private DisplayCameraListener originalListener;

        /**
         * Constructor for DelayedCameraListener
         * @param originalListener the original DisplayCameraListener
         * @param priority the priority of this listener
         * @param cost the cost in "work points" for processing work items generated by this
         */
        public DelayedCameraListener(DisplayCameraListener originalListener, int priority, int cost)
        {
            super(priority, cost);
            this.originalListener = originalListener;
        }

        /**
         * The method which is called when the work items that are created using this type
         * need to be processed.
         * @param workItem work item being processed.
         */
        @Override
        public void run(DelayedWorkQueueItem workItem) {
            Object data = workItem.getData();
            CameraUpdate cameraUpdate = (CameraUpdate) data;
            originalListener.onUpdate(cameraUpdate);
        }

        /**
         * Called when the camera has been updated.
         * @param cameraUpdate the data for the camera update
         */
        @Override
        public void onUpdate(CameraUpdate cameraUpdate) {
            delayedWorker.addWorkItem(createWorkItem(cameraUpdate));
        }
    }

    /**
     * Add a new {@link DelayedCameraListener} to this display and to it's {@link DelayedWorker}.
     * @param camera camera to listen to
     * @param listener listener which will be listening to that camera
     * @param priority priority of work items generated from updates to the camera
     * @param cost cost of work items generated from updates to the camera in "work points"
     */
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
            removeListener = delayedCameraListener;
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

    /**
     * Update the display
     */
    public void update()
    {
        delayedWorker.run();
    }

}
