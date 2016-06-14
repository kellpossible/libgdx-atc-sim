#define N_TRACK_LOCAL 20
#define N_PREDICTIONS_LOCAL 20

#define SRC_ITEM_INTS_SIZE 1
#define SRC_ITEM_INTS (SRC_ITEM_INTS_SIZE * N_TRACK_LOCAL)

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

  int n_tracks = 0;

  {
    int srcItemIntsItemIndex = gid * SRC_ITEM_INTS;
    int srcItemLongsItemIndex = gid * SRC_ITEM_LONGS;
    int srcItemFloatsItemIndex = gid * SRC_ITEM_FLOATS;


    for(int i = 0; i<N_TRACK_LOCAL; i++)
    {
      int srcIntsIndex = srcItemIntsItemIndex + i;
      int srcLongsIndex = srcItemLongsItemIndex + i;
      int srcFloatsIndex = srcItemFloatsItemIndex + (i * SRC_ITEM_FLOATS_SIZE);

      n_tracks = i + 1;
      int itemID = srcItemInts[srcIntsIndex];

      if (itemID == 0) {
        break;
      }

      trackTimes[i] = srcItemLongs[srcLongsIndex];
      trackPositions[i].x = srcItemFloats[srcFloatsIndex+0];
      trackPositions[i].y = srcItemFloats[srcFloatsIndex+1];
      trackPositions[i].z = srcItemFloats[srcFloatsIndex+2];
    }
  }



  {
    int dstItemFloatsItemIndex = gid * DST_ITEM_FLOATS;
    int dstItemLongsItemIndex = gid * DST_ITEM_LONGS;

    for(int i=0; i<n_tracks; i++)
    {
      int dstFloatsIndex = dstItemFloatsItemIndex + (i * DST_ITEM_FLOATS_SIZE);
      int dstLongsIndex = dstItemLongsItemIndex + i;

      dstItemLongs[dstLongsIndex] = trackTimes[i];
      dstItemFloats[dstFloatsIndex+0] = trackPositions[i].x;
      dstItemFloats[dstFloatsIndex+1] = trackPositions[i].y;
      dstItemFloats[dstFloatsIndex+2] = trackPositions[i].z;
    }
  }

};
