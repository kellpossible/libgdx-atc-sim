package com.atc.simulator.navdata;

import com.atc.simulator.vectors.GeographicCoordinate;

/**
 * Created by luke on 7/04/16.
 * A navigational reference position. ID/Naming conventions:
 * http://www.jeppesen.com/download/navdata/navdata_info1.pdf
 *
 * @author Luke Frisken
 */
public class Waypoint {
    private GeographicCoordinate position;
    private String icaoId;
    public Waypoint(String icaoId, GeographicCoordinate position)
    {
        this.setIcaoId(icaoId);
        this.setPosition(position);
    }

    /**
     * Get the position
     * @return
     */
    public GeographicCoordinate getPosition() {
        return position;
    }

    public void setPosition(GeographicCoordinate position) {
        this.position = position;
    }

    public String getIcaoId() {
        return icaoId;
    }

    public void setIcaoId(String icaoId) {
        this.icaoId = icaoId;
    }
}
