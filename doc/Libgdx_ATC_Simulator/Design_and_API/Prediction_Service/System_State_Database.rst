System State Database
======================

.. java:package:: com.atc.simulator.flightdata.system_state_database
	:noindex:

The :java:type:`SystemStateDatabase` stores the current state of all aircraft
being tracked by the system. It is designed to be capable of sorting, merging
and interpolating input data as it is updated by the various input sources.

Updates to the database trigger a
:java:meth:`SystemStateDatabaseListener.onSystemStateUpdate` on any
:java:type:`SystemStateDatabaseListener` currently listening to the database,
letting them know that an update has been recieved, and a list of aircraft
whos state has been affected.
