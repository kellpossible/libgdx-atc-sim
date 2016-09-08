package com.atc.simulator.Display.VectorText;

import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 * Hershy text font to render out using the MeshPartBuilder to construct lines.
 * @author Luke Frisken
 */
public class HershyText {
    private String text;
    private ArrayList<Vector3[]> lines;
    private Vector3 scale;
    private Vector3 position;
    private HersheyFont font;

    public HershyText(String text, HersheyFont font, Vector3 position, Vector3 scale, float depth)
    {
        this.setPosition(position);
        this.font = font;
        this.setScale(scale);
        lines = new ArrayList<Vector3[]>();

        byte[] chars = text.getBytes(StandardCharsets.US_ASCII);
        for(int j = 0; j<chars.length; j++)
        {
            byte character = chars[j];
            Vector2[][] newLines = font.getCharacterLines(character);
            if (newLines == null)
            {
                System.out.println("Character " + character);
            }

            for(Vector2[] line : newLines)
            {
                Vector3[] transformedLine = new Vector3[line.length];

                for(int i=0;i<line.length;i++)
                {
                    Vector3 point = new Vector3(line[i], depth);
                    point = point.add(new Vector3(j*0.5f + 0.5f, 0f, 0f));
                    point = point.mul(new Matrix4().setToScaling(scale));
                    point = point.add(position);
                    transformedLine[i] = point;
                }

                lines.add(transformedLine);
            }
        }
    }

    public String getText()
    {
        return text;
    }

    public void render(MeshPartBuilder builder)
    {
        for(Vector3[] line : lines)
        {
            for (int i = 1; i<line.length; i++)
            {
                builder.line(line[i-1], line[i]);
            }
        }
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public HersheyFont getFont() {
        return font;
    }
}
