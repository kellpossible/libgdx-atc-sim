Performance
===========

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

The Problem
-----------

What we really have to work with: \* Assuming efficiency of 80% per
worker thread \* 4 worker threads \* Requirement of 400ms latency per
work item \* worst case maximum 200 work items at once. (we need to ask
client about that, will they be all at once, or one at a time?)

::

    max work items per worker = 200/(4*0.8)
    max time per work item = 400/(200/(4*0.8) = 6.4ms
    or max time per work item = 1.6*number of workers

Strategy
--------

We want to remove as much overhead as possible from either side of the
PredictionEngine sandwich.

The biggest gains and losses will probably be made in the algorithm
itself. First need to measure how long each slice of the sandwhich is
taking to justify which parts to spend more time optimising, remembering
that the algorithm effects need to be multiplied by the number of
workers/threads.

Solutions
---------

Easy Optimizations
~~~~~~~~~~~~~~~~~~

This `website <http://java-performance.com/>`__ has some great notes
about what we can pay attention to in our code to ensure we get good
performance.

Some things I’ve noticed:

-  remove Calendar
-  remove calls to System.getTime and other slow system calls
-  profiling
-  Java HotspotJIT (flags and things we can do to help)
-  try and minimize garbage collection

Networking
~~~~~~~~~~

If it turns out that the networking is a major performance bottleneck
there is the option of switching to UDP communications. We already
timestamp all of our predictions, so it’s only a matter of providing a
checksum for each UDP Datagram and we can have a reliable UDP
implementation, with low latency.

A small change in the server and client will be required to switch to
UDP, and implement the checksum creation and checking. The client will
also need to keep a record of Datagrams it receives in order to know
whether it is getting the latest prediction, or if they are coming in
out of order. (Just select if it is newer than what it already has).

JNI to native C/C++ code
~~~~~~~~~~~~~~~~~~~~~~~~

`Stack Overflow Overhead of JNI
Call <http://stackoverflow.com/questions/13973035/what-is-the-quantitative-overhead-of-making-a-jni-call>`__

That description puts a single JNI call at 0.019ms

So an average overhead of 0.04ms (in and out) possibly a little more to
convert data types. Bunches of work items could be sent using this to
reduce the overhead per item.

`JNI Performance Blog
Post <http://normanmaurer.me/blog/2014/01/07/JNI-Performance-Welcome-to-the-dark-side/>`__

`JNI Java Performance
Comparison <https://www.researchgate.net/publication/269935434_Performance_comparison_between_Java_and_JNI_for_optimal_implementation_of_computational_micro-kernels>`__

goes over some of the finer details and points of using JNI for
performance.

`Java Language
Performance <http://benchmarksgame.alioth.debian.org/u64q/java.html>`__

This page puts our best chance of improvement an average of around a
factor of 2, which is pretty good if the overhead of a jni call is so
small. This would allow us to almost double the performance of the
PredictionEngine section of the system.

JNI to OpenCL or CUDA
~~~~~~~~~~~~~~~~~~~~~

`article
1 <http://ieeexplore.ieee.org/xpl/login.jsp?tp=&arnumber=6604011&url=http%3A%2F%2Fieeexplore.ieee.org%2Fiel7%2F6588636%2F6603992%2F06604011.pdf%3Farnumber%3D6604011>`__

This paper says it can take up to around 10-20ms transfer from host
memory to GPU using CUDA. Is this a fixed function, or a function of the
amount of memory being transfered?

`article
2 <http://www.utdallas.edu/~cxl137330/courses/spring14/AdvRTS/protected/slides/20.pdf>`__

IO R/W method seems to have the best latency performance for our
application (data in is smaller than 4mb) better than DMA anyway.

This paper demonstrates that the data size in host to device using
Memory-mapped Read and Write, starts to affect the latency when it
exceeds 16kb, and equals 1ms at 4mb. If we super were lazy, we could
probably send all info at 3ms and 16mb. If we were super efficient, we
could probably get all info into a 16kb chunk, and the latency would be
down to 0.01ms

so, host (cpu) to device (gpu) latency using IO read/write

+-------+---------+
| data  | latency |
+=======+=========+
| 16kb  | 0.01ms  |
+-------+---------+
| 256kb | 0.03ms  |
+-------+---------+
| 4mb   | 1ms     |
+-------+---------+
| 16mb  | 3ms     |
+-------+---------+

Going the other direction is not so great, but thankfully we only need
to return a single position prediction per aircraft, so the data will be
absolutely tiny. Not sure if you can swap to using DMA for the transfer
back but will assume not.

so, host (cpu) to device (gpu) latency using DMA

+-------+---------+
|  data | latency |
+=======+=========+
| 16kb  | 0.3ms   |
+-------+---------+
| 256kb | 0.3ms   |
+-------+---------+
| 4mb   | 1ms     |
+-------+---------+
| 16mb  | 3ms     |
+-------+---------+

device (gpu) to host (cpu) using IO read/write

+-------+---------+
|  data | latency |
+=======+=========+
| 1kb   | 0.1ms   |
+-------+---------+
| 4kb   | 0.2ms   |
+-------+---------+
| 16kb  | 1ms     |
+-------+---------+
| 64kb  | 3ms     |
+-------+---------+
| 256kb | 15ms    |
+-------+---------+
| 1mb   | 100ms   |
+-------+---------+
| 4mb   | 300ms   |
+-------+---------+

anything over around 16kb of data out, makes DMA more worthwhile.

device (gpu) to host (cpu) using DMA

+-------+---------+
|  data | latency |
+=======+=========+
| 1kb   | 0.03ms  |
+-------+---------+
| 4kb   | 0.03ms  |
+-------+---------+
| 16kb  | 0.03ms  |
+-------+---------+
| 64kb  | 0.08ms  |
+-------+---------+
| 256kb | 0.1ms   |
+-------+---------+
| 1mb   | 0.3ms   |
+-------+---------+
| 4mb   | 1ms     |
+-------+---------+

8bytes for a 64 bit double for position vector element. 24bytes per
position vector.

if using a long for time, 8 bytes, otherwise 4 bytes for time as
integer.

let’s go with 4 bytes for aircraft ID as integer, so that’s 36 bytes per
prediction position.

A resolution of say, 5 seconds per predicted position, 2 minutes into
the future gives us 24 positions per prediction.

so:our total data in prediction sending back would be:

::

    200 * 24 * 36 = 172kb

This means that when using DMA our latency due to transfer would be:

::

    3ms + 0.1ms = 3.1ms

In ray tracing benchmarks such as http://www.luxmark.info/ and
https://docs.google.com/spreadsheets/d/1rybGWiISHtgaUI-E_DIOM0wf6DW5UG1-p1ooizHimUI/edit?ts=56d095bd#gid=0

The gain ratio is anywhere between a factor 2 and a factor of 5 over
using C++ on equivalent level devices. So compare this with Java, and we
have a gain factor between 4 and 10.

Taking worst case result of improvement with a factor of 4, and sticking
this in with a mid range transfer time of 15ms each way which equals
30ms total, I come to the conclusion that we will be able to process

::

    improvement factor = 4
    time for same amount of work as cpu threads = 3 + 400/4
    = 103ms

Roughly 4 times the latency performance with 200 aircraft in the worst
case.

::

    improvement factor = 10
    time for same amount of work as cpu threads = 3 + 400/10
    = 43ms

in the best case, roughly 10 times the latency performance

There Also exists the possiblity of adding multiple GPUS, take the best
case scenario, with 2 gpus:

::

    improvement factor = 2*10 = 20
    time for same amount of work as cpu threads = 3*2 + 400/20
    = 26ms

roughly 15 times the latency performance.

Adding extra GPUs on top of this means that the transfer latency could
well increase above the benefits of having an extra GPU.

OpenCL Design
~~~~~~~~~~~~~

Vector type in OpenCL:

`using-own-vector-type-in-opencl <http://stackoverflow.com/questions/20200203/using-own-vector-type-in-opencl-seems-to-be-faster>`__

Creating queues

`opencl-dg-events-stream. <http://sa09.idav.ucdavis.edu/docs/SA09-opencl-dg-events-stream.pdf>`__

constant memory structs

`constant-memory-structs <http://enja.org/2011/03/30/adventures-in-opencl-part-3-constant-memory-structs/>`__

constant memory performance

`link <http://stackoverflow.com/questions/12153443/is-the-access-performance-of-constant-memory-as-same-as-global-memory-on-ope>`__

“We have a small, read-only dataset, and we are broadcasting the same
coefficient value to each thread in our warp. Constant memory should be
very efficient in this situation.”

`constant vs read only
cache <http://www.acceleware.com/blog/constant-cache-vs-read-only-cache>`__

Nvidia caches constant memory. Not sure about AMD.

using local memory

`how-do-i-use-local-memory-in-opencl <http://stackoverflow.com/questions/2541929/how-do-i-use-local-memory-in-opencl>`__

Applying_Shared_Local_Memory

`whats-the-advantage-of-the-local-memory-in-opencl <http://stackoverflow.com/questions/21872810/whats-the-advantage-of-the-local-memory-in-opencl>`__

There appears to be two ways to declare local memory, either make it one
of the input buffers to the kernal a local type, or declare an array or
variable as local type in the body of the kernel.

Work Group = each aircraft/prediction

this can come later, but there is the fantastic possibility of having
multiple local work items (more than 1) in a group operating on a single
aircraft track/prediction.

With my GTX570 there are 15 Streaming Multiprocessors available running
at 1500mhz each. Tthis is roughly half my i5 cpu frequency. With 1 work
item (cuda core) per work group (Streaming Multiprocessor) I have 4
times the effective number of threads on the gpu. So, we can assume if
we compare this to native multithreaded C++ code, I’ll get roughly
double the performance using a single cuda core (work item) per group.
Keep in mind the the GTX570 does have up to 32 cuda cores per SMP and
most of these would not be getting used in this scenario. 480 cuda
cores.

My GTX670 on the other hand, clocks in at 1000mhz with 6 Streaming
Multiprocessors, so, making the same assumptions, it would run at
roughly half the speed of the cpu when we are using 1 work item per work
group. However, the 670 has a whapping total of 192 work items per work
group available. 1152 cuda cores.

Both of these cards have 48kb of local memory per work group, and 64kb
of constant memory. Local memory is the fastest, followed closely by
constant memory, and finally global memory being the biggest but also
the slowest. They also have 63 32bit registers per thread. Use any more
than that, and they begin to spill over into local memory space.

A note on Global Memory access, if you organise it so each thread will
be accessing similar locations in global memory (coalesced) at a similar
time during their execution, then global memory can be a lot faster,
than say in the case of ray tracing where, each ray could potentially
end up needing to query any possible element in the global scene at any
given time. Random access is a lot faster within local memory. If your
local memory begins to spill however, this will be slower than had you
used global memory.

If you can anticipate the spill this
`answer <http://stackoverflow.com/questions/12726527/opencl-and-cuda-registers-usage-optimization>`__
is perhaps a good one. Limiting the scope of variables with {} brackets
allows you to force them to be removed from the registers when they go
out of scope.

A roundup of other available OpenCL hardware: GTX1080, possibly best
consumer card on the market. Clock speed 1600mhz, 2560 cuda cores, 20
streaming multiprocessors, making that 128 cores per work group. 96kb of
local memory.

AMD Radeon R9 Fury x Clock speed 1000mhz, 4096 stream processing units,
64 compute units (work groups) making that 64 work items per work group.
32kb of local memory.

As you can probably tell, nvidia architecture is better value,
especially with the larger local memory size this allows you to do more
with those cores, and do it more efficiently with less access to global
memory.

Ideally you want to keep all memory access local to the workgroup,
because this is a lot lot faster. With the 48kb of local memory on my
GPUs, I’ve calculated what kind of data I could fit.

Let’s say we go with floats for vector positions for now, well:

-  4 bytes \* 3 = position
-  8 bytes long integer for time (this could be shortened to an int if
   necessary)
-  4 bytes for speed (velocity vector can be calculated)

And that’s it for now, so this adds up to a grand total of:

36bytes per aircraft state, which gives us capacity to store 1333
aircraft states in local memory. We certainly don’t need all this, maybe
only 50 states tops for input and output This would leave 46.2kb left
for other purposes. This could could include: flight plan information,
wind speed/direction. Also neglected here is the

One could decide to divide this work group up and assign 26 aircraft per
work group. If the aircraft were in the surrounding area, you could also
compute intersections between them and give warnings, but this would be
outside the scope of this project.

The choice of whether to divide the group up or not would depend on the
algorithm being employed, how much local memory it requires, and how
difficult it is to subdivide the algorithm itself (does it have
iterative loops which can allow you to subdivide the algorithm itself?)

After a bit more reading, I discovered that before you use local memory,
you need to read the values from global memory. So if it only gets used
once there is no advantage. Will the algorithm use positions multiple
times? Certainly a simple one might not, but a more complicated
algorithm, might be trying to fit a curve to the points, or run multiple
iterations for optimising the result.

Conclusion
----------

If it’s a toss up between 15 pcs with high power cps, or 1 pc with 2
gpus to get the required performance out of the system, the latter seems
like a good option. Another way to think of it, is we could potentially
have up to 15 times higher quality predictions given the same number of
computers.

The tradeoff is of course increased system complexity, potentially
reduced reliability, and increased software maintenance overhead, as GPU
software is often tuned to a specific peice/type of hardware, and when
this is superceded, more work needs to go into maintaining the software
on new hardware.
