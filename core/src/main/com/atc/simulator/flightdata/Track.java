package com.atc.simulator.flightdata;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by luke on 7/04/16.
 * Represents a continuous track of an aircraft as it flies through the air, with regular
 * TrackEntry's representing the state of the aircraft for each point in time.
 *
 * @author Luke Frisken
 */
public class Track extends ArrayList<AircraftState> {
    private Calendar startTime = null;
    private Calendar endTime = null;

    /**
     * Get the last/latest item in this track
     * @return
     */
    public AircraftState getLatest()
    {
        return this.get(this.size()-1);
    }

    public boolean add(AircraftState element)
    {
        if (startTime != null && endTime != null)
        {
            Calendar elementTime = element.getTime();
            if (elementTime.compareTo(startTime) < 0)
            {
                startTime = elementTime;
            } else if (elementTime.compareTo(endTime)  > 0)
            {
                endTime = elementTime;
            }
        } else {
            startTime = element.getTime();
            endTime = element.getTime();
        }
        return super.add(element);
    }

    /**
     * Generate a GL_LINES model of the track
     * @return
     */
    public Model getModel(){
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part(
                "track",
                GL20.GL_LINES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorUnpacked,
                new Material());
        builder.setColor(Color.RED);

        //jump, just in case we want to skip some elements (it was having trouble drawing the entire track)
        //for performance reasons.
        int jump = 1;
        Vector3 previousPositionDrawVector = this.get(0).getPosition().getModelDrawVector();
        for(int i = jump; i < this.size(); i+=jump)
        {
            AircraftState state = this.get(i);
//            System.out.println(state.getPosition());
            Vector3 positionDrawVector = state.getPosition().getModelDrawVector();
//            System.out.println(previousPositionDrawVector.len());
//            System.out.println(positionDrawVector.len());
//            System.out.println(positionDrawVector);
            builder.line(previousPositionDrawVector, positionDrawVector);
            previousPositionDrawVector = positionDrawVector;
        }

        return modelBuilder.end();
    }


    /**
     * Find a linearly interpolated AircraftState which matches the time
     * @param time
     * @return
     */
    public AircraftState lerp(Calendar time)
    {
        long timeMillis = time.getTimeInMillis();
        for(int i = 0; i < this.size()-1; i++)
        {
            AircraftState i1aircraftState = this.get(i);
            AircraftState i2aircraftState = this.get(i+1);
            long i1aircraftStateTimeMillis = i1aircraftState.getTime().getTimeInMillis();
            long i2aircraftStateTimeMillis = i2aircraftState.getTime().getTimeInMillis();

            //if this time is between two.
            if (timeMillis >= i1aircraftStateTimeMillis && timeMillis < i2aircraftStateTimeMillis)
            {
                double i1i2TimeDiff = i2aircraftStateTimeMillis - i1aircraftStateTimeMillis;
                double timeDiff = timeMillis - i1aircraftStateTimeMillis;

                double t = timeDiff/i1i2TimeDiff;

                return i1aircraftState.lerp(i2aircraftState, t);
            }
        }

        return this.get(0); //todo: implement this properly
    }

    /**
     * Find the AircraftState with the closest time
     *
     * todo: this isn't implemented completely correctly (it's close but only the first closest)
     * @param time
     * @return
     */
    public AircraftState closest(Calendar time)
    {
        long timeMillis = time.getTimeInMillis();
        for(int i = 0; i < this.size()-1; i++)
        {
            AircraftState i1aircraftState = this.get(i);
            AircraftState i2aircraftState = this.get(i+1);
            long i1aircraftStateTimeMillis = i1aircraftState.getTime().getTimeInMillis();
            long i2aircraftStateTimeMillis = i2aircraftState.getTime().getTimeInMillis();

            //if this time is between two.
            if (timeMillis >= i1aircraftStateTimeMillis && timeMillis <= i2aircraftStateTimeMillis)
            {
                return i1aircraftState;
            }
        }

        return this.get(0); //todo: implement this properly
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public boolean timeWithinBounds(Calendar time)
    {
        if (time.compareTo(startTime) >= 0) {
            if (time.compareTo(endTime) <= 0) {
                return true;
            }
        }

        return false;
    }
}
