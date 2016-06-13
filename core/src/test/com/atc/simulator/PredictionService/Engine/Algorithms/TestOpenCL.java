package com.atc.simulator.PredictionService.Engine.Algorithms;

import com.atc.simulator.PredictionService.Engine.Algorithms.OpenCL.OpenCLPredictionAlgorithm;
import com.atc.simulator.PredictionService.Engine.Algorithms.OpenCL.OpenCLUtils;
import com.atc.simulator.PredictionService.Engine.PredictionWorkItem;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import pythagoras.d.Vector3;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * SphericalCoordinate Tester.
 *
 * @author <Authors name>
 * @since <pre>May 27, 2016</pre>
 * @version 1.0
 */
public class TestOpenCL {

    static {

    }

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void printPlatformInfoTest()
    {
        OpenCLUtils.printPlatformInfo();
    }


    @Test
    public void mainOpenCLTest()
    {
        OpenCLPredictionAlgorithm oclA = new OpenCLPredictionAlgorithm();

        Track track = new Track();
        track.add(new AircraftState(
                "test",
                Calendar.getInstance(),
                new GeographicCoordinate(new Vector3(1.11, 1.12, 1.13)),
                new SphericalVelocity(new Vector3(1.21, 1.22, 1.23)),
                1.31
                ));
        track.add(new AircraftState(
                "test",
                Calendar.getInstance(),
                new GeographicCoordinate(new Vector3(2.11, 2.12, 2.13)),
                new SphericalVelocity(new Vector3(2.21, 2.22, 2.23)),
                2.31
        ));
        track.add(new AircraftState(
                "test",
                Calendar.getInstance(),
                new GeographicCoordinate(new Vector3(3.11, 3.12, 3.13)),
                new SphericalVelocity(new Vector3(3.21, 3.22, 3.23)),
                3.31
        ));
        track.add(new AircraftState(
                "test",
                Calendar.getInstance(),
                new GeographicCoordinate(new Vector3(4.11, 4.12, 4.13)),
                new SphericalVelocity(new Vector3(4.21, 4.22, 4.23)),
                4.31
        ));

        PredictionWorkItem workItem = new PredictionWorkItem("test", track, PredictionAlgorithmType.PASSTHROUGH);
        ArrayList<PredictionWorkItem> work = new ArrayList<PredictionWorkItem>();
        work.add(workItem);

        oclA.run(work);
//        oclA.release();
    }

}
