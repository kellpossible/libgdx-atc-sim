package com.atc.simulator.Config;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.EnumSet;

/**
 * A singleton class representing the configuration of this application.
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

    /**
     * Get the instance of this singleton ApplicationConfig class
     * @return
     */
    private static Config getInstance()
    {
        return singleton;
    }

    /**
     * Make constructor private so other classes
     * can't instantiate it
     */
    private ApplicationConfig() { }

    /**
     * @see Config#getString(String)
     */
    public static String getString(String path)
    {
        return getInstance().getString(path);
    }

    /**
     * @see Config#getInt(String)
     */
    public static Integer getInt(String path)
    {
        return getInstance().getInt(path);
    }

    /**
     * @see Config#getBoolean(String)
     */
    public static boolean getBoolean(String path)
    {
        return getInstance().getBoolean(path);
    }

    /**
     * Get an enumerator
     * @param path path to value in json config file
     * @param clazz the enumerator type to use
     * @param <E> the enumerator type to use
     * @return
     */
    public static <E extends Enum<E>> Enum<E> getEnum(String path, Class<E> clazz)
    {
        String enumString = getString(path);
        Enum<E> e = Enum.valueOf(clazz, enumString);
        return e;
    }
}
