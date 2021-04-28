package edu.sorting.bench;

/*
 * Copyright 2015 Goldman Sachs.
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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
import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort20210424;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class SortingIntBenchmarkTestJMH {

    public static final int MAX_VALUE = 1_000_000;

    @Param({"pairFlipZeroPairFlip", "pairFlipOneHundredPairFlip",
         "zeroHi", "hiZeroLow", "hiFlatLow", "identical",
            "randomDups", "randomNoDups", "sortedReversedSorted", "pairFlip", "endLessThan"})

    public String listType;

    private int[] array;
    private static final int LIST_SIZE = 10_000_000;
    public static final int NUMBER_OF_ITERATIONS = 10;

    @Setup
    public void setUp() {
        Random random = new Random(123456789012345L);
        this.array = new int[LIST_SIZE];
        int threeQuarters = (int) (LIST_SIZE * 0.75);
        if ("zeroHi".equals(this.listType)) {
            for (int i = 0; i < threeQuarters; i++) {
                this.array[i] = 0;
            }
            int k = 1;
            for (int i = threeQuarters; i < LIST_SIZE; i++) {
                this.array[i] = k;
                k++;
            }
        } else if ("hiFlatLow".equals(this.listType)) {
            int oneThird = LIST_SIZE / 3;
            for (int i = 0; i < oneThird; i++) {
                this.array[i] = i;
            }
            int twoThirds = oneThird * 2;
            int constant = oneThird - 1;
            for (int i = oneThird; i < twoThirds; i++) {
                this.array[i] = constant;
            }
            for (int i = twoThirds; i < LIST_SIZE; i++) {
                this.array[i] = constant - i + twoThirds;
            }
        } else if ("hiZeroLow".equals(this.listType)) {
            int oneThird = LIST_SIZE / 3;
            for (int i = 0; i < oneThird; i++) {
                this.array[i] = i;
            }
            int twoThirds = oneThird * 2;
            for (int i = oneThird; i < twoThirds; i++) {
                this.array[i] = 0;
            }
            for (int i = twoThirds; i < LIST_SIZE; i++) {
                this.array[i] = oneThird - i + twoThirds;
            }
        } else if ("identical".equals(this.listType)) {
            for (int i = 0; i < LIST_SIZE; i++) {
                this.array[i] = 0;
            }
        } else if ("randomDups".equals(this.listType)) {
            for (int i = 0; i < LIST_SIZE; i++) {
                this.array[i] = random.nextInt(1000);
            }
        } else if ("randomNoDups".equals(this.listType)) {
            Set<Integer> set = new HashSet();
            while (set.size() < LIST_SIZE + 1) {
                set.add(random.nextInt());
            }
            List<Integer> list = new ArrayList<>(LIST_SIZE);
            list.addAll(set);
            for (int i = 0; i < LIST_SIZE; i++) {
                this.array[i] = list.get(i);
            }
        } else if ("sortedReversedSorted".equals(this.listType)) {
            for (int i = 0; i < LIST_SIZE / 2; i++) {
                this.array[i] = i;
            }
            int num = 0;
            for (int i = LIST_SIZE / 2; i < LIST_SIZE; i++) {
                this.array[i] = LIST_SIZE - num;
                num++;
            }
        } else if ("pairFlip".equals(this.listType)) {
            for (int i = 0; i < LIST_SIZE; i++) {
                this.array[i] = i;
            }
            for (int i = 0; i < LIST_SIZE; i += 2) {
                int temp = this.array[i];
                this.array[i] = this.array[i + 1];
                this.array[i + 1] = temp;
            }
        } else if ("endLessThan".equals(this.listType)) {
            for (int i = 0; i < LIST_SIZE - 1; i++) {
                this.array[i] = 3;
            }
            this.array[LIST_SIZE - 1] = 1;
        } else if ("pairFlipZeroPairFlip".equals(this.listType)) {
            //pairflip
            for (int i = 0; i < 64; i++) {
                this.array[i] = i;
            }
            for (int i = 0; i < 64; i += 2) {
                int temp = this.array[i];
                this.array[i] = this.array[i + 1];
                this.array[i + 1] = temp;
            }
            //zero
            for (int i = 64; i < this.array.length - 64; i++) {
                this.array[i] = 0;
            }
            //pairflip
            for (int i = this.array.length - 64; i < this.array.length; i++) {
                this.array[i] = i;
            }
            for (int i = this.array.length - 64; i < this.array.length; i += 2) {
                int temp = this.array[i];
                this.array[i] = this.array[i + 1];
                this.array[i + 1] = temp;
            }
        } else if ("pairFlipOneHundredPairFlip".equals(this.listType)) {
            //10, 5
            for (int i = 0; i < 64; i++) {
                if (i % 2 == 0) {
                    this.array[i] = 10;
                } else {
                    this.array[i] = 5;
                }
            }

            //100
            for (int i = 64; i < this.array.length - 64; i++) {
                this.array[i] = 100;
            }

            //10, 5
            for (int i = this.array.length - 64; i < this.array.length; i++) {
                if (i % 2 == 0) {
                    this.array[i] = 10;
                } else {
                    this.array[i] = 5;
                }
            }
        }
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void sortNewWay() {
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            DualPivotQuicksort20210424.INSTANCE.sort(this.array, 0, this.array.length - 1);
        }
    }

    @Warmup(iterations = 20)
    @Measurement(iterations = 10)
    @Benchmark
    public void sortCurrentWay() {
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            DualPivotQuickSort2011.INSTANCE.sort(this.array, 0, this.array.length - 1);
        }
    }

}
