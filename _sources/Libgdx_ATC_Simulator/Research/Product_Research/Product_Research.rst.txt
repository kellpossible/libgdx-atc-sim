Product Research
====================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

.. contents:: Table of Contents
  :depth: 3

FLARM
-------------------

From the `FLARM Website <https://flarm.com/technology/>`_ :cite:`FLARM`

.. epigraph::

  *FLARM is an award-winning and affordable collision avoidance system
  for General Aviation, light aircraft and UAVs.*

  -- FLARM

FLARM is a distributed system, installed on many light aircraft and gliders. The
onboard GPS of the FLARM is used to gather information about the aircraft's
current 3D trajectory. This in turn is used to find the turn radius, and the
wind, providing an accurate prediction of the future prediction. :cite:`FLARM`
This prediction is then broadcast over radio to any nearby aircraft with a FLARM
device installed. The prediction information is encoded as a series of points at
regular time step intervals into the future :cite:`OmegaTauFLARM`.


Upon receiving the predictions from other aircraft, the FLARM device then runs
an algorithm to predict potential collisions with other aircraft. This is then
communicated to the pilot in a number of different ways, and it is also possible
to connect a third party display to show other aircraft on a screen.




Bibliography
-------------------------

.. bibliography:: /references/SoftwareFinalYear.bib
  :style: unsrtalpha
  :filter: docname in docnames
