package com.atc.simulator.DebugDataFeed;
import com.atc.simulator.Config.ApplicationConfig;
import com.atc.simulator.RunnableThread;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.ISO8601;
import com.atc.simulator.flightdata.SystemState;
import com.atc.simulator.ProtocolBuffers.DebugDataFeedServe.*;
import com.atc.simulator.vectors.GeographicCoordinate;
import com.atc.simulator.vectors.SphericalVelocity;
import java.util.concurrent.ArrayBlockingQueue;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Created by Chris, Uros on 8/05/2016.
 *
 * Modified 30/05/16, Chris. Added comments for flow
 */
public class DebugDataFeedServerThread implements RunnableThread, DataPlaybackListener {
    private static final boolean enableDebugPrint = ApplicationConfig.getBoolean("settings.debug.print-debugdatafeedserver");
    private static final boolean enableDebugPrintQueues = ApplicationConfig.getBoolean("settings.debug.print-queues");
    private static final boolean enableDebugPrintThreading = ApplicationConfig.getBoolean("settings.debug.print-threading");
    private static final int PORT = ApplicationConfig.getInt("settings.debug-data-feed.server.port-number");

    private ArrayBlockingQueue<SystemStateMessage> toBeSentBuffer;
    private Thread serverThread; //Thread to accept connections by clients

    private ServerSocket serverSocket; //ServerSocket that handles connect requests by clients
    private Socket clientSocket; //The socket that a successful client connection can be sent message through
    private boolean continueThread = true;
    private Thread thread;
    private final String threadName = "DebugDataFeedServerThread";

    public DebugDataFeedServerThread() {
        // using same value as predictionfeedserver thread for capacity, (400).
        toBeSentBuffer = new ArrayBlockingQueue<SystemStateMessage>(400);
        clientSocket = new Socket();
        try
        {
            serverSocket = new ServerSocket(PORT);
            serverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        clientSocket = serverSocket.accept();
                    }
                    catch (IOException e)
                    {
                        System.err.println(threadName + " Server connection error");
                    }

                    if(enableDebugPrint){ System.out.println(threadName + " DebugData client connected + accept thread ended"); }
                }
            });
        } catch (IOException e) {
            System.err.println(threadName + "Server Creation error");
        }
//        ApplicationConfig.debugPrint("print-debugdatafeedserver", threadName + " Server/ArrayList created");
    }

    /**
     * Threaded method, checks the buffer, deletes the latest info if there are no clients,
     * or shoots the message off to the client!
     */
    public void run() {
        serverThread.start();
        try {
            while (continueThread) {
                if (!clientSocket.isConnected()) //If nothing is connected
                {
                    toBeSentBuffer.clear(); //Delete the data
                    System.out.println("No client, data deleted");
                } else
                {
                    try
                    {
                        toBeSentBuffer.take().writeDelimitedTo(clientSocket.getOutputStream()); //Try to send message
                    } catch ( InterruptedException e) {System.err.println("Interrupted process");
                        e.printStackTrace();
                    } catch (IOException i ) {
                        System.err.println("Send to DebugDataFeed Client failed");
                        i.printStackTrace();
                    }
                }
            }
        } finally {
            try {
                clientSocket.close();
            } catch (IOException i) {
                System.err.println("DebugDataFeedServerThread Can't close ServerSocket");
            }
        }
        if(enableDebugPrintThreading){ System.out.println(threadName + " killed"); }
    }


    /**
     * This method gets called when there is a system update, and gets
     * passed the new system state
     *
     * @param systemState the updated system state
     */
    @Override
    public void onSystemUpdate(SystemState systemState) {
        //Create a new builder
        SystemStateMessage.Builder systemStateMessageBuilder = SystemStateMessage.newBuilder();
        //Save the time of the SystemState message creation
        systemStateMessageBuilder.setTime(systemState.getTime());

        //For every AircraftState received, turn them into messages
        for (AircraftState aircraftState : systemState.getAircraftStates()) {
            AircraftStateMessage.Builder aircraftStateMessageBuilder = AircraftStateMessage.newBuilder();
            //With ID
            aircraftStateMessageBuilder.setAircraftID(aircraftState.getAircraftID());
            //TimeStamp
            aircraftStateMessageBuilder.setTime(aircraftState.getTime());
            //A current position
            GeographicCoordinate position = aircraftState.getPosition();
            aircraftStateMessageBuilder.setPosition(
                    GeographicCoordinateMessage.newBuilder()
                            .setAltitude(position.getAltitude())
                            .setLatitude(position.getLatitude())
                            .setLongitude(position.getLongitude())
            );
            //A super complicated velocity
            SphericalVelocity velocity = aircraftState.getVelocity();
            aircraftStateMessageBuilder.setVelocity(
                    SphericalVelocityMessage.newBuilder()
                            .setDr(velocity.getDR())
                            .setDtheta(velocity.getDTheta())
                            .setDphi(velocity.getDPhi())
            );
            //and a cheeky heading for good measure
            aircraftStateMessageBuilder.setHeading(aircraftState.getHeading());
            //Add the new AircraftState message to the big system message
            systemStateMessageBuilder.addAircraftState(aircraftStateMessageBuilder);
        }
        //Once that's done, we wrap up the system message nicely
        SystemStateMessage systemStateMessage = systemStateMessageBuilder.build(); //yay the final message all built
        //And store it ready for sending
        toBeSentBuffer.add(systemStateMessage);

        //TODO: send message to client when this is called by placing it in the buffer.
    }

    public void start()
    {
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
        continueThread = false;
    }

    /**
     * Join this thread.
     */
    @Override
    public void join() throws InterruptedException {
        thread.join();
    }
}
