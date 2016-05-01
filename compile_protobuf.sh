#!/bin/sh
protoc -I=./ --java_out=./core/src/ ./protobuf/DebugDataFeedServe.proto
