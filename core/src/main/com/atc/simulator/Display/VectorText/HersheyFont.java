package com.atc.simulator.Display.VectorText;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

        public float[][][] linesToFloat()
        {
            float size = right - left;

            float[][][] newLines = new float[lines.length][2][2];

            for (int i = 0; i < lines.length; i++)
            {
                int[][] line = lines[i];

                for(int j=0; j<2; j++)
                {
                    int[] point = line[j];
                    int x = point[0];
                    int y = point[1];

                    float newX = ((float) x)/size;
                    float newY = ((float) y)/size;

                    newLines[i][j][0] = newX;
                    newLines[i][j][0] = newY;
                }
            }

            return newLines;
        }

        public float[][] boundsToFloat()
        {
            float size = right - left;
            float[][] newBbox = new float[2][2];
            newBbox[0][0] = ((float) bbox[0][0])/size;
            newBbox[0][1] = ((float) bbox[0][1])/size;
            newBbox[1][0] = ((float) bbox[1][0])/size;
            newBbox[1][1] = ((float) bbox[1][1])/size;
            return newBbox;
        }

        public FloatGlyph toFloatGlyph()
        {
            FloatGlyph newGlyph = new FloatGlyph();
            newGlyph.bbox = boundsToFloat();
            newGlyph.lines = linesToFloat();
            newGlyph.charcode = charcode;
            return newGlyph;
        }
    }

    private class FloatGlyph {
        int charcode; // Hershey (not ascii) character code
        float[][] bbox; // glyph bounding box
        float[][][] lines; // array of array of arrays (array of lines of points)

        public String toString()
        {
            return "Hershey " + charcode;
        }

    }

    private HashMap<Integer, FloatGlyph> glyphs; //glyphs referenced by their Hershey Code
    private HashMap<Integer, FloatGlyph> simplexGlyphs; //simplex glyph set referenced by their ASCII code
    private HashMap<CharacterSet, HashMap<Integer, FloatGlyph>> characterSets;

    public enum CharacterSet {
        SIMPLEX
    }

    public HersheyFont()
    {
        characterSets = new HashMap<CharacterSet, HashMap<Integer, FloatGlyph>>();
        try {
            FileReader fileReader = new FileReader("assets/fonts/hershey-occidental.json");
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(fileReader);
            Glyph[] glyphs = gson.fromJson(jsonReader, Glyph[].class);

            this.glyphs = new HashMap<Integer, FloatGlyph>();
            for (Glyph glyph : glyphs)
            {
                this.glyphs.put(glyph.charcode, glyph.toFloatGlyph());
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

    public float[][][] getCharacter(int keycode, CharacterSet characterSet)
    {
        return characterSets.get(characterSet).get(keycode).lines;
    }
}
