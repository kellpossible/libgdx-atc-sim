package com.atc.simulator.desktop;

import com.atc.simulator.DebugDataFeed.DataPlaybackThread;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServerThread;
import com.atc.simulator.DebugDataFeed.Scenarios.ADSBRecordingScenario;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.Display.PredictionFeedClientThread;
import com.atc.simulator.PredictionService.DebugDataFeedClientThread;
import com.atc.simulator.PredictionService.PredictionFeedServerThread;
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
		DebugDataFeedServerThread debugDataFeedServerThread = new DebugDataFeedServerThread();
		PredictionFeedClientThread predictionFeedClientThread = new PredictionFeedClientThread();
		PredictionFeedServerThread predictionFeedServerThread = new PredictionFeedServerThread();
		DebugDataFeedClientThread debugDataFeedClientThread = new DebugDataFeedClientThread(predictionFeedServerThread);

		predictionFeedClientThread.addListener(display);
		dataPlaybackThread.addListener(display);
		dataPlaybackThread.addListener(debugDataFeedServerThread);

		predictionFeedServerThread.start();
		predictionFeedClientThread.start();

		debugDataFeedServerThread.start();
		debugDataFeedClientThread.start();

		dataPlaybackThread.start();
		new LwjglApplication(display, config);
	}
}
