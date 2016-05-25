package com.atc.simulator.desktop;

import com.atc.simulator.DebugDataFeed.DataPlaybackThread;
import com.atc.simulator.DebugDataFeed.Scenarios.Scenario;
import com.atc.simulator.DebugDataFeed.Scenarios.YMMLtoYSCBScenario;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.atc.simulator.Display.SimulatorDisplay;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 768;
		config.width = 1024;


		Scenario scenario = new YMMLtoYSCBScenario();
		DataPlaybackThread dataPlaybackThread = new DataPlaybackThread(scenario, scenario.getRecommendedUpdateRate());
		SimulatorDisplay display =  new SimulatorDisplay(scenario);
		dataPlaybackThread.addListener(display);
		dataPlaybackThread.start();
		new LwjglApplication(display, config);
	}
}
