package com.atc.simulator;

/**
 * Created by amiritis on 9/04/2016.
 */
public abstract class NavigationPoint
{
    private Position fPosition;
    private String fICAOID;

    public NavigationPoint(Position aPosition, String aICAOID)
    {
        fPosition = aPosition;
        fICAOID = aICAOID;
    }
}
