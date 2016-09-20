package com.atc.simulator.Display.Model;

/**
 * @author Luke Frisken
 */
public class DisplayHud {
    private int numInstances = 0;
    private float crossHairSize = 10f;
    private Display display;

    /**
     * Constructor for DisplayHUD
     * @param display
     */
    public DisplayHud(Display display)
    {
        this.display = display;
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
    }

    /**
     * Get the display object.
     * @return
     */
    public Display getDisplay() {
        return display;
    }
}
