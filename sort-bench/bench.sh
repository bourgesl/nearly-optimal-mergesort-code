#!/bin/bash

WTIME=100ms
ITER=3
TIME=100ms
FORK=1
# Available formats: text, csv, scsv, json, latex
FORMAT=text

TEST=ArraySortBenchmark
# ArrayCopyBenchmark

lscpu

../cpu_fixed.sh

echo "JAVA:"
java -version

# define CPU core to use
# Note: use linux kernel GRUB_CMDLINE_LINUX="isolcpus=3" in /etc/default/grub
CORE=3


# define Java options
# -XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation
# -XX:-TieredCompilation
# -verbose:gc -Xloggc:gc.log
# -XX:+PrintGCApplicationStoppedTime
#JAVA_OPTS="-Xms1g -Xmx1g"
#JAVA_OPTS="-Xms1g -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=300000"
JAVA_OPTS="-Xms1g -Xmx1g -XX:-TieredCompilation -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=300000"
echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running JMH ..." 
# single-threaded:
taskset -c $CORE java $JAVA_OPTS -jar target/edu-sorting-bench.jar $TEST -wi 3 -w $WTIME -i $ITER -r $TIME -f $FORK -t 1 -rf $FORMAT -rff bench.out &> bench.log 

