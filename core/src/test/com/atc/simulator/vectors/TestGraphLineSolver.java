package com.atc.simulator.vectors;

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
public class TestGraphLineSolver {
    public static void main(String[] arg)
    {
        GeographicCoordinate reference = GeographicCoordinate.fromDegrees(0, -37.811151, 144.963714);

        GnomonicProjection projection = new GnomonicProjection(reference);

        ArrayList<Vector3> data1 = new ArrayList<Vector3>();
        data1.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.8342164,144.8746305)));
        data1.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.8205219,144.9055724)));
        data1.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.8094357,144.9322658)));
        data1.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.8014675,144.9607615)));
        data1.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.7927862,144.9880128)));
        data1.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.7901409,145.0093846)));

        ArrayList<Vector3> data2 = new ArrayList<Vector3>();
        data2.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.729742, 144.932598)));
        data2.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.728076, 144.941279)));
        data2.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.720362, 144.948843)));
        data2.add(projection.transformPositionTo(GeographicCoordinate.fromDegrees(0, -37.714653, 144.949926)));


        for(Vector3 point: data2)
        {
            System.out.println("[" + point.x + ", " + point.y + "]");
        }

        Line fit = LineSolver.LeastSquaresFit(data1);

        ArrayList<Vector3> positions = new ArrayList<Vector3>();
        positions.addAll(data1);
        positions.add(new Vector3(fit.x1, fit.y1, 0));
        positions.add(new Vector3(fit.x2, fit.y2, 0));


        double[] xData = new double[positions.size()];
        double[] yData = new double[positions.size()];
        for (int i=0; i<positions.size();i++)
        {
            xData[i] = positions.get(i).x;
            yData[i] = positions.get(i).y;
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
        chart.setWidth(1000);
        chart.setHeight(1000);

        // Show it
        new SwingWrapper(chart).displayChart();


    }
}
