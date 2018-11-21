package edu.sorting.perf;

import edu.sorting.ArrayUtils;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Locale;
import wildinter.net.WelfordVariance;

/**
 * @author Jon Bentley
 */
public final class BentleyBasher {

    private final static boolean REPORT_VERBOSE = false;
    private final static boolean REPORT_TIME_ERR = false;

    private final static boolean REPORT_DEBUG_ESTIMATOR = false;

    private final static boolean SHOW_DIST_FLAGS = false;

    private final static boolean USE_RMS = true; // false means use MIN(TIME)

    private final static int TWEAK_INC = 4; // 2 originally

    private final static boolean DO_WARMUP = true;
    private final static int WARMUP_REPS = 20;
    private final static int[] WARMUP_LENGTHS = new int[]{501, 10001};

    // 5ms >> [Full GC (System.gc())  794K->794K(1013632K), 0,0022879 secs]
    private final static long GC_LATENCY = 10l;

    // TODO: use arguments for selected sorters, (sorter reference), warmup, sizes ... at least
    public final static int IDX_BASELINE = IntSorter.BASELINE.ordinal();
    public final static int IDX_REF = 1; // IntSorter.DPQ_18_11.ordinal();
    public final static int IDX_TEST = 2; // IntSorter.DPQ_18_11P.ordinal();

    public final static long MIN_NS = 50; // latency in ns
    public final static double MIN_PREC = 1e-9 * MIN_NS / 1e3; // ms

    private static final int HUGE_N = 10 * 1000 * 1001;

    private static final int[] LENGTHS = {50, 100, 200, 500, 1000, 2000, 5000, 10000, 50000 /*, 100000, HUGE_N */};
//    private static final int[] lengths = {50 * 1000};

    // threshold to do more iterations (inner-loop) for small arrays in order to reduce variance:
    private static final int SMALL_TH = 10000;

    private static final int REP_MIN = 5;
    private static final int REP_DISTRIB = 10;
    private static final int REP_SKIP = 10;
    private static final int ADJ_MAX = 10;

    private static final long SEC_IN_NS = 1000 * 1000 * 1000; // 1s in ns;
    private static final long MIN_LOOP_TIME = SEC_IN_NS / 250; // 4ms
    private static final long MAX_LOOP_TIME = 10 * SEC_IN_NS; // 10s max

    private static final int ERR_DIST_TH = 3; // 3% per timing loop
    private static final int ERR_WARNING = 25; // 25% warning on all distributions

    private final static DecimalFormat df2;
    private final static DecimalFormat df6;

    static {
        // Set the default locale to en-US locale (for Numerical Fields "." ",")
        Locale.setDefault(Locale.US);
        df2 = new DecimalFormat("0.00");
        df6 = new DecimalFormat("0.000000");
    }

    public static void main(String[] args) {
        System.out.println("\n--- Bentley Basher ---\n");
        if (DO_WARMUP) {
            System.out.println("Start warm up ...");
            // At least more than 10,000 per sorter (JIT hotspot threshold)
            final int warmupCount = sort(WARMUP_REPS, WARMUP_LENGTHS);
            System.out.println("  End warm up (" + warmupCount + " iterations per sort).\n");
        }

        System.out.println("-----\n");
        System.out.println("\nStarting benchmarks ...");
        if (USE_RMS) {
            System.out.println("Timings are given in milli-seconds (rms = mean + 1 stddev)");
        } else {
            System.out.println("Timings are given in milli-seconds (min time)");
        }
        System.out.println("\n");
        sort(0, null);
        System.out.println("\n-----\n");
    }

    private static int reps(int n) {
        return (int) (12000000 / (n * Math.log10(n)));
    }

    private static int sort(final int maxReps, final int[] forceLengths) {
        final boolean warmup = (maxReps > 0);

        final IntSorter[] sorters = IntSorter.values();
        final double[] times = new double[sorters.length];

        final PrintStream out = System.out;

        if (!warmup) {
            out.print("Length Sub-size  Builder Tweaker");

            if (REPORT_VERBOSE) {
                out.print("\tSorter\tRMS\tMean\tStdDev\tCount\tMin\tMax\tExtra");
            } else {
                for (IntSorter sorter : sorters) {
                    out.print("\t" + sorter);
                }
                out.print("\t[" + sorters[IDX_REF] + " % " + sorters[IDX_TEST] + "]");
            }
            out.println();
            out.println();
        }

        long start;
        long time, estTime, maxEstTime;

        final int[] realLengths = (forceLengths != null) ? forceLengths : LENGTHS;

        final WelfordVariance statSorter = new WelfordVariance();
        final WelfordVariance statSorterRef = new WelfordVariance();
        WelfordVariance statDist;

        // adjust reps to sample properly distributions
        final int repDist = (warmup) ? 2 : REP_DISTRIB;

        final WelfordVariance[] statDists = new WelfordVariance[repDist];
        for (int d = 0; d < repDist; d++) {
            statDists[d] = new WelfordVariance();
        }

        int loopCount = 0;
        int numTest = 0;

        for (int n : realLengths) {
            int reps = reps(n);
            if (warmup) {
                if (reps > maxReps) {
                    reps = maxReps;
                }
                out.println("Warmup length: " + n + " reps: " + reps);
            }
//            out.print("reps: " + reps + "\n");
            final int lreps = 1 + ((n <= SMALL_TH) ? (int) Math.ceil(4.0 * SMALL_TH / n) : 0);
            if (lreps > 1) {
                reps = Math.max(1, reps / lreps);
            }

            // adjust reps to sample properly distributions
            reps = Math.max((warmup) ? 1 : REP_MIN, reps / repDist);

            out.println("lreps: " + lreps + " with reps: " + reps + "\n");

            final int skipReps = (warmup) ? 0 : REP_SKIP;
            final int maxAdjSteps = (warmup) ? 0 : ADJ_MAX;

            // Allocate working array:
            final int[] input = new int[n];
            final int[] proto = new int[n];
            final int[] test = new int[n];

            // adjust tweak increment depending on array size
            final int tweakInc = (n > 100000) ? 16 : TWEAK_INC;

            boolean noBL;
            double timeBL = 0.0;
            double ratio, avg;
            long totReps, newTotReps;
            String testHeader = "";

            for (int m = 1, end = 2 * n; m < end; m *= tweakInc) {

                for (IntArrayTweaker iat : IntArrayTweaker.values()) {

                    for (ParamIntArrayBuilder iab : ParamIntArrayBuilder.values()) {

                        if (!warmup) {
                            testHeader = n + " " + m + "  " + iab + " " + iat;
                            if (!REPORT_VERBOSE) {
                                out.print(testHeader);
                            }
                        }

                        timeBL = 0.0;

                        for (int i = 0, d, e, r, l, loopReps, statReps, newLoopReps; i < sorters.length; i++) {
                            final IntSorter sorter = sorters[i];
                            statSorter.reset();

                            if (!warmup) {
                                cleanup();
                            }

                            // Reset tweaker to have sample initial conditions (seed):
                            ParamIntArrayBuilder.reset();

                            // adjust max estimated time per distribution except for baseline and first 3 tests:
                            maxEstTime = ((i == IDX_BASELINE) || (numTest < 3)) ? MAX_LOOP_TIME : (MAX_LOOP_TIME / repDist);

                            // sample 10x times the distribution
                            for (d = 0; d < repDist; d++) {
                                // Get new distribution sample:
                                iab.build(input, m);

                                // tweak sample:
                                iat.tweak(input, proto);

                                // Backup Sorter stats:
                                statSorterRef.copy(statSorter);

                                for (loopReps = lreps, statReps = reps, l = 0;;) {
                                    statDist = statDists[d];
                                    statDist.reset();

                                    time = 0l;

                                    for (e = statReps + skipReps; e >= 0; e--) {
                                        // reduce variance on very small arrays (use more repeats):

                                        // measurement loop:
                                        start = System.nanoTime();

                                        for (r = loopReps; r >= 0; r--) {
                                            ArrayUtils.clone(proto, test);
                                            sorter.sort(test);
                                        } // hot timing loop

                                        time = System.nanoTime() - start;
                                        loopCount += loopReps;

                                        if (time > MIN_NS) {
                                            final double measure = ((double) time) / loopReps;

                                            statDist.add(measure);

                                            if (e >= skipReps) {
                                                statSorter.add(measure);
                                            }
                                        } else {
                                            System.err.println("Too small loop time: " + time);
                                        }
                                    } // statistics loop

                                    if (warmup) {
                                        break;
                                    }

                                    if (REPORT_DEBUG_ESTIMATOR) {
                                        out.print(testHeader);
                                        out.print('\t');
                                        out.print(sorter);
                                        out.print('\t');
                                        out.print(round(df6, 1E-6 * statDist.mean())); // ms
                                        out.print('\t');
                                        out.print(round(df6, 1E-6 * statDist.stddev()));
                                        out.print('\t');
                                        out.print(statDist.nSamples());
                                        out.print('\t');
                                        out.print(round(df6, 1E-6 * statDist.min()));
                                        out.print('\t');
                                        out.print(round(df6, 1E-6 * statDist.max()));
                                        out.print('\t');
                                    }

                                    if (statDist.errorPercent() <= ERR_DIST_TH) {
                                        if (REPORT_DEBUG_ESTIMATOR) {
                                            out.println("Loop " + l + ": " + round(df2, statDist.rawErrorPercent()) + " % (OK)");
                                        }
                                        break;
                                    }

                                    if (++l > maxAdjSteps) {
                                        break;
                                    }

                                    // TODO: test mean time convergence ie loop stability on statDist.mean()
                                    avg = statDist.mean();

                                    totReps = ((long) statReps) * loopReps; // may overflow ?

                                    if (time < MIN_LOOP_TIME) {
                                        ratio = 1.1 * MIN_LOOP_TIME / avg; // 10% more (ns)

                                        if (REPORT_DEBUG_ESTIMATOR) {
                                            System.out.print("ratio time: " + round(df2, ratio) + "\t");
                                        }

                                        // adjust loopReps:
                                        newLoopReps = (int) Math.ceil(ratio * loopReps);

                                        if (newLoopReps < 0) {
                                            newLoopReps = Integer.MAX_VALUE; // overflow
                                        }
                                        loopReps = Math.min(100 * loopReps, newLoopReps); // avoid too big step
                                    }

                                    // adjust all reps proportionnally to square of the error scale
                                    ratio = statDist.rawErrorPercent() / ERR_DIST_TH;
                                    ratio *= 1.1 * ratio; // 10% more

                                    newTotReps = (long) Math.ceil(ratio * totReps);
                                    if (newTotReps < 0) {
                                        newTotReps = Integer.MAX_VALUE; // overflow
                                    }
                                    newTotReps = Math.min(5 * totReps, newTotReps);

                                    // check for long test duration:
                                    estTime = (long) Math.ceil(avg * newTotReps);

                                    statReps = Math.max(REP_MIN, (int) (newTotReps / loopReps));

                                    if (REPORT_DEBUG_ESTIMATOR) {
                                        out.println("! Loop " + l + " [tot: " + totReps + "] : " + round(df2, statDist.rawErrorPercent()) + " %"
                                                + " => try sr: " + statReps + " lr: " + loopReps
                                                + " tot: " + newTotReps + " estTime: " + round(df6, 1E-6 * estTime) + " ms");
                                    }

                                    if (estTime > maxEstTime) {
                                        System.err.println("Too long loop: " + round(df2, 1E-6 * estTime) + " ms for test ["
                                                + testHeader + " " + sorter + "] : " + round(df2, statDist.rawErrorPercent()) + " %");
                                        break;
                                    }

                                    // Restore Sorter stats to previous state:
                                    statSorter.copy(statSorterRef);

                                } // inner-loop (timing)

                                if (!sorter.skipCheck()) {
                                    check(test, proto);
                                }
                            } // distribution sampling

                            // Collect statistics:
                            times[i] = 1E-6 * ((USE_RMS) ? statSorter.rms() : statSorter.min());

                            if (times[i] > timeBL) {
                                times[i] -= timeBL;
                                noBL = false;
                            } else {
                                noBL = true;
                            }

                            if (!warmup) {
                                if (i == IDX_BASELINE) {
                                    timeBL = times[i];
                                }

                                if (REPORT_VERBOSE) {
                                    out.print(testHeader);
                                    out.print('\t');
                                    out.print(sorter);
                                }
                                out.print('\t');

                                if (SHOW_DIST_FLAGS) {
                                    out.print("[");

                                    // Check distribution statistics (all flags):
                                    for (d = 0; d < repDist; d++) {
                                        if (statDists[d].errorPercent() > ERR_DIST_TH) {
                                            out.print('!');
                                        } else {
                                            out.print(' ');
                                        }
                                    }
                                    out.print("]\t");
                                } else {
                                    // Check distribution statistics (1 flag only):
                                    for (d = 0; d < repDist; d++) {
                                        if (statDists[d].errorPercent() > ERR_DIST_TH) {
                                            out.print('$');
                                            break;
                                        }
                                    }
                                }

                                if (statSorter.errorPercent() >= ERR_WARNING) {
                                    out.print('!');
                                } else {
                                    out.print(' ');
                                }
                                if (noBL) {
                                    out.print('#');
                                }
                                if (REPORT_VERBOSE) {
                                    out.print(round(df6, 1E-6 * statSorter.rms()));
                                    out.print('\t');
                                    out.print(round(df6, 1E-6 * statSorter.mean()));
                                    out.print('\t');
                                    out.print(round(df6, 1E-6 * statSorter.stddev()));
                                    out.print('\t');
                                    out.print(statSorter.nSamples());
                                    out.print('\t');
                                    out.print(round(df6, 1E-6 * statSorter.min()));
                                    out.print('\t');
                                    out.print(round(df6, 1E-6 * statSorter.max()));
                                    out.println();
                                } else {
                                    out.print(round(df6, times[i])); // ms

                                    if (REPORT_TIME_ERR) {
                                        out.print(" ~");
                                        out.print(statSorter.errorPercent()); // %
                                        out.print('%');
                                    }
                                }
                            }

                        } // sorter loop

                        if (!warmup && !REPORT_VERBOSE) {
                            ratio = times[IDX_REF] / times[IDX_TEST];
                            if (!warmup) {
                                out.print('\t');
                                out.print(round(df2, 100.0 * ratio));
                            }
                            out.print(" % ");
                            String mark = "";

                            if (1.00 <= ratio && ratio < 1.05) {
                                mark = "..";
                            } else if (1.05 <= ratio && ratio < 1.10) {
                                mark = ".!.";
                            } else if (1.10 <= ratio && ratio < 1.20) {
                                mark = ".!!.";
                            } else if (1.20 <= ratio && ratio < 1.60) {
                                mark = ".!!!.";
                            } else if (1.60 <= ratio && ratio < 2.00) {
                                mark = ".!!!!.";
                            } else if (2.00 <= ratio) {
                                mark = ".!!!!!.";
                            }
                            out.println(mark);
                        }

                        numTest++;
                    } // builder
                } // tweaker
            } // sub-size
        } // size
        return loopCount / sorters.length;
    }

    private static void check(int[] a1, int[] ref) {
        for (int i = 0; i < a1.length - 1; i++) {
            if (a1[i] > a1[i + 1]) {
                throw new RuntimeException("!!! Array is not sorted at: " + i);
            }
        }
        int plusCheckSum1 = 0;
        int plusCheckSum2 = 0;
        int xorCheckSum1 = 0;
        int xorCheckSum2 = 0;

        for (int i = 0; i < a1.length; i++) {
            plusCheckSum1 += a1[i];
            plusCheckSum2 += ref[i];
            xorCheckSum1 ^= a1[i];
            xorCheckSum2 ^= ref[i];
        }
        if (plusCheckSum1 != plusCheckSum2) {
            throw new RuntimeException("!!! Array is not sorted correctly [+].");
        }
        if (xorCheckSum1 != xorCheckSum2) {
            throw new RuntimeException("!!! Array is not sorted correctly [^].");
        }
    }

    private static String round(DecimalFormat df, double value) {
        if (Double.isNaN(value)) {
            return "NaN";
        }
        String s = df.format(value);
        for (int i = s.length(); i < 10; ++i) {
            s = " " + s;
        }
        return s;
    }

    /**
     * Cleanup (GC + pause)
     */
    static void cleanup() {
//        final long freeBefore = Runtime.getRuntime().freeMemory();
        // Perform GC:
        System.runFinalization();
        System.gc();

        // pause for 100 ms :
        try {
            Thread.sleep(GC_LATENCY);
        } catch (InterruptedException ie) {
            System.err.println("thread interrupted");
        }
        /*        
        final long freeAfter = Runtime.getRuntime().freeMemory();
        System.out.println(String.format("cleanup (explicit Full GC): %,d / %,d bytes free.", freeBefore, freeAfter));
         */
    }
}
