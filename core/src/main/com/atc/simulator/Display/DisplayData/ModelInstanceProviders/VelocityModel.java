package com.atc.simulator.Display.DisplayData.ModelInstanceProviders;

import com.atc.simulator.Display.DisplayData.DisplayAircraft;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Luke Frisken
 */
public class VelocityModel extends SimpleModelInstanceProvider{
    private DisplayAircraft aircraft;

    /**
     * Constructor VelocityModel creates a new VelocityModel instance.
     *
     * @param aircraft of type DisplayAircraft
     */
    public VelocityModel(DisplayAircraft aircraft)
    {
        this.aircraft = aircraft;
        update();
    }

    /**
     * Call to update the instance provided by this class.
     */
    @Override
    public void update()
    {
        if (model != null)
        {
            model.dispose();
        }
        ModelBuilder modelBuilder = new ModelBuilder();

        GeographicCoordinate position = aircraft.getPosition();
        double depthAdjustment = -0.01;
        Vector3 modelDrawVector = position.getModelDrawVector(depthAdjustment);

        SphericalVelocity velocity = aircraft.getVelocity();

        GeographicCoordinate velocityEndPos = new GeographicCoordinate(velocity.angularVelocityTranslate(position, 120));
        model = modelBuilder.createArrow(
                modelDrawVector,
                velocityEndPos.getModelDrawVector(depthAdjustment),
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        modelInstance = new ModelInstance(model);
    }
}
