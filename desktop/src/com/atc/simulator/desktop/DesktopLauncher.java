package com.atc.simulator.desktop;

import IntegrationTesting.TestAccuracy;
import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.DebugDataFeed.DataPlaybackThread;
import com.atc.simulator.DebugDataFeed.Scenarios.ADSBRealtimeScenario;
import com.atc.simulator.DebugDataFeed.Scenarios.ADSBRecordingScenario;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServerThread;
import com.atc.simulator.Display.DisplayApplication;
import com.atc.simulator.Display.Model.Display;
import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.PredictionService.PredictionFeedServerThread;
import com.atc.simulator.flightdata.RealTimeSource;
import com.atc.simulator.flightdata.SystemStateDatabase.SystemStateDatabase;
import com.atc.simulator.PredictionService.DebugDataFeedClientThread;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * The main launcher class which creates the libgdx instance using lwjgl as the
 * windowing/library/backend for Windows/Linux/Mac.
 *
 * @author Luke Frisken
 */
public class DesktopLauncher {
	private static final String recordingFile = ApplicationConfig.getString("settings.debug-data-feed.adsb-recording-scenario.file");
	private static final int javaWorkerThreads = ApplicationConfig.getInt("settings.prediction-service.prediction-engine.java-worker-threads");
	private static final Boolean accuracyTest = ApplicationConfig.getBoolean("settings.testing.run-accuracy-test");
	private static final boolean useMSAA = ApplicationConfig.getBoolean("settings.display.use-msaa");
	private static final int msaaSamples = ApplicationConfig.getInt("settings.display.msaa-samples");
	private static final boolean debugDatafeedEnabled = ApplicationConfig.getBoolean("settings.debug-data-feed.enabled");
    private static final GeographicCoordinate projectionReference = ApplicationConfig.getCoordinate("settings.projection-reference");

	/**
	 * Main method
	 * @param arg command line arguments
	 */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		if (useMSAA) {
			config.samples = msaaSamples;
		}

		config.height = 768;
		config.width = 1024;

        Scenario scenario;
        DisplayApplication displayApplication;
        SystemStateDatabase systemStateDatabase;
        DebugDataFeedServerThread debugDataFeedServerThread = null;
        DataPlaybackThread dataPlaybackThread = null;

        if (debugDatafeedEnabled){
            scenario = new ADSBRecordingScenario(recordingFile);
        } else {
            scenario = new ADSBRealtimeScenario(projectionReference);
        }
		Scenario.setCurrentScenario(scenario);

        if (debugDatafeedEnabled)
        {
            dataPlaybackThread = new DataPlaybackThread(scenario, scenario.getRecommendedUpdateRate());
            displayApplication =  new DisplayApplication(scenario, dataPlaybackThread);
            debugDataFeedServerThread = new DebugDataFeedServerThread();
            systemStateDatabase = new SystemStateDatabase(dataPlaybackThread);
            dataPlaybackThread.addListener(displayApplication);
            dataPlaybackThread.addListener(debugDataFeedServerThread);
        } else {
            systemStateDatabase = new SystemStateDatabase(new RealTimeSource());
            displayApplication =  new DisplayApplication(scenario);
        }

		PredictionFeedClientThread predictionFeedClientThread = new PredictionFeedClientThread();
		PredictionFeedServerThread predictionFeedServerThread = new PredictionFeedServerThread();

		PredictionEngineThread predictionEngine = new PredictionEngineThread(
				predictionFeedServerThread,
				systemStateDatabase,
				javaWorkerThreads);

		DebugDataFeedClientThread debugDataFeedClientThread = new DebugDataFeedClientThread(systemStateDatabase);

		predictionFeedClientThread.addListener(displayApplication);
        
		systemStateDatabase.addListener(predictionEngine);

		if(accuracyTest) {
			TestAccuracy accuracyTester = new TestAccuracy(scenario);
			predictionFeedClientThread.addListener(accuracyTester);
			accuracyTester.start();
		}

		predictionFeedServerThread.start();
		predictionFeedClientThread.start();
		predictionEngine.start();


        if (debugDatafeedEnabled)
        {
            debugDataFeedServerThread.start();
        }

		debugDataFeedClientThread.start();

        // start the data playback thread now that everything is set up
        if (debugDatafeedEnabled)
        {
            dataPlaybackThread.start();
        }
		new LwjglApplication(displayApplication, config);
	}
}
