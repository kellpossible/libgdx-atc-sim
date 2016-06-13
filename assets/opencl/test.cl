#define N_TRACK_LOCAL 20
#define N_PREDICTIONS_LOCAL 20

#define SRC_ITEM_FLOATS_SIZE 6
#define SRC_ITEM_FLOATS (SRC_ITEM_FLOATS_SIZE * N_TRACK_LOCAL)

#define SRC_ITEM_LONGS_SIZE 1
#define SRC_ITEM_LONGS (SRC_ITEM_LONGS_SIZE * N_TRACK_LOCAL)

#define DST_ITEM_FLOATS_SIZE 3
#define DST_ITEM_FLOATS (DST_ITEM_FLOATS_SIZE * N_PREDICTIONS_LOCAL)

#define DST_ITEM_LONGS_SIZE 1
#define DST_ITEM_LONGS (DST_ITEM_LONGS_SIZE * N_PREDICTIONS_LOCAL)

typedef struct {float x, y, z} Vec;

__kernel void sampleKernel(__global const float *srcItemFloats,
                          __global const int *srcItemInts,
                          __global const long *srcItemLongs,
                          __global float *dstItemFloats,
                          __global long *dstItemLongs)
{
    int gid = get_global_id(0);
    __local Vec trackPositions[N_TRACK_LOCAL];
    __local long trackTimes[N_TRACK_LOCAL];
    __local Vec trackPredictions[N_PREDICTIONS_LOCAL];

    {
	    int srcItemLongsItemIndex = gid * SRC_ITEM_LONGS;
	    int srcItemFloatsItemIndex = gid * SRC_ITEM_FLOATS;
	    int dstItemFloatsItemIndex = gid * DST_ITEM_FLOATS;

	    for(int i = 0; i<N_TRACK_LOCAL; i++)
	    {
	    	int srcLongsIndex = srcItemLongsItemIndex + i;
	    	int srcFloatsIndex = srcItemFloatsItemIndex + (i * SRC_ITEM_FLOATS_SIZE);
	    	int dstFloatsIndex = dstItemFloatsItemIndex + (i * DST_ITEM_FLOATS_SIZE);

	    	dstItemLongs[srcLongsIndex] = srcItemLongs[srcLongsIndex];
	    	dstItemFloats[dstFloatsIndex+0] = srcItemFloats[srcFloatsIndex+0];
	    	dstItemFloats[dstFloatsIndex+1] = srcItemFloats[srcFloatsIndex+1];
	    	dstItemFloats[dstFloatsIndex+2] = srcItemFloats[srcFloatsIndex+2];
	    }
	}

};
