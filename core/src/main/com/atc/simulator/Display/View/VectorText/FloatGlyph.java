package com.atc.simulator.Display.View.VectorText;

import com.badlogic.gdx.math.Vector2;

/**
 * A {@link com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph} who's position
 * data is stored in floating point.
 * @author Luke Frisken
 */
public class FloatGlyph {
    public static final FloatGlyph SPACE;

    static {
        SPACE = new FloatGlyph(
                197,
                new Vector2[]{new Vector2(-0.5f, -0.5f), new Vector2(0.5f, 0.5f)},
                new Vector2[0][0],
                -0.5f,
                0.5f
                );
    }

    private int charcode; // Hershey (not ascii) character code
    private Vector2[] bbox; // glyph bounding box
    private Vector2[][] lines; // array of array of arrays (array of lines of points)
    private float left;
    private float right;

    public FloatGlyph(int charcode, Vector2[] bbox, Vector2[][] lines, float left, float right)
    {
        this.charcode = charcode;
        this.bbox = bbox;
        this.lines = lines;
        this.left = left;
        this.right = right;
    }

    public String toString()
    {
        return "Hershey " + getCharcode();
    }

    /**
     * Method getCharcode returns the Hershey charcode of this FloatGlyph object.
     * @return the charcode (type int) of this FloatGlyph object.
     */
    public int getCharcode() {
        return charcode;
    }

    /**
     * Method getBbox returns the bounding box of this FloatGlyph object.
     *
     * @return the bbox (type Vector2[]) of this FloatGlyph object.
     */
    public Vector2[] getBbox() {
        return bbox;
    }

    /**
     * Method getLines returns the lines of this FloatGlyph object.
     * (array of lines consisting of coordinates)
     * @return the lines (type Vector2[][]) of this FloatGlyph object.
     */
    public Vector2[][] getLines() {
        return lines;
    }

    /**
     * Method getLeft returns the x coordinate of the left side of this FloatGlyph object.
     *
     * @return the left (type float) of this FloatGlyph object.
     */
    public float getLeft() {
        return left;
    }

    /**
     * Method getRight returns the x coordinate of the right side of this FloatGlyph object.
     *
     * @return the right (type float) of this FloatGlyph object.
     */
    public float getRight() {
        return right;
    }
}
