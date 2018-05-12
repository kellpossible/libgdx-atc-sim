# Libraries

_Section author: Luke Frisken <[l.frisken@gmail.com](mailto:l.frisken@gmail.com)>_

## Charts
<https://github.com/SINTEF-9012/rtcharts>


## Java Matrix Libraries

Comparisons:
+ <https://java-matrix.org/>
+ <http://ojalgo.org/performance_ejml.html>

Ended up chosing [EJML](http://ejml.org/wiki/index.php?title=Main_Page).
It has decent performance, great documentation, it's pure java, and it has an Apache 2 license.


## OpenCL Matrix Libraries

One issue with the OpenCL matrix libraries is that they
seem to be all inclusive affairs, generating their own seperate kernels. I'm really just looking for something that I can include in my own kernel.

Might be better rolling my own solution in OpenCL...

Not sure if this one does everything we need...

<https://github.com/clMathLibraries/clBLAS>

<http://www.raijincl.org/>

<http://icl.cs.utk.edu/magma/software/view.html?id=190>



Tutorial:

<http://www.cedricnugteren.nl/tutorial.php>
