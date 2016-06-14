#define N_TRACK_LOCAL 20
#define N_PREDICTIONS_LOCAL 20

//currently ints are just one per work item
#define SRC_ITEM_INTS_SIZE 2
#define SRC_ITEM_INTS (SRC_ITEM_INTS_SIZE)

#define SRC_ITEM_FLOATS_SIZE 6
#define SRC_ITEM_FLOATS (SRC_ITEM_FLOATS_SIZE * N_TRACK_LOCAL)

#define SRC_ITEM_LONGS_SIZE 1
#define SRC_ITEM_LONGS (SRC_ITEM_LONGS_SIZE * N_TRACK_LOCAL)

#define DST_ITEM_FLOATS_SIZE 3
#define DST_ITEM_FLOATS (DST_ITEM_FLOATS_SIZE * N_PREDICTIONS_LOCAL)

#define DST_ITEM_LONGS_SIZE 1
#define DST_ITEM_LONGS (DST_ITEM_LONGS_SIZE * N_PREDICTIONS_LOCAL)

//our own vector type
//from http://davibu.interfree.it/opencl/smallptgpu2/smallptGPU2.html
//which was released under the MIT license
typedef struct {float x, y, z} Vec;
#define vinit(v, a, b, c) { (v).x = a; (v).y = b; (v).z = c; }
#define vassign(a, b) vinit(a, (b).x, (b).y, (b).z)
#define vclr(v) vinit(v, 0.f, 0.f, 0.f)
#define vadd(v, a, b) vinit(v, (a).x + (b).x, (a).y + (b).y, (a).z + (b).z)
#define vsub(v, a, b) vinit(v, (a).x - (b).x, (a).y - (b).y, (a).z - (b).z)
#define vsadd(v, a, b) { float k = (a); vinit(v, (b).x + k, (b).y + k, (b).z + k) }
#define vssub(v, a, b) { float k = (a); vinit(v, (b).x - k, (b).y - k, (b).z - k) }
#define vmul(v, a, b) vinit(v, (a).x * (b).x, (a).y * (b).y, (a).z * (b).z)
#define vsmul(v, a, b) { float k = (a); vinit(v, k * (b).x, k * (b).y, k * (b).z) }
#define vdot(a, b) ((a).x * (b).x + (a).y * (b).y + (a).z * (b).z)
#define vnorm(v) { float l = 1.f / sqrt(vdot(v, v)); vsmul(v, l, v); }
#define vxcross(v, a, b) vinit(v, (a).y * (b).z - (a).z * (b).y, (a).z * (b).x - (a).x * (b).z, (a).x * (b).y - (a).y * (b).x)
#define vfilter(v) ((v).x > (v).y && (v).x > (v).z ? (v).x : (v).y > (v).z ? (v).y : (v).z)
#define viszero(v) (((v).x == 0.f) && ((v).x == 0.f) && ((v).z == 0.f))
//end vector type

#define PASSTHROUGH


__kernel void sampleKernel(__global const float *srcItemFloats,
                          __global const int *srcItemInts,
                          __global const long *srcItemLongs,
                          __global float *dstItemFloats,
                          __global long *dstItemLongs)
{
  int gid = get_global_id(0);
  __local Vec srcTrackPositions[N_TRACK_LOCAL];
  __local long srcTrackTimes[N_TRACK_LOCAL];
  __local Vec predictionPositions[N_PREDICTIONS_LOCAL];

  int n_tracks = 0;

  {
    int srcItemIntsItemIndex = gid * SRC_ITEM_INTS;
    int itemID = srcItemInts[srcItemIntsItemIndex];
    n_tracks = srcItemInts[srcItemIntsItemIndex+1];


    int srcItemLongsItemIndex = gid * SRC_ITEM_LONGS;
    int srcItemFloatsItemIndex = gid * SRC_ITEM_FLOATS;

    #ifdef PASSTHROUGH
    int dstItemFloatsItemIndex = gid * DST_ITEM_FLOATS;
    int dstItemLongsItemIndex = gid * DST_ITEM_LONGS;

    for(int i=0; i<n_tracks; i++)
    {
      int dstFloatsIndex = dstItemFloatsItemIndex + (i * DST_ITEM_FLOATS_SIZE);
      int dstLongsIndex = dstItemLongsItemIndex + (i * DST_ITEM_LONGS_SIZE);
      int srcLongsIndex = srcItemLongsItemIndex + (i * SRC_ITEM_LONGS_SIZE);
      int srcFloatsIndex = srcItemFloatsItemIndex + (i * SRC_ITEM_FLOATS_SIZE);

      dstItemLongs[dstLongsIndex] = srcItemLongs[srcLongsIndex];
      dstItemFloats[dstFloatsIndex+0] = srcItemFloats[srcFloatsIndex+0];
      dstItemFloats[dstFloatsIndex+1] = srcItemFloats[srcFloatsIndex+0];
      dstItemFloats[dstFloatsIndex+2] = srcItemFloats[srcFloatsIndex+2];
    }
    #else
    for(int i = 0; i<n_tracks; i++)
    {
      int srcLongsIndex = srcItemLongsItemIndex + i;
      int srcFloatsIndex = srcItemFloatsItemIndex + (i * SRC_ITEM_FLOATS_SIZE);

      // srcTrackTimes[i] = srcItemLongs[srcLongsIndex];
      srcTrackPositions[i].x = srcItemFloats[srcFloatsIndex+0];
      srcTrackPositions[i].y = srcItemFloats[srcFloatsIndex+1];
      srcTrackPositions[i].z = srcItemFloats[srcFloatsIndex+2];
    }
    #endif
  }
  {
    int dstItemFloatsItemIndex = gid * DST_ITEM_FLOATS;
    int dstItemLongsItemIndex = gid * DST_ITEM_LONGS;

    //set the unused bits to 0 (helpful for debugging, uses a bit of extra
    //performance though)
    for (int i=n_tracks; i<N_PREDICTIONS_LOCAL; i++)
    {
      int dstFloatsIndex = dstItemFloatsItemIndex + (i * DST_ITEM_FLOATS_SIZE);
      int dstLongsIndex = dstItemLongsItemIndex + i;

      dstItemLongs[dstLongsIndex] = 0;
      dstItemFloats[dstFloatsIndex+0] = 0.0f;
      dstItemFloats[dstFloatsIndex+1] = 0.0f;
      dstItemFloats[dstFloatsIndex+2] = 0.0f;
    }
  }
};
