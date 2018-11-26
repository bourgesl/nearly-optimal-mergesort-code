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
JAVA_OPTS="-Xms1g -Xmx1g"
echo "JAVA_OPTS: $JAVA_OPTS"

PREFIX="taskset -c $CORE java $JAVA_OPTS -jar out/edu-sorting.jar "
SEED=248442268

echo Running study 1 - Random Permutations
$PREFIX 200          100,500,1000,5000,10_000,50_000,100_000   $SEED rp        times-rp             > times-rp.out
#$PREFIX  201      10_000_000          $SEED rp        times-rp-10m         > times-rp-10m.out
#$PREFIX   21     100_000_000          $SEED rp        times-rp-100m        > times-rp-100m.out

#echo Running study 2 - iid runs
#$PREFIX  200     100,500,100,500,1000,5000,10_000,50_000,100_000          $SEED iid500    times-iid500      > times-iid500.out

echo Running study 3 - sqrt-n runs
$PREFIX  200     100,500,1000,5000,10_000,50_000,100_000          $SEED runs30    times-runs30         > times-runs30.out
#$PREFIX  201      10_000_000          $SEED runs3000    times-runs3k-10m   > times-runs3k-10m.out

echo Running study 4 - timdrag input
$PREFIX  200      100,500,1000,5000,10_000,50_000,100_000          $SEED timdrag32   times-timdrag32 > times-timdrag32.out

echo "done."
