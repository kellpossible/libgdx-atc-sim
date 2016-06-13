__kernel void sampleKernel(__global const float *a,
                          __global const int *b,
                          __global float *c,
                          __global int *d)
{
    int gid = get_global_id(0);
    c[gid] = a[gid] * ((float)b[gid]);
};
