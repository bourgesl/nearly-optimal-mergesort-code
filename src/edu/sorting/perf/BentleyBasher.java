package edu.sorting.perf;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * @author Jon Bentley
 */
public final class BentleyBasher {

    private final static int IDX_REF = IntSorter.DPQ_11.ordinal();
    private final static int IDX_TEST = IntSorter.DPQ_18_11.ordinal();

    public final static long MIN_NS = 50; // latency in ns
    public final static double MIN_PREC = 1e-9 * MIN_NS / 1e3; // ms

    private static final int WARMUP_N = 10 * 1001;
    private static final int HUGE_N = 10 * 1000 * 1001;

    private static final int[] LENGTHS = {500, 1000, 2000, 5000, 10000, 50000, 100000, HUGE_N};
//    private static final int[] lengths = {50 * 1000};

    private final static DecimalFormat df;

    static {
        // Set the default locale to en-US locale (for Numerical Fields "." ",")
        Locale.setDefault(Locale.US);
        df = new DecimalFormat("0.000000");
    }

    public static void main(String[] args) {
        if (true) {
            // warmUp();
            System.out.println("Start warm up ...");
            final int warmupCount = sort(25, WARMUP_N);
            System.out.println("  End warm up (" + warmupCount + " iterations per sort).\n");
        }

        System.out.println("\nStarting benchmarks ...");
        sort(0, 0);
        System.out.println("\ndone.");
    }

    private static int reps(int n) {
        return (int) (12000000 / (n * Math.log10(n)));
    }

    private static int sort(final int maxReps, final int len) {
        System.out.print("                           ");

        final IntSorter[] sorters = IntSorter.values();
        final double[] times = new double[sorters.length];

        for (IntSorter sorter : sorters) {
            System.out.print("        " + sorter);
        }
        System.out.print("        [" + sorters[IDX_REF] + " % " + sorters[IDX_TEST] + "]");
        System.out.println();
        System.out.println();

        long start;
        long time;
        long minTime;
        long minTimeTh;

        final int[] realLengths = (len > 0) ? new int[]{len} : LENGTHS;

        final boolean warmup = (maxReps > 0);
        int loopCount = 0;

        for (int n : realLengths) {
            int reps = reps(n);
            if (warmup) {
                if (reps > maxReps) {
                    reps = maxReps;
                }
                System.out.println("warmup length: " + n + " reps: " + reps);
            }
            if (reps < 3) {
                reps = 3;
            }
//            System.out.print("reps: " + reps + "\n");
            final int lreps = 1 + ((n <= 1000) ? (int) Math.ceil(1.0 * 1000 / n) : 0);
            if (lreps > 1) {
                reps /= lreps;
//                System.out.println("lreps: " + lreps + " with reps: " + reps + "\n");

                minTimeTh = MIN_NS * lreps;
            } else {
//                System.out.println("reps: " + reps + "\n");
                minTimeTh = MIN_NS;
            }

            for (int m = 1; m < 2 * n; m *= 2) {
                for (ParamIntArrayBuilder iab : ParamIntArrayBuilder.values()) {
                    final int[] proto1 = iab.build(n, m);

                    for (IntArrayTweaker iat : IntArrayTweaker.values()) {
                        final int[] proto2 = iat.tweak(proto1);
                        if (!warmup) {
                            System.out.print(n + " " + m + "  " + iab + " " + iat + "  ");
                        }

                        for (int i = 0; i < sorters.length; i++) {
                            final IntSorter sorter = sorters[i];
                            minTime = Long.MAX_VALUE;
                            int[] test = null;

                            if (!warmup) {
                                cleanup();
                            }

                            for (int k = 0; k < reps; k++) {
                                // reduce variance on very small arrays (use more repeats):
                                time = 0l;
                                for (int q = 0; q < lreps; q++) {
                                    test = proto2.clone();
                                    start = System.nanoTime();
                                    sorter.sort(test);
                                    time += System.nanoTime() - start;
                                    loopCount++;
                                }
                                if (time > minTimeTh) {
                                    minTime = Math.min(minTime, time);
                                }
                            }
                            check(test, proto2);
//                            System.out.print("\t" + minTime);

                            times[i] = (minTime != Long.MAX_VALUE)
                                    ? (((double) minTime) / lreps) : Double.NaN;
                            if (!warmup) {
                                System.out.print("\t" + round(1E-6 * times[i])); // ms
                            }
                        }
                        if (!warmup) {
                            final double ratio = times[IDX_REF] / times[IDX_TEST];
                            if (!warmup) {
                                System.out.printf("\t  " + round(ratio));
                            }
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
                            if (mark.length() == 0) {
                                System.out.println();
                            } else {
                                System.out.println(" " + mark);
                            }
                        }
                    }
                }
            }
        }
        return loopCount / sorters.length;
    }

    private static void warmUp() {
        System.out.println("start warm up");
        int MAX_M = Math.min(2 * WARMUP_N, 8 + 1);

        for (int m = 1; m < MAX_M; m *= 2) {
            for (ParamIntArrayBuilder iab : ParamIntArrayBuilder.values()) {
                int[] proto1 = iab.build(WARMUP_N, m);

                for (IntArrayTweaker iat : IntArrayTweaker.values()) {
                    int[] proto2 = iat.tweak(proto1);

                    for (IntSorter sorter : IntSorter.values()) {
                        sorter.sort(proto2.clone());
                    }
                }
            }
        }
        System.out.println("  end warm up");
        System.out.println();
    }

    private static void check(int[] a1, int[] a2) {
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
            plusCheckSum2 += a2[i];
            xorCheckSum1 ^= a1[i];
            xorCheckSum2 ^= a2[i];
        }
        if (plusCheckSum1 != plusCheckSum2) {
            throw new RuntimeException("!!! Array is not sorted correctly [+].");
        }
        if (xorCheckSum1 != xorCheckSum2) {
            throw new RuntimeException("!!! Array is not sorted correctly [^].");
        }
    }

    private static String round(double value) {
        if (Double.isNaN(value)) {
            return "NaN";
        }
        return df.format(value);
        /*        
        String s = "" + (((long) Math.round(value * 1000000.0)) / 1000000.0);
        int k = s.length() - s.indexOf(".");

        for (int i = k; i <= 6; ++i) {
            s = s + "0";
        }
        for (int i = s.length(); i < 10; ++i) {
            s = " " + s;
        }
        return s;
         */
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
            Thread.sleep(100l);
        } catch (InterruptedException ie) {
            System.out.println("thread interrupted");
        }
        /*        
        final long freeAfter = Runtime.getRuntime().freeMemory();
        System.out.println(String.format("cleanup (explicit Full GC): %,d / %,d bytes free.", freeBefore, freeAfter));
         */
    }
}
