Prediction Engine
===================

The
:java:extdoc:`com.atc.simulator.prediction_service.engine`
package forms the core of the *Prediction Service*.

.. java:package:: com.atc.simulator.prediction_service.engine
	:noindex:

.. rubric:: Diagrams

.. figure:: Class_Design.svg
	:width: 100 %
	:align: center

	Class Design


PredictionEngineThread
------------------------

Within this package, the :java:type:`PredictionEngineThread` is a thread which
runs continuously, waiting for updates to the :java:type:`com.atc.simulator.flightdata.system_state_database.SystemStateDatabase`
to occur. Each update results in the creation of a new
:ref:`prediction-work-item`. The new work item is then placed in a queue to
later be assigned to one of the :ref:`workers` belonging to the prediction
engine.


PredictionEngineListener
-------------------------

For classes wishing to interact with the output of the prediction engine, there
is the :java:type:`PredictionEngineListener`. The primary example of this is the
:java:type:`PredictionFeedServerThread` which waits for prediction work items
to complete in the prediction engine, and broadcasts the predictions to its
clients.

.. _prediction-work-item:

PredictionWorkItem
----------------------

The :java:type:`PredictionWorkItem` class is designed to be a self contained
unit of work for the :ref:`workers` to work on to produce predictions using
various :ref:`algorithms`. It is used to keep track of what work is being done
and by whom, and stores a complete thread-safe copy of the data required for
the worker to perform the work required to complete this work item.

The PredictionWorkItem is also used to help track of the realtime status of work
items and when they are complete This gains useful information on the
performance of the system in order to accurately identify any major performance
flaws if and when they occur.


.. _workers:

Workers
-------------

Workers do work on :ref:`prediction-work-item` for the prediction engine.
There are currently two types of workers in the project.

Java Prediction Worker
~~~~~~~~~~~~~~~~~~~~~~~
The :java:type:`workers.JavaPredictionWorkerThread` is a pure java thread which performs
the job of a worker for the prediction engine. Its algorithm implementations
are stored here: :java:extdoc:`com.atc.simulator.prediction_service.engine.algorithms`

OpenCL Prediction Worker
~~~~~~~~~~~~~~~~~~~~~~~~

The :java:type:`workers.OpenCLPredictionWorkerThread` is a java thread, which uses the
`OpenCL <https://www.khronos.org/opencl/>`_ api to perform its work item
computations using specialised parallel compute hardware. The implementation
is currently optimised for use on an NVIDIA GTX760 graphics processing unit.

The front end to its algorithm implementations are stored here:
:java:extdoc:`com.atc.simulator.prediction_service.engine.algorithms.opencl`

The back end to its algorithm implementations, the OpenCL C code is stored in
the project assets directory: */assets/opencl/*

.. _algorithms:

Algorithms
-------------

The main prediction algorithm currently employed uses least-squares method to fit circles
to track data. Detailed documentation can be found here: :doc:`../../Algorithm/Algorithm`
