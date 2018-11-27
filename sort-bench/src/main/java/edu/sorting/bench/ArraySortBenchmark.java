/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
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
import org.openjdk.jmh.runner.Defaults;
import org.openjdk.jmh.runner.NoBenchmarksException;
import org.openjdk.jmh.runner.ProfilersFailedException;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.CommandLineOptionException;
import org.openjdk.jmh.runner.options.CommandLineOptions;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class ArraySortBenchmark {

    private static final boolean TRACE = false;

    private static final int REP_DISTRIB = 10;

    private final static int TWEAK_INC = 4; // 2 originally

    @State(Scope.Benchmark)
    public static class BenchmarkState {

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
        // benchmark loop state:
        IntSorter sorter = null;
        int[] proto = null;
        int[] test = null;

        @Setup(Level.Iteration)
        public void setUpIteration(final BenchmarkState bs) {
            // Use many working arrays (1 per distribution):
            sorter = bs.tSorter;
            proto = bs.protos[idx];
            test = bs.tests[idx];

            if (TRACE) {
                System.out.println("\nsetUpIteration: proto[" + idx + "] = \n"
                        + Arrays.toString(proto));
            }

            // go forward
            idx = (idx + 1) % REP_DISTRIB;

            // Cleanup (GC)
            BentleyBasher.cleanup();
        }

        /*
         * And, check the benchmark went fine afterwards:
         */
        @TearDown(Level.Iteration)
        public void check() {
            if (sorter != null && !sorter.skipCheck()) {
                for (int d = 0; d < REP_DISTRIB; d++) {
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

            String[] subSizes = null;

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

            ChainedOptionsBuilder builder = new OptionsBuilder().parent(cmdOptions)
                    .include(ArraySortBenchmark.class.getSimpleName())
                    .shouldFailOnError(false);

            if (subSizes != null) {
                builder.param("arraySubSize", subSizes);
            }

            final Options opt = builder.build();

            final Runner runner = new Runner(opt);

            if (cmdOptions.shouldHelp()) {
                cmdOptions.showHelp();
                return;
            }

            if (cmdOptions.shouldList()) {
                runner.list();
                return;
            }

            if (cmdOptions.shouldListWithParams()) {
                runner.listWithParams(cmdOptions);
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

            try {
                runner.run();
            } catch (NoBenchmarksException e) {
                System.err.println("No matching benchmarks. Miss-spelled regexp?");

                if (cmdOptions.verbosity().orElse(Defaults.VERBOSITY) != VerboseMode.EXTRA) {
                    System.err.println("Use " + VerboseMode.EXTRA + " verbose mode to debug the pattern matching.");
                } else {
                    runner.list();
                }
                System.exit(1);
            } catch (ProfilersFailedException e) {
                // This is not exactly an error, set non-zero exit code
                System.err.println(e.getMessage());
                System.exit(1);
            } catch (RunnerException e) {
                System.err.print("ERROR: ");
                e.printStackTrace(System.err);
                System.exit(1);
            }

        } catch (CommandLineOptionException e) {
            System.err.println("Error parsing command line:");
            System.err.println(" " + e.getMessage());
            System.exit(1);
        }
    }

}
