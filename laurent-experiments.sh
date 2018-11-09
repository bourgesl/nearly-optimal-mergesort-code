#! /bin/sh
# -XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation
# -XX:-TieredCompilation
# -verbose:gc
PREFIX="taskset -c 2 java -Xms1g -Xmx1g -jar out/nearly-optimal-mergesort.jar "
SEED=248442268

echo Running study 1 - Random Permutations
$PREFIX 200          1000,5000,10_000,50_000,100_000   $SEED rp        times-rp             > times-rp.out
#$PREFIX  201      10_000_000          $SEED rp        times-rp-10m         > times-rp-10m.out
#$PREFIX   21     100_000_000          $SEED rp        times-rp-100m        > times-rp-100m.out

#echo Running study 2 - iid runs
#$PREFIX  200     1000,5000,10_000,50_000,100_000          $SEED iid500    times-iid500      > times-iid500.out

echo Running study 3 - sqrt-n runs
$PREFIX  200     1000,5000,10_000,50_000,100_000          $SEED runs30    times-runs30         > times-runs30.out
#$PREFIX  201      10_000_000          $SEED runs3000    times-runs3k-10m   > times-runs3k-10m.out

echo Running study 4 - timdrag input
$PREFIX  200      1000,5000,10_000,50_000,100_000          $SEED timdrag32   times-timdrag32 > times-timdrag32.out

