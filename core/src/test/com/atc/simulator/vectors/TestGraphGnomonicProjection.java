package com.atc.simulator.vectors;

import com.atc.simulator.debug_data_feed.Scenarios.ADSBRecordingScenario;
import com.atc.simulator.debug_data_feed.Scenarios.Scenario;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
import org.knowm.xchart.*;
import pythagoras.d.Vector3;

/**
 * A visual test of the GnomonicProjection class
 * @author Luke Frisken
 */
public class TestGraphGnomonicProjection {
    public static void main(String[] arg)
    {
        Scenario scenario = new ADSBRecordingScenario("assets/flight_data/YMML_26_05_2016/database.json");
        Track track = scenario.getTracks().get(2);

        double[] xData = new double[track.size()];
        double[] yData = new double[track.size()];

        GnomonicProjection projection = new GnomonicProjection(track.get(0).getPosition());


        for (int i = 0; i<track.size();i++)
        {
            AircraftState state = track.get(i);
            GeographicCoordinate position = state.getPosition();

            Vector3 position2D = projection.transformPositionTo(position);
            xData[i] = position2D.x;
            yData[i] = position2D.y;

            GeographicCoordinate newPosition = projection.transformPositionFrom(position2D);

            System.out.println("position" + position);
            System.out.println("newPosition" + newPosition);
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
        chart.getStyler().setXAxisMin(-16000);
        chart.getStyler().setXAxisMax(16000);
        chart.getStyler().setYAxisMin(-16000);
        chart.getStyler().setYAxisMax(16000);
        chart.setWidth(1000);
        chart.setHeight(1000);

        // Show it
        new SwingWrapper(chart).displayChart();
    }

}
