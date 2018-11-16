package edu.sorting.perf;

import edu.sorting.ArrayUtils;
import java.text.DecimalFormat;
import java.util.Locale;
import wildinter.net.WelfordVariance;

/**
 * @author Jon Bentley
 */
public final class BentleyBasher {

    private final static boolean USE_RMS = true; // false means use MIN(TIME)

    private final static int TWEAK_INC = 4; // 2 originally

    private final static boolean DO_WARMUP = true;
    private final static int WARMUP_REPS = 40;
    private final static int[] WARMUP_LENGTHS = new int[]{501, 10001};

    // 10ms >> 3 x [Full GC (System.gc())  794K->794K(1013632K), 0,0022879 secs]
    private final static long GC_LATENCY = 10l;

    // TODO: use arguments for selected sorters, (sorter reference), warmup, sizes ... at least
    private final static int IDX_REF = IntSorter.DPQ_11.ordinal();
    private final static int IDX_TEST = IntSorter.DPQ_18_11.ordinal();

    public final static long MIN_NS = 50; // latency in ns
    public final static double MIN_PREC = 1e-9 * MIN_NS / 1e3; // ms

    private static final int HUGE_N = 10 * 1000 * 1001;

    private static final int[] LENGTHS = {100, 1000, 10000, 100000 /*, HUGE_N */};
//    private static final int[] lengths = {50 * 1000};

    // threshold to do more iterations (inner-loop) for small arrays in order to reduce variance:
    private static final int SMALL_TH = 10000;
    
    private static final int REP_REPEAT = 5;
    private static final int REP_SKIP = 10;

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

        if (!warmup) {
            System.out.print("                           ");

            for (IntSorter sorter : sorters) {
                System.out.print("         " + sorter);
            }
            System.out.print("        [" + sorters[IDX_REF] + " % " + sorters[IDX_TEST] + "]");
            System.out.println();
            System.out.println();
        }

        long start;
        long time;
        long minTimeTh;

        final int[] realLengths = (forceLengths != null) ? forceLengths : LENGTHS;

        final WelfordVariance samples = new WelfordVariance();
        int loopCount = 0;

        for (int n : realLengths) {
            int reps = reps(n);
            if (warmup) {
                if (reps > maxReps) {
                    reps = maxReps;
                }
                System.out.println("warmup length: " + n + " reps: " + reps);
            } else {
                reps *= REP_REPEAT;
            }
            if (reps < 3) {
                reps = 3;
            }
//            System.out.print("reps: " + reps + "\n");
            final int lreps = 1 + ((n <= SMALL_TH) ? (int) Math.ceil(3.0 * SMALL_TH / n) : 0);
            if (lreps > 1) {
                reps = Math.max(1, reps / lreps);
//                System.out.println("lreps: " + lreps + " with reps: " + reps + "\n");

                minTimeTh = MIN_NS * lreps;
            } else {
//                System.out.println("reps: " + reps + "\n");
                minTimeTh = MIN_NS;
            }
            
            final int effReps = reps + REP_SKIP;
//                System.out.println("effReps: " + effReps + "\n");

            // Allocate working array:
            final int[] input = new int[n];
            final int[] proto = new int[n];
            final int[] test = new int[n];

            for (int m = 1, end = 2 * n; m < end; m *= TWEAK_INC) {
                for (IntArrayTweaker iat : IntArrayTweaker.values()) {
                    for (ParamIntArrayBuilder iab : ParamIntArrayBuilder.values()) {
                        // TODO: sample 10x times the distribution
                        iab.build(input, m);

                        ParamIntArrayBuilder.reset();
                        iat.tweak(input, proto);

                        // TODO: IntArrayTweaker.SORT is independent of the ParamIntArrayBuilder
                        // => do not run multiple times in this case (useless) !
                        if (!warmup) {
                            System.out.print(n + " " + m + "  " + iab + " " + iat + "  ");
                        }

                        for (int i = 0; i < sorters.length; i++) {
                            final IntSorter sorter = sorters[i];
                            samples.reset();

                            if (!warmup) {
                                cleanup();
                            }

                            for (int k = 0; k < effReps; k++) {
                                // reduce variance on very small arrays (use more repeats):
                                time = 0l;

                                for (int q = 0; q < lreps; q++) {
                                    ArrayUtils.clone(proto, test);
                                    start = System.nanoTime();
                                    sorter.sort(test);
                                    time += System.nanoTime() - start;
                                    loopCount++;
                                }
                                if ((k > REP_SKIP) && (time > minTimeTh)) {
                                    samples.add(((double) time) / lreps);
                                }
                            }
                            check(test, proto);

                            times[i] = 1E-6 * ((USE_RMS) ? samples.rms() : samples.min());

                            if (!warmup) {
                                System.out.print('\t');
                                if (samples.errorPercent() >= 10) {
                                    System.out.print('!');
                                } else {
                                    System.out.print(' ');
                                }
                                System.out.print(round(df6, times[i])); // ms
                            }
                            // TODO detailed report (all stats)
//                            System.out.println("\t" + samples.toString());
                        }
                        if (!warmup) {
                            final double ratio = times[IDX_REF] / times[IDX_TEST];
                            if (!warmup) {
                                System.out.print('\t');
                                System.out.print(round(df2, 100.0 * ratio));
                            }
                            System.out.print(" % ");
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
                            System.out.println(mark);
                        }
                    }
                }
            }
        }
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
            s += " ";
        }
        /*        
        String s = "" + (((long) Math.round(value * 1000000.0)) / 1000000.0);
        int k = s.length() - s.indexOf(".");

        for (int i = k; i <= 6; ++i) {
            s = s + "0";
        }
         */
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
        System.gc();
        System.gc();

        // pause for 100 ms :
        try {
            Thread.sleep(GC_LATENCY);
        } catch (InterruptedException ie) {
            System.out.println("thread interrupted");
        }
        /*        
        final long freeAfter = Runtime.getRuntime().freeMemory();
        System.out.println(String.format("cleanup (explicit Full GC): %,d / %,d bytes free.", freeBefore, freeAfter));
         */
    }
}
