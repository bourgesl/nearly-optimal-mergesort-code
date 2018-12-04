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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
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
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.IterationResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.BenchmarkList;
import org.openjdk.jmh.runner.BenchmarkListEntry;
import org.openjdk.jmh.runner.ProfilersFailedException;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.WorkloadParams;
import org.openjdk.jmh.runner.format.OutputFormat;
import org.openjdk.jmh.runner.format.OutputFormatFactory;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.CommandLineOptionException;
import org.openjdk.jmh.runner.options.CommandLineOptions;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;
import wildinter.net.WelfordVariance;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class ArraySortBenchmark2 {

    private final static VerboseMode VERBOSITY = VerboseMode.NORMAL;

    private final static boolean DEBUG = false;

    private final static int WARMUP_INITIAL_ITER = 5;
    private final static long WARMUP_INITIAL_TIME = 4L * 1000 * 1000; // 4ms in nanoseconds
    private final static int WARMUP_BLK = 3;
    private final static long WARMUP_MIN_OPS = 100;

    private final static double WARMUP_TOL = 0.02; // 2%

    private static final boolean TRACE = false;

    private static final int REP_DISTRIB = 10;

    private final static int TWEAK_INC = 4; // 2 originally

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
                if (cmdOptions.getParameter("arraySize").hasValue()) {
                    Collection<String> sizes = cmdOptions.getParameter("arraySize").get();
                    int max = Integer.MIN_VALUE;
                    for (String size : sizes) {
                        int v = Integer.valueOf(size);
                        if (v > max) {
                            max = v;
                        }
                    }
                    System.out.println("max(arraySize): " + max);

                    // adjust tweak increment depending on array size
                    final int tweakInc = (max > 100000) ? 16 : TWEAK_INC;

                    final List<String> subSizeList = new ArrayList<String>();

                    for (int m = 1, end = 2 * max; m < end; m *= tweakInc) {
                        subSizeList.add(String.valueOf(m));
                    }

                    System.out.println("subSizeList: " + subSizeList);

                    subSizes = subSizeList.toArray(new String[0]);
                }
            }

            final ChainedOptionsBuilder builder = new OptionsBuilder().parent(cmdOptions)
                    .include(ArraySortBenchmark2.class.getSimpleName())
                    .shouldFailOnError(false);

            if (DEBUG) {
                builder.param("arraySize", new String[]{"100", "1000", "10000", "100000", "1000000"});
                builder.param("arraySubSize", "64");
                builder.param("dataTweaker", "DITHER____");
                builder.param("distBuilder", "_RANDOM");
                builder.param("tSorter", new String[]{"BASELINE", "DPQ_11", "DPQ_18_11_21"});
            } else {
                if (subSizes != null) {
                    builder.param("arraySubSize", subSizes);
                }
            }

            autotune(builder);

        } catch (CommandLineOptionException e) {
            System.err.println("Error parsing command line:");
            System.err.println(" " + e.getMessage());
            System.exit(1);
        }

    }

    private static void autotune(final ChainedOptionsBuilder builder) {
        // Autotune start small
        builder.verbosity(VerboseMode.NORMAL);

        final Options baseOptions = builder.build();
        try {
            final OutputFormat out = OutputFormatFactory.createFormatInstance(System.out, VERBOSITY);

            final Options testOptions = new OptionsBuilder().parent(baseOptions)
                    .param("_distSamples", String.valueOf(REP_DISTRIB)).build(); // use all distributions to get variance

            final List<BenchmarkListEntry> benchmarks = prepareBenchmarks(out, testOptions);

            System.out.println("Benchmarks ...");

            double[] warmupTimes = null;

            for (BenchmarkListEntry br : benchmarks) {
                System.out.println("Benchmark: " + br.getUsername() + " " + br.getMode());

                // Automatic WARMUP:
                int warmupIter = WARMUP_INITIAL_ITER;
                long warmupNs = WARMUP_INITIAL_TIME;

                for (;;) {
                    final int warmupCount = WARMUP_BLK * warmupIter;

                    if (warmupTimes == null || warmupTimes.length < warmupCount) {
                        warmupTimes = new double[warmupCount];
                    }

                    System.out.println("Warmup: " + warmupCount + " iterations ...");

                    // Create a new Runner:
                    final ChainedOptionsBuilder tBuilder = new OptionsBuilder().parent(baseOptions).forks(1)
                            .warmupIterations(0).measurementIterations(warmupCount)
                            .measurementTime(TimeValue.nanoseconds(warmupNs));

                    final WorkloadParams params = br.getWorkloadParams();

                    for (String key : params.keys()) {
                        final String value = params.get(key);
                        if (!"_distSamples".equals(key)) {
                            System.out.println("  " + key + " = " + value);
                            tBuilder.param(key, value);
                        }
                    }
                    // Override distSamples:
                    tBuilder.param("_distSamples", "1"); // use only first distribution

                    final Options runOptions = tBuilder.build();

                    final Runner runner = new Runner(runOptions, out);

                    getRawTimes(runner, warmupTimes);

                    // decide best warmup count:
                    final WelfordVariance[] statDists = new WelfordVariance[warmupIter];
                    double minRms = Double.POSITIVE_INFINITY;

                    for (int d = 0; d < warmupIter; d++) {
                        statDists[d] = new WelfordVariance();
                        final int k = d * WARMUP_BLK;

                        for (int i = 0; i < WARMUP_BLK; i++) {
                            statDists[d].add(warmupTimes[k + i]);
                        }
                        System.out.println("stats[blk " + d + "]: " + statDists[d]);

                        final double rms = statDists[d].rms();

                        if (minRms > rms) {
                            minRms = rms;
                        }
                    }
                    System.out.println("minRms: " + minRms);

                    final long ops = (long) Math.ceil(warmupNs / minRms);
                    System.out.println("ops: " + ops);

                    if (ops < WARMUP_MIN_OPS) {
                        // use longer time:
                        warmupNs = (long) Math.ceil(1.1 * WARMUP_MIN_OPS * minRms);
                        System.out.println("adjust warmupNs: " + warmupNs);
                        continue;
                    }

                    double best = Double.POSITIVE_INFINITY;
                    int minBlk = -1;

                    for (int d = 0; d < warmupIter; d++) {
                        // distance between (rms - minRms)
                        final double err = (statDists[d].rms() - minRms) / minRms;

                        System.out.println("stats[blk " + d + "]: " + err);

                        if (err <= WARMUP_TOL) {
                            if (minBlk == -1) {
                                best = statDists[d].rms();
                                minBlk = d;
                            }
                        }
                        // TODO: check plateau ?
                    }

                    System.out.println("Best stats[blk " + minBlk + "]: " + statDists[minBlk]);

                    if ((minBlk == 0) || (minBlk == (warmupIter - 1))) {
                        // go on:
                        warmupIter += WARMUP_INITIAL_ITER;
                        continue;
                    } else {
                        warmupIter = minBlk;
                        break;
                    }
                }

                // TODO: MEASUREMENTS
                final int warmupCount = WARMUP_BLK * warmupIter + 2; // margin

                System.out.println("Warmup: " + warmupCount + " iterations ...");

                // Create a new Runner:
                final ChainedOptionsBuilder tBuilder = new OptionsBuilder().parent(testOptions).forks(3)
                        .warmupIterations(warmupCount).warmupTime(TimeValue.nanoseconds(warmupNs))
                        .measurementIterations(REP_DISTRIB * 3) // 3 x 3 = 9 per distrib
                        .measurementTime(TimeValue.nanoseconds(warmupNs * 5)); // TODO

                final WorkloadParams params = br.getWorkloadParams();

                for (String key : params.keys()) {
                    final String value = params.get(key);
                    tBuilder.param(key, value); // include proper distSample
                }

                final Options runOptions = tBuilder.build();

                final Runner runner = new Runner(runOptions, out);
                final Collection<RunResult> results = runner.run();

                // TODO: collect best results ie median [0-65% percentile] => variance ie stddev (without outliers)
            }

        } catch (ProfilersFailedException e) {
            // This is not exactly an error, set non-zero exit code
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (RunnerException e) {
            System.err.print("ERROR: ");
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private int getWarmUpLength(List<Double> times, final double tolerance, final int plateauSize) {
        double expected = times.get(times.size() - 1);
        int lastCount = 0;
        for (int i = times.size() - 1; i >= 0; --i) {
            double curr = times.get(i);
            if (Math.abs(curr - expected) <= tolerance * Math.min(curr, expected)) {
                ++lastCount;
            } else {
                break;
            }
        }
        if (lastCount >= plateauSize) {
            return times.size() - lastCount + plateauSize;
        } else {
            return -1;
        }
    }

    private static void getRawTimes(final Runner runner, final double[] times) throws RunnerException {
        final Collection<RunResult> results = runner.run();
        int i = 0;

        for (RunResult result : results) {

            for (BenchmarkResult benchmarkResult : result.getBenchmarkResults()) {

                for (IterationResult iterationResult : benchmarkResult.getIterationResults()) {

                    for (Result<?> primary : iterationResult.getRawPrimaryResults()) {

                        final long n = primary.getStatistics().getN();

                        if (n != 1L) {
                            throw new IllegalStateException("Unable to dig through JMH output: getN() != 1", null);
                        }

                        final double value = primary.getScore();

                        if (i == times.length) {
                            throw new IllegalStateException("Too many results: " + times.length);
                        }

                        times[i++] = value;
                    }
                }
            }
        }
    }

    private static List<BenchmarkListEntry> prepareBenchmarks(final OutputFormat out, final Options options) throws RunnerException {

        final SortedSet<BenchmarkListEntry> benchmarks = BenchmarkList.defaultList().find(out, options.getIncludes(), options.getExcludes());

        final List<BenchmarkListEntry> newBenchmarks = new ArrayList<>();

        for (BenchmarkListEntry br : benchmarks) {
            if (br.getParams().hasValue()) {
                for (WorkloadParams p : explodeAllParams(br, options)) {
                    newBenchmarks.add(br.cloneWith(p));
                }
            } else {
                newBenchmarks.add(br);
            }
        }
        return newBenchmarks;
    }

    private static List<WorkloadParams> explodeAllParams(final BenchmarkListEntry br, final Options options) throws RunnerException {
        Map<String, String[]> benchParams = br.getParams().orElse(Collections.<String, String[]>emptyMap());

        List<WorkloadParams> ps = new ArrayList<>();
        for (Map.Entry<String, String[]> e : benchParams.entrySet()) {
            String k = e.getKey();
            String[] vals = e.getValue();
            Collection<String> values = options.getParameter(k).orElse(Arrays.asList(vals));
            if (values.isEmpty()) {
                throw new RunnerException("Benchmark \"" + br.getUsername()
                        + "\" defines the parameter \"" + k + "\", but no default values.\n"
                        + "Define the default values within the annotation, or provide the parameter values at runtime.");
            }
            if (ps.isEmpty()) {
                int idx = 0;
                for (String v : values) {
                    WorkloadParams al = new WorkloadParams();
                    al.put(k, v, idx);
                    ps.add(al);
                    idx++;
                }
            } else {
                List<WorkloadParams> newPs = new ArrayList<>();
                for (WorkloadParams p : ps) {
                    int idx = 0;
                    for (String v : values) {
                        WorkloadParams al = p.copy();
                        al.put(k, v, idx);
                        newPs.add(al);
                        idx++;
                    }
                }
                ps = newPs;
            }
        }
        return ps;
    }

}
