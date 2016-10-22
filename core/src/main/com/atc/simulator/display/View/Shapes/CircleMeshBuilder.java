package com.atc.simulator.display.View.Shapes;

import com.atc.simulator.vectors.GeographicCoordinate;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by luke on 3/10/16.
 */
public class CircleMeshBuilder {
    public static void build(MeshPartBuilder builder,
                             GeographicCoordinate position,
                             double scale,
                             int segments,
                             double depthAdjustment)
    {
        Vector3 modelDrawVector = position.getModelDrawVector(depthAdjustment);

        Vector3 normal = new Vector3(modelDrawVector).scl(-1).nor();

        pythagoras.d.Vector3 cartPos = position.getCartesian();
        pythagoras.d.Vector3 phiVec = position.thetaCartesianUnitVector();

        pythagoras.d.Vector3 circleVec = phiVec.mult(scale);
        Vector3 circleDrawVec = GeographicCoordinate.fromCartesian(circleVec.add(cartPos)).getModelDrawVector();
        Vector3 prevRotatedDrawVec = circleDrawVec;
        pythagoras.d.Vector3 cartNormal = cartPos.normalize();

//        System.out.println("Original Position: " + position);
//        System.out.println("From Cartesian" + GeographicCoordinate.fromCartesian(cartPos));
//        System.out.println("FromCartesianDistance: " + GeographicCoordinate.fromCartesian(cartPos).distance(position));

        double segmentsDouble = (double) segments;
        for (int i = 0; i < segments; i++)
        {
            double angle = 2*Math.PI*(((double) i)/segmentsDouble);
            pythagoras.d.Matrix3 rotation = new pythagoras.d.Matrix3().setToRotation(angle,cartNormal);
            pythagoras.d.Vector3 rotated = rotation.transform(circleVec);
            GeographicCoordinate rotatedGeographic = GeographicCoordinate.fromCartesian(rotated.add(cartPos));
//            System.out.println("Rotated Geographic: " + rotatedGeographic);
            Vector3 rotatedDrawVec = rotatedGeographic.getModelDrawVector();

            builder.triangle(rotatedDrawVec, prevRotatedDrawVec, modelDrawVector);
            prevRotatedDrawVec = rotatedDrawVec;
        }

        builder.triangle(circleDrawVec, prevRotatedDrawVec, modelDrawVector);
    }
}
