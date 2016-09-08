package com.atc.simulator.Display.VectorText;

import com.badlogic.gdx.math.Vector2;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.util.ArrayList;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * @author Luke Frisken
 */

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

        HersheyFont font = new HersheyFont(HersheyFont.CharacterSet.SIMPLEX);

        ArrayList<Vector2[]> linesArrayList = new ArrayList<Vector2[]>();

        for (int x = 32; x <= 127; x++)
        {
            Vector2[][] lines = font.getCharacterLines(x);

            if (lines == null)
            {
                continue;
            }


            for (Vector2[] line: lines)
            {
                Vector2[] shiftedLine = new Vector2[line.length];
                for (int line_i = 0; line_i <line.length; line_i++)
                {
                    shiftedLine[line_i] = line[line_i].add(new Vector2(x%12*0.5f, -x/12));
                }
                linesArrayList.add(shiftedLine);
            }
        }


        XYSeries[] series = new XYSeries[linesArrayList.size()];
        for (int i = 0; i < linesArrayList.size(); i++)
        {
            Vector2[] line = linesArrayList.get(i);
            XYData data = dataFromVectorList(line);
            series[i] = chart.addSeries("line " + i, data.xData, data.yData);
            series[i].setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
            series[i].setMarker(SeriesMarkers.NONE);
        }




//        chart.getStyler().setXAxisMin(-0.5);
//        chart.getStyler().setXAxisMax(0.5);
//        chart.getStyler().setYAxisMin(-0.5);
//        chart.getStyler().setYAxisMax(0.5);

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

