package com.atc.simulator.Display.Model;

import com.atc.simulator.Display.View.ModelInstanceProviders.HudModel;

/**
 * @author Luke Frisken
 */
public class DisplayHud {
    private int numInstances = 0;
    private float crossHairSize = 10f;
    private Display display;
    private HudModel hudModel;

    /**
     * Constructor for DisplayHUD
     * @param display
     */
    public DisplayHud(Display display, HudModel hudModel)
    {
        this.display = display;
        this.hudModel = hudModel;
    }

    /**
     * Set the hud model
     * @param hudModel
     */
    public void setHudModel(HudModel hudModel)
    {
        this.hudModel = hudModel;
    }

    /**
     * Get the hud's recorded number of instances being rendered
     * @return
     */
    public int getNumInstances() {
        return numInstances;
    }

    /**
     * Set the hud's text for the number of instances being rendered
     * @param numInstances
     */
    public void setNumInstances(int numInstances) {
        this.numInstances = numInstances;
        update();
    }

    /**
     * Get the crosshair size
     * @return
     */
    public float getCrossHairSize() {
        return crossHairSize;
    }

    /**
     * Set the croshair size.
     * @param crossHairSize
     */
    public void setCrossHairSize(float crossHairSize) {
        this.crossHairSize = crossHairSize;
        update();
    }

    /**
     * Get the display object.
     * @return
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * Update the model
     */
    public void update()
    {
        hudModel.update();
    }
}
