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

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class ArraySortBenchmark {

    private static final boolean TRACE = false;

    private static final int REP_DISTRIB = 10;

    @State(Scope.Benchmark)
    public static class BenchmarkState {

        @Param({"50", "100"
        /*, "500", "1000", "5000" */
 /*, "10000", "50000", "100000", "500000", "1000000" */
        })
        int arraySize;

        @Param({"1", "4", "16", "64" /*, "256", "1024", "4096" */})
        int arraySubSize;

        /* IntArrayTweaker.values() */
        @Param({"IDENT_____", "REVERSE___", "REVERSE_FR", "REVERSE_BA", "SORT______", "DITHER____"})
        IntArrayTweaker dataTweaker;

        /* ParamIntArrayBuilder.values() */
        @Param({"STAGGER", "SAWTOTH", "_RANDOM", "PLATEAU", "SHUFFLE"})
        ParamIntArrayBuilder distBuilder;

        @Param({"BASELINE", "DPQ_11", "DPQ_18_11_21", "DPQ_18_11I", "DPQ_18_11P", "RADIX" /*, "MARLIN" */})
        IntSorter tSorter;

        final int[][] inputs = new int[REP_DISTRIB][];
        final int[][] protos = new int[REP_DISTRIB][];
        final int[][] tests = new int[REP_DISTRIB][];
        private int lastArraySize = -1;

        @Setup(Level.Trial)
        public void setUpTrial() {
            if (TRACE) {
                System.out.println("setUpTrial");
            }
            if (arraySize != lastArraySize) {
                lastArraySize = arraySize;

                // Allocate many working arrays to circumvent any alignment issue (int[] are aligned to 8/16/24/32):
                for (int d = 0; d < REP_DISTRIB; d++) {
                    inputs[d] = new int[arraySize];
                    protos[d] = new int[arraySize];
                    tests[d] = new int[arraySize];
                }
                // Promote all the arrays (GC)
                BentleyBasher.cleanup();
            }
        }

        @Setup(Level.Iteration)
        public void setUpIteration() {
            // Reset tweaker to have sample initial conditions (seed):
            ParamIntArrayBuilder.reset();

            // Allocate many working arrays to circumvent any alignment issue (int[] are aligned to 8/16/24/32):
            for (int d = 0; d < REP_DISTRIB; d++) {
                // Get new distribution sample:
                distBuilder.build(inputs[d], arraySubSize);

                // tweak sample:
                dataTweaker.tweak(inputs[d], protos[d]);
            }
            // Promote all the arrays (GC)
            BentleyBasher.cleanup();
        }

        /*
         * And, check the benchmark went fine afterwards:
         */
        @TearDown(Level.Iteration)
        public void check() {
            if (!tSorter.skipCheck()) {
                for (int d = 0; d < REP_DISTRIB; d++) {
                    // may throw runtime exception
                    BentleyBasher.check(tests[d], protos[d]);
                }
            }
        }
    }

    @State(Scope.Thread)
    public static class ThreadState {
        // current index in distributions
        int idx = 0;
    }

    @Benchmark
    public int sort(final BenchmarkState bs, final ThreadState ts) {
        // Use many working arrays (1 per distribution):
        final int i = ts.idx;
        final int[] proto = bs.protos[i];
        final int[] test = bs.tests[i];
        // go forward
        ts.idx = (i + 1) % REP_DISTRIB;

        ArrayUtils.clone(proto, test);
        bs.tSorter.sort(test);

        // always consume test array
        return test[0];
    }

}
