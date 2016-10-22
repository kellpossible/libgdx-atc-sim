#!/bin/sh

rm -f buildlog.txt && ./gradlew alljavadoc -i 2>&1 | tee buildlog.txt