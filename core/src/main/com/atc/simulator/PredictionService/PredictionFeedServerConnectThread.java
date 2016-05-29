package com.atc.simulator.PredictionService;

import com.atc.simulator.RunnableThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Chris Coleman, Luke Frisken
 */
class PredictionFeedServerConnectThread implements RunnableThread {
    private Socket connectedClient;
    private ServerSocket connectionSocket;
    private Thread thread;
    private final String threadName = "PredictionFeedServerConnectThread";

    public PredictionFeedServerConnectThread(ServerSocket connectionSocket, Socket connectedClient)
    {
        this.connectedClient = connectedClient;
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run()
    {
        try {
            connectedClient = connectionSocket.accept();
        }catch(IOException e){System.out.println(threadName + " accept error");}
        System.out.println(threadName + " ended");
    }

    /**
     * Start this thread
     */
    @Override
    public void start() {
        if (thread == null)
        {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

    /**
     * Kill this thread.
     */
    @Override
    public void kill() {

    }

    /**
     * Join this thread.
     */
    @Override
    public void join() throws InterruptedException {
        thread.join();
    }
}