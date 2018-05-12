Circle Fitting Info
======================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

http://stackoverflow.com/questions/15481242/python-optimize-leastsq-fitting-a-circle-to-3d-set-of-points

http://au.mathworks.com/matlabcentral/fileexchange/34792-circlefit3d-fit-circle-to-three-points-in-3d-space


http://mathematica.stackexchange.com/questions/16209/how-to-determine-the-center-and-radius-of-a-circle-given-some-points-in-3d

Least Squares
-----------------------

`Linear Least Squares <https://en.wikipedia.org/wiki/Linear_least_squares_(mathematics)>`_

Circle Equation:

:math:`(y - \beta_2)^2 + (x - \beta_1)^2 = \beta_0^2`


:math:`y = \beta_2 \pm \sqrt{\beta_0^2 - (x - \beta_1)^2}`

We want to constrain the circle to intersect with the lastest recorded point.

if :math:`y = \beta_2 \pm \sqrt{\beta_0^2 - (x - \beta_1)^2}`


Levenberg-Marquardt
--------------------

`How does it work? <http://stackoverflow.com/questions/1136416/how-does-the-levenberg-marquardt-algorithm-work-in-detail-but-in-an-understandab>`_

`how to do it in opencl <https://community.amd.com/thread/202678>`_

`a really detailed description <http://www.imagingshop.com/linear-and-nonlinear-least-squares-with-math-net/>`_
