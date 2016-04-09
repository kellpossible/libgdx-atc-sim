package com.atc.simulator.navdata;

import com.atc.simulator.SphericalPosition;

/**
 * Created by luke on 7/04/16.
 */
public class NavObject {
    private SphericalPosition position;
    private String ICAO_id;
    public NavObject(String ICAO_id, SphericalPosition position)
    {
        this.setICAO_id(ICAO_id);
        this.setPosition(position);
    }


    public SphericalPosition getPosition() {
        return position;
    }

    public void setPosition(SphericalPosition position) {
        this.position = position;
    }

    public String getICAO_id() {
        return ICAO_id;
    }

    public void setICAO_id(String ICAO_id) {
        this.ICAO_id = ICAO_id;
    }
}
