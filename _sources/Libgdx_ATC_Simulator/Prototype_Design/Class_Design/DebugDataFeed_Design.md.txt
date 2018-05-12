# DebugDataFeed design document

Needs to read the ATC state information contained within different types of
files.

The first type is the simulator generated csv and fms files. This csv file has
rows, each row containing state information about the aircraft at a  given
moment in time includes at least position, speed, heading and vertical speed,
along with the time associated with this.

We've got plenty of ram for the simulation, so just load the entire
track into the memory.


DebugDataScenario has the start time and the end time for the scenario. Each scenario is responsible for loading the data, and providing system states.

A system state consists of an array of all the aircraft states.
