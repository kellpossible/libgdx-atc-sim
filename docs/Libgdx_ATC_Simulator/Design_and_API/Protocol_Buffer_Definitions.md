# Protocol Buffer Definitions 

_Section author: Uros Vukanovic_

## DebugDataFeedServe.proto

+ The protocol buffer [DebugDataFeedServe.proto](../../src/Libgdx_ATC_Simulator/core/src/main/proto/DebugDataFeedServe.proto) is used to communicate serialized messages from the Debug Data Feed Server to the Client. The protocol buffer uses the proto2 syntax. 
+ The message consists of a SphericalVelocity, GeographicalCoordinate, AircraftState and a SystemState Message. It will serialize these message structures, prepare them for network transmission and send them, while de-serializing on the receiving end. 

## PredictionFeedServe.proto

+ The protocol buffer [PredictionFeedServe.proto](../../src/Libgdx_ATC_Simulator/core/src/main/proto/PredictionFeedServe.proto) is used to communicate serialized messages from the Prediction Feed Server to the Client over a TCP network protocol. The protocol buffer uses proto2 syntax. 
+ The serialized message consists of: Spherical velocity, GeographicCoordinate, PreditionAircraftState, Track, AircraftPredictionMessage, which consists of a State.




