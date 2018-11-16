#!/bin/bash

JAVA_OPTS="-Xms1g -Xmx1g"
echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running Statistic"
java $JAVA_OPTS -cp out/nearly-optimal-mergesort.jar edu.sorting.perf.Statistic basher-results.out >> basher-results.out

echo "done."

