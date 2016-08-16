package com.atc.simulator.vectors;

import org.ejml.simple.SimpleMatrix;
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
        final Vector3 p0 = points.get(0);
        final Vector3 pend = points.get(points.size()-1);
        Line beta = new Line(p0.x, p0.y, pend.x, pend.y);

        Function function = new Function() {
            @Override
            public double evaluate(double[] values, double[] parameters) {
                double m = parameters[0];
                double x = values[0];
                System.out.println(x);
                return m*(x - pend.x) + pend.y;


            }

            @Override
            public int getNParameters() {
                return 1;
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



        fitter.setParameters(new double[]{m});

        double[] parameters = fitter.getParameters();

        System.out.println(Arrays.toString(parameters));

        fitter.fitData();

        parameters = fitter.getParameters();

        System.out.println(Arrays.toString(parameters));


        double newM = parameters[0];

        return new Line(p0.x, newM * (p0.x - pend.x) + pend.y,
                pend.x, newM * (pend.x - pend.x) + pend.y);

    }

    public static Line LeastSquaresFit2(List<Vector3> points) {
        final Vector3 p0 = points.get(0);
        final Vector3 pend = points.get(points.size() - 1);
        Line beta = new Line(p0.x, p0.y, pend.x, pend.y);

        int m, n;

        m = points.size();
        n = 2;

        double[][] xinit = new double[m][n];


        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if (j == 0)
                {
                    xinit[i][j] = 1;
                }
                if (j == 1)
                {
                    xinit[i][j] = points.get(i).x;
                }
            }
        }

        SimpleMatrix X = new SimpleMatrix(xinit);

        double[][] yinit = new double[m][1];

        for(int i = 0; i < m; i++)
        {
            yinit[i][0] = points.get(i).y;
        }

        SimpleMatrix Y = new SimpleMatrix(yinit);

        return null;
    }
}
