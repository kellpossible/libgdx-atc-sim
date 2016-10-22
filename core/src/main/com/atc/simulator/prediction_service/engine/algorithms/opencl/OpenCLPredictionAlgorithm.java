package com.atc.simulator.prediction_service.engine.algorithms.opencl;

import com.atc.simulator.config.ApplicationConfig;
import com.atc.simulator.prediction_service.engine.PredictionWorkItem;
import com.atc.simulator.flightdata.AircraftState;
import com.atc.simulator.flightdata.Track;
import org.jocl.*;
import pythagoras.d.Vector3;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.jocl.CL.*;
import static org.jocl.CL.clReleaseProgram;

/**
 * @author Luke Frisken
 */
public class OpenCLPredictionAlgorithm {
    private static final boolean enableTimer = ApplicationConfig.getBoolean("settings.debug.algorithm-timer");
    private cl_kernel kernel;
    private cl_program program;
    private cl_context context;
    private cl_command_queue commandQueue;
    private cl_mem memObjects[]; //memory objects for the input- and output data

    private static final int N_TRACK = 20;
    private static final int N_PREDICTIONS = 20;

    //(position + velocity) * N_TRACK
    private static final int SRC_ITEM_FLOATS_SIZE = (3 + 3);
    private static final int SRC_ITEM_FLOATS = SRC_ITEM_FLOATS_SIZE * N_TRACK;

    //currently this is just one SRC_ITEM_INTS per work item
    //itemID + n_tracks
    private static final int SRC_ITEM_INTS_SIZE = 2;
    private static final int SRC_ITEM_INTS = SRC_ITEM_INTS_SIZE;

    //(time*N_TRACK)
    private static final int SRC_ITEM_LONGS_SIZE = 1;
    private static final int SRC_ITEM_LONGS = SRC_ITEM_LONGS_SIZE * N_TRACK;

    //position * N_PREDICTIONS
    private static final int DST_ITEM_FLOATS_SIZE = 3;
    private static final int DST_ITEM_FLOATS = DST_ITEM_FLOATS_SIZE * N_PREDICTIONS;

    //time * N_PREDICTIONS
    private static final int DST_ITEM_LONGS_SIZE = 1;
    private static final int DST_ITEM_LONGS = DST_ITEM_LONGS_SIZE * N_PREDICTIONS;


    /**
     * The source code of the OpenCL program to execute
     */
    private static String sourceFilePath = "assets/opencl/test.cl";


    private boolean built;
    private boolean contextCreated;
    private boolean argumentsSet;

    public OpenCLPredictionAlgorithm()
    {
        built = false;
        contextCreated = false;
        argumentsSet = false;
        memObjects = new cl_mem[5];
    }

    public void run(List<PredictionWorkItem> workItemList)
    {
        long start1=0, start2=0;
        // Create input- and output data
        int n = workItemList.size();

        float[] srcFloats = new float[n*SRC_ITEM_FLOATS];
        int[] srcInts = new int[n*SRC_ITEM_INTS];
        long[] srcLongs = new long[n*SRC_ITEM_LONGS];
        float[] dstFloats = new float[n*DST_ITEM_FLOATS];
        long[] dstLongs = new long[n*DST_ITEM_LONGS];


        for(int k = 0; k < n; k++)
        {
            PredictionWorkItem workItem = workItemList.get(k);
            Track track = workItem.getTrack();
            int trackLength = track.size();
            int subTrackLength = Math.min(trackLength, N_TRACK);

            srcInts[k*SRC_ITEM_INTS] = workItem.getAircraftID().hashCode();
            srcInts[k*SRC_ITEM_INTS+1] = trackLength;

            int j = 0;
            for (int i=trackLength-subTrackLength; i<trackLength; i++)
            {
                AircraftState state = track.get(i);
                srcLongs[(k*SRC_ITEM_LONGS) + j*SRC_ITEM_LONGS_SIZE] = k*100+j;

                int floatsIndex = (k*SRC_ITEM_FLOATS) + j*SRC_ITEM_FLOATS_SIZE;
                Vector3 position = state.getPosition();
                Vector3 velocity = state.getVelocity();
                srcFloats[floatsIndex + 0] = (float)position.x;
                srcFloats[floatsIndex + 1] = (float)position.y;
                srcFloats[floatsIndex + 2] = (float)position.z;
                srcFloats[floatsIndex + 3] = (float)velocity.x;
                srcFloats[floatsIndex + 4] = (float)velocity.y;
                srcFloats[floatsIndex + 5] = (float)velocity.z;

                j++;
            }
        }

        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        createContext(0, CL_DEVICE_TYPE_ALL, 0);
        buildKernel();
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println("OpenCLPredictionAlgorithm setup time: " + (((double) diff)/1000000.0) + " ms");
        }

        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        setKernelArguments(n, srcFloats, srcInts, srcLongs);
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println("OpenCLPredictionAlgorithm MemCopy time: " + (((double) diff)/1000000.0) + " ms");
        }

        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        executeKernel(n, dstFloats, dstLongs);
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println("OpenCLPredictionAlgorithm executeKernel 1 time: " + (((double) diff)/1000000.0) + " ms");
        }

        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        executeKernel(n, dstFloats, dstLongs);
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println("OpenCLPredictionAlgorithm executeKernel 2 time: " + (((double) diff)/1000000.0) + " ms");
        }

        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        executeKernel(n, dstFloats, dstLongs);
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println("OpenCLPredictionAlgorithm executeKernel 3 time: " + (((double) diff)/1000000.0) + " ms");
        }

        if(enableTimer)
        {
            start1 = System.nanoTime();
            // maybe add here a call to a return to remove call up time, too.
            // Avoid optimization
            start2 = System.nanoTime();
        }
        executeKernel(n, dstFloats, dstLongs);
        if(enableTimer)
        {
            long stop = System.nanoTime();
            long diff = stop - 2*start2 + start1;
            System.out.println("OpenCLPredictionAlgorithm executeKernel 4 time: " + (((double) diff)/1000000.0) + " ms");
        }

        for(int k=0; k<n; k++)
        {
//            System.out.println("Prediction: " + k);
            int dstItemLongsItemIndex = k * DST_ITEM_LONGS;
            int dstItemFloatsItemIndex = k * DST_ITEM_FLOATS;
            for(int i=0; i<N_PREDICTIONS; i++)
            {
                long time = dstLongs[dstItemLongsItemIndex + (i * DST_ITEM_LONGS_SIZE)];
                int floatsIndex = dstItemFloatsItemIndex + (i * DST_ITEM_FLOATS_SIZE);
                Vector3 position = new Vector3(
                        (double)dstFloats[floatsIndex+0],
                        (double)dstFloats[floatsIndex+1],
                        (double)dstFloats[floatsIndex+2]
                        );
//                System.out.println("time: " + time + " position: " + position);
            }
        }

        release();
    }

    private String loadSource()
    {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(sourceFilePath));
            return new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createContext(int platformIndex,
                              long deviceType,
                              int deviceIndex
                              )
    {

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue for the selected device
        commandQueue =
                clCreateCommandQueue(context, device, 0, null);

        contextCreated = true;

    }

    private void buildKernel()
    {
        if(contextCreated) {
            if (!built) {
                // Create the program from the source code
                String sourceCode = loadSource();
                program = clCreateProgramWithSource(context,
                        1, new String[]{sourceCode}, null, null);

                // Build the program
                clBuildProgram(program, 0, null, null, null, null);

                // Create the kernel
                kernel = clCreateKernel(program, "sampleKernel", null);
                built = true;
            }
        } else {
            System.err.println("You need to create the context before building the kernel");
        }

    }

    private void setKernelArguments(int n, float[] srcFloats, int[] srcInts, long[] srcLongs)
    {
        if(contextCreated && built) {
            Pointer srcFloatsPtr = Pointer.to(srcFloats);
            Pointer srcIntsPtr = Pointer.to(srcInts);
            Pointer srcLongsPtr = Pointer.to(srcLongs);

            // Allocate the memory objects for the input- and output data
            memObjects[0] = clCreateBuffer(context,
                    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_float * SRC_ITEM_FLOATS * n, srcFloatsPtr, null);
            memObjects[1] = clCreateBuffer(context,
                    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_int * SRC_ITEM_INTS * n, srcIntsPtr, null);
            memObjects[2] = clCreateBuffer(context,
                    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_long * SRC_ITEM_LONGS * n, srcLongsPtr, null);
            memObjects[3] = clCreateBuffer(context,
                    CL_MEM_READ_WRITE,
                    Sizeof.cl_float * DST_ITEM_FLOATS * n, null, null);
            memObjects[4] = clCreateBuffer(context,
                    CL_MEM_READ_WRITE,
                    Sizeof.cl_long * DST_ITEM_LONGS * n, null, null);


            // Set the arguments for the kernel
            clSetKernelArg(kernel, 0,
                    Sizeof.cl_mem, Pointer.to(memObjects[0]));
            clSetKernelArg(kernel, 1,
                    Sizeof.cl_mem, Pointer.to(memObjects[1]));
            clSetKernelArg(kernel, 2,
                    Sizeof.cl_mem, Pointer.to(memObjects[2]));
            clSetKernelArg(kernel, 3,
                    Sizeof.cl_mem, Pointer.to(memObjects[3]));
            clSetKernelArg(kernel, 4,
                    Sizeof.cl_mem, Pointer.to(memObjects[4]));

            argumentsSet = true;
        } else {
            System.err.println(
                    "You need to create the context and build the " +
                    "kernel before setting the kernel arguments");
        }
    }

    private void executeKernel(int n, float[] dstFloats, long[] dstLongs)
    {
        if (argumentsSet && contextCreated && built)
        {
            Pointer dstFloatsPtr = Pointer.to(dstFloats);
            Pointer dstIntsPtr = Pointer.to(dstLongs);

            // Set the work-item dimensions
            long global_work_size[] = new long[]{n};
            long local_work_size[] = new long[]{10};

            // Execute the kernel
            clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                    global_work_size, local_work_size, 0, null, null);

            // Read the output data
            //TODO: figure out whether these are executing in parallel and if not, whether that would be better
            clEnqueueReadBuffer(commandQueue, memObjects[3], CL_TRUE, 0,
                    Sizeof.cl_float * DST_ITEM_FLOATS * n, dstFloatsPtr, 0, null, null);
            clEnqueueReadBuffer(commandQueue, memObjects[4], CL_TRUE, 0,
                    Sizeof.cl_long * DST_ITEM_LONGS * n, dstIntsPtr, 0, null, null);


        } else {
            System.err.println(
                    "You need to create the context, build the " +
                            "kernel and set the kernel arguments before executing the kernel");
        }


    }

    public boolean isBuilt()
    {
        return built;
    }

    public void release()
    {
        // Release kernel, program, and memory objects

        if(built)
        {
            clReleaseKernel(kernel);
            clReleaseProgram(program);
        }

        if(argumentsSet)
        {
            for (int i=0; i < memObjects.length; i++)
            {
                clReleaseMemObject(memObjects[i]);
            }
        }

        if(contextCreated)
        {
            clReleaseCommandQueue(commandQueue);
            clReleaseContext(context);
        }
    }

}
