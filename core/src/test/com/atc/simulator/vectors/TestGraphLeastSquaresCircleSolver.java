package com.atc.simulator.vectors;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;
import pythagoras.d.Circle;
import pythagoras.d.Vector3;

import java.util.ArrayList;

/**
 * Created by luke on 18/08/16.
 */
public class TestGraphLeastSquaresCircleSolver {

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

    public static XYData dataFromVectorList(ArrayList<Vector3> vectors) {
        XYData data = new XYData(vectors.size());

        for (int i=0; i<vectors.size();i++)
        {
            data.xData[i] = vectors.get(i).x;
            data.yData[i] = vectors.get(i).y;
        }

        return data;
    }

    public TestGraphLeastSquaresCircleSolver(double position1x, double position1y, double position2x, double position2y,
                                             double position3x, double position3y, double position4x, double position4y)
    {

        ArrayList<Vector3> positions = new ArrayList<Vector3>();
        positions.add(new Vector3(position1x, position1y, 0));
        positions.add(new Vector3(position2x, position2y, 0));
        positions.add(new Vector3(position3x, position3y, 0));
        positions.add(new Vector3(position4x, position4y, 0));

        Circle betaCircle = CircleSolver.FromThreePoints(positions.get(0), positions.get(1), positions.get(2));
        Circle fitCircle = CircleSolver.LeastSquares(positions, betaCircle);

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


        XYData positionData = dataFromVectorList(positions);
        XYData betaCircleData = dataFromVectorList(CircleSolver.getDrawPositions(betaCircle, 100));
        XYData fitCircleData = dataFromVectorList(CircleSolver.getDrawPositions(fitCircle, 100));

        // Create Chart
        XYChart chart = new XYChartBuilder().width(1000).height(1000).title("Least Squares Circle Solver").build();
        XYSeries seriesPositions = chart.addSeries("positions", positionData.xData, positionData.yData);
        seriesPositions.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        seriesPositions.setMarker(SeriesMarkers.CIRCLE);
        chart.getStyler().setMarkerSize(8);

        XYSeries seriesBetaCircle = chart.addSeries("beta circle", betaCircleData.xData, betaCircleData.yData);
        seriesBetaCircle.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        seriesBetaCircle.setMarker(SeriesMarkers.NONE);

        XYSeries seriesFitCircle = chart.addSeries("fit circle", fitCircleData.xData, fitCircleData.yData);
        seriesFitCircle.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        seriesFitCircle.setMarker(SeriesMarkers.NONE);

//        chart.getStyler().setXAxisMin(-16000);
//        chart.getStyler().setXAxisMax(16000);
//        chart.getStyler().setYAxisMin(-16000);
//        chart.getStyler().setYAxisMax(16000);

        // Show it
        new SwingWrapper(chart).displayChart();
    }
}
