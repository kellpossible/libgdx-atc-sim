Prediction Service
========================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

The
:java:extdoc:`com.atc.simulator.prediction_service`
is the core component of this project. It functions as a standalone unit capable
of taking input in the form of radar or ADSB tracks, then processing and
creating predictions in order to output predictions to a display. It is designed
such that the prediction service could run on a seperate machine on the network,
with its inputs and outputs being network sockets/streams.

.. rubric:: Diagrams

.. figure:: Class_Design.svg
	:width: 100 %
	:align: center

	Class Design

.. rubric:: Table of Contents

.. toctree::
  :maxdepth: 2

  Input
  Output
  System_State_Database
  Engine/Prediction_Engine
