package edu.sorting.perf;

import java.util.Locale;

/**
 * @author Jon Bentley
 */
public final class BentleyBasher {

    private static final int MAX_N = 1000001;

    private static final int[] lengths = {10, 100, 1000, 10000, MAX_N};

    private static int reps(int n) {
        return (int) (12000000 / (n * Math.log10(n)));
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        
        warmUp();
        sort();
    }

    private static void sort() {
        System.out.print("                           ");

        final IntSorter[] sorters = IntSorter.values();
        final double[] times = new double[sorters.length];

        for (IntSorter sorter : sorters) {
            System.out.print("         " + sorter);
        }
        System.out.println();
        System.out.println();

        long startTime;
        long endTime;
        long minTime;

        for (int n : lengths) {
            int reps = reps(n);
            if (reps < 3) {
                reps = 3;
            }
//            System.out.print("reps: " + reps + "\n");

            for (int m = 1; m < 2 * n; m *= 2) {
                for (ParamIntArrayBuilder iab : ParamIntArrayBuilder.values()) {
                    int[] proto1 = iab.build(n, m);

                    for (IntArrayTweaker iat : IntArrayTweaker.values()) {
                        int[] proto2 = iat.tweak(proto1);
                        System.out.print(n + " " + m + "  " + iab + " " + iat + "  ");

                        for (int i = 0; i < sorters.length; i++) {
                            final IntSorter sorter = sorters[i];
                            minTime = Long.MAX_VALUE;
                            int[] test = null;

                            for (int k = 0; k < reps; k++) {
                                test = proto2.clone();
                                startTime = System.nanoTime();
                                sorter.sort(test);
                                endTime = System.nanoTime();
                                minTime = Math.min(minTime, endTime - startTime);
                            }
                            check(test, proto2);
                            System.out.print("\t" + round(minTime / 1000000.0));
                            times[i] = minTime;
                        }
                        double ratio = times[0] / times[1];
                        System.out.printf("\t  " + round(ratio));
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
                        System.out.println(" " + mark);
                    }
                }
            }
        }
    }

    private static void warmUp() {
        System.out.println("start warm up");
        int MAX_M = Math.min(2 * MAX_N, 8 + 1);

        for (int m = 1; m < MAX_M; m *= 2) {
            for (ParamIntArrayBuilder iab : ParamIntArrayBuilder.values()) {
                int[] proto1 = iab.build(MAX_N, m);

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
        String s = "" + (((long) Math.round(value * 1000000.0)) / 1000000.0);
        int k = s.length() - s.indexOf(".");

        for (int i = k; i <= 6; ++i) {
            s = s + "0";
        }
        for (int i = s.length(); i < 10; ++i) {
            s = " " + s;
        }
        return s;
    }
}
