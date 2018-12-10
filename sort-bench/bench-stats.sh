#!/bin/bash

IN="sort-all.tsv"

JAVA_OPTS="-Xms1g -Xmx1g"
echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running Statistic"
java $JAVA_OPTS -cp ../out/edu-sorting.jar edu.sorting.perf.BentleyStatistics $IN > $IN-stats.out

echo "done."

