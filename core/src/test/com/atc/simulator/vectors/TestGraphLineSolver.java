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
        GeographicCoordinate p1 = GeographicCoordinate.fromDegrees(0, -37.8342164,144.8746305);
        GeographicCoordinate p2 = GeographicCoordinate.fromDegrees(0, -37.8205219,144.9055724);
        GeographicCoordinate p3 = GeographicCoordinate.fromDegrees(0, -37.8094357,144.9322658);
        GeographicCoordinate p4 = GeographicCoordinate.fromDegrees(0, -37.8014675,144.9607615);
        GeographicCoordinate p5 = GeographicCoordinate.fromDegrees(0, -37.7927862,144.9880128);
        GeographicCoordinate p6 = GeographicCoordinate.fromDegrees(0, -37.7901409,145.0093846);
        GeographicCoordinate reference = GeographicCoordinate.fromDegrees(0, -37.811151, 144.963714);

        GnomonicProjection projection = new GnomonicProjection(reference);

        ArrayList<Vector3> points = new ArrayList<Vector3>();
        points.add(projection.transformPositionTo(p1));
        points.add(projection.transformPositionTo(p2));
        points.add(projection.transformPositionTo(p3));
        points.add(projection.transformPositionTo(p4));
        points.add(projection.transformPositionTo(p5));
        points.add(projection.transformPositionTo(p6));
        Line fit = LineSolver.LeastSquaresFit(points);

        ArrayList<Vector3> positions = new ArrayList<Vector3>();
        positions.addAll(points);
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
