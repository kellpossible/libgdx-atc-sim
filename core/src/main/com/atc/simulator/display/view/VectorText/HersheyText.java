package com.atc.simulator.display.view.VectorText;

import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 * Hershy text font to buildMesh out using the MeshPartBuilder to construct lines.
 * @author Luke Frisken
 */
public class HersheyText {
    private static float MONOSPACE_SIZE = 0.6f;
    private String text;
    private ArrayList<Vector3[]> lines;
    private Vector3 scale;
    private Vector3 position;
    private HersheyFont font;

    /**
     * Constructor for HersheyText
     * @param text String of text contained in this text to be displayed
     * @param font font to render this text with
     * @param position position to render this text within its model
     * @param scale scale of this text within its model
     * @param depth z depth of this text within its model
     */
    public HersheyText(String text, HersheyFont font, Vector3 position, Vector3 scale, float depth)
    {
        this.setPosition(position);
        this.font = font;
        this.setScale(scale);
        lines = new ArrayList<Vector3[]>();

        byte[] chars = text.getBytes(StandardCharsets.US_ASCII);
        FloatGlyph previousGlyph = null;

        float xScale = scale.x;
        float totalShift = 0;
        for(int j = 0; j<chars.length; j++)
        {
            byte character = chars[j];
            FloatGlyph glyph = font.getGlyph(character);

            totalShift += MONOSPACE_SIZE;

// start of an implementation for type spacing
//            float x_offset = -glyph.left;
//            if (previousGlyph != null)
//            {
//                x_offset += previousGlyph.right;
//            }

            if (glyph == null)
            {
                System.out.println("Character " + character);
            }

            for(Vector2[] line : glyph.getLines())
            {
                Vector3[] transformedLine = new Vector3[line.length];

                for(int i=0;i<line.length;i++)
                {
                    Vector3 point = new Vector3(line[i], depth);
                    point = point.add(new Vector3(totalShift, 0f, 0f));
                    point = point.mul(new Matrix4().setToScaling(scale));
                    point = point.add(position);
                    transformedLine[i] = point;
                }

                lines.add(transformedLine);
            }

            previousGlyph = glyph;
        }
    }

    /**
     * Get the String text contained in this text.
     * @return
     */
    public String getText()
    {
        return text;
    }

    /**
     * Build the mesh for this text object.
     * @param builder MeshPartBuilder with which to build the mesh.
     */
    public void buildMesh(MeshPartBuilder builder)
    {
        for(Vector3[] line : lines)
        {
            for (int i = 1; i<line.length; i++)
            {
                builder.line(line[i-1], line[i]);
            }
        }
    }

    /**
     * Get the scale of this text.
     * @return
     */
    public Vector3 getScale() {
        return scale;
    }

    /**
     * Set the scale of this text
     * @param scale
     */
    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    /**
     * Get the position of this text.
     * @return
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Set the position of this text.
     * @param position
     */
    public void setPosition(Vector3 position) {
        this.position = position;
    }

    /**
     * Get the font that this text is using.
     * @return
     */
    public HersheyFont getFont() {
        return font;
    }
}
