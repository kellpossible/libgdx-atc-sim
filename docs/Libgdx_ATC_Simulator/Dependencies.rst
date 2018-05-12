Dependencies
====================

This document outlines the dependencies employed in this java project, their
purpose within the project, and the licenses associated with them.

.. rubric:: Diagrams

.. figure:: Libgdx_ATC_Simulator_Diagram.svg
	:width: 100 %
	:align: center

	Class Dependency Diagram for Libgdx ATC Simulator

.. rubric:: Table of Dependencies

.. raw:: html

	<table border="1" class="docutils">
	<thead valign="bottom">
	<tr class="row-odd"><th class="head">Dependency</th>
	<th class="head">Purpose</th>
	<th class="head">License</th>
	</tr>
	</thead>
	<tbody valign="top">
	<tr class="row-even"><td><a href="https://libgdx.badlogicgames.com/">libgdx</a></td>
	<td>Used as the opengl and window library for the display</td>
	<td><a href="https://github.com/libgdx/libgdx/blob/master/LICENSE">Apache 2</a></td>
	</tr>
	<tr class="row-even"><td><a href="https://github.com/typesafehub/config">config</a></td>
	<td>Used for parsing the configuration files</td>
	<td><a href="https://github.com/typesafehub/config/blob/master/LICENSE-2.0.txt">Apache 2</a></td>
	</tr>
	<tr class="row-even"><td><a href="https://github.com/google/gson">gson</a></td>
	<td>Used for serializing/deserializing java objects to json</td>
	<td><a href="https://github.com/google/gson/blob/master/LICENSE">Apache 2</a></td>
	</tr>
	<tr class="row-odd"><td><a href="https://github.com/timmolter/XChart">xchart</a></td>
	<td>Used for graphs in a few of the tests</td>
	<td><a href="https://github.com/timmolter/XChart/blob/develop/LICENSE">Apache 2</a></td>
	</tr>
	<tr class="row-even"><td><a class="reference external" href="http://www.jocl.org/">jocl</a></td>
	<td>Used for the opencl worker implementation</td>
	<td><a href="https://github.com/gpu/JOCL/blob/master/LICENSE.TXT">MIT</a></td>
	</tr>
	<tr class="row-odd"><td><a class="reference external" href="http://ddogleg.org/">ddogleg</a></td>
	<td>Used for the least squares fitting algorithms</td>
	<td><a class="reference external" href="https://github.com/lessthanoptimal/ddogleg/blob/master/LICENSE-2.0.txt">Apache 2</a></td>
	</tr>
	<tr class="row-even"><td><a href="http://junit.org/">junit</a></td>
	<td>Used for unit testing</td>
	<td><a href="http://junit.org/junit4/license.html">Eclipse Public License 1.0</a></td>
	</tr>
	<tr class="row-even"><td><a href="http://openjdk.java.net/projects/code-tools/jmh/">jmh</a></td>
	<td>Used for some performance benchmarks/unit tests. May need removal due to license.</td>
	<td><a href="http://hg.openjdk.java.net/code-tools/jmh/file/0060fbb99146/jmh-core/LICENSE">GPL 2.0</a></td>
	</tr>
	<tr class="row-odd"><td><a href="https://github.com/samskivert/pythagoras">pythagoras</a></td>
	<td>Compact vector math library.</td>
	<td><a href="https://github.com/samskivert/pythagoras/blob/master/LICENSE">Apache 2</a></td>
	</tr>
	<tr class="row-even"><td><a href="https://developers.google.com/protocol-buffers/">protobuf</a></td>
	<td>Used for protocol buffers, network packets.</td>
	<td><a class="reference external" href="https://github.com/google/protobuf/blob/master/LICENSE">BSD 2-clause</a></td>
	</tr>
	<tr class="row-odd"><td><a class="reference external" href="http://ejml.org/">ejml</a></td>
	<td>Efficient Java Matrix Library (EJML), used for matrix operations</td>
	<td><a class="reference external" href="https://github.com/lessthanoptimal/ejml/blob/master/LICENSE-2.0.txt">Apache 2</a></td>
	</tr>
	</tbody>
	</table>
