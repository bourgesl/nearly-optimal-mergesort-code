#!/bin/bash

source env.sh

# do not force GC as setupTrial does cleanup() and sorters use pre-allocation
GC=false
FORK=1

# min iter = 10 (to sample all distributions)
WITER=4
WTIME=1s
ITER=5
TIME=2s

# Available formats: text, csv, scsv, json, latex
FORMAT=text

lscpu

../cpu_fixed.sh

echo "JAVA:"
java -version

# define CPU core to use
# Note: use linux kernel GRUB_CMDLINE_LINUX="isolcpus=3" in /etc/default/grub
export CPU_CORE_IDS=3


# define Java options
# -XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation
# -XX:-TieredCompilation
# -verbose:gc -Xloggc:gc.log
# -XX:+PrintGCApplicationStoppedTime
# -XX:-TieredCompilation to disable C1/C2 tiered compilation
# -XX:TieredStopAtLevel=1 to disable C2 ie only C1
JAVA_OPTS="-Xms1g -Xmx1g -XX:+UseParallelGC -XX:-BackgroundCompilation"
#JAVA_OPTS="-Xms1g -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=300000"
#JAVA_OPTS="-Xms1g -Xmx1g -XX:-TieredCompilation -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=300000"

#JAVA_OPTS="-Xms1g -Xmx1g -XX:+UseParallelGC -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI -XX:+UseJVMCICompiler"

echo "JAVA_OPTS: $JAVA_OPTS"

echo "Running JMH ..." 
# show help
#java -jar target/edu-sorting-bench.jar -h

# show benchmarks & parameters
#java -jar target/edu-sorting-bench.jar -lp

# single-threaded:
java $JAVA_OPTS -cp target/edu-sorting-bench.jar org.openjdk.jmh.Main SortingIntBenchmarkTestJMH -gc $GC -wi $WITER -w $WTIME -i $ITER -r $TIME -f $FORK -t 1 1> "SortingIntBenchmarkTestJMH.log" 2> "SortingIntBenchmarkTestJMH.err" 
