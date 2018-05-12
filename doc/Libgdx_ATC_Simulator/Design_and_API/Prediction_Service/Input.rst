Input
============

.. java:package:: com.atc.simulator
	:noindex:

The *Prediction Service* is designed to be able to take input from multiple
different kinds of sources, simultaneously. Inputs provide updates in the form
of :java:type:`flightdata.AircraftState` which are pushed into the
:java:type:`flightdata.system_state_database.SystemStateDatabase` as an update using its
:java:meth:`flightdata.system_state_database.SystemStateDatabase.update` method.

Currently the only input available is provided by the *DebugDataFeed* component
of the project. The data from this component is received via tcp in the form of
a protocol buffer and decoded in the :java:type:`prediction_service.DebugDataFeedClientThread`.
