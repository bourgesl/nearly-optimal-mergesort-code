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

    // general settings
    public static final long SEC_IN_NS = 1000 * 1000 * 1000; // 1s in ns;

    // Small array lengths:
    private static final int[] LENGTHS = {50, 100, 200, 500, 1000, 2000, 5000, 10000};
    // Large array lengths:
//    private static final int[] LENGTHS = {50 * 1000, 100 * 1000, 500 * 1000};
    // Very large array lengths:
//    private static final int[] LENGTHS = {1000 * 1000, 10 * 1000 * 1000};

    private static final long MAX_LOOP_TIME = 3 * SEC_IN_NS; // 3s max per test (small)
//    private static final long MAX_LOOP_TIME = 10 * SEC_IN_NS; // 30s max per test (large)    

    private static final double ERR_DIST_TH = 2.0; // 2% max per timing loop
    private static final double CONFIDENCE_AVG = 4.0; // 4 sigma confidence on mean estimation

    // true means MEAN(time) + STDDEV, false means use MIN(TIME) + STDDEV
    private final static boolean USE_RMS = false;

    private final static boolean DO_WARMUP = true;

    private final static boolean REPORT_VERBOSE = true;
    private final static boolean REPORT_DEBUG_ESTIMATOR = REPORT_VERBOSE && false;

    private final static boolean REPORT_TIME_ERR = false;
    private final static boolean SHOW_DIST_FLAGS = false;

    // internal settings
    private final static int TWEAK_INC = 4; // 2 originally

    private static final int WARMUP_REP_DISTRIB = 2;
    private final static int WARMUP_REPS = 30 * WARMUP_REP_DISTRIB;

    private final static int[] WARMUP_LENGTHS = new int[]{501, 10001};

    // 10ms >> [Full GC (System.gc())  794K->794K(1013632K), 0,0022879 secs] for 1g heap
    private final static long GC_LATENCY = 10l;

    // TODO: use arguments for selected sorters, (sorter reference), warmup, sizes ... at least
    public final static int IDX_BASELINE = IntSorter.BASELINE.ordinal();
    public final static int IDX_REF = 1; // IntSorter.DPQ_18_11.ordinal();
    public final static int IDX_TEST = 2; // IntSorter.DPQ_18_11P.ordinal();

    private final static long MIN_NS = 50; // latency in ns

    // to gather enough statistics
    private static final int REP_MIN = 90;
    private static final int LREP_MIN = 9;
    private static final int REP_DISTRIB = 10;
    private static final int REP_SKIP = 10;
    private static final int ADJ_MAX = 10;

    private static final long MIN_LOOP_TIME = SEC_IN_NS / 1000; // 1ms

    private static final int ERR_WARNING = 25; // 25% warning on all distributions

    public final static String HEADER_COLUMNS = "COLUMNS:";
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
            System.out.println("Timings are given in milli-seconds (min time + 1 stddev)");
        }
        System.out.println("\nTimings are estimated using auto-tune to satisfy the maximal uncertainty of " + round(df2, ERR_DIST_TH) + " % "
                + ", convergence at " + round(df2, CONFIDENCE_AVG) + " sigma with max test duration = " + round(df2, 1E-6 * MAX_LOOP_TIME) + " ms.");

        // TODO: estimate uncertainty on ratios: (100 +/- err) / (100 +- err) non linear
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
            out.print(HEADER_COLUMNS + "Length Sub-size  Builder Tweaker");

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
        final int repDist = (warmup) ? WARMUP_REP_DISTRIB : REP_DISTRIB;

        final WelfordVariance[] statDists = new WelfordVariance[repDist];
        for (int d = 0; d < repDist; d++) {
            statDists[d] = new WelfordVariance();
        }

        // Allocate many working arrays to circumvent any alignment issue (int[] are aligned to 8/16/24/32):
        final int[][] inputs = new int[repDist][];
        final int[][] protos = new int[repDist][];
        final int[][] tests = new int[repDist][];

        int loopCount = 0;
        int numTest = 0;

        boolean noBL;
        double timeBL;
        double ratio, avg, prevAvg, confidence;
        long totReps, newTotReps;
        int total = 0;
        String testHeader = "";

        for (int n : realLengths) {
            int reps = reps(n);
            if (warmup) {
                if (reps > maxReps) {
                    reps = maxReps;
                }
                out.println("Warmup length: " + n + " reps: " + reps);
            }
//            out.print("reps: " + reps + "\n");

            int lreps = LREP_MIN;
            if (lreps > 1) {
                reps = Math.max(1, reps / lreps);
            }
//            out.println("lreps: " + lreps + " with reps: " + reps + "\n");

            // adjust reps to sample properly distributions
            if (warmup) {
                reps = Math.max(1, reps / repDist);
            } else {
                if (reps < REP_MIN) {
                    lreps = Math.max(1, reps * lreps / REP_MIN);
                    reps = REP_MIN;
                }
            }
            out.println("lreps: " + lreps + " with reps: " + reps + "\n");

            final int skipReps = (warmup) ? 0 : REP_SKIP;
            final int maxAdjSteps = (warmup) ? 0 : ADJ_MAX;

            // Allocate many working arrays to circumvent any alignment issue (int[] are aligned to 8/16/24/32):
            for (int d = 0; d < repDist; d++) {
                inputs[d] = new int[n];
                protos[d] = new int[n];
                tests[d] = new int[n];
            }

            // adjust tweak increment depending on array size
            final int tweakInc = (n > 100000) ? 16 : TWEAK_INC;

            testHeader = "";

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

                            // adjust max estimated time per distribution except for baseline and first 2 tests:
                            maxEstTime = (((i == IDX_BASELINE) || (numTest < 2)) ? 3 : 1) * (MAX_LOOP_TIME / repDist);

                            // previous average should be applicable among distributions (for same test):
                            prevAvg = 0.0;
                            confidence = 0.0;

                            // Set loops at initial conditions:
                            loopReps = lreps;
                            statReps = reps;

                            // sample 10x times the distribution
                            for (d = 0; d < repDist; d++) {
                                // Use many working arrays (1 per distribution):
                                final int[] input = inputs[d];
                                final int[] proto = protos[d];
                                final int[] test = tests[d];

                                // Get new distribution sample:
                                iab.build(input, m);

                                // tweak sample:
                                iat.tweak(input, proto);

                                // Backup Sorter stats:
                                statSorterRef.copy(statSorter);

                                for (l = 0;;) {
                                    statDist = statDists[d];
                                    statDist.reset();

                                    time = 0l;
                                    total = 0;

                                    for (e = 0; e < statReps + skipReps; e++) {
                                        // reduce variance on very small arrays (use more repeats):

                                        // measurement loop:
                                        start = System.nanoTime();

                                        for (r = loopReps; r >= 0; r--) {
                                            ArrayUtils.clone(proto, test);
                                            sorter.sort(test);
                                            // always consume test array
                                            total += test[0];
                                        } // hot timing loop

                                        time = System.nanoTime() - start;
                                        loopCount += loopReps;

                                        if (time > MIN_NS) {
                                            final double measure = ((double) time) / loopReps;

                                            statDist.add(measure);

                                            if (e >= skipReps) {
                                                // TODO: use stddev(min) ?
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
                                        out.print(">> ");
                                        out.print(testHeader);
                                        out.print('\t');
                                        out.print(sorter);
                                        out.print('\t');
                                        out.print(round(df6, 1E-6 * statDist.rms())); // ms
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

                                    // test mean time convergence ie loop stability on statDist.mean()
                                    avg = statDist.mean();

                                    if ((prevAvg != 0.0) && (statDist.rawErrorPercent() <= ERR_DIST_TH)) {
                                        confidence = Math.abs(prevAvg - avg) / statDist.stddev();

                                        // confidence at 3 sigma:
                                        if (confidence <= CONFIDENCE_AVG) {
                                            if (REPORT_DEBUG_ESTIMATOR) {
                                                out.println("Loop " + l + ": " + round(df2, statDist.rawErrorPercent()) + " % "
                                                        + "Confidence: " + round(df2, confidence) + " σ (OK)");
                                            }
                                            break;
                                        }
                                    }

                                    if (++l > maxAdjSteps) {
                                        System.err.println("Too many loop adjustments for test ["
                                                + testHeader + " " + sorter + "] : " + round(df2, statDist.rawErrorPercent()) + " % "
                                                + "Confidence: " + round(df2, confidence) + " σ");
                                        break;
                                    }

                                    totReps = ((long) statReps) * loopReps; // may overflow ?

                                    if (time < MIN_LOOP_TIME) {
                                        ratio = 1.1 * MIN_LOOP_TIME / avg; // 10% more (ns)

                                        if (REPORT_DEBUG_ESTIMATOR) {
                                            System.out.print("ratio time: " + round(df2, ratio) + " ");
                                        }

                                        // adjust loopReps:
                                        newLoopReps = (int) Math.ceil(ratio);

                                        // avoid too big step
                                        loopReps = Math.max(1, Math.min(newLoopReps, 1000 * loopReps));

                                        if (REPORT_DEBUG_ESTIMATOR) {
                                            System.out.print("loopReps: " + loopReps + " ");
                                        }
                                    }

                                    // adjust all reps proportionnally to square of the error scale
                                    ratio = statDist.rawErrorPercent() / ERR_DIST_TH;
                                    ratio *= 1.1 * ratio; // 10% more

                                    newTotReps = (long) Math.ceil(ratio * totReps);
                                    if (newTotReps < 0) {
                                        newTotReps = Integer.MAX_VALUE; // overflow
                                    }
                                    // avoid too big or too small step
                                    newTotReps = Math.min(newTotReps, (ratio > 1.0) ? 10 * totReps : totReps / 10);

                                    // may exceed time limit
// avoid / 0 (KILL ?)
                                    if (loopReps == 0) {
                                        loopReps = lreps;
                                        System.err.print("! loopReps = 0 : Loop " + l + " [tot: " + totReps + "] : " + round(df2, statDist.rawErrorPercent())
                                                + " % => try sr: " + statReps + " lr: " + loopReps
                                                + " tot: " + newTotReps);
                                    }
                                    statReps = Math.max(REP_MIN, (int) (newTotReps / loopReps));

                                    // check for long test duration:
                                    estTime = (long) Math.ceil(avg * statReps * loopReps);

                                    if (REPORT_DEBUG_ESTIMATOR) {
                                        out.print("! Loop " + l + " [tot: " + totReps + "] : " + round(df2, statDist.rawErrorPercent()) + " %"
                                                + " => try sr: " + statReps + " lr: " + loopReps
                                                + " tot: " + newTotReps + " estTime: " + round(df6, 1E-6 * estTime) + " ms");
                                    }

                                    if (estTime > maxEstTime) {
                                        // adjust reps to time limit:
                                        newTotReps = (long) Math.ceil(maxEstTime / avg);

                                        if (statReps == REP_MIN) {
                                            loopReps = Math.max(1, (int) (newTotReps / REP_MIN));
                                        } else {
                                            statReps = Math.max(REP_MIN, (int) (newTotReps / loopReps));
                                        }
                                        estTime = (long) Math.ceil(avg * statReps * loopReps);

                                        if (REPORT_DEBUG_ESTIMATOR) {
                                            out.print(" !! Time-Limit => try sr: " + statReps + " lr: " + loopReps
                                                    + " tot: " + newTotReps + " estTime: " + round(df6, 1E-6 * estTime) + " ms");
                                        }
                                    }
                                    if (REPORT_DEBUG_ESTIMATOR) {
                                        out.println();
                                    }

                                    // Restore Sorter stats to previous state:
                                    statSorter.copy(statSorterRef);

                                    // cleanup again
                                    if (!warmup) {
                                        cleanup();
                                    }

                                    prevAvg = avg;

                                } // inner-loop (timing)

                                if (!sorter.skipCheck()) {
                                    check(sorter, test, proto);
                                }
                            } // distribution sampling

                            // Collect statistics:
                            times[i] = 1E-6 * ((USE_RMS) ? statSorter.rms() : statSorter.min() + statSorter.stddev());

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
                                        if (statDists[d].rawErrorPercent() > ERR_DIST_TH) {
                                            out.print('!');
                                        } else {
                                            out.print(' ');
                                        }
                                    }
                                    out.print("]\t");
                                } else {
                                    // Check distribution statistics (1 flag only):
                                    for (d = 0; d < repDist; d++) {
                                        if (statDists[d].rawErrorPercent() > ERR_DIST_TH) {
                                            out.print('$');
                                            break;
                                        }
                                    }
                                }

                                if (statSorter.rawErrorPercent() >= ERR_WARNING) {
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
                                        out.print(round(df2, statSorter.rawErrorPercent())); // %
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

        out.println("total: " + total);

        return loopCount / sorters.length;
    }

    public static void check(IntSorter sorter, int[] a1, int[] ref) {
        for (int i = 0; i < a1.length - 1; i++) {
            if (a1[i] > a1[i + 1]) {
                throw new RuntimeException(sorter.name() + " !!! Array is not sorted at: " + i);
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
            throw new RuntimeException(sorter.name() + " !!! Array is not sorted correctly [+].");
        }
        if (xorCheckSum1 != xorCheckSum2) {
            throw new RuntimeException(sorter.name() + " !!! Array is not sorted correctly [^].");
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
    public static void cleanup() {
//        final long freeBefore = Runtime.getRuntime().freeMemory();
        // Perform GC:
        System.runFinalization();
        System.gc();

        // pause for few ms :
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
