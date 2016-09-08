package com.atc.simulator.Display.VectorText;

import com.atc.simulator.flightdata.SystemState;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.atc.simulator.Display.VectorText.HersheyFont.CharacterSet.SIMPLEX;

/**
 * @author Luke Frisken
 */
public class HersheyFont {



    private class Glyph {
        int charcode; // Hershey (not ascii) character code
        int left; // left bearing X coordinate
        int right; // right bearing X coordinate
        int[][][] lines; // array of array of arrays (array of lines of points)
        int[][] bbox; // glyph bounding box
        int verticies; // number of vertices?


        public String toString()
        {
            return "Hershey " + charcode;
        }

        public Vector2[][] linesToVector()
        {
            float size = getSize();

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

                    newLines[i][j] = new Vector2(((float) x)/size, ((float) -y)/size);
                }
            }

            return newLines;
        }

        public int getSize()
        {
            return (right - left)*2;
        }

        public Vector2[] boundsToVector()
        {
            float size = getSize();
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


            return newGlyph;
        }
    }

    private class FloatGlyph {
        int charcode; // Hershey (not ascii) character code
        Vector2[] bbox; // glyph bounding box
        Vector2[][] lines; // array of array of arrays (array of lines of points)

        public String toString()
        {
            return "Hershey " + charcode;
        }

    }

    private HashMap<Integer, FloatGlyph> glyphs; //glyphs referenced by their Hershey Code
    private HashMap<Integer, FloatGlyph> simplexGlyphs; //simplex glyph set referenced by their ASCII code
    private HashMap<CharacterSet, HashMap<Integer, FloatGlyph>> characterSets;
    private CharacterSet characterSet;

    public enum CharacterSet {
        SIMPLEX
    }

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
                this.glyphs.put(glyph.charcode, floatGlyph);
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

    class SimplexIndex {
        Map<Integer, Integer> keycodes;
    }

    void initSimplex() throws FileNotFoundException {
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

    public Vector2[][] getCharacterLines(int keycode)
    {
        if (keycode == 32)
        {
            return new Vector2[0][0];
        }
        HashMap<Integer, FloatGlyph>  glyphs = characterSets.get(characterSet);
        FloatGlyph glyph = glyphs.get(keycode);
        if (glyph == null)
        {
            return null;
        } else {
            return glyph.lines;
        }
    }
}
