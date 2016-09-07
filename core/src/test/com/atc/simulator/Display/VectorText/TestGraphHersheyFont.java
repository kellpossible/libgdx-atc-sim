package com.atc.simulator.Display.VectorText;

import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.GnomonicProjection;
import com.atc.simulator.vectors.LineSolver;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import pythagoras.d.Line;
import pythagoras.d.Vector3;

import java.util.ArrayList;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import pythagoras.d.Circle;
import pythagoras.d.Line;
import pythagoras.d.Matrix3;
import pythagoras.d.Vector3;

import java.util.ArrayList;

/**
 * @author Luke Frisken
 */

import com.atc.simulator.Display.VectorText.HersheyFont;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import pythagoras.d.Circle;
import pythagoras.d.Line;
import pythagoras.d.Matrix3;
import pythagoras.d.Vector3;

import java.util.ArrayList;

/**
 * @author Luke Frisken
 */
public class TestGraphHersheyFont {
    public static void main(String[] arg)
    {
        HersheyFont font = new HersheyFont();

        float[][][] lines = font.getCharacter(35, HersheyFont.CharacterSet.SIMPLEX);


        double[] xData = new double[lines.length*2*2];
        double[] yData = new double[lines.length*2*2];

        int k = 0;
        for (int i = 0; i < lines.length; i++)
        {
            float[][] line = lines[i];

            for(int j=0; j<2; j++)
            {
                float[] point = line[j];
                float x = point[0];
                float y = point[1];

                xData[k] = x;
                yData[k] = y;
                k++;
            }
        }

        System.out.println("WHAT THE HELL");

        // Create Chart
        XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
        chart.setWidth(1000);
        chart.setHeight(1000);

        // Show it
        new SwingWrapper(chart).displayChart();


    }
}

