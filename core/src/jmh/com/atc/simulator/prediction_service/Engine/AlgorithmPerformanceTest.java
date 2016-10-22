package com.atc.simulator.prediction_service.Engine;
import com.atc.simulator.prediction_service.Engine.Algorithms.Java.JavaPredictionAlgorithm;
import com.atc.simulator.prediction_service.Engine.Algorithms.PredictionAlgorithmType;
import com.atc.simulator.flightdata.SimulatorTrackLoader;
import com.atc.simulator.flightdata.Track;
import com.atc.simulator.flightdata.TrackLoader;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.List;

public class AlgorithmPerformanceTest {
    private static Track largeTrack;
    private static Track smallTrack;
    private static Track mediumTrack;
    private static JavaPredictionAlgorithm passThroughAlgorithm;

    static {
        TrackLoader loader = new SimulatorTrackLoader("assets/flight_data/YMMLtoYSCB/YMML2YSCB_track.csv");
        try {
            largeTrack = loader.load();
            List subSet = largeTrack.subList(100, 150);
            smallTrack = new Track(subSet);
            subSet = largeTrack.subList(100, 200);
            mediumTrack = new Track(subSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        passThroughAlgorithm = JavaPredictionAlgorithm.getInstance(PredictionAlgorithmType.PASSTHROUGH);

    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AlgorithmPerformanceTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public static void testPassthroughAlgorithm()
    {
        passThroughAlgorithm.makePrediction(smallTrack);
        return;
    }
	
}