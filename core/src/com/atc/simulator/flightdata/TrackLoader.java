package com.atc.simulator.flightdata;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by luke on 8/04/16.
 */
public class TrackLoader extends AsynchronousAssetLoader<Track, TrackLoader.TrackParameters> {
    private Track track;

    public TrackLoader (FileHandleResolver resolver) {
        super(resolver);
    }
    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TrackParameters parameter) {
        System.out.println("File Exists: " + file.exists());
        System.out.println("File Path: " + file.path());
        track = Track.readFromCSVFile(file);
    }

    @Override
    public Track loadSync(AssetManager manager, String fileName, FileHandle file, TrackParameters parameter) {
        Track track = this.track;
        this.track = null;
        return track;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TrackParameters parameter) {
        return null;
    }

    static public class TrackParameters extends AssetLoaderParameters<Track>
    {

    }
}
