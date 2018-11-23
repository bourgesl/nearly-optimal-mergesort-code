#!/bin/bash

lscpu

./cpu_fixed.sh

echo "JAVA:"
java -version

# define CPU core to use
# Note: use linux kernel GRUB_CMDLINE_LINUX="isolcpus=3" in /etc/default/grub
CORE=3

# define Java options
# -XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation
# -XX:-TieredCompilation
# -verbose:gc
# -XX:+PrintGCApplicationStoppedTime
#JAVA_OPTS="-Xms1g -Xmx1g"
#JAVA_OPTS="-Xms1g -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=300000"
JAVA_OPTS="-Xms1g -Xmx1g -XX:-TieredCompilation -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=300000"
echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running Bentley basher"
taskset -c $CORE java $JAVA_OPTS -cp out/nearly-optimal-mergesort.jar edu.sorting.perf.BentleyBasher > basher-results.out

echo "done."
