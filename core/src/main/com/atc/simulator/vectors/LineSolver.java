package com.atc.simulator.vectors;

import org.orangepalantir.leastsquares.Fitter;
import org.orangepalantir.leastsquares.Function;
import org.orangepalantir.leastsquares.fitters.LinearFitter;
import pythagoras.d.Line;
import pythagoras.d.Vector3;

import java.util.Arrays;
import java.util.List;

/**
 * Doesn't work for vertical lines though I'm pretty sure because
 * it's using vertical offsets rather than perpendicular ones.
 *
 * Also currently completely broken for some reason :S
 *
 * @author Luke Frisken
 */
public class LineSolver {
    public static Line LeastSquaresFit(List<Vector3> points)
    {
        Vector3 p0 = points.get(0);
        Vector3 pend = points.get(points.size()-1);
        Line beta = new Line(p0.x, p0.y, pend.x, pend.y);

        Function function = new Function() {
            @Override
            public double evaluate(double[] values, double[] parameters) {
                double m = parameters[0];
                double c = parameters[1];
                double x = values[0];
                System.out.println(x);
                return m*x + c;


            }

            @Override
            public int getNParameters() {
                return 2;
            }

            @Override
            public int getNInputs() {
                return 1;
            }
        };

        Fitter fitter = new LinearFitter(function);

        double[][] xs = new double[points.size()][1];
        double[] zs = new double[points.size()];

        for(int i = 0; i < points.size(); i++)
        {
            xs[i][0] = points.get(i).x;
            zs[i] = points.get(i).z;
        }

        fitter.setData(xs, zs);


        double m = (beta.y2-beta.y1)/(beta.x2-beta.x1);
        double c = beta.y1 - beta.x1*m;



        fitter.setParameters(new double[]{m,c});

        double[] parameters = fitter.getParameters();

        System.out.println(Arrays.toString(parameters));

        fitter.fitData();

        parameters = fitter.getParameters();

        System.out.println(Arrays.toString(parameters));


        double newM = parameters[0];
        double newC = parameters[1];

        return new Line(p0.x, p0.x * newM + newC,
                pend.x, pend.x*newM + newC);

    }
}
