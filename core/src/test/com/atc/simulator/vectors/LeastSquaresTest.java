package com.atc.simulator.vectors;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Adam on 13/09/2016.
 * Referenced Luke's Graphing Test   (TestGraphLeastSquaresCircleSolver).
 */
public class LeastSquaresTest {


    @Test
    public void leastSquares() throws Exception
    {
        TestGraphLeastSquaresCircleSolver t1 = new TestGraphLeastSquaresCircleSolver(-2736.5842226839904, 9051.690473155024, -1973.134855393139, 9237.219387656745, -1307.9423416823379, 10095.120237700188, -1212.8163721100887, 10729.934781668326);
        TestGraphLeastSquaresCircleSolver t2 = new TestGraphLeastSquaresCircleSolver(-2262.1283971238912, 9987.682173728978, -1468.129378912379, 7687.231323333345, -985.12983791827897, 12578.213912873218, -1456.1239871289789, 13774.218736821736);
        TestGraphLeastSquaresCircleSolver t3 = new TestGraphLeastSquaresCircleSolver(-3012.1298371289789, 7002.231897987987, -2299.129378912379, 7552.231323333345, -500.23198789722337, 9078.2131231233334, -1603.8789789273897, 9302.1231231231313);
        TestGraphLeastSquaresCircleSolver t4 = new TestGraphLeastSquaresCircleSolver(-3220.2310298234561, 12032.28798172389, -689.8821739817298, 12003.23123123232, -1100.12983791827897, 7986.213912873218, -958.1239871289789, 11235.218736821736);

    }

}