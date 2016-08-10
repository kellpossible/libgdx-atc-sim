package com.atc.simulator.vectors;

import pythagoras.d.Circle;
import pythagoras.d.Vector3;

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

    public static Circle LeastSquares(List<Vector3> points, Circle beta)
    {
        return null;
    }
}
