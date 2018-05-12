Output
==========

.. java:package:: com.atc.simulator
	:noindex:

The *Prediction Service* is designed to be able to output to a single client
application using protocol buffer encoded data via a tcp connection. The output
data consists of a :java:type:`flightdata.Prediction` object per aircraft currently being
tracked by the system and contained in the :java:type:`flightdata.system_state_database.SystemStateDatabase`.

Output is currently handled by the :java:type:`prediction_service.PredictionFeedServerThread`.
