package com.atc.simulator.Config;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

/**
 * Created by luke on 7/06/16.
 * @author Luke Frisken
 */
public class ApplicationConfig {
    private static Config singleton;
    static {
        /**
         * Load the settings.json
         * and overlay this on top of the default settings.
         */
        File defaultConfigFile = new File("assets/default_settings.json");
        File myConfigFile = new File("assets/settings.json");
        Config defaultConfig = ConfigFactory.parseFile(defaultConfigFile);
        Config myConfig = ConfigFactory.parseFile(myConfigFile);
        Config mergedConfig = myConfig.withFallback(defaultConfig);
        singleton = ConfigFactory.load(mergedConfig);
    }
    public static Config getInstance()
    {
        return singleton;
    }

    /**
     * Make constructor private so other classes
     * can't instantiate it
     */
    private ApplicationConfig() { }
}
