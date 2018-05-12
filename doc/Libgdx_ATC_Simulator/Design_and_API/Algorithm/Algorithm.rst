Algorithm
==============

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>


The :java:extdoc:`com.atc.simulator.prediction_service.engine.algorithms.java`
package contains all of the java implementations for the different algorithm
types.

.. java:package:: com.atc.simulator.prediction_service.engine
	:noindex:

The most relavent algorithm employed in this project is in the
:java:extdoc:`algorithms.java.JavaLMLeastSquaresAlgorithmV3` class, and also worth
checking out for understanding is :java:extdoc:`algorithms.java.JavaPredictionAlgorithm`.


Each time the algorithm is executed it uses the :java:extdoc:`algorithms.java.AlgorithmState`
passed to it from the :java:extdoc:`PredictionEngineThread`
from where it was stored in the :java:extdoc:`PredictionEngineSystemStateDatabase`
keyed to the aircraft which is currently having its prediction created.
It uses the AlgorithmState to store persisent state information about the algorithm
between runs of the algorithm in WorkItems.

LMLeastSquaresV3 Algorithm
--------------------------

The algorithm in :java:extdoc:`algorithms.java.JavaLMLeastSquaresAlgorithmV3` is the
latest iteration. Old iterations are kept for reference and testing purposed.

This algorithm uses Levenberg-Marquardt Least Squares Circle Fit method
to calculate a circle which matches recorded location points.

For an overview of this method of circle calculation, see
the research done in :doc:`../../Research/Circle_Fitting/Circle_Fitting_Notebook`.

It also uses :java:extdoc:`algorithms.java.JavaLinearAlgorithm` to calculate a linear projection
to submit in the prediction. Then a blending of the two prediction lines occurs
to produce the centre line which is then passed with the other two lines for
rendering in the :doc:`../Display/Display`

.. uml::
	:caption: Action Diagram fo LMLeastSquaresV3 Algorithm
	:width: 99 %
	:align: center

	@startuml
	!include Libgdx_ATC_Simulator/Design_and_API/Algorithm/Algorithm_Action_Diagram.puml
	@enduml
