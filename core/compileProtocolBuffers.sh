#!/bin/sh
echo "Compiling Protocol Buffers..."
protoc -I=src/ --java_out=src/ src/main/proto/PredictionFeedServe.proto
mv src/com/atc/simulator/protocol_buffers/PredictionFeedServe.java src/main/com/atc/simulator/protocol_buffers/PredictionFeedServe.java
mv python_src/main/proto/DebugDataFeedServe_pb2.py python_src/DebugDataFeedServe_pb2.py
protoc -I=src/ --java_out=src/ --python_out=python_src/ src/main/proto/DebugDataFeedServe.proto
mv src/com/atc/simulator/protocol_buffers/DebugDataFeedServe.java src/main/com/atc/simulator/protocol_buffers/DebugDataFeedServe.java
sleep 0.1
rm -rf src/com/
rm -rf python_src/main
echo "Finished Compiling Protocol Buffers"
