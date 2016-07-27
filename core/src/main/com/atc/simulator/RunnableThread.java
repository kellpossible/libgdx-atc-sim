package com.atc.simulator;

/**
 * An extension of runnable, which implements an actual thread,
 * which you can start and kill.
 *
 * @author Luke Frisken, Chris Coleman
 */
public interface RunnableThread extends Runnable {
    /**
     * Start this thread
     */
    public void start();

    /**
     * Kill this thread asynchronously
     */
    public void kill();

    /**
     * Join this thread.
     */
    public void join() throws InterruptedException;
}
