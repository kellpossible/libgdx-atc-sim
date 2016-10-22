rem untested, probably doesn't work
protoc -I=src/ --java_out=src/ src/main/proto/PredictionFeedServe.proto
protoc -I=src/ --java_out=src/ src/main/proto/DebugDataFeedServe.proto
pause
