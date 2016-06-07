package com.atc.simulator.desktop;

import com.atc.simulator.DebugDataFeed.DataPlaybackThread;
import com.atc.simulator.DebugDataFeed.Scenarios.ADSBRecordingScenario;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.PredictionService.Engine.PredictionEngineThread;
import com.atc.simulator.PredictionService.PredictionFeedServerThread;
import com.atc.simulator.PredictionService.SystemStateDatabase;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.atc.simulator.Display.SimulatorDisplay;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 768;
		config.width = 1024;


//		Scenario scenario = new YMMLtoYSCBScenario();
		Scenario scenario = new ADSBRecordingScenario("assets/flight_data/YMML_26_05_2016/database.json");
		DataPlaybackThread dataPlaybackThread = new DataPlaybackThread(scenario, scenario.getRecommendedUpdateRate());
		SimulatorDisplay display =  new SimulatorDisplay(scenario);
//		DebugDataFeedServerThread debugDataFeedServerThread = new DebugDataFeedServerThread();

		SystemStateDatabase systemStateDatabase = new SystemStateDatabase();


		PredictionFeedClientThread predictionFeedClientThread = new PredictionFeedClientThread();
		PredictionFeedServerThread predictionFeedServerThread = new PredictionFeedServerThread();

		PredictionEngineThread predictionEngine = new PredictionEngineThread(
				predictionFeedServerThread,
				systemStateDatabase,
				4);

//		DebugDataFeedClientThread debugDataFeedClientThread = new DebugDataFeedClientThread(predictionEngine);

		predictionFeedClientThread.addListener(display);
		dataPlaybackThread.addListener(display);
//		dataPlaybackThread.addListener(debugDataFeedServerThread);
		dataPlaybackThread.addListener(systemStateDatabase);
		systemStateDatabase.addListener(predictionEngine);

		predictionFeedServerThread.start();
		predictionFeedClientThread.start();
		predictionEngine.start();

//		debugDataFeedServerThread.start();
//		debugDataFeedClientThread.start();

		dataPlaybackThread.start();
		new LwjglApplication(display, config);
	}
}
