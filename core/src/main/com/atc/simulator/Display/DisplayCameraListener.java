package com.atc.simulator.Display;

import com.badlogic.gdx.graphics.Camera;

/**
 * Listener interface for classes wishing to listen to
 * updates provided by the display's camera.
 * @author Luke Frisken
 */
public interface DisplayCameraListener {

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
     * @param camera the camera providing the update
     * @param updateType the type of update that happened
     */
    void onUpdate(Camera camera, UpdateType updateType);
}
