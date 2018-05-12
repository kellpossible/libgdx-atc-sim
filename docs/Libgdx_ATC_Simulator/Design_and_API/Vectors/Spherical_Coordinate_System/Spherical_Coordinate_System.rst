Spherical Coordinate System
===============================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

.. java:package:: com.atc.simulator.vectors
	:noindex:

Introduction
--------------------

Spherical and Spheroid based coordinate systems make up the backbone of this
project and are used throughout to represent positions and velocities.

All angles are in radians unless otherwise specified.


SphericalCoordinate
--------------------

The :java:type:`SphericalCoordinate` class represents a coordinate in
the spherical coordinate system. The notation used for spherical coordinates
can vary. The notation used in this project is more popular in mathematics,
and is the same as outlined in
`Wolfram Alpha - Spherical Coordinates <http://mathworld.wolfram.com/SphericalCoordinates.html>`_.

+------------------------------------+---------------------------+
|               order                |          notation         |
+------------------------------------+---------------------------+
| :math:`(radial, azimuthal, polar)` | :math:`(r, \theta, \phi)` |
+------------------------------------+---------------------------+


+----------------+----------------------------+---------+
|    ordinate    |           range            |   unit  |
+----------------+----------------------------+---------+
| :math:`r`      | :math:`0 <= r < \infty`    | meters  |
+----------------+----------------------------+---------+
| :math:`\theta` | :math:`0 <= \theta < 2\pi` | radians |
+----------------+----------------------------+---------+
| :math:`\phi`   | :math:`-\pi <= \phi < \pi` | radians |
+----------------+----------------------------+---------+


GeographicCoordinate
--------------------

The :java:type:`GeographicCoordinate` class represents a coordinate in the
geographic coordinate system. It inherits from :java:type:`SphericalCoordinate`
and uses it to store the data, and provide common methods. This works because
both are based a pure sphere. Future types of geographic coordinates may be
based on alternative, more accurate representations of the shape of the earth.

*Altitude* currently is represented as distance above a mean sea level radius
of 6371000.0m

+-----------------------------------------+
|                  order                  |
+-----------------------------------------+
| :math:`(altitude, latitude, longitude)` |
+-----------------------------------------+

+-------------------+-----------------------------------------------+---------+
|      ordinate     |                     range                     |   unit  |
+-------------------+-----------------------------------------------+---------+
| :math:`altitude`  | :math:`0 <= r < \infty`                       | meters  |
+-------------------+-----------------------------------------------+---------+
| :math:`latitude`  | :math:`\frac{-\pi}{2} < \phi < \frac{\pi}{2}` | radians |
+-------------------+-----------------------------------------------+---------+
| :math:`longitude` | :math:`-\pi <= \theta < \pi`                  | radians |
+-------------------+-----------------------------------------------+---------+

SphericalVelocity
--------------------

The :java:type:`SphericalVelocity` class represents the rate of change of
spherical position in the spherical coordinate system. This velocity is valid
only for a given location/instant in time.

Integration
~~~~~~~~~~~~~~~~~
For simple integration it will need to be converted into an equivalent velocity
in a different coordinate system which is more Euclidian.

This could either be by using a map projection from
:doc:`../Projection/Projections` to flatten the coordinate system, or by
converting it into cartesian space, or by converting it into angular velocity in
cartesian space.

Correct integration of spherical velocity over the spherical or geographical
coordinate systems is quite a bit more involved, and the math for that has not
yet been explored or deemed useful.
