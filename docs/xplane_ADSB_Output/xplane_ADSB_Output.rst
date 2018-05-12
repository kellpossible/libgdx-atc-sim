X-Plane ADSB Output Plugin
===========================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

In the `xplane-adsb-output`_ repository is a python plugin created
during for project for x-plane which allows data from the `X-Plane 10
flight simulator <http://x-plane.com/>`_ to be downlinked directly to
:doc:`../Libgdx_ATC_Simulator/Libgdx_ATC_Simulator` in place of the
DebugDataFeed.

This plugin was created to facilitate easy testing of the algorithm
during developement in specific scenarios, and for generating specific
realistic data for use in regression testing and potential
optimization applications.

.. _`xplane-adsb-output`: https://github.com/kellpossible/xplane-adsb-output

Videos
-------

Some of this tool being used are available on YouTube.

.. youtube:: U8mXVNskZf8
	:width: 100%

Testing with an f-22 fighter jet

.. youtube:: oC8Hk0qTTlk
	:width: 100%

Testing with a Boeing 747

Installation
-------------

To begin with you'll first need a copy of x-plane 10 (probably 11 will work too
but no guarantees). Installing the demo should be fine.

Next you'll need to follow the rather archaeic instructions to install
the x-plane python interface which can be found `here <http://www.xpluginsdk.org/python_interface.htm>`_.

Then, copy the contents of the *src/xplane_ADSB_output* folder into the
x-plane */Resources/plugins/PythonScripts/* folder in order to install
the plugin.

Running
----------

Start x-plane, and the plugin should be running in the background.
Then you'll need to set the **settings.debug-data-feed.enabled** value to **false**
in the settings.json file before running Libgdx_ATC_Simulator. The x-plane plugin
is by default set to listen to port 6989, the same one as the debug data feed.
Now all you need to do is run, and it should automatically connect.

If you need to change the port or the listen address you will need to edit the plugin
script called *PI_ADSBOutput.py*.

.. figure:: output.png
	:align: center

	Running x-plane and Libgdx_ATC_Simulator connected with each other
