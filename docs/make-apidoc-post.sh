#!/bin/sh

pushd ../
./gradlew -info alljavadoc
popd

mkdir -p _build/html/Libgdx_ATC_Simulator/Design_and_API/javadoc/

cp -r ../build/docs/javadoc/* _build/html/Libgdx_ATC_Simulator/Design_and_API/javadoc/