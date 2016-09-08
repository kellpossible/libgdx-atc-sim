package com.atc.simulator.Display;

import com.badlogic.gdx.graphics.Camera;

/**
 * Created by luke on 8/09/16.
 */
public interface DisplayCameraListener {
    enum UpdateType {
        ZOOM,
        PAN,
        RESIZE
    }
    void onUpdate(Camera camera, UpdateType updateType);
}
