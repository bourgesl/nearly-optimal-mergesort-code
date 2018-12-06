package edu.sorting.bench;

import edu.sorting.ArrayUtils;
import edu.sorting.perf.BentleyBasher;
import edu.sorting.perf.IntArrayTweaker;
import edu.sorting.perf.IntSorter;
import edu.sorting.perf.ParamIntArrayBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.CommandLineOptionException;
import org.openjdk.jmh.runner.options.CommandLineOptions;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class ArraySortBenchmark2 {

    public final static String PARAM_SIZE = "arraySize";
    public final static String PARAM_SUB_SIZE = "arraySubSize";
    public final static String PARAM_DATA_TWEAKER = "dataTweaker";
    public final static String PARAM_DIST_BUILDER = "distBuilder";
    public final static String PARAM_SORTER = "tSorter";

    public static final int REP_DISTRIB = 10;

    private final static int TWEAK_INC = 4; // 2 originally

    private final static boolean DEBUG = false;

    private static final boolean TRACE = false;

    @State(Scope.Benchmark)
    public static class BenchmarkState {

        @Param({})
        int _distSamples;

        @Param({ /* "50", "100", "500" */
            "1000"
        /* "5000", "10000", "50000", "100000", "500000", "1000000" */
        })
        int arraySize;

        @Param({})
        int arraySubSize;

        /* IntArrayTweaker.values() */
        @Param({"IDENT_____", "REVERSE___", "REVERSE_FR", "REVERSE_BA", "SORT______", "DITHER____"})
        IntArrayTweaker dataTweaker;

        /* ParamIntArrayBuilder.values() */
        @Param({"STAGGER", "SAWTOTH", "_RANDOM", "PLATEAU", "SHUFFLE"})
        ParamIntArrayBuilder distBuilder;

        @Param({"BASELINE", "DPQ_11", "DPQ_18_11_21", "DPQ_18_11_27", "DPQ_18_11I", "RADIX", "MARLIN"})
        IntSorter tSorter;

        final int[][] inputs = new int[REP_DISTRIB][];
        final int[][] protos = new int[REP_DISTRIB][];
        final int[][] tests = new int[REP_DISTRIB][];

        @Setup(Level.Trial)
        public void setUpTrial() {
            if (TRACE) {
                System.out.println("\nsetUpTrial");
            }
            if (arraySubSize > (2 * arraySize)) {
                throw new IllegalStateException("Invalid arraySubSize: " + arraySubSize + " for arraySize: " + arraySize);
            }
            // Reset tweaker to have sample initial conditions (seed):
            ParamIntArrayBuilder.reset();

            // Allocate many working arrays to circumvent any alignment issue (int[] are aligned to 8/16/24/32):
            for (int d = 0; d < REP_DISTRIB; d++) {
                inputs[d] = new int[arraySize];
                protos[d] = new int[arraySize];
                tests[d] = new int[arraySize];

                // Get new distribution sample:
                distBuilder.build(inputs[d], arraySubSize);

                // tweak sample:
                dataTweaker.tweak(inputs[d], protos[d]);

                if (TRACE) {
                    System.out.println("\nsetUpTrial: protos[" + d + "] = \n"
                            + Arrays.toString(protos[d]));
                }
            }
            // Promote all the arrays (GC)
            BentleyBasher.cleanup();
        }
    }

    @State(Scope.Thread)
    public static class ThreadState {

        // current index in distributions
        int idx = 0;
        int distSamples = 0;
        // benchmark loop state:
        IntSorter sorter = null;
        int[] proto = null;
        int[] test = null;

        @Setup(Level.Iteration)
        public void setUpIteration(final BenchmarkState bs) {
            // Use many working arrays (1 per distribution):
            distSamples = bs._distSamples;
            sorter = bs.tSorter;
            proto = bs.protos[idx];
            test = bs.tests[idx];

            if (TRACE) {
                System.out.println("\nsetUpIteration: proto[" + idx + "] = \n"
                        + Arrays.toString(proto));
            }

            // go forward
            idx = (idx + 1) % distSamples;
        }

        /*
         * And, check the benchmark went fine afterwards:
         */
        @TearDown(Level.Iteration)
        public void check() {
            if (sorter != null && !sorter.skipCheck()) {
                for (int d = 0; d < distSamples; d++) {
                    // may throw runtime exception
                    BentleyBasher.check(test, proto);
                }
            }
        }
    }

    @Benchmark
    public int sort(final ThreadState ts) {
        final int[] proto = ts.proto;
        final int[] test = ts.test;

        ArrayUtils.clone(proto, test);
        ts.sorter.sort(test);

        // always consume test array
        return test[0];
    }

    /**
     * Custom main()
     */
    public static void main(String[] argv) throws IOException {
        // From org.openjdk.jmh.Main:
        try {
            final CommandLineOptions cmdOptions = new CommandLineOptions(argv);

            if (cmdOptions.shouldHelp()) {
                cmdOptions.showHelp();
                return;
            }

            if (cmdOptions.shouldListProfilers()) {
                cmdOptions.listProfilers();
                return;
            }

            if (cmdOptions.shouldListResultFormats()) {
                cmdOptions.listResultFormats();
                return;
            }

            Runner runner = new Runner(cmdOptions);

            if (cmdOptions.shouldList()) {
                runner.list();
                return;
            }

            if (cmdOptions.shouldListWithParams()) {
                runner.listWithParams(cmdOptions);
                return;
            }

            // GO ...
            String[] subSizes = null;

            if (!DEBUG) {
                // Generate arraySubSize according to the arraySize parameter:
                if (cmdOptions.getParameter(PARAM_SIZE).hasValue()) {
                    final Collection<String> sizes = cmdOptions.getParameter(PARAM_SIZE).get();
                    
                    int max = Integer.MIN_VALUE;
                    for (String size : sizes) {
                        int v = Integer.valueOf(size);
                        if (v > max) {
                            max = v;
                        }
                    }

                    // adjust tweak increment depending on array size
                    final int tweakInc = (max > 100000) ? 16 : TWEAK_INC;

                    final List<String> subSizeList = new ArrayList<String>();

                    for (int m = 1, end = 2 * max; m < end; m *= tweakInc) {
                        subSizeList.add(String.valueOf(m));
                    }

                    System.err.println(">> arraySubSize: " + subSizeList);

                    subSizes = subSizeList.toArray(new String[0]);
                }
            }

            final ChainedOptionsBuilder builder = new OptionsBuilder()
                    .parent(cmdOptions)
                    .include(ArraySortBenchmark2.class.getSimpleName());

            if (DEBUG) {
                builder.param(PARAM_SIZE, new String[]{"100", "1000", "10000", "100000", "1000000"});
                builder.param(PARAM_SUB_SIZE, "64");
                builder.param(PARAM_DATA_TWEAKER, "DITHER____");
                builder.param(PARAM_DIST_BUILDER, "_RANDOM");
                builder.param(PARAM_SORTER, new String[]{"BASELINE", "DPQ_11", "DPQ_18_11_21"});
            } else {
                if (subSizes != null) {
                    builder.param(PARAM_SUB_SIZE, subSizes);
                }
            }

            JMHAutoTuneDriver.autotune(builder, REP_DISTRIB);

        } catch (CommandLineOptionException e) {
            System.err.println(">> Error parsing command line:");
            System.err.println(" " + e.getMessage());
            System.exit(1);
        }
    }

}
