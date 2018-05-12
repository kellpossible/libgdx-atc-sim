Design & API
=====================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

.. toctree::
	:caption: Java API (All Packages)
	:maxdepth: 1

	javadoc/index

This contains the javadoc generated api documentation.


.. toctree::
	:caption: Flight Data
	:maxdepth: 2

	Flight_Data/Flight_Data

The :java:extdoc`com.atc.simulator.flightdata` package provides classes for
dealing with flight information data.

.. toctree::
	:caption: Prediction Service
	:maxdepth: 2

	Prediction_Service/Prediction_Service

The
:java:extdoc:`com.atc.simulator.prediction_service`
is the core component of this project. It functions as a standalone unit capable
of taking input in the form of radar or ADSB tracks, then processing and
creating predictions in order to output predictions to a display. It is designed
such that the prediction service could run on a seperate machine on the network,
with its inputs and outputs being network sockets/streams.

.. toctree::
	:caption: Algorithm
	:maxdepth: 2

	Algorithm/Algorithm

The :java:extdoc:`com.atc.simulator.prediction_service.engine.algorithms.java`
package contains all of the java implementations for the different algorithm
types.

.. toctree::
	:caption: Display
	:maxdepth: 2

	Display/Display

The display is designed to be a high performance opengl application.
It achieves this aim by using libgdx's models api to create instances
of the draw geometry to get stored in the gpu vertex buffers.

The :java:extdoc:`com.atc.simulator.display` package contains all the display
functionality for this application.

.. toctree::
	:caption: Vectors Package
	:maxdepth: 1

	Vectors/Vectors_Package

The :java:extdoc:`com.atc.simulator.vectors`
package consists of various important base types used to represent and
manipulate the vector data in this project which are not already supplied
by the libraries it depends on.

.. toctree::
	:caption: Protocol Buffers
	:maxdepth: 1

	Protocol_Buffers
	Protocol_Buffer_Definitions

This project uses `Google's Protocol Buffers <https://developers.google.com/protocol-buffers/>`_
to serialize data for inter-thread/inter-process/network communication between
its modules.

Design Diagrams
---------------

.. figure:: Class_Design.svg
	:width: 100 %
	:align: center

	Class Design

The class diagram is a simplified overview, take a look at the specific
package documentation (all listed on this page) for detailed diagrams of each
package.


.. figure:: System_Design.svg
	:width: 100 %
	:align: center

	System Design

The system design diagram shows the thread/process/logic seperation of
the project. The ATC Simulator process actually also contains the
:doc:`Prediction_Service/Prediction_Service` at this point, but for a
real implementation (as shown on the diagram), this would be placed in
its own own process, able to operate independently of the simulator
(and headlessly) using external commercial grade Servers and Display
for the data input and output pipes via network communication.
