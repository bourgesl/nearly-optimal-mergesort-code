#!/bin/bash

JAVA_OPTS="-Xms1g -Xmx1g"
echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running Data Parser"
java $JAVA_OPTS -cp target/edu-sorting-bench.jar edu.sorting.bench.ArraySortDataParser results/sort-100.log > sort-100.tsv

echo "done."

