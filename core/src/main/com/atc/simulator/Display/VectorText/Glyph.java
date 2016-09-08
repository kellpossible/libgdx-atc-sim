package com.atc.simulator.Display.VectorText;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by luke on 9/09/16.
 */
public class Glyph {
    int charcode; // Hershey (not ascii) character code
    int left; // left bearing X coordinate
    int right; // right bearing X coordinate
    int[][][] lines; // array of array of arrays (array of lines of points)
    int[][] bbox; // glyph bounding box
    int verticies; // number of vertices?
    float maxSize = 32;

    public String toString()
    {
        return "Hershey " + charcode;
    }

    public Vector2[][] linesToVector()
    {


        Vector2[][] newLines = new Vector2[lines.length][];

        for (int i = 0; i < lines.length; i++)
        {
            int[][] line = lines[i];
            newLines[i] = new Vector2[line.length];

            for(int j=0; j<line.length; j++)
            {
                int[] point = line[j];
                int x = point[0];
                int y = point[1];

                newLines[i][j] = new Vector2(((float) x)/ maxSize, ((float) -y)/ maxSize);
            }
        }

        return newLines;
    }

    public int getMaxSize()
    {
        return (right - left)*2;
    }

    public Vector2[] boundsToVector()
    {
        float size = getMaxSize();
        Vector2[] newBbox = new Vector2[2];
        newBbox[0] = new Vector2(((float) bbox[0][0])/size, ((float) bbox[0][1])/size);
        newBbox[1] = new Vector2(((float) bbox[1][0])/size, ((float) bbox[1][1])/size);
        return newBbox;
    }

    public FloatGlyph toFloatGlyph()
    {
        FloatGlyph newGlyph = new FloatGlyph();
        newGlyph.bbox = boundsToVector();
        newGlyph.lines = linesToVector();
        newGlyph.charcode = charcode;
        newGlyph.left = ((float) left)/ maxSize;
        newGlyph.right = ((float) right)/maxSize;

        return newGlyph;
    }
}
