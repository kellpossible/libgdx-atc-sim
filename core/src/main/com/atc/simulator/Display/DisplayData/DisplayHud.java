package com.atc.simulator.Display.DisplayData;

/**
 * @author Luke Frisken
 */
public class DisplayHud {
    private int numInstances = 0;
    private float crossHairSize = 10f;
    public DisplayHud()
    {

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
}
