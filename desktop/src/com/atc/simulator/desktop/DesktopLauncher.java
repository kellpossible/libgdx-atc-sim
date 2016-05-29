package com.atc.simulator.desktop;

import com.atc.simulator.DebugDataFeed.DataPlaybackThread;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServe;
import com.atc.simulator.DebugDataFeed.DebugDataFeedServerThread;
import com.atc.simulator.DebugDataFeed.Scenarios.ADSBRecordingScenario;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.DebugDataFeed.Scenarios.YMMLtoYSCBScenario;
import com.atc.simulator.Display.PredictionFeedClient;
import com.atc.simulator.PredictionService.DebugDataFeedClient;
import com.atc.simulator.PredictionService.PredictionFeedServer;
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
		PredictionFeedClient predictionFeedClient = new PredictionFeedClient();
		PredictionFeedServer predictionFeedServer = new PredictionFeedServer();
		DebugDataFeedClient debugDataFeedClient = new DebugDataFeedClient(predictionFeedServer);

		predictionFeedClient.addListener(display);
		dataPlaybackThread.addListener(display);
		dataPlaybackThread.addListener(debugDataFeedServerThread);

		predictionFeedServer.start();
		predictionFeedClient.start();

		debugDataFeedServerThread.start();
		debugDataFeedClient.start();

		dataPlaybackThread.start();
		new LwjglApplication(display, config);
	}
}
