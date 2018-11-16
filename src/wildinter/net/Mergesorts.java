package wildinter.net;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort201802;
import edu.sorting.DualPivotQuicksort201802Ext;
import edu.sorting.DualPivotQuicksort201811;
import wildinter.net.mergesort.BottomUpMergesort;
import wildinter.net.mergesort.Inputs;
import wildinter.net.mergesort.MergesAndRuns;
import edu.sorting.RadixSort;
import wildinter.net.mergesort.Sorter;
import wildinter.net.mergesort.TopDownMergesort;
import wildinter.net.mergesort.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.marlin.MarlinMergeSort;
import org.marlin.MarlinSort;
import wildinter.net.mergesort.PeekSort;

import static wildinter.net.mergesort.Util.shuffle;

/**
 * Main class for running time experiments.
 */
public class Mergesorts {

    public static boolean ABORT_IF_RESULT_IS_NOT_SORTED = true;
    public static final boolean TIME_ALL_RUNS_IN_ONE_MEASUREMENT = false;

    public static void main(String[] args) throws IOException {
        // Set the default locale to en-US locale (for Numerical Fields "." ",")
        Locale.setDefault(Locale.US);

        if (args.length == 0) {
            System.out.println("Usage: Mergesorts [reps] [n1,n2,n3] [seed] [inputs] [outfile]");
        }

        final ArrayList<Sorter> algos = new ArrayList<Sorter>();

//        algos.add(new PowerSort(true, false, 24));
        algos.add(new PeekSort(24, false));

//   	  algos.add(new PowerSort(true, false, 2));
//   	  algos.add(new PeekSort(2, false));
        algos.add(new TopDownMergesort(24, true));
//   	  algos.add(new TopDownMergesort(2, true));

        algos.add(new BottomUpMergesort(24, true));
//   	  algos.add(new BottomUpMergesort(2, true));

//   	  algos.add(TimsortTrot.INSTANCE);
//   	  algos.add(TimsortStrippedDown.INSTANCE);
//   	  algos.add(Timsort.INSTANCE);
        algos.add(MarlinSort.INSTANCE);
        algos.add(MarlinMergeSort.INSTANCE);
//        algos.add(DynPivotSort.INSTANCE);
//        algos.add(Qsorte.INSTANCE);

        algos.add(DualPivotQuickSort2011.INSTANCE);
        algos.add(DualPivotQuicksort201802.INSTANCE);
        algos.add(DualPivotQuicksort201811.INSTANCE);
        algos.add(DualPivotQuicksort201802Ext.INSTANCE);
//        algos.add(Sorter.SYSTEMSORT);

        algos.add(RadixSort.INSTANCE);

//	    algos.add(new Nop());
//	    algos.add(new Shuffle());
        int reps = 100;

        if (args.length >= 1) {
            reps = Integer.parseInt(args[0]);
        }

        List<Integer> sizes = Arrays.asList(1_000_000);
        if (args.length >= 2) {
            sizes = new LinkedList<>();
            for (final String size : args[1].split(",")) {
                sizes.add(Integer.parseInt(size.replaceAll("\\D", "")));
            }
        }

        long seed = 42424242;
        if (args.length >= 3) {
            seed = Long.parseLong(args[2]);
        }

        Inputs.InputGenerator inputs = Inputs.RANDOM_PERMUTATIONS_GENERATOR;
        if (args.length >= 4) {
            if (args[3].equalsIgnoreCase("rp")) {
                inputs = Inputs.RANDOM_PERMUTATIONS_GENERATOR;
            }
            if (args[3].startsWith("runs")) {
                inputs = Inputs.randomRunsGenerator(Integer.parseInt(
                        args[3].substring(4).replaceAll("\\D", "")));
            }
            if (args[3].startsWith("iid")) {
                inputs = Inputs.randomIidIntsGenerator(Integer.parseInt(args[3].substring(3)));
            }
            if (args[3].startsWith("timdrag")) {
                inputs = Inputs.timsortDragGenerator(Integer.parseInt(args[3].substring(7)));
            }
        }

        String outFileName = "mergesorts";
        if (args.length >= 5) {
            outFileName = args[4];
        }

        timeSorts(algos, reps, sizes, seed, inputs, outFileName);
    }

    public static void timeSorts(final List<Sorter> algos, final int reps, final List<Integer> sizes, final long seed, final Inputs.InputGenerator inputs, String outFileName) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("-yyyy-MM-dd_HH-mm-ss");
        outFileName += format.format(new Date());
        outFileName += "-reps" + reps;
        outFileName += "-ns";
        for (int n : sizes) {
            outFileName += "-" + n;
        }
        outFileName += "-seed" + seed;
        outFileName += ".csv";
        File outFile = new File(outFileName);

        System.out.println("algos  = " + algos);
        System.out.println("sizes  = " + sizes);
        System.out.println("reps   = " + reps);
        System.out.println("seed   = " + seed);
        System.out.println("inputs = " + inputs);
        System.out.println("Writing to " + outFile.getAbsolutePath());

        int maxSize = 0;
        for (final int size : sizes) {
            if (maxSize < size) {
                maxSize = size;
            }
        }

        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));

        if (MergesAndRuns.COUNT_MERGE_COSTS) {
            out.write("algo,ms,n,input,input-num,merge-cost\n");
            System.out.println("Also counting merge cost in MergeUtil.mergeRuns");
        } else {
            out.write("algo,ms,n,input,input-num\n");
            System.out.println("Not counting merge costs.");
        }

        Random random = new Random(seed);
        {
            // warm up
            final int warmupRounds = 10000; // hotspot method count
            final int[] warmupSizes = new int[]{10000, 1000, 1000};

            System.out.println("Doing warmup (" + warmupRounds + " rounds)");
            final int[][] Ainitial = new int[warmupSizes.length][];
            for (int i = 0; i < warmupSizes.length; i++) {
                final int size = warmupSizes[i];
                Ainitial[i] = inputs.next(size, random, null);
            }
            final int warmupStep = 1 + warmupRounds / 10;
            int total = 0;
            for (int r = 0; r < warmupRounds; ++r) {
                if (r % warmupStep == 0) {
                    System.out.println("Warmup [" + (r / warmupStep) + "0%] ...");
                }

                for (int i = 0; i < warmupSizes.length; i++) {
                    final int size = warmupSizes[i];
                    int[] A = Ainitial[i];

                    for (final Sorter algo : algos) {
                        A = inputs.next(size, random, A);
                        algo.sort(A, 0, size - 1);
                        total += A[A.length / 2];

                        if (false && !Util.isSorted(A)) {
                            System.err.println("RESULT NOT SORTED!");
                            System.exit(3);
                        }
                    }
                }
            }
            System.out.println("Warmup finished! (" + total + ")\n");
        }

        final int warmupMin = 10;

        System.out.println("\nRuns with individual timing (skips first " + warmupMin + " runs):");
        for (final int size : sizes) {
            final int freps = warmupMin + reps;
            final int lreps = 1 + ((size <= 10000) ? (int) Math.ceil(1.0 * maxSize / size) : 0);
            System.out.println("adjusted reps: " + freps + " + inner loop: " + lreps);

            for (final Sorter algo : algos) {
                final String algoName = algo.toString();
                random = new Random(seed);
                final WelfordVariance samples = new WelfordVariance();
                final WelfordVariance samplesCost = new WelfordVariance();

                int total = 0;
                int[] A = inputs.next(size, random, null);
                cleanup();

                for (int r = 0; r < freps; ++r) {
                    // reduce variance on very small arrays (use more repeats):
                    long time = 0l;
                    for (int q = 0; q < lreps; q++) {
                        A = inputs.next(size, random, A);
                        MergesAndRuns.totalMergeCosts = 0;
                        MergesAndRuns.totalMoveCosts = 0;
                        final long start = System.nanoTime();
                        algo.sort(A, 0, size - 1);
                        time += (System.nanoTime() - start);
                        total += A[A.length / 2];
                    }
                    if (ABORT_IF_RESULT_IS_NOT_SORTED && !Util.isSorted(A)) {
                        System.err.println("RESULT NOT SORTED!");
                        System.exit(3);
                    }
                    if (r >= warmupMin) {
                        final double msDiff = (time / 1e6) / lreps;
                        // Skip first iteration, often slower!
                        samples.add(msDiff);
                        if (MergesAndRuns.COUNT_MOVE_COSTS) {
                            out.write(algoName + "," + msDiff + "," + size + "," + inputs + "," + r + "," + MergesAndRuns.totalMoveCosts + "\n");
                            samplesCost.add(MergesAndRuns.totalMoveCosts);
                        } else if (MergesAndRuns.COUNT_MERGE_COSTS) {
                            out.write(algoName + "," + msDiff + "," + size + "," + inputs + "," + r + "," + MergesAndRuns.totalMergeCosts + "\n");
                        } else {
                            out.write(algoName + "," + msDiff + "," + size + "," + inputs + "," + r + "\n");
                        }
                        if (false) {
                            out.flush();
                        }
                    }
                }
                if (MergesAndRuns.COUNT_MOVE_COSTS) {
                    System.out.println("avg-ms=" + (float) (samples.mean()) + "(+/- " + samples.errorPercent() + " %),\t algo=" + algoName
                            + ", n=" + size + "     (" + total + ")\t" + samples + "\tcost: " + samplesCost);
                } else {
                    System.out.println("avg-ms=" + (float) (samples.mean()) + "(+/- " + samples.errorPercent() + " %),\t algo=" + algoName
                            + ", n=" + size + "     (" + total + ")\t" + samples);
                }
            }
        }
        out.write("#finished: " + format.format(new Date()) + "\n");
        out.close();

        if (TIME_ALL_RUNS_IN_ONE_MEASUREMENT) {
            System.out.println("\n\n\nRuns with overall timing (incl. input generation):");
            for (final Sorter algo : algos) {
                random = new Random(seed);
                final String algoName = algo.toString();
                for (final int size : sizes) {
                    int[] A = inputs.next(size, random, null);
                    cleanup();

                    final long startNanos = System.nanoTime();
                    int total = 0;
                    for (int r = 0; r < reps; ++r) {
                        if (r != 0) {
                            A = inputs.next(size, random, A);
                        }
                        algo.sort(A, 0, size - 1);
                        total += A[A.length / 2];
                        //					if (!Util.isSorted(A)) throw new AssertionError();
                    }
                    final long endNanos = System.nanoTime();
                    final float msDiff = (endNanos - startNanos) / 1e6f;
                    System.out.println("avg-ms=" + (msDiff / reps) + ",\t algo=" + algoName + ", n=" + size + "    (" + total + ")");
                }
            }
        }

    }

    public static final class Nop implements Sorter {

        @Override
        public void sort(final int[] A, final int left, final int right) {
            ABORT_IF_RESULT_IS_NOT_SORTED = false;
        }

        @Override
        public String toString() {
            return "nop";
        }
    }

    private static class Shuffle implements Sorter {

        @Override
        public void sort(final int[] A, final int left, final int right) {
            ABORT_IF_RESULT_IS_NOT_SORTED = false;
            shuffle(A, left, right, new Random());
        }

        @Override
        public String toString() {
            return "random-shuffle";
        }

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
