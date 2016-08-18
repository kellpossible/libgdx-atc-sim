package com.atc.simulator.desktop;

import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.DebugDataFeed.DataPlaybackThread;
import com.atc.simulator.DebugDataFeed.Scenarios.ADSBRecordingScenario;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServerThread;
import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.PredictionService.PredictionFeedServerThread;
import com.atc.simulator.PredictionService.SystemStateDatabase;
import com.atc.simulator.PredictionService.DebugDataFeedClientThread;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.atc.simulator.Display.SimulatorDisplay;

public class DesktopLauncher {
	private static final String recordingFile = ApplicationConfig.getInstance().getString("settings.debug-data-feed.adsb-recording-scenario.file");
	private static final int javaWorkerThreads = ApplicationConfig.getInstance().getInt("settings.prediction-service.prediction-engine.java-worker-threads");

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 768;
		config.width = 1024;


//		Scenario scenario = new YMMLtoYSCBScenario();
		Scenario scenario = new ADSBRecordingScenario(recordingFile);
		Scenario.setCurrentScenario(scenario);

		DataPlaybackThread dataPlaybackThread = new DataPlaybackThread(scenario, scenario.getRecommendedUpdateRate());
		SimulatorDisplay display =  new SimulatorDisplay(scenario);
		DebugDataFeedServerThread debugDataFeedServerThread = new DebugDataFeedServerThread();

		SystemStateDatabase systemStateDatabase = new SystemStateDatabase();


		PredictionFeedClientThread predictionFeedClientThread = new PredictionFeedClientThread();
		PredictionFeedServerThread predictionFeedServerThread = new PredictionFeedServerThread();

		PredictionEngineThread predictionEngine = new PredictionEngineThread(
				predictionFeedServerThread,
				systemStateDatabase,
				javaWorkerThreads);

		DebugDataFeedClientThread debugDataFeedClientThread = new DebugDataFeedClientThread(systemStateDatabase);

		predictionFeedClientThread.addListener(display);
		dataPlaybackThread.addListener(display);
		dataPlaybackThread.addListener(debugDataFeedServerThread);

		// commented out, previous by-pass used by Luke
//		dataPlaybackThread.addListener(systemStateDatabase);
		systemStateDatabase.addListener(predictionEngine);

		predictionFeedServerThread.start();
		predictionFeedClientThread.start();
		predictionEngine.start();

		debugDataFeedServerThread.start();
		debugDataFeedClientThread.start();

		dataPlaybackThread.start();
		new LwjglApplication(display, config);
	}
}
