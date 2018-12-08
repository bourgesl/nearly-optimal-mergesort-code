package edu.sorting.bench;

import static edu.sorting.bench.ArraySortBenchmark2.PARAM_SORTER;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
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
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;
import wildinter.net.WelfordVariance;

public class JMHAutoTuneDriver {

    private final static VerboseMode VERBOSITY = VerboseMode.NORMAL;

    private final static int WARMUP_BLK = 4;
    private final static int WARMUP_PLATEAU = 5;
    private final static int WARMUP_INITIAL_ITER = 3 * WARMUP_PLATEAU;
    private final static int WARMUP_MAX_ADJUST = 10;
    private final static long WARMUP_INITIAL_TIME = 2L * 1000 * 1000; // 1ms in nanoseconds
    private final static long WARMUP_MIN_OPS = 5;
    // 15% as a local minimum may happen and plateau is then difficult to have
    private final static double WARMUP_TOL = 0.10;

    private final static double BENCHMARK_PCT = 0.90; // 90% percentile

    private final static String HEADER_COLUMNS = ">> COLUMNS:";
    private final static DecimalFormat df = new DecimalFormat("0.0000000");

    private JMHAutoTuneDriver() {
        // forbidden
    }

    // TODO: kill
    private final static boolean WARMUP_FIX_DIST_SAMPLES = false;
    private final static String PARAM_DIST_SAMPLES = "_distSamples";

    public static void autotune(final ChainedOptionsBuilder builder, final int distributionCount) {

        System.out.println("WARMUP_FIX_DIST_SAMPLES: " + WARMUP_FIX_DIST_SAMPLES);

        // Autotune start small
        builder.verbosity(VERBOSITY)
                .shouldFailOnError(false)
                .addProfiler(LinuxAffinityHelperProfiler.class);

        final Options baseOptions = builder.build();
        try {
            final OutputFormat out = OutputFormatFactory.createFormatInstance(System.err, VERBOSITY);

            final Options testOptions = new OptionsBuilder().parent(baseOptions)
                    .param(PARAM_DIST_SAMPLES, String.valueOf(distributionCount)).build(); // use all distributions to get variance

            final List<BenchmarkListEntry> benchmarks = prepareBenchmarks(out, testOptions);

            System.out.println(">> Benchmarks ...");

            // TODO: generalize output using testOptions parameters:
            System.out.println(HEADER_COLUMNS + "Mode\tLength\tSub-size\tBuilder\tTweaker\tSorter"
                    + "\tRMS\tMean\tStdDev\tCount\tMin\tMax");
            System.err.println(HEADER_COLUMNS + "Mode\tLength\tSub-size\tBuilder\tTweaker\tSorter"
                    + "\tRMS\tMean\tStdDev\tCount\tMin\tMax");

            double[] warmupTimes = null;
            double[] measureTimes = null;
            final StringBuilder sb = new StringBuilder(256);

            for (BenchmarkListEntry br : benchmarks) {
                System.err.println(">> Benchmark: " + br.getUsername() + " " + br.getMode());

                // Automatic WARMUP:
                int warmupIter = WARMUP_INITIAL_ITER;
                long warmupNs = WARMUP_INITIAL_TIME;

                for (int warmupAdj = 0;;) {
                    final int warmupCount = WARMUP_BLK * warmupIter;

                    if (warmupTimes == null || warmupTimes.length < warmupCount) {
                        warmupTimes = new double[warmupCount];
                    } else {
                        Arrays.fill(warmupTimes, 0.0);
                    }

                    System.err.println(">> Warmup: " + warmupCount + " iterations [" + (warmupAdj + 1) + " pass] ...");

                    // Create a new Runner:
                    final ChainedOptionsBuilder tBuilder = new OptionsBuilder()
                            .parent(baseOptions).forks(1)
                            .warmupIterations(0).measurementIterations(warmupCount)
                            .measurementTime(TimeValue.nanoseconds(warmupNs));

                    final WorkloadParams params = br.getWorkloadParams();

                    for (String key : params.keys()) {
                        final String value = params.get(key);
                        if (!WARMUP_FIX_DIST_SAMPLES || !PARAM_DIST_SAMPLES.equals(key)) {
                            System.err.println("  " + key + " = " + value);
                            tBuilder.param(key, value);
                        }
                    }
                    if (WARMUP_FIX_DIST_SAMPLES) {
                        // Override distSamples:
                        tBuilder.param(PARAM_DIST_SAMPLES, "1"); // use only first distribution
                    }

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
                        System.err.println(">> stats[blk " + d + "]: " + statDists[d]);

                        final double rms = statDists[d].rms();

                        if (minRms > rms) {
                            minRms = rms;
                        }
                    }
                    System.err.println(">> minRms: " + minRms);

                    final long ops = (long) Math.ceil(warmupNs / minRms);

                    if (ops < WARMUP_MIN_OPS) {
                        System.err.println(">> ops: " + ops + " < " + WARMUP_MIN_OPS);
                        // use longer time:
                        warmupNs = (long) Math.ceil(1.05 * WARMUP_MIN_OPS * minRms);
                        System.err.println(">> Adjusted warmup time: " + (1E-6 * warmupNs) + " ms.");
                        continue;
                    }

                    double best = Double.POSITIVE_INFINITY;
                    int minBlk = -1;
                    int belowCount = 0;

                    for (int d = 0; d < warmupIter; d++) {
                        // distance between (rms - minRms)
                        final double err = (statDists[d].rms() - minRms) / minRms;

                        System.err.println(">> stats[blk " + d + "]: " + err);

                        if (err <= WARMUP_TOL) {
                            if (minBlk == -1) {
                                best = statDists[d].rms();
                                minBlk = d;
                            }
                            belowCount++;
                        }
                    }

                    System.err.println(">> Best stats[blk " + minBlk + "]: " + statDists[minBlk]
                            + " below: " + belowCount);

                    if ((warmupAdj++ < WARMUP_MAX_ADJUST)
                            && ((minBlk == 0)
                            || (minBlk == (warmupIter - 1))
                            || (belowCount < WARMUP_PLATEAU))) {
                        // try again with more warmup iterations:
                        warmupIter += WARMUP_INITIAL_ITER;
                        continue;
                    } else {
                        warmupIter = minBlk;
                        System.err.println(">> Final warmup stats: " + statDists[minBlk]);
                        break;
                    }
                }

                // MEASUREMENTS
                final int warmupCount = WARMUP_BLK * warmupIter + (WARMUP_PLATEAU / 2);

                System.err.println(">> Adjusted Warmup: " + warmupCount + " iterations, time: " + (1E-6 * warmupNs)
                        + " ms");

                // 4 x 3 = 12 per distribution:
                final int forks = 4;
                final int measureIter = distributionCount * 3;
                final long measureNs = warmupNs * 2;

                System.err.println(">> Adjusted test: " + measureIter + " iterations, time: " + (1E-6 * measureNs)
                        + " ms with " + forks + " forks ...");

                final int measureCount = forks * measureIter;

                if (measureTimes == null || measureTimes.length < measureCount) {
                    measureTimes = new double[measureCount];
                } else {
                    Arrays.fill(measureTimes, 0.0);
                }

                // Create a new Runner:
                final ChainedOptionsBuilder tBuilder = new OptionsBuilder()
                        .parent(testOptions).forks(forks)
                        .warmupIterations(warmupCount).warmupTime(TimeValue.nanoseconds(warmupNs))
                        .measurementIterations(measureIter)
                        .measurementTime(TimeValue.nanoseconds(measureNs));

                final WorkloadParams params = br.getWorkloadParams();

                // TODO: generalize output using testOptions parameters:
                String testHeader = null;
                sb.setLength(0);

                for (String key : params.keys()) {
                    final String value = params.get(key);
//                    System.out.println("  " + key + " = " + value);
                    tBuilder.param(key, value); // include proper distSample
                    if (!PARAM_DIST_SAMPLES.equals(key)) {
                        sb.append('\t').append(value);

                        if (PARAM_SORTER.equals(key)) {
                            // padding (right)
                            for (int i = 8 - value.length(); i >= 0; i--) {
                                sb.append(' ');
                            }
                        }
                    }
                }
                testHeader = sb.toString();

                final Options runOptions = tBuilder.build();

                final Runner runner = new Runner(runOptions, out);

                getRawTimes(runner, measureTimes);

                // collect best results ie 90% percentile
                // => variance ie stddev (without outliers)
                Arrays.sort(measureTimes, 0, measureCount);

                final WelfordVariance statAll = new WelfordVariance();
                final WelfordVariance statPct = new WelfordVariance();

                final int idxPct = (int) Math.round(measureCount * BENCHMARK_PCT);

                for (int d = 0; d < measureCount; d++) {
                    statAll.add(measureTimes[d]);

                    if (d <= idxPct) {
                        statPct.add(measureTimes[d]);
                    }
                }

                dumpResult(System.out, "Best", testHeader, statPct);
                dumpResult(System.err, "Best", testHeader, statPct);
                dumpResult(System.err, "All", testHeader, statAll);
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

    private static void dumpResult(final PrintStream ps, final String mode, final String testHeader, final WelfordVariance stats) {
        ps.print(">> ");
        ps.print(mode);
        ps.print('\t');
        ps.print(testHeader);
        ps.print('\t');
        ps.print(df.format(1E-6 * stats.rms()));
        ps.print('\t');
        ps.print(df.format(1E-6 * stats.mean())); // ms
        ps.print('\t');
        ps.print(df.format(1E-6 * stats.stddev()));
        ps.print('\t');
        ps.print(stats.nSamples());
        ps.print('\t');
        ps.print(df.format(1E-6 * stats.min()));
        ps.print('\t');
        ps.print(df.format(1E-6 * stats.max()));
        ps.print('\n');
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

                        if (i == times.length) {
                            throw new IllegalStateException("Too many results: " + times.length);
                        }
                        times[i++] = primary.getScore();
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
        final Map<String, String[]> benchParams = br.getParams().orElse(Collections.<String, String[]>emptyMap());

        // sort parameters by names to ensure proper sequence of workload parameters:
        final List<String> params = new ArrayList<>();
        for (String p : benchParams.keySet()) {
            params.add(p);
        }
        Collections.sort(params);

        List<WorkloadParams> ps = new ArrayList<>();

        for (String p : params) {
            final String[] vals = benchParams.get(p);

            Collection<String> values = options.getParameter(p).orElse(Arrays.asList(vals));
            if (values.isEmpty()) {
                throw new RunnerException("Benchmark \"" + br.getUsername()
                        + "\" defines the parameter \"" + p + "\", but no default values.\n"
                        + "Define the default values within the annotation, or provide the parameter values at runtime.");
            }

            System.out.println("Parameter[" + p + "]: " + values);

            if (ps.isEmpty()) {
                int idx = 0;
                for (String v : values) {
                    WorkloadParams al = new WorkloadParams();
                    al.put(p, v, idx);
                    ps.add(al);
                    idx++;
                }
            } else {
                final List<WorkloadParams> newPs = new ArrayList<>();
                for (WorkloadParams wp : ps) {
                    int idx = 0;
                    for (String v : values) {
                        WorkloadParams al = wp.copy();
                        al.put(p, v, idx);
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
