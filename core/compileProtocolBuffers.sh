#!/bin/sh
echo "Compiling Protocol Buffers..."
protoc -I=src/ --java_out=src/ src/main/proto/PredictionDisplay_Proto.proto
echo "Finished Compiling Protocol Buffers"
