package com.atc.simulator.display.View.BitmapText;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Luke Frisken
 */
public class BitmapText {
    private Vector2 position;
    private String text;
    private BitmapFont font;

    /**
     * Constructor for BitmapText
     * @param text text to be rendered
     * @param position position in screen coordintates to buildMesh this text
     * @param font the font to buildMesh this text with.
     */
    public BitmapText(String text, Vector2 position, BitmapFont font)
    {
        this.position = position;
        this.text = text;
        this.font = font;
    }

    /**
     * Get the position of this text in screen coordinates
     * @return the position
     */
    public Vector2 getPosition()
    {
        return position;
    }

    /**
     * Get the string being rendered in this text
     * @return the text string
     */
    public String getText()
    {
        return text;
    }

    /**
     * Get the font being used to buildMesh this text
     * @return the BitmapFont being used to buildMesh this text.
     */
    public BitmapFont getFont()
    {
        return font;
    }

    /**
     * Draw this text to a sprite batch.
     * @param spriteBatch the spritebatch to draw this text to.
     */
    public void draw(SpriteBatch spriteBatch)
    {
        font.draw(spriteBatch, text, position.x, position.y);
    }
}
