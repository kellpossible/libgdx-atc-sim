package com.atc.simulator.PredictionService.Engine.Algorithms;

import com.atc.simulator.PredictionService.Engine.Algorithms.OpenCL.OpenCLPredictionAlgorithm;
import com.atc.simulator.PredictionService.Engine.Algorithms.OpenCL.OpenCLUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

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
        oclA.run();
//        oclA.release();
    }

}
