#!/bin/bash

lscpu

./cpu_fixed.sh

echo "JAVA:"
java -version

# define CPU core to use
CORE=0

# define Java options
# -XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation
# -XX:-TieredCompilation
# -verbose:gc
JAVA_OPTS="-Xms1g -Xmx1g"
echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running Bentley basher"
taskset -c $CORE java $JAVA_OPTS -cp out/nearly-optimal-mergesort.jar edu.sorting.perf.BentleyBasher > basher-results.out

echo "done."
