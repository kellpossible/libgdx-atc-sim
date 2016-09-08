package com.atc.simulator.Display.VectorText;

import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.GnomonicProjection;
import com.atc.simulator.vectors.LineSolver;
import com.atc.simulator.vectors.TestGraphLeastSquaresCircleSolver;
import com.badlogic.gdx.math.Vector2;
import org.knowm.xchart.*;
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

    public static class XYData{
        public double[] xData;
        public double[] yData;

        public XYData(double[] xData, double[] yData)
        {
            this.xData = xData;
            this.yData = yData;
        }

        public XYData(int size)
        {
            xData = new double[size];
            yData = new double[size];
        }
    }


    public static void main(String[] arg)
    {

        // Create Chart
        XYChart chart = new XYChartBuilder().width(1000).height(1000).title("Test Graph Hershey Font").build();


        HersheyFont font = new HersheyFont();

        Vector2[][] lines = font.getCharacter(50, HersheyFont.CharacterSet.SIMPLEX);


        XYSeries[] series = new XYSeries[lines.length];
        for (int i = 0; i < lines.length; i++)
        {
            Vector2[] line = lines[i];
            XYData data = dataFromVectorList(line);
            series[i] = chart.addSeries("line " + i, data.xData, data.yData);
            series[i].setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        }

        chart.getStyler().setXAxisMin(-0.5);
        chart.getStyler().setXAxisMax(0.5);
        chart.getStyler().setYAxisMin(-0.5);
        chart.getStyler().setYAxisMax(0.5);

        // Show it
        new SwingWrapper(chart).displayChart();


    }

    public static XYData dataFromVectorList(Vector2[] vectors) {
        XYData data = new XYData(vectors.length);

        for (int i=0; i<vectors.length;i++)
        {
            data.xData[i] = vectors[i].x;
            data.yData[i] = vectors[i].y;
        }

        return data;
    }
}

