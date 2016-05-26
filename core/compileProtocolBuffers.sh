#!/bin/sh
echo "Compiling Protocol Buffers..."
protoc -I=src/ --java_out=src/ src/main/proto/PredictionFeedServe.proto
protoc -I=src/ --java_out=src/ src/main/proto/DebugDataFeedServe.proto
echo "Finished Compiling Protocol Buffers"
