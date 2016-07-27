package com.atc.simulator.vectors;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import pythagoras.d.Circle;
import pythagoras.d.Matrix3;
import pythagoras.d.Vector3;

import java.util.ArrayList;

/**
 * Created by luke on 27/07/16.
 */
public class TestGraphCircleSolver {
    public static void main(String[] arg)
    {
        GeographicCoordinate p1 = GeographicCoordinate.fromDegrees(0, -37.841908, 144.876470);
        GeographicCoordinate p2 = GeographicCoordinate.fromDegrees(0, -37.815351, 144.894070);
        GeographicCoordinate p3 = GeographicCoordinate.fromDegrees(0, -37.829122, 144.918976);
        GeographicCoordinate reference = GeographicCoordinate.fromDegrees(0, -37.811151, 144.963714);

        GnomonicProjection projection = new GnomonicProjection(reference);
        Vector3 p1c = projection.transformPositionTo(p1);
        Vector3 p2c = projection.transformPositionTo(p2);
        Vector3 p3c = projection.transformPositionTo(p3);
        Circle circle = CircleSolver.FromThreePoints(p1c, p2c, p3c);
        Vector3 centre = new Vector3(circle.x, circle.y, 0);

        ArrayList<Vector3> positions = new ArrayList<Vector3>();
        positions.add(p1c);
        positions.add(p2c);
        positions.add(p3c);
        positions.add(centre);

//        for(int i=0; i<21; i++)
//        {
//            double dtheta = 2.0*Math.PI/20.0;
//            double dy = Math.sin(i * dtheta) * circle.radius;
//            double dx = Math.cos(i * dtheta) * circle.radius;
//            Vector3 circlePos = new Vector3(centre);
//            circlePos.x += dx;
//            circlePos.y += dy;
//            positions.add(circlePos);
//        }

        for(int i=0; i<21; i++)
        {
            double dtheta = 2.0*Math.PI/20.0;
            Vector3 toVec = p1c.subtract(centre);
            Matrix3 rotation = new Matrix3().setToRotation(dtheta*i, new Vector3(0, 0, 1));
            Vector3 circlePos = rotation.transform(toVec).add(centre);
            positions.add(circlePos);
        }




        double[] xData = new double[positions.size()];
        double[] yData = new double[positions.size()];
        for (int i=0; i<positions.size();i++)
        {
            xData[i] = positions.get(i).x;
            yData[i] = positions.get(i).y;
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
//        chart.getStyler().setXAxisMin(-16000);
//        chart.getStyler().setXAxisMax(16000);
//        chart.getStyler().setYAxisMin(-16000);
//        chart.getStyler().setYAxisMax(16000);
        chart.setWidth(1000);
        chart.setHeight(1000);

        // Show it
        new SwingWrapper(chart).displayChart();


    }
}
