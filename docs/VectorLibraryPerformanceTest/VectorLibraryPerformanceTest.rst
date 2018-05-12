Vector Library Performance Test
===============================

*Section author: Luke Frisken <l.frisken@gmail.com>*

The purpose of this performance test was to review several
methods/libraries available for vector/matrix math operations.

The source code for the project can be found at https://github.com/kellpossible/java-vector-math-performance-test

-  `Apache
   commons-math <https://commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/geometry/euclidean/threed/Vector3D.html>`__
-  `Pythagoras Library <https://github.com/samskivert/pythagoras>`__
-  `Yeppp! Library <http://www.yeppp.info/>`__
-  LWJGL OpenCL (TODO)

Vector classes using double precision floating point numbers were used.
Where classes were missing, a plain java alternative was implemented.

Performance Test Results
------------------------

+-------------------+---------+-------+---------+-------+
| Benchmark         | Samples | Score | Error   | units |
+===================+=========+=======+=========+=======+
| testApacheCommons | 20      | 1.916 | ± 0.428 | ops/s |
+-------------------+---------+-------+---------+-------+
| testPythagoras    | 20      | 7.572 | ± 0.355 | ops/s |
+-------------------+---------+-------+---------+-------+
| testYepp          | 20      | 0.205 | ± 0.001 | ops/s |
+-------------------+---------+-------+---------+-------+

Review of Libraries
-------------------

Apache commons-math
~~~~~~~~~~~~~~~~~~~

-  + lots of other classes we may want to use
-  - bloated
-  ~ performance is okay but not the best.

Pythagoras
~~~~~~~~~~

The Pythagoras has the best performance. It is also small

-  + very small
-  + performance
-  - no unit tests for double class

Yeppp!
~~~~~~

Yeppp doesn’t have the best performance for our application, and we’d
have alter the design to cater for performance to get any kind of
benefit.

-  + small
-  + popular
-  - poor performance in java and for our application

`c++ - Performance with Yeppp! is slower than native implementation -
Stack
Overflow <http://stackoverflow.com/questions/26504111/performance-with-yeppp-is-slower-than-native-implementation>`__
> Yeppp! is optimized for processing arrays of 100+ elements. > It is
not efficient on small arrays (like length-3 array in your example) >
due to limited ability to use SIMD and overheads of function call,
dynamic > dispatching, and parameter checks.
