package com.atc.simulator;

/**
 * Created by luke on 24/05/16.
 *
 * @author Luke Frisken, Chris Coleman
 */
public interface RunnableThread extends Runnable {
    /**
     * Start this thread
     */
    public void start();

    /**
     * Kill this thread.
     */
    public void kill();
}
