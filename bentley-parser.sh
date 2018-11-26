#!/bin/bash

JAVA_OPTS="-Xms1g -Xmx1g"
echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running Statistic"
java $JAVA_OPTS -cp out/edu-sorting.jar edu.sorting.perf.BentleyDataParser basher-results.out.50 > basher-results.out.50.tsv

echo "done."

