package com.atc.simulator.display.view.VectorText;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a visual character which can be rendered.
 * @author Luke Frisken
 */
public class Glyph {
    private int charcode; // Hershey (not ascii) character code
    private int left; // left bearing X coordinate
    private int right; // right bearing X coordinate
    private int[][][] lines; // array of array of arrays (array of lines of points)
    private int[][] bbox; // glyph bounding box
    private int verticies; // number of vertices?
    private static final float maxSize = 32;

    /**
     *
     * @param charcode Hershey keycode
     * @param bbox bounding box
     * @param lines lines of which the character comprises of (array of lines consisting of coordinates)
     * @param left left x coordinate of the character
     * @param right right x coordinate of the character
     * @param verticies number of verticies in the character
     */
    public Glyph(int charcode, int[][] bbox, int[][][] lines, int left, int right, int verticies)
    {
        this.charcode = charcode;
        this.bbox = bbox;
        this.lines = lines;
        this.left = left;
        this.right = right;
        this.verticies = verticies;
    }

    /**
     * Method toString represents this character as a string.
     * @return String
     */
    public String toString()
    {
        return "Hershey " + getCharcode();
    }

    /**
     * Convert integer lines to a floating point vector representation
     * @return
     */
    private Vector2[][] linesToVector()
    {


        Vector2[][] newLines = new Vector2[getLines().length][];

        for (int i = 0; i < getLines().length; i++)
        {
            int[][] line = getLines()[i];
            newLines[i] = new Vector2[line.length];

            for(int j=0; j<line.length; j++)
            {
                int[] point = line[j];
                int x = point[0];
                int y = point[1];

                newLines[i][j] = new Vector2(((float) x)/ getMaxSize(), ((float) -y)/ getMaxSize());
            }
        }

        return newLines;
    }

    /**
     * Get the maximum size of this glyph.
     * @return maximum size.
     */
    public int getMaxSize()
    {
        return 32;
    }

    public Vector2[] boundsToVector()
    {
        float size = getMaxSize();
        Vector2[] newBbox = new Vector2[2];
        newBbox[0] = new Vector2(((float) getBbox()[0][0])/size, ((float) getBbox()[0][1])/size);
        newBbox[1] = new Vector2(((float) getBbox()[1][0])/size, ((float) getBbox()[1][1])/size);
        return newBbox;
    }

    /**
     * Convert this Glyph to a float glyph.
     * @return
     */
    public FloatGlyph toFloatGlyph()
    {
        FloatGlyph newGlyph = new FloatGlyph(
                getCharcode(),
                boundsToVector(),
                linesToVector(),
                ((float) getLeft())/ getMaxSize(),
                ((float) getRight())/ getMaxSize()
                );

        return newGlyph;
    }

    /**
     * Method getCharcode returns the Hershey charcode of this Glyph object.
     * @return the charcode (type int) of this Glyph object.
     */
    public int getCharcode() {
        return charcode;
    }

    /**
     * Method getLeft returns the x coordinate of the left side of this GGlyph object.
     *
     * @return the left (type int) of this Glyph object.
     */
    public int getLeft() {
        return left;
    }


    /**
     * Method getRight returns the x coordinate of the right side of this Glyph object.
     *
     * @return the right (type int) of this Glyph object.
     */
    public int getRight() {
        return right;
    }


    /**
     * Method getLines returns the lines of this Glyph object.
     * (array of lines consisting of coordinates)
     * @return the lines (type int[][][]) of this Glyph object.
     */
    public int[][][] getLines() {
        return lines;
    }

    /**
     * Method getBbox returns the bounding box of this Glyph object.
     *
     * @return the bbox (type int[][]) of this Glyph object.
     */
    public int[][] getBbox() {
        return bbox;
    }

    /**
     * Method getVerticies returns the number of verticies in this Glyph object.
     *
     * @return the verticies (type int) of this Glyph object.
     */
    public int getVerticies() {
        return verticies;
    }
}
