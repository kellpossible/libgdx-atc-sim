package com.atc.simulator.display.view.vector_text;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.atc.simulator.display.view.vector_text.HersheyFont.CharacterSet.SIMPLEX;

/**
 * A single line font which can be rendered using opengl lines
 * as libgdx 3d models.
 *
 * Useful resources:
 * http://www.astro.caltech.edu/~tjp/pgplot/hershey.html
 * http://paulbourke.net/dataformats/hershey/
 *
 * Data source from:
 * https://github.com/scruss/python-hershey
 * License is also there.
 *
 * @author Luke Frisken
 */
public class HersheyFont {
    private HashMap<Integer, FloatGlyph> glyphs; //glyphs referenced by their Hershey Code
    private HashMap<Integer, FloatGlyph> simplexGlyphs; //simplex glyph set referenced by their ASCII code
    private HashMap<CharacterSet, HashMap<Integer, FloatGlyph>> characterSets;
    private CharacterSet characterSet;

    /**
     * Types of character sets available to load.
     */
    public enum CharacterSet {
        SIMPLEX
    }

    /**
     * Constructor for HersheyFont
     * @param characterSet Which character set to load
     */
    public HersheyFont(CharacterSet characterSet)
    {
        this.characterSet = characterSet;

        characterSets = new HashMap<CharacterSet, HashMap<Integer, FloatGlyph>>();
        try {
            FileReader fileReader = new FileReader("assets/fonts/hershey-occidental.json");
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(fileReader);
            Glyph[] intGlyphs = gson.fromJson(jsonReader, Glyph[].class);

            this.glyphs = new HashMap<Integer, FloatGlyph>();
            for (Glyph glyph : intGlyphs)
            {
                FloatGlyph floatGlyph = glyph.toFloatGlyph();
                this.glyphs.put(glyph.getCharcode(), floatGlyph);
            }

            fileReader.close();
            jsonReader.close();

            initSimplex();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class definition for use by gson to load the index
     * mapping hershey codes to ascii codes for the simplex character set.
     */
    private class SimplexIndex {
        Map<Integer, Integer> keycodes;
    }

    /**
     * Load the simplex font
     * @throws FileNotFoundException
     */
    private void initSimplex() throws FileNotFoundException {
        FileReader fileReader = new FileReader("assets/fonts/hershey-simplex-index.json");
        Gson gson = new Gson();
        SimplexIndex simplexIndex = gson.fromJson(fileReader, SimplexIndex.class);

        simplexGlyphs = new HashMap<Integer, FloatGlyph>();

        for (int asciiIndex : simplexIndex.keycodes.keySet())
        {
            int hersheyIndex = simplexIndex.keycodes.get(asciiIndex);
            FloatGlyph glyph = glyphs.get(hersheyIndex);
            if (glyph != null)
            {
                simplexGlyphs.put(asciiIndex, glyph);
            }


        }

        characterSets.put(SIMPLEX, simplexGlyphs);
    }

    /**
     * Get the character glyph belonging to this font/characterset by its ascii key code.
     * @param keycode ascii code of character to get.
     * @return FloatGlyph character in font corresponding to the provided keycode.
     */
    public FloatGlyph getGlyph(int keycode)
    {
        if (keycode == 32)
        {
            return FloatGlyph.SPACE;
        }
        HashMap<Integer, FloatGlyph>  glyphs = characterSets.get(characterSet);
        FloatGlyph glyph = glyphs.get(keycode);
        if (glyph == null)
        {
            return null;
        } else {
            return glyph;
        }
    }
}
