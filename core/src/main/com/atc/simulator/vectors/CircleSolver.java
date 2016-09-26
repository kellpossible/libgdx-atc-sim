package com.atc.simulator.vectors;

import org.ddogleg.optimization.FactoryOptimization;
import org.ddogleg.optimization.UnconstrainedLeastSquares;
import org.ddogleg.optimization.UtilOptimize;
import org.ddogleg.optimization.functions.FunctionNtoM;
import pythagoras.d.Circle;
import pythagoras.d.Matrix3;
import pythagoras.d.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 20/07/16.
 *
 * algorithm from:
 * http://stackoverflow.com/questions/4103405/what-is-the-algorithm-for-finding-the-center-of-a-circle-from-three-points
 */
public class CircleSolver {
    public static Circle FromThreePoints(Vector3 p1, Vector3 p2, Vector3 p3)
    {
        double dy1 = p2.y - p1.y;
        double dx1 = p2.x - p1.x;
        double dy2 = p3.y - p2.y;
        double dx2 = p3.x - p2.x;

        double slope1 = dy1/dx1;
        double slope2 = dy2/dx2;

        double x = (slope1*slope2*(p1.y - p3.y) + slope2*(p1.x + p2.x)
                - slope1*(p2.x+p3.x) )/(2* (slope2-slope1) );
        double y = -1*(x - (p1.x+p2.x)/2)/slope1 +  (p1.y+p2.y)/2;

        double r = Math.sqrt((x-p1.x)*(x-p1.x) + (y-p1.y)*(y-p1.y));

        return new Circle(x,y,r);
    }

    /**
     * Use ddogleg's LM least squares optimizer to
     * find a circle which matches a set of points
     * @param points points to match
     * @param beta initial circle parameters
     * @return
     */
    public static Circle LeastSquares(final List<Vector3> points, Circle beta)
    {
        // Define the function being optimized and create the optimizer
        FunctionNtoM func = new FunctionNtoM() {
            @Override
            public int getNumOfInputsN() {
                return 3;
            }

            @Override
            public int getNumOfOutputsM() {
                return points.size();
            }

            @Override
            public void process(double[] input, double[] output) {
                double a = input[0];
                double b = input[1];
                double r = input[2];
                Circle c = new Circle(a, b, r);

                for (int i = 0; i<points.size(); i++)
                {
                    Vector3 point = points.get(i);
                    output[i] = distance2FromCircle(c, point);
                }
            }
        };

        UnconstrainedLeastSquares optimizer = FactoryOptimization.leastSquaresLM(1e-3, true);
        optimizer.setFunction(func, null);
        optimizer.initialize(new double[]{beta.x, beta.y, beta.radius}, 1e-12,1e-12);

        // iterate 500 times or until it converges.
        UtilOptimize.process(optimizer, 500);

        double found[] = optimizer.getParameters();

        return new Circle(found[0], found[1], found[2]);
    }

    /**
     * The distance squared of a point from a circle
     * @param c
     * @param point
     * @return
     */
    public static double distance2FromCircle(Circle c, Vector3 point)
    {
        double dx = point.x - c.x;
        double dy = point.y - c.y;
        double d = Math.sqrt(dx*dx + dy*dy) - c.radius;
        return d*d;
    }


    /**
     * Get an ArrayList of positions spaced evenly along a circle
     * @param circle
     * @param nPoints number of positions
     * @return
     */
    public static ArrayList<Vector3> getDrawPositions(Circle circle, int nPoints)
    {
        Vector3 centre = new Vector3(circle.x, circle.y, 0);
        Vector3 startPoint = centre.add(new Vector3(circle.radius, 0, 0));
        ArrayList<Vector3> positions = new ArrayList<Vector3>();

        for(int i=0; i<nPoints+1; i++)
        {
            double dtheta = 2.0*Math.PI/nPoints;
            Vector3 toVec = startPoint.subtract(centre);
            Matrix3 rotation = new Matrix3().setToRotation(dtheta*i, new Vector3(0, 0, 1));
            Vector3 circlePos = rotation.transform(toVec).add(centre);
            positions.add(circlePos);
        }

        return positions;
    }
}
