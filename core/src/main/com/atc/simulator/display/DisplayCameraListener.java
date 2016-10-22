package com.atc.simulator.display;

import com.badlogic.gdx.graphics.Camera;

/**
 * Listener interface for classes wishing to listen to
 * updates provided by the display's camera.
 * @author Luke Frisken
 */
public interface DisplayCameraListener {

    class CameraUpdate
    {
        public Camera camera;
        public DisplayCameraListener.UpdateType updateType;

        public CameraUpdate(Camera camera, DisplayCameraListener.UpdateType updateType)
        {
            this.camera = camera;
            this.updateType = updateType;
        }
    }

    /**
     * Enum UpdateType ...
     * The types of updates that this listener can choose to listen to.
     * @author Luke Frisken
     */
    enum UpdateType {
        /**
         * update where the camera field of view has changed (zoomed)
         */
        ZOOM,
        /**
         * update where the camera has been panned
         */
        PAN,
        /**
         * update where the window has been resized
         */
        RESIZE
    }

    /**
     * Called when the camera has been updated.
     * @param cameraUpdate the data for the camera update
     */
    void onUpdate(CameraUpdate cameraUpdate);
}
