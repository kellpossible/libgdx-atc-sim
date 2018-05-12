Compare Results Script
======================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

The compare_results.py script located in the
`libgdx-atc-sim/Results/Accuracy`_ directory can be used for comparing
outputs of the Libgdx_ATC_Simulator's integration testing. See
:doc:`../Libgdx_ATC_Simulator/User_Manual/Integration_Testing` for
more info on how to generate test json files as output from running
the simulator.

.. _`libgdx-atc-sim/Results/Accuracy`: https://github.com/kellpossible/libgdx-atc-sim/tree/master/Results/Accuracy/

Requirements
------------


Python 3 is a prerequisite. You also need to install the required files listed
in the requirements.txt document. This can be done with the following command:

.. code-block:: none

  pip install -r requirements.txt

On windows a better option may be to manually download and install the required
packages from `here <http://www.lfd.uci.edu/~gohlke/pythonlibs/>`_.


Running
------------


The --help flag documents the basic usage of this command line script.

.. code-block:: none

  usage: compare_results.py [-h] [-a [ID]] [-u [value]] [-e [Method]]
                            File [File ...]

  Compare integration test results.

  positional arguments:
    File                  Files for analysis/comparison

  optional arguments:
    -h, --help            show this help message and exit
    -a [ID], --aircraft-id [ID]
                          id of aircraft you want to select for analysis
                          (default: None)
    -u [value], --upper-error-bound [value]
                          The upper error bound (for display) (default: 40000)
    -e [Method], --error-calc [Method]
                          Method for error calculation. Allowed values:
                          Weighted, Average, Total (default: Total)

  Notes: Default aircraft for analysis is the first that appears.

As explained in the help, the script defaults to comparing on the first aircraft
that appears, but you can manually select an aircraft to use for comparison
using the -a command line argument.


Example
~~~~~~~~

An example using the script on two json files which were produced after
successive executions of the :doc:`../Libgdx_ATC_Simulator/Libgdx_ATC_Simulator`
using different algorithms:

.. code-block:: none

  python compare_results.py -u 200000 QFA696_LMLEASTSQUARESV3.json QFA696LINEAR2d.json


.. figure:: QFA696_2.png
	:align: center

	Fig1. - Example Output



User Interface Controls
------------------------


The user interface includes a time slider which you can click and drag to
view the output of the predictions at any given point in time. The map, and
error graph can be panned and zoomed using the provided matplotlib controls.
