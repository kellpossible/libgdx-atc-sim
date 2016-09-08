package com.atc.simulator.Display.VectorText;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by luke on 9/09/16.
 */
public class FloatGlyph {
    public static final FloatGlyph SPACE;

    static {
        SPACE = new FloatGlyph();
        SPACE.left = -0.5f;
        SPACE.right = 0.5f;
        SPACE.bbox = new Vector2[]{new Vector2(-0.5f, -0.5f), new Vector2(0.5f, 0.5f)};
        SPACE.lines = new Vector2[0][0];
        SPACE.charcode = 197;
    }

    int charcode; // Hershey (not ascii) character code
    Vector2[] bbox; // glyph bounding box
    Vector2[][] lines; // array of array of arrays (array of lines of points)
    float left;
    float right;

    public String toString()
    {
        return "Hershey " + charcode;
    }

}
