package com.atc.simulator.config;


import com.atc.simulator.vectors.GeographicCoordinate;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.List;

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
     * @see Config#getDouble(String)
     */
    public static double getDouble(String path)
    {
        return getInstance().getDouble(path);
    }

    /**
     * @see Config#getStringList(String)
     */
    public static List<String> getStringList(String path)
    {
        return getInstance().getStringList(path);
    }

    /**
     * Get the geographic coordinate from an array of doubles in the config file
     * of the format [altitude, latitude, longitude], where the latitude and
     * longitude are specified in degrees.
     * For example:
     * [0.0, 38.0, 148.0]
     * @param path to element that contains the array
     * @return GeographicCoordinate
     */
    public static GeographicCoordinate getCoordinate(String path)
    {
        List<Double> list = getInstance().getDoubleList(path);

        if (list.size() != 3)
        {
            throw new RuntimeException("Invalid list length for GeographicCoordinate: " + list.size());
        }

        return GeographicCoordinate.fromDegrees(list.get(0), list.get(1), list.get(2));
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
