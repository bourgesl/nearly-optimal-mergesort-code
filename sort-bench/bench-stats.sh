#!/bin/bash

source env.sh

IN="sort-$SIZES"

JAVA_OPTS="-Xms1g -Xmx1g"
echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running Statistic"
java $JAVA_OPTS -cp ../out/edu-sorting.jar edu.sorting.perf.BentleyStatistics $IN.tsv > $IN-stats.out

echo "done."

