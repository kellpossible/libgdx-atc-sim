Integration Testing
====================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

Integration testing can be enabled in the settings by changing the
**settings.testing.run-accuracy-test** to **true**
and also setting one or both of the **settings.testing.save-csv** or
**settings.testing.save-json** to **true**.

When it is enabled, upon running Libgdx_ATC_Simulator,
ouput files are created in the Results/Accuracy folder relative to the
execution working directory. If this folder does not exist, you may
need to create it.

Files are named according to the Scenario being played, the algorithm being used
for prediction, and a unix time stamp.

If using the json output option, you can then use the :doc:`../../Compare_Results/Compare_Results`
for analysing the data, and comparing the results between runs.
