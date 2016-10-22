package com.atc.simulator.display.view.shapes;

import com.atc.simulator.display.model.DisplayAircraft;
import com.atc.simulator.flightdata.AircraftState;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 7/10/16.
 *
 * @author Luke Frisken
 */
public class TrackLineMeshBuilder {
    /**
     * Build a stippled line from a list of aircraft states/positions
     * @param builder
     * @param states
     * @param aircraft
     * @param depthAdjustment
     * @param color1
     * @param color2
     */
    public static void buildStippled(MeshPartBuilder builder,
                              List<AircraftState> states,
                              DisplayAircraft aircraft,
                              double depthAdjustment,
                              Color color1,
                              Color color2)
    {
        ArrayList<Vector3> color1Lines = new ArrayList<Vector3>();
        ArrayList<Vector3> Color2Lines = new ArrayList<Vector3>();

        AircraftState state = null;
        Vector3 positionDrawVector = null;
        color1Lines.add(aircraft.getPosition().getModelDrawVector(depthAdjustment));
        state = states.get(0);
        positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
        color1Lines.add(positionDrawVector);

        for(int i = 1; i+1 < states.size(); i+=2)
        {
            state = states.get(i);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            color1Lines.add(positionDrawVector);

            state = states.get(i+1);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            color1Lines.add(positionDrawVector);
        }

        for(int i = 0; i+1 < states.size(); i+=2)
        {
            state = states.get(i);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            Color2Lines.add(positionDrawVector);

            state = states.get(i+1);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            Color2Lines.add(positionDrawVector);
        }

        //start of prediction line is the current aircraft position.
        color1Lines.add(aircraft.getPosition().getModelDrawVector(depthAdjustment));
        for(int i = 0; i < states.size(); i+=2)
        {
            state = states.get(i);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            color1Lines.add(positionDrawVector);
        }

        builder.setColor(color1);

        for (int i = 0; i+1 < color1Lines.size(); i+=2)
        {
            Vector3 p1 = color1Lines.get(i);
            Vector3 p2 = color1Lines.get(i+1);
            builder.line(p1, p2);
        }

        builder.setColor(color2);

        for (int i = 0; i+1 < Color2Lines.size(); i+=2)
        {
            Vector3 p1 = Color2Lines.get(i);
            Vector3 p2 = Color2Lines.get(i+1);
            builder.line(p1, p2);
        }
    }

    public static void build(MeshPartBuilder builder,
                             List<AircraftState> states,
                             DisplayAircraft aircraft,
                             double depthAdjustment)
    {
        ArrayList<Vector3> color1Lines = new ArrayList<Vector3>();

        AircraftState state = null;
        Vector3 positionDrawVector = null;
        Vector3 prevPositionDrawVector = aircraft.getPosition().getModelDrawVector(depthAdjustment);

        for(int i = 0; i+1 < states.size(); i+=2)
        {
            state = states.get(i);
            positionDrawVector = state.getPosition().getModelDrawVector(depthAdjustment);
            builder.line(prevPositionDrawVector, positionDrawVector);
            prevPositionDrawVector = positionDrawVector;
        }
    }
}
