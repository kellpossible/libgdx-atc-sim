Coordinate Systems
===================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

We are getting data for velocity in a cartesian format, and data for position in a polar format, this is a problem if we want to combine the data to do extrapolation. It would be nice to have a way to convert the cartesian velocity into polar velocity.


Conversion between polar and cartesian
--------------------------------------


how to convert between wgs84 lat/lon polar and cartesian coordinate systems
http://www.movable-type.co.uk/scripts/latlong.html


`Geographic Coordinate Conversion <https://en.wikipedia.org/wiki/Geographic_coordinate_conversion>`_


Projections
-----------------------------------

Gnomonic
~~~~~~~~~~~
`Gnomonic Projection <https://en.wikipedia.org/wiki/Gnomonic_projection>`_ :cite:`WikiGnomonic`

http://www.progonos.com/furuti/MapProj/Normal/ProjAz/projAz.html

Dad used this on a radar project

Mercator
~~~~~~~~~~~~~~

`Transverse Mercator Projection <https://en.wikipedia.org/wiki/Transverse_Mercator_projection>`_

`Web Mercator Projection <https://en.wikipedia.org/wiki/Web_Mercator>`_

Cartesian vs Polar
----------------------

Velocity
~~~~~~~~~~~~~~~~~~~~~

One of the main differentiating factors between using cartesian and polar
coordinate systems in our calculations will be the way that we want to treat
velocity. A constant spherical velocity, is definitely not the same as a
constant line cartesian velocity vector, while the difference is initially
small, eventually the cartesian velocity (in the case of an aircraft flying)
will move away from the surface of the planet.

TODO: insert illustration here.

In the case of a constant cartesian velocity, if there were not autopilot and no
gravity, and no aerodynamic effects this would certainly be an accurate
prediction of the trajectory of the aircraft.

As it happens, aircraft, while on autopilot, or while trimmed at a constant
engine power, wind velocity, temperature and pressure, tend to maintain a
constant altitude above mean sea level. Pilots, while following a flight plan,
always intend to hold a certain altitude. The exception to this is during
climb or descent. Sometimes a constant rate descent is employed, or as is often
the case with aircraft with more complicated computers, a variable
ascent/descent rate is used to optimise fuel economy.

Working in polar coordinates for velocity provides a simple means to represent
the inherent desire for pilots to maintain a constant altitude and follow great
circle tracks during straight and level flight, especially over longer periods
or at faster speeds where the curvature of the earth becomes apparent. A single
constant spherical velocity vector can accurately predict the motion of a
cruising aircraft for a long period of time. One does however need to recognise that
vehicles such as military aircraft or rockets flying at higher speeds can defy
these expectations.

For this reason I'd like to propose that we use Spherical Velocity vectors as
our default representation of velocity. For the usual constant state flying
scenarios it should do the best job. We can of course convert these vectors
into cartesian vectors for the motion prediction algorithm, but in this case,
these effects of curvature need to be taken into account by a changing velocity
along the length of the trajectory prediction, even for straight lines.


Work that still needs to get done in the library is:
- find out how to transform a SphericalCoordinate + heading + distance into
another spherical coordinate at that distance.


Bibliography
-------------------------

.. bibliography:: /references/SoftwareFinalYear.bib
  :style: unsrtalpha
  :filter: docname in docnames
