#!/bin/bash

source env.sh

IN="sort-$SIZES"

JAVA_OPTS="-Xms1g -Xmx1g"
echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running Data Parser"
java $JAVA_OPTS -cp target/edu-sorting-bench.jar edu.sorting.bench.ArraySortDataParser $IN.log > $IN.tsv

echo "done."

