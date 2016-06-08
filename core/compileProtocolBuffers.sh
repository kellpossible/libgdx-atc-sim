#!/bin/sh
echo "Compiling Protocol Buffers..."
protoc -I=src/ --java_out=src/ src/main/proto/PredictionFeedServe.proto
mv src/com/atc/simulator/ProtocolBuffers/PredictionFeedServe.java src/main/com/atc/simulator/ProtocolBuffers/PredictionFeedServe.java
protoc -I=src/ --java_out=src/ src/main/proto/DebugDataFeedServe.proto
mv src/com/atc/simulator/ProtocolBuffers/DebugDataFeedServe.java src/main/com/atc/simulator/ProtocolBuffers/DebugDataFeedServe.java
rm -rf src/com/
echo "Finished Compiling Protocol Buffers"
