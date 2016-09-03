package com.atc.simulator.Display.DisplayModel;

import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by luke on 4/09/16.
 */
public class Aircraft implements ModelInstanceProvider {
    private String name;
    private String aircraftID;
    private AircraftState currentState;

    private ModelInstance modelInstance = null;
    private Model model = null;

    public Aircraft(String name, String aircraftID, AircraftState currentState)
    {

    }

    @Override
    public ModelInstance getModelInstance() {
        return modelInstance;
    }
}
