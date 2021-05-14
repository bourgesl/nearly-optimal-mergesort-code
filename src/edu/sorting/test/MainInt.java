/*
 * Copyright (c) 2021, Oracle and/or its affiliates. All rights reserved.
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
package edu.sorting.test;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort20210424;
import edu.sorting.DualPivotQuicksort202105;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public final class MainInt {

    static final int WARM_PASS = 3; // 1 is not enough to ensure results are 'stable' enough

    static final int ALIGN_MAX = 16; // 16 * 4 = 64

    static final boolean TRACING = true;

    public static void main(String[] args) {
        out("# Data type = int, Sorting type = " + (parallelism == 0 ? "sequential" : "parallel " + (parallelism + 1)) + ", Length = " + MAX_N);
        out("test_name;loop_time;jdk_time;dpqs_time;gain\n");

        Locale.setDefault(new Locale("us", "US"));

        if (!TRACING && (COUNT > 100)) {
            WARMUP = true;
            for (int i = 1; i <= WARM_PASS; i++) {
                System.out.println("# Warmup " + i + "...");
                doSort();
            }
            WARMUP = false;
        }

        System.out.println("# Benchmark ...");
        doSort();
    }

    private static void doSort() {
        if (false) {
            // Perform GC before test
            System.gc();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                // ignore
            }
        }
        totalTime1 = 0.0;
        totalTime2 = 0.0;
        totalTime3 = 0.0;

        common();
        allBig();

        if (WARMUP) {
            System.out.printf("# Warmup %s;%.2f;%.2f;%.2f;%.3f\n", "TOTAL", totalTime1, totalTime2, totalTime3, (totalTime2 - totalTime3) / totalTime2);
        } else {
            System.out.printf("%s;%.2f;%.2f;%.2f;%.3f\n", "TOTAL", totalTime1, totalTime2, totalTime3, (totalTime2 - totalTime3) / totalTime2);
        }
    }

    private final static Sortable SORT_NONE = new Sortable() {
        public final void sortArray(final int[] a, int off) {
            a[MAX_N / 2] = 1;
            /* touch */
        }
    };
    private final static Sortable SORT_JDK = new Sortable() {
        public final void sortArray(final int[] a, int off) {
            Arrays.sort(a, LEFT + off, HIGH + off); // exclusive
        }
    };
    private final static Sortable SORT_NEW_DPQS_REF = new Sortable() {
        public final void sortArray(final int[] a, int off) {
            edu.sorting.ref.Arrays.sort(a, LEFT + off, HIGH + off); // exclusive
        }
    };
    private final static Sortable SORT_DPQS_11 = new Sortable() {
        public final void sortArray(final int[] a, int off) {
            DualPivotQuickSort2011.INSTANCE.sort(a, LEFT + off, RIGHT + off); // inclusive
        }
    };
    private final static Sortable SORT_DPQS_21_04 = new Sortable() {
        public final void sortArray(final int[] a, int off) {
            DualPivotQuicksort20210424.INSTANCE.sort(a, LEFT + off, RIGHT + off); // inclusive
        }
    };
    private final static Sortable SORT_DPQS_21_05_NEW = new Sortable() {
        public final void sortArray(final int[] a, int off) {
            DualPivotQuicksort202105.INSTANCE.sort(a, LEFT + off, RIGHT + off); // inclusive
        }
    };
    private final static Sortable SORT_DPQS_21_05_RADIX_NEW = new Sortable() {
        public final void sortArray(final int[] a, int off) {
            DualPivotQuicksort202105.RADIX_NEW.sort(a, LEFT + off, RIGHT + off); // inclusive
        }
    };

    private static void doIteration(final String testName) {
        time1 = -1.0;
        time2 = -1.0;
        time3 = -1.0;

        doSort(testName, "none", SORT_NONE);

        if (true) {
            doSort(testName, "dpqs2105", SORT_DPQS_21_05_NEW);
            doSort(testName, "radixNew", SORT_DPQS_21_05_RADIX_NEW);
        } else {
            if (true) {
                doSort(testName, "jdk", SORT_JDK);
                doSort(testName, "dpqsNew", SORT_NEW_DPQS_REF);
            } else {
                doSort(testName, "dpqs11", SORT_DPQS_11);
                doSort(testName, "dpqs21", SORT_DPQS_21_04);
            }
        }

        if (WARMUP) {
            System.out.printf("# Warmup %s;%.2f;%.2f;%.2f;%.3f\n", testName, time1, time2, time3, (time2 - time3) / time2);
        } else {
            System.out.printf("%s;%.2f;%.2f;%.2f;%.3f\n", testName, time1, time2, time3, (time2 - time3) / time2);
        }
    }

    private static int doSort(final String testName, final String algoName, Sortable sortable) {
        if (TRACING) {
            System.out.println("Sort[" + testName + "] with " + algoName);
        }
        long startTime;
        long endTime;
        long minTime = Long.MAX_VALUE;

        prepareGolden();

        final int[] _golden = golden;
        final int[] test = test_data;
        final int count = WARMUP ? (MAX_N < 1500 ? 104000 : WARM_COUNT) : COUNT;

        int off = 0, blackhole = 0;

        // Hot benchmark loop:
        for (int i = 0; i < count; ++i) {
            // BUGGY CHECK or SORT if off != 0:
            if (false) {
                // move offset: JVM aligns on 8-bytes by default, so increment by 1 int (4) 
                // to sample all combinations in [0 - 64[ 
                off += 1;
                if (off >= ALIGN_MAX) {
                    off = 0;
                }
            }
            // no allocation:
            for (int j = 0; j < _golden.length; j++) {
                test[off + j] = _golden[j];
            }

            startTime = System.nanoTime();
            sortable.sortArray(test, off);
            endTime = System.nanoTime();
            blackhole += test[MAX_N / 2]; // always consume results

            long time = endTime - startTime;
            minTime = Math.min(minTime, time);
            /*
            if (time > 0L) {
                results[n++] = (int)(time);
            }
             */
            if (CHECK) {
                check(algoName, golden, test, off, LEFT, RIGHT);
            }
        }
        long allTime = minTime;
        /*
        // Find median using java.util.Arrays to avoid hotspot profiling pollution:
        java.util.Arrays.sort(results, 0, n);
//            System.out.println("stats: "+results[0] + " - " +results[n / 2] + " - " + results[n - 1]);

        final int cut = Math.max(2, Math.min(100, n / 100)); // best 1% or fastest 10th
        for (int i = 0; i < cut; ++i) {
            allTime += results[i];
        }
         */
        if (false && CHECK /* && !warm */) {
            check(algoName, golden, test, off, LEFT, RIGHT);
        }

        long div = 1L;

        if (MAX_N > 500000) {
            div = 1_000_000L;
        } else if (MAX_N > 10000) {
            div = 1_000L;
        }
        if (time1 < 0.0) {
            time1 = ((double) allTime) / div;
            totalTime1 += time1;
        } else if (time2 < 0.0) {
            time2 = ((double) allTime) / div;
            totalTime2 += time2;
        } else if (time3 < 0.0) {
            time3 = ((double) allTime) / div;
            totalTime3 += time3;
        }

//        if (CHECK && warm) System.out.println("blackhole: "+blackhole);
        return blackhole;
    }

    private static int noiseS() { // 1% prob add uniform noise
        int r = random.nextInt();
        return (r & 127) == 0 ? r : 0;
    }

    private static int noiseM() { // 50% prob add 1
        return (random.nextInt() & 1);
    }

    private static int noiseL() { // add uniform -3...+3
        return (random.nextInt() & 7) - 3;
    }

    private static void ascendingM() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i + noiseM();
        }
    }

    private static void ascendingL() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i + noiseL();
        }
    }

    private static void ascendingS() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i + noiseS();
        }
    }

    private static void equalM() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = noiseM();
        }
    }

    private static void equalL() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = noiseL();
        }
    }

    private static void equalS() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = noiseS();
        }
    }

    private static void latchM(int m) {
        int max = MAX_N / m;
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = (i + noiseM()) % max;
        }
    }

    private static void latchL(int m) {
        int max = MAX_N / m;
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = (i + noiseL()) % max;
        }
    }

    private static void latchS(int m) {
        int max = MAX_N / m;
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = (i + noiseS()) % max;
        }
    }

    private static void staggerM(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = ((i + noiseM()) * m + i) % MAX_N;
        }
    }

    private static void staggerL(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = ((i + noiseL()) * m + i) % MAX_N;
        }
    }

    private static void staggerS(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = ((i + noiseS()) * m + i) % MAX_N;
        }
    }

    private static void organPipesM() {
        int middle = MAX_N / 2;
        for (int i = 0; i <= middle; ++i) {
            int j = i + noiseM();
            golden[i] = j;
            golden[MAX_N - 1 - i] = j;
        }
    }

    private static void organPipesL() {
        int middle = MAX_N / 2;
        for (int i = 0; i <= middle; ++i) {
            int j = i + noiseL();
            golden[i] = j;
            golden[MAX_N - 1 - i] = j;
        }
    }

    private static void organPipesS() {
        int middle = MAX_N / 2;
        for (int i = 0; i <= middle; ++i) {
            int j = i + noiseS();
            golden[i] = j;
            golden[MAX_N - 1 - i] = j;
        }
    }

    private static void common() {
        if (false) {
            random();
            doIteration("random");
        }

        equal();
        doIteration("equal");
        ascending();
        doIteration("ascending");
        descending();
        doIteration("descending");
        // organPipes();  doIteration("organPipes");

        period(4);
        doIteration("period[4]");
        period(5);
        doIteration("period[5]");
        period(6);
        doIteration("period[6]");

        // stagger( 2);   doIteration("stagger: 2");
        // stagger( 4);   doIteration("stagger: 4");
        stagger(15);
        doIteration("stagger[15]");
        stagger(24);
        doIteration("stagger[24]");
        stagger(33);
        doIteration("stagger[33]");
        // stagger(64);   doIteration("stagger: 64");

        // latch(4);      doIteration("latch: 4");
        // latch(8);      doIteration("latch: 8");
        // latch(9);      doIteration("latch: 9");
        shuffle(6);
        doIteration("shuffle[6]");
        shuffle(7);
        doIteration("shuffle[7]");
        shuffle(8);
        doIteration("shuffle[8]");
    }

    private static void all() {
        monotonousAll();
        otherAll();
        staggerAllBig();
        latchAllBig();
        shuffleAllBig();
        shuffleDitherAllBig();
        periodAllBig();
        randomAllBig();
        plateauAllBig();
        tableAllBig();
    }

    private static void allBig() {
        all();
        ringAll();
        pearlAll();
    }

    private static void shuffleAll() {
        shuffle(2);
        doIteration("shuffle: 2");
        shuffle(3);
        doIteration("shuffle: 3");
        shuffle(4);
        doIteration("shuffle: 4");
        shuffle(5);
        doIteration("shuffle: 5");
        shuffle(6);
        doIteration("shuffle: 6");
        shuffle(7);
        doIteration("shuffle: 7");
        shuffle(8);
        doIteration("shuffle: 8");
        shuffle(16);
        doIteration("shuffle: 16");
    }

    private static void shuffleSmall() {
        shuffle(2);
        doIteration("shuffle 2");
        shuffle(3);
        doIteration("shuffle 3");
        shuffle(4);
        doIteration("shuffle 4");
        shuffle(5);
        doIteration("shuffle 5");
    }

    private static void shuffleAllBig() {
        shuffleAll();

        shuffle(32);
        doIteration("shuffle 32");
        shuffle(64);
        doIteration("shuffle 64");
        shuffle(128);
        doIteration("shuffle 128");
        shuffle(256);
        doIteration("shuffle: 256");
        shuffle(512);
        doIteration("shuffle: 512");
        shuffle(1024);
        doIteration("shuffle: 1024");
        shuffle(2024);
        doIteration("shuffle: 2024");
    }

    private static void shuffleDitherAll() {
        shuffleDither(2);
        doIteration("shuffle d2");
        shuffleDither(4);
        doIteration("shuffle d4");
        shuffleDither(6);
        doIteration("shuffle d6");
        shuffleDither(8);
        doIteration("shuffle d8");
    }

    private static void shuffleDitherAllBig() {
        shuffleDitherAll();
        shuffleDither(16);
        doIteration("shuffle  d16");
        shuffleDither(32);
        doIteration("shuffle  d32");
        shuffleDither(64);
        doIteration("shuffle  d64");
        shuffleDither(128);
        doIteration("shuffle d128");
    }

    private static void periodAll() {
        period(2);
        doIteration("period[2]");
        period(3);
        doIteration("period[3]");
        period(4);
        doIteration("period[4]");
        period(5);
        doIteration("period[5]");
        period(6);
        doIteration("period[6]");
        period(7);
        doIteration("period[7]");
        period(8);
        doIteration("period[8]");
        period(9);
        doIteration("period[9]");
    }

    private static void periodSmall() {
        period(2);
        doIteration("period[2]");
        period(3);
        doIteration("period[3]");
        period(4);
        doIteration("period[4]");
    }

    private static void periodAllBig() {
        periodAll();
        period(10);
        doIteration("period[ 10]");
        period(12);
        doIteration("period[ 12]");
        period(14);
        doIteration("period[ 14]");
        period(16);
        doIteration("period[ 16]");
        period(32);
        doIteration("period[ 32]");
        period(64);
        doIteration("period[ 64]");
        period(128);
        doIteration("period[128]");
        period(258);
        doIteration("period[258]");
        period(512);
        doIteration("period[512]");

        period(1024);
        doIteration("period: 1024");
        period(1500);
        doIteration("period: 1500");
        period(2124);
        doIteration("period: 2124");
        period(4224);
        doIteration("period: 4224");
        period(8224);
        doIteration("period: 8224");

        period(16224);
        doIteration("period: 16224");
        period(32224);
        doIteration("period: 32224");
        period(64224);
        doIteration("period: 64224");
        period(124224);
        doIteration("period: 124224");
        period(254224);
        doIteration("period: 254224");
        period(512224);
        doIteration("period: 512224");
    }

    private static void randomAll() {
        random(2);
        doIteration("random[2]");
        random(3);
        doIteration("random[3]");
        random(4);
        doIteration("random[4]");
        random(5);
        doIteration("random[5]");
        random(6);
        doIteration("random[6]");
        random(7);
        doIteration("random[7]");
        random(8);
        doIteration("random[8]");
        random(9);
        doIteration("random[9]");
    }

    private static void randomSmall() {
        random(2);
        doIteration("random[2]");
        random(3);
        doIteration("random[3]");
        random(4);
        doIteration("random[4]");
        random(6);
        doIteration("random[6]");
        random(8);
        doIteration("random[8]");
    }

    private static void randomAllBig() {
        randomAll();
        random(10);
        doIteration("random[ 10]");
        random(12);
        doIteration("random[ 12]");
        random(14);
        doIteration("random[ 14]");
        random(16);
        doIteration("random[ 16]");
        random(32);
        doIteration("random[ 32]");
        random(64);
        doIteration("random[ 64]");
        random(128);
        doIteration("random[128]");
        random(258);
        doIteration("random[258]");
        random(512);
        doIteration("random[512]");
    }

    private static void staggerSmall() {
        stagger(1);
        doIteration("stagger:    1");
        stagger(2);
        doIteration("stagger:    2");
        stagger(4);
        doIteration("stagger:    4");
        stagger(8);
        doIteration("stagger:    8");
        stagger(16);
        doIteration("stagger:   16");
    }

    private static void staggerAll() {
        stagger(1);
        doIteration("stagger:    1");
        stagger(2);
        doIteration("stagger:    2");
        stagger(3);
        doIteration("stagger:    3");
        stagger(4);
        doIteration("stagger:    4");
        stagger(5);
        doIteration("stagger:    5");
        stagger(6);
        doIteration("stagger:    6");
        stagger(7);
        doIteration("stagger:    7");
        stagger(8);
        doIteration("stagger:    8");
        stagger(9);
        doIteration("stagger:    9");
        stagger(64);
        doIteration("stagger:   16");
        stagger(64);
        doIteration("stagger:   32");
        stagger(64);
        doIteration("stagger:   64");
        stagger(128);
        doIteration("stagger:  128");
        stagger(512);
        doIteration("stagger:  512");
        stagger(1024);
        doIteration("stagger: 1024");
    }

    private static void staggerAllBig() {
        staggerAll();

        stagger(16);
        doIteration("stagger: 16");
        stagger(33);
        doIteration("stagger: 33");
        stagger(62);
        doIteration("stagger: 62");
        stagger(63);
        doIteration("stagger: 63");
        stagger(65);
        doIteration("stagger: 65");
        stagger(66);
        doIteration("stagger: 66");
        stagger(67);
        doIteration("stagger: 67");
        stagger(68);
        doIteration("stagger: 68");
        stagger(256);
        doIteration("stagger: 256");
        stagger(1000);
        doIteration("stagger: 1000");
        stagger(2000);
        doIteration("stagger: 2000");
        stagger(3000);
        doIteration("stagger: 3000");
        stagger(4000);
        doIteration("stagger: 4000");
        stagger(5000);
        doIteration("stagger: 5000");
        stagger(8000);
        doIteration("stagger: 8000");
        stagger(16000);
        doIteration("stagger: 16000");
        stagger(64000);
        doIteration("stagger: 64000");
    }

    private static void latchAll() {
        latch(1);
        doIteration("latch: 1");
        latch(2);
        doIteration("latch: 2");
        latch(3);
        doIteration("latch: 3");
        latch(4);
        doIteration("latch: 4");
        latch(5);
        doIteration("latch: 5");
        latch(6);
        doIteration("latch: 6");
        latch(7);
        doIteration("latch: 7");
        latch(8);
        doIteration("latch: 8");
        latch(9);
        doIteration("latch: 9");
    }

    private static void latchAllBig() {
        latchAll();

        latch(16);
        doIteration("latch: 16");
        latch(32);
        doIteration("latch: 32");
        latch(33);
        doIteration("latch: 33");
        latch(62);
        doIteration("latch: 62");
        latch(63);
        doIteration("latch: 63");
        latch(64);
        doIteration("latch: 64");
        latch(65);
        doIteration("latch: 65");
        latch(66);
        doIteration("latch: 66");
        latch(67);
        doIteration("latch: 67");
        latch(68);
        doIteration("latch: 68");
        latch(128);
        doIteration("latch: 128");
        latch(256);
        doIteration("latch: 256");
    }

    private static void otherAll() {
        organPipes();
        doIteration("organPipes");
        staggerRandom();
        doIteration("stagger rnd");
        staggerFr(1);
        doIteration("staggerFr 1");
        mountain();
        doIteration("mountain");      // jdk9 fails
        reverseLong();
        doIteration("reverseLong");
        reverseShort();
        doIteration("reverseShort");

        ascMergeDesc();
        doIteration("ascMergeDesc");
        ascMergeDesc10();
        doIteration("ascMergeDesc10");
        plateauRe(1);
        doIteration("plateauRe 1");
        plateauFr(1);
        doIteration("plateauFr 1");
        periodReBa(2);
        doIteration("periodReBa: 2");
    }

    private static void monotonous() {
        equal();
        doIteration("equal");
        ascending();
        doIteration("ascending");
        descending();
        doIteration("descending");
    }

    private static void monotonousAll() {
        equal();
        doIteration("equal");
        ascending();
        doIteration("ascending");
        descending();
        doIteration("descending");

        equal(1);
        doIteration("equal 1");
        equal(2);
        doIteration("equal 2");
        equal(3);
        doIteration("equal 3");

        asc(1);
        doIteration("asc 1");
        asc(2);
        doIteration("asc 2");
        asc(4);
        doIteration("asc 4");
        asc(8);
        doIteration("asc 8");
        asc(32);
        doIteration("asc 32");
        asc(64);
        doIteration("asc 64");

        ascend(1);
        doIteration("ascend 1");
        ascend(2);
        doIteration("ascend 2");   // jdk9 fails
        ascend(4);
        doIteration("ascend 4");   // jdk9 fails
        ascend(8);
        doIteration("ascend 8");   // jdk9 fails
        ascend(64);
        doIteration("ascend 64");  // jdk9 fails

        ascending(1);
        doIteration("ascending 1");
        ascending(2);
        doIteration("ascending 2");
        ascending(4);
        doIteration("ascending 4");
        ascending(8);
        doIteration("ascending 8");
        ascending(32);
        doIteration("ascending 32");
        ascending(64);
        doIteration("ascending 64");
    }

    private static void plateauAll() {
        plateau(1);
        doIteration("plateau: 1");
        plateau(2);
        doIteration("plateau: 2");
        plateau(3);
        doIteration("plateau: 3");
        plateau(4);
        doIteration("plateau: 4");
        plateau(5);
        doIteration("plateau: 5");
        plateau(6);
        doIteration("plateau: 6");
        plateau(7);
        doIteration("plateau: 7");
        plateau(8);
        doIteration("plateau: 8");
        plateau(9);
        doIteration("plateau: 9");
    }

    private static void plateauAllBig() {
        plateauAll();

        plateau(10);
        doIteration("plateau  10");
        plateau(12);
        doIteration("plateau  12");
        plateau(14);
        doIteration("plateau  14");
        plateau(16);
        doIteration("plateau  16");
        plateau(32);
        doIteration("plateau  32");
        plateau(64);
        doIteration("plateau  64");
        plateau(128);
        doIteration("plateau 128");
        plateau(258);
        doIteration("plateau 258");
        plateau(512);
        doIteration("plateau 512");
    }

    private static void tableAll() {
        table(1);
        doIteration("table: 1");
        table(2);
        doIteration("table: 2");
        table(3);
        doIteration("table: 3");
        table(4);
        doIteration("table: 4");
        table(5);
        doIteration("table: 5");
        table(6);
        doIteration("table: 6");
        table(7);
        doIteration("table: 7");
        table(8);
        doIteration("table: 8");
        table(9);
        doIteration("table: 9");
    }

    private static void tableAllBig() {
        tableAll();

        table(10);
        doIteration("table  10");
        table(12);
        doIteration("table  12");
        table(14);
        doIteration("table  14");
        table(16);
        doIteration("table  16");
        table(32);
        doIteration("table  32");
        table(64);
        doIteration("table  64");
        table(128);
        doIteration("table 128");
        table(258);
        doIteration("table 258");
        table(512);
        doIteration("table 512");
    }

    private static void ringAll() {
        ring_A_EB_A();
        doIteration("ring_A_EB_A");
        ring_A_EB_D();
        doIteration("ring_A_EB_D");
        ring_A_EC_A();
        doIteration("ring_A_EC_A");
        ring_A_EC_D();
        doIteration("ring_A_EC_D");
        ring_A_ET_A();
        doIteration("ring_A_ET_A");
        ring_A_ET_D();
        doIteration("ring_A_ET_D");
        ring_D_EB_A();
        doIteration("ring_D_EB_A");
        ring_D_EB_D();
        doIteration("ring_D_EB_D");
        ring_D_EC_A();
        doIteration("ring_D_EC_A");
        ring_D_EC_D();
        doIteration("ring_D_EC_D");
        ring_D_ET_A();
        doIteration("ring_D_ET_A");
        ring_D_ET_D();
        doIteration("ring_D_ET_D");
    }

    private static void pearlAll() {
        pearl_E_1_LB();
        doIteration("pearl_E_1_LB");
        pearl_A_1_RB();
        doIteration("pearl_A_1_RB"); // last run - one element
        pearl_E_2_CB();
        doIteration("pearl_E_2_CB");
        pearl_E_1_CB();
        doIteration("pearl_E_1_CB");

        pearl_E_1_CB();
        doIteration("pearl_E_1_CB");
        pearl_E_1_LB();
        doIteration("pearl_E_1_LB");
        pearl_E_1_LT();
        doIteration("pearl_E_1_LT");
        pearl_E_1_RB();
        doIteration("pearl_E_1_RB");
        pearl_E_1_RT();
        doIteration("pearl_E_1_RT");
        pearl_E_2_CB();
        doIteration("pearl_E_2_CB");
        pearl_E_2_LB();
        doIteration("pearl_E_2_LB");
        pearl_E_2_LT();
        doIteration("pearl_E_2_LT");
        pearl_E_2_RB();
        doIteration("pearl_E_2_RB");
        pearl_E_2_RC();
        doIteration("pearl_E_2_RC");
        pearl_E_2_RD();
        doIteration("pearl_E_2_RD");
        pearl_E_2_RT();
        doIteration("pearl_E_2_RT");

        pearl_E_1_CT();
        doIteration("pearl_E_1_CT"); // jdk9 fails
        pearl_E_1_CT();
        doIteration("pearl_E_1_CT"); // jdk9 fails
        pearl_E_2_CC();
        doIteration("pearl_E_2_CC"); // jdk9 fails
        pearl_E_2_CD();
        doIteration("pearl_E_2_CD"); // jdk9 fails
        pearl_E_2_CT();
        doIteration("pearl_E_2_CT"); // jdk9 fails
        pearl_E_2_LC();
        doIteration("pearl_E_2_LC"); // jdk9 fails
        pearl_E_2_LD();
        doIteration("pearl_E_2_LD"); // jdk9 fails
        pearl_E_2_LZ();
        doIteration("pearl_E_2_LZ"); // jdk9 fails

        pearl_A_1_CB();
        doIteration("pearl_A_1_CB");
        pearl_A_1_CT();
        doIteration("pearl_A_1_CT");
        pearl_A_1_LB();
        doIteration("pearl_A_1_LB");
        pearl_A_1_LT();
        doIteration("pearl_A_1_LT");
        pearl_A_1_RB();
        doIteration("pearl_A_1_RB"); // last run - one element
        pearl_A_1_RT();
        doIteration("pearl_A_1_RT");
        pearl_A_2_CB();
        doIteration("pearl_A_2_CB");
        pearl_A_2_CT();
        doIteration("pearl_A_2_CT");
        pearl_A_2_LB();
        doIteration("pearl_A_2_LB");
        pearl_A_2_LT();
        doIteration("pearl_A_2_LT");
        pearl_A_2_RB();
        doIteration("pearl_A_2_RB"); // last run - one element
        pearl_A_2_RT();
        doIteration("pearl_A_2_RT");

        pearl_D_1_CB();
        doIteration("pearl_D_1_CB");
        pearl_D_1_CT();
        doIteration("pearl_D_1_CT");
        pearl_D_1_LB();
        doIteration("pearl_D_1_LB");
        pearl_D_1_LT();
        doIteration("pearl_D_1_LT");
        pearl_D_1_RB();
        doIteration("pearl_D_1_RB"); // last run - one element
        pearl_D_1_RT();
        doIteration("pearl_D_1_RT");
        pearl_D_2_CB();
        doIteration("pearl_D_2_CB");
        pearl_D_2_CT();
        doIteration("pearl_D_2_CT");
        pearl_D_2_LB();
        doIteration("pearl_D_2_LB");
        pearl_D_2_LT();
        doIteration("pearl_D_2_LT");
        pearl_D_2_RB();
        doIteration("pearl_D_2_RB"); // last run - one element
        pearl_D_2_RT();
        doIteration("pearl_D_2_RT");
    }

    private static void staggerRandom() {
        final int MAX_RUN_COUNT = 66;
        int count = MAX_RUN_COUNT - random.nextInt(10);
        int step = MAX_N / count;

        for (int i = 0, k = 0; i < count && k < MAX_N; i++, k++) {
            int init = random.nextInt(MAX_RUN_COUNT);
            golden[k] = init;
            int delta = random.nextInt(5) + 1;

            for (int s = 0; ++k < MAX_N && s < step; s++) {
                golden[k] = golden[k - 1] + delta;
            }
        }
    }

    private static void reverseLong() {
        ascending();
        reverse(golden, 0, golden.length - 1);
    }

    private static void reverseShort() {
        ascending();
        reverse(golden, 0, 2);
    }

    private static void mountain() { // jdk9 fails
        int step = MAX_N >>> 4;

        for (int i = 0, k = 0;; k++) {
            if (k % 3 == 0) {
                for (int j = 0; j < step; ++j) {
                    if (i == MAX_N) {
                        return;
                    }
                    golden[i++] = 0;
                }
            } else if (k % 3 == 1) {
                for (int j = 0; j < step; ++j) {
                    if (i == MAX_N) {
                        return;
                    }
                    golden[i++] = j;
                }
            } else if (k % 3 == 2) {
                for (int j = 0; j < step; ++j) {
                    if (i == MAX_N) {
                        return;
                    }
                    golden[i++] = -j;
                }
            }
        }
    }

    private static void random() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = random.nextInt();
        }
    }

    private static void randomForPair() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = random.nextInt();
        }
        golden[0] = Integer.MIN_VALUE;
    }

    private static void ascending() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i;
        }
    }

    private static void descending() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = MAX_N - i;
        }
    }

    private static void equal() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = 0;
        }
    }

    private static void ascMergeDesc() {
        ascending();

        for (int i = MAX_N / 2; i < MAX_N; ++i) {
            golden[i] = MAX_N * 2 - i;
        }
    }

    private static void ascMergeDesc10() {
        ascending();

        for (int i = MAX_N / 2; i < MAX_N; ++i) {
            golden[i] = MAX_N + 10 - i;
        }
    }

    // Ring
    private static void ring_A_EB_A() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = 0; i < k1; ++i) {
            golden[i] = k++;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = 0;
        }
        for (int i = k2, k = 0; i < MAX_N; ++i) {
            golden[i] = k++;
        }
    }

    private static void ring_A_EB_D() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = 0; i < k1; ++i) {
            golden[i] = k++;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = 0;
        }
        for (int i = k2, k = level; i < MAX_N; ++i) {
            golden[i] = k--;
        }
    }

    private static void ring_A_EC_A() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = 0; i < k1; ++i) {
            golden[i] = k++;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = level / 2;
        }
        for (int i = k2, k = 0; i < MAX_N; ++i) {
            golden[i] = k++;
        }
    }

    private static void ring_A_EC_D() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = 0; i < k1; ++i) {
            golden[i] = k++;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = level / 2;
        }
        for (int i = k2, k = level; i < MAX_N; ++i) {
            golden[i] = k--;
        }
    }

    private static void ring_A_ET_A() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = 0; i < k1; ++i) {
            golden[i] = k++;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = level;
        }
        for (int i = k2, k = 0; i < MAX_N; ++i) {
            golden[i] = k++;
        }
    }

    private static void ring_A_ET_D() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = 0; i < k1; ++i) {
            golden[i] = k++;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = level;
        }
        for (int i = k2, k = level; i < MAX_N; ++i) {
            golden[i] = k--;
        }
    }

    private static void ring_D_EB_A() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = level; i < k1; ++i) {
            golden[i] = k--;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = 0;
        }
        for (int i = k2, k = 0; i < MAX_N; ++i) {
            golden[i] = k++;
        }
    }

    private static void ring_D_EB_D() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = level; i < k1; ++i) {
            golden[i] = k--;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = 0;
        }
        for (int i = k2, k = level; i < MAX_N; ++i) {
            golden[i] = k--;
        }
    }

    private static void ring_D_EC_A() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = level; i < k1; ++i) {
            golden[i] = k--;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = level / 2;
        }
        for (int i = k2, k = 0; i < MAX_N; ++i) {
            golden[i] = k++;
        }
    }

    private static void ring_D_EC_D() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = level; i < k1; ++i) {
            golden[i] = k--;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = level / 2;
        }
        for (int i = k2, k = level; i < MAX_N; ++i) {
            golden[i] = k--;
        }
    }

    private static void ring_D_ET_A() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = level; i < k1; ++i) {
            golden[i] = k--;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = level;
        }
        for (int i = k2, k = 0; i < MAX_N; ++i) {
            golden[i] = k++;
        }
    }

    private static void ring_D_ET_D() {
        int k1 = MAX_N / 3;
        int k2 = MAX_N / 3 * 2;
        int level = MAX_N / 3;

        for (int i = 0, k = level; i < k1; ++i) {
            golden[i] = k--;
        }
        for (int i = k1; i < k2; ++i) {
            golden[i] = level;
        }
        for (int i = k2, k = level; i < MAX_N; ++i) {
            golden[i] = k--;
        }
    }

    // Pearl.equal
    private static void pearl_E_1_LT() {
        equal();
        golden[LEFT] = 10;
    }

    private static void pearl_E_2_LT() {
        equal();
        golden[LEFT] = 10;
        golden[MAX_N / 2] = 10;
    }

    private static void pearl_E_2_LC() {
        equal();
        golden[LEFT] = 10;
        golden[MAX_N / 2] = -10;
    }

    private static void pearl_E_2_LD() {
        equal();
        golden[LEFT] = -10;
        golden[MAX_N / 2] = 10;
    }

    private static void pearl_E_1_LB() {
        equal();
        golden[LEFT] = -10;
    }

    private static void pearl_E_2_LB() {
        equal();
        golden[LEFT] = -10;
        golden[MAX_N / 2] = -10;
    }

    private static void pearl_E_2_LZ() {
        equal();
        golden[LEFT] = LEFT - 10;
        golden[MAX_N / 2] = MAX_N / 2 - 10;
    }

    private static void pearl_E_1_CT() {
        equal();
        golden[MAX_N / 2] = 10;
    }

    private static void pearl_E_2_CT() {
        equal();
        golden[MAX_N / 3] = 10;
        golden[MAX_N / 3 * 2] = 10;
    }

    private static void pearl_E_2_CC() {
        equal();
        golden[MAX_N / 3] = 10;
        golden[MAX_N / 3 * 2] = -10;
    }

    private static void pearl_E_2_CD() {
        equal();
        golden[MAX_N / 3] = -10;
        golden[MAX_N / 3 * 2] = 10;
    }

    private static void pearl_E_1_CB() {
        equal();
        golden[MAX_N / 2] = -10;
    }

    private static void pearl_E_2_CB() {
        equal();
        golden[MAX_N / 3] = -10;
        golden[MAX_N / 3 * 2] = -10;
    }

    private static void pearl_E_1_RT() {
        equal();
        golden[RIGHT] = 10;
    }

    private static void pearl_E_2_RT() {
        equal();
        golden[RIGHT] = 10;
        golden[MAX_N / 2] = 10;
    }

    private static void pearl_E_2_RC() {
        equal();
        golden[RIGHT] = -10;
        golden[MAX_N / 2] = 10;
    }

    private static void pearl_E_2_RD() {
        equal();
        golden[RIGHT] = 10;
        golden[MAX_N / 2] = -10;
    }

    private static void pearl_E_1_RB() {
        equal();
        golden[RIGHT] = -10;
    }

    private static void pearl_E_2_RB() {
        equal();
        golden[RIGHT] = -10;
        golden[MAX_N / 2] = -10;
    }

    // Pearl.ascending
    private static void pearl_A_1_LT() {
        ascending();
        golden[LEFT] = LEFT + 10;
    }

    private static void pearl_A_2_LT() {
        ascending();
        golden[LEFT] = LEFT + 10;
        golden[MAX_N / 2] = MAX_N / 2 + 10;
    }

    private static void pearl_A_1_LB() {
        ascending();
        golden[LEFT] = LEFT - 10;
    }

    private static void pearl_A_2_LB() {
        ascending();
        golden[LEFT] = LEFT - 10;
        golden[MAX_N / 2] = MAX_N / 2 - 10;
    }

    private static void pearl_A_1_CT() {
        ascending();
        golden[MAX_N / 2] = MAX_N / 2 + 10;
    }

    private static void pearl_A_2_CT() {
        ascending();
        golden[MAX_N / 3] = MAX_N / 3 + 10;
        golden[MAX_N / 3 * 2] = MAX_N / 3 * 2 + 10;
    }

    private static void pearl_A_1_CB() {
        ascending();
        golden[MAX_N / 2] = MAX_N / 2 - 10;
    }

    private static void pearl_A_2_CB() {
        ascending();
        golden[MAX_N / 3] = MAX_N / 3 - 10;
        golden[MAX_N / 3 * 2] = MAX_N / 3 * 2 - 10;
    }

    private static void pearl_A_1_RT() {
        ascending();
        golden[RIGHT] = RIGHT + 10;
    }

    private static void pearl_A_2_RT() {
        ascending();
        golden[RIGHT] = RIGHT + 10;
        golden[MAX_N / 2] = MAX_N / 2 + 10;
    }

    private static void pearl_A_1_RB() {
        ascending();
        golden[RIGHT] = RIGHT - 10;
    }

    private static void pearl_A_2_RB() {
        ascending();
        golden[RIGHT] = RIGHT - 10;
        golden[MAX_N / 2] = MAX_N / 2 - 10;
    }

    // Pearl.descending
    private static void pearl_D_1_LT() {
        descending();
        golden[LEFT] = LEFT + 10;
    }

    private static void pearl_D_2_LT() {
        descending();
        golden[LEFT] = LEFT + 10;
        golden[MAX_N / 2] = MAX_N / 2 + 10;
    }

    private static void pearl_D_1_LB() {
        descending();
        golden[LEFT] = LEFT - 10;
    }

    private static void pearl_D_2_LB() {
        descending();
        golden[LEFT] = LEFT - 10;
        golden[MAX_N / 2] = MAX_N / 2 - 10;
    }

    private static void pearl_D_1_CT() {
        descending();
        golden[MAX_N / 2] = MAX_N / 2 + 10;
    }

    private static void pearl_D_2_CT() {
        descending();
        golden[MAX_N / 3] = MAX_N / 3 + 10;
        golden[MAX_N / 3 * 2] = MAX_N / 3 * 2 + 10;
    }

    private static void pearl_D_1_CB() {
        descending();
        golden[MAX_N / 2] = MAX_N / 2 - 10;
    }

    private static void pearl_D_2_CB() {
        descending();
        golden[MAX_N / 3] = MAX_N / 3 - 10;
        golden[MAX_N / 3 * 2] = MAX_N / 3 * 2 - 10;
    }

    private static void pearl_D_1_RT() {
        descending();
        golden[RIGHT] = RIGHT + 10;
    }

    private static void pearl_D_2_RT() {
        descending();
        golden[RIGHT] = RIGHT + 10;
        golden[MAX_N / 2] = MAX_N / 2 + 10;
    }

    private static void pearl_D_1_RB() {
        descending();
        golden[RIGHT] = RIGHT - 10;
    }

    private static void pearl_D_2_RB() {
        descending();
        golden[RIGHT] = RIGHT - 10;
        golden[MAX_N / 2] = MAX_N / 2 - 10;
    }

    private static void killer() {
//      new DPQKiller().kill(golden);
    }

    private static void permutation() {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i;
        }
        for (int i = MAX_N; i > 1; i--) {
            int k = random.nextInt(i);
            int t = golden[i - 1];
            golden[i - 1] = golden[k];
            golden[k] = t;
        }
    }

    private static void plateauFr(int m) {
        plateau(m);
        reverse(golden, 0, golden.length / 2);
    }

    private static void plateauRe(int m) {
        plateau(m);
        reverse(golden, 0, golden.length);
    }

    private static void reverse() {
        reverse(golden, 0, golden.length);
    }

    private static void reverse(int[] a, int start, int hi) {
        hi--;
        while (start < hi) {
            int tmp = a[start];
            a[start++] = a[hi];
            a[hi--] = tmp;
        }
    }

    private static void asc(int m) {
        for (int i = 0; i < MAX_N - m; ++i) {
            golden[i] = i;
        }
        for (int k = 0, i = MAX_N - m; i < MAX_N; i++, k++) {
            if (i < 0) {
                i = 0;
            }
            golden[i] = k;
        }
    }

    private static void ascend(int m) {
        ascending();

        for (int i = MAX_N - 1, j = 1; j <= m; j++, i--) {
            if (i < 0) {
                i = 0;
            }
            golden[i] = 0;
        }
    }

    private static void ascending(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i;
        }
        for (int i = MAX_N - m; i < MAX_N; ++i) {
            if (i < 0) {
                i = 0;
            }
            golden[i] = random.nextInt(MAX_N);
        }
    }

    private static void equal(int m) {
        equal();

        for (int i = MAX_N - m; i < MAX_N; ++i) {
            if (i < 0) {
                i = 0;
            }
            golden[i] = -1;
        }
    }

    private static void plateau(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = Math.min(i, m);
        }
    }

    private static void table(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = Math.max(i, m);
        }
    }

    private static void shuffle() {
        for (int i = 0, j = 0, k = 1; i < MAX_N; ++i) {
            golden[i] = random.nextBoolean() ? (j += 2) : (k += 2);
        }
    }

    private static void shuffle(int m) {
        for (int i = 0, j = 0, k = 1; i < MAX_N; ++i) {
            golden[i] = random.nextInt(m) > 0 ? (j += 2) : (k += 2);
        }
    }

    private static void shuffleDither(int m) {
        for (int i = 0, j = 0, k = 1; i < MAX_N; ++i) {
            golden[i] = random.nextInt(m) > 0 ? (j += 2) : (k += 2);
        }

        for (int i = 0; i < golden.length; ++i) {
            golden[i] += (i % 5);
        }
    }

    private static void staggerFr(int m) {
        stagger(m);
        reverse(golden, 0, golden.length / 2);
    }

    private static void stagger(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = (i * m + i) % MAX_N;
        }
    }

    private static void staggerDither(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = (i * m + i) % MAX_N;
            golden[i] += (i % 5);
        }
    }

    private static void latch(int m) {
        int max = MAX_N / m;
        if (max == 0) {
            max = 1;
        }

        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i % max;
        }
    }

    private static void stagger2T(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i;
        }
        for (int i = MAX_N - m, k = 0; i < MAX_N; ++i) {
            golden[i] = ++k;
        }
    }

    private static void period(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i % m;
        }
    }

    private static void periodReBa(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = i % m;
        }
        reverse(golden, golden.length / 2, golden.length);
    }

    private static void random(int m) {
        for (int i = 0; i < MAX_N; ++i) {
            golden[i] = random.nextInt(m);
        }
    }

    private static void organPipesAll() {
        organPipes();
        doIteration("organPipes");
        organPipes(2);
        doIteration("organPipes[2]");
        organPipes(3);
        doIteration("organPipes[3]");
        organPipes(4);
        doIteration("organPipes[4]");
        organPipes(5);
        doIteration("organPipes[5]");
        organPipes(6);
        doIteration("organPipes[6]");
        organPipes(7);
        doIteration("organPipes[7]");
        organPipes(8);
        doIteration("organPipes[8]");
        organPipes(9);
        doIteration("organPipes[9]");
    }

    private static void organPipes(int m) {
        period(MAX_N / m);
    }

    private static void organPipes() {
        int middle = MAX_N / 2;
        int k = 0;

        for (int i = 0; i < middle; ++i) {
            golden[i] = k++;
        }
        for (int i = middle; i < MAX_N; ++i) {
            golden[i] = --k;
        }
    }

    public static void outArr(String name, int[] a, int off) {
        out("\n" + name);
        outArr(a, off);
    }

    public static void outArr(int[] a, int off) {
        outArr(a, off, a.length - 1);
    }

    public static void outArr(int[] a, int left, int right) {
        for (int i = left; i <= right; ++i) {
            System.out.print(a[i] + (i == right ? "" : ", "));
        }
        System.out.println();
    }

    public static void outArr(int[] a, int left, int high, boolean b) {
        for (int i = 0; i < a.length; ++i) {
            System.out.print((i == left || i == high - 1 ? "(" : "") + a[i] + (i == left || i == high - 1 ? ")" : "") + (i == high - 1 ? "" : ", "));
        }
        System.out.println();
    }

    public static void prepareGolden() {
        for (int i = 0; i < LEFT; ++i) {
            golden[i] = Integer.MAX_VALUE;
        }
        for (int i = MAX_N - 1; i > RIGHT; i--) {
            golden[i] = Integer.MIN_VALUE;
        }
    }

    public static void check(String name, int[] ref, int[] test, int off, int left, int right) {
        if (!"none".equalsIgnoreCase(name)) {
            check(name, ref, test, off, left, right, false);
        }
    }

    public static void check(String name, int[] ref, int[] test, int off, int left, int right, boolean show) {
        String message;

        for (int i = 0; i < LEFT; ++i) {
            if (ref[i] != Integer.MAX_VALUE) {
                message = "\n\n" + name + ": Left.1 part is modified. RandomInit: " + randomInit + "\n";
                System.out.println(message);
                System.err.println(message);
                throw new RuntimeException(message);
            }
            if (test[off + i] != Integer.MAX_VALUE) {
                message = "\n\n" + name + ": Left.2 part is modified. RandomInit: " + randomInit + "\n";
                System.out.println(message);
                System.err.println(message);
                throw new RuntimeException(message);
            }
        }
        for (int i = MAX_N - 1; i > RIGHT; i--) {
            if (ref[i] != Integer.MIN_VALUE) {
                message = "\n\n" + name + ": Right.1 part is modified. RandomInit: " + randomInit + "\n";
                System.out.println(message);
                System.err.println(message);
                throw new RuntimeException(message);
            }
            if (test[off + i] != Integer.MIN_VALUE) {
                message = "\n\n" + name + ": Right.2 part is modified. RandomInit: " + randomInit + "\n";
                System.out.println(message);
                System.err.println(message);
                throw new RuntimeException(message);
            }
        }
        for (int i = left; i < right; ++i) {
            if (test[off + i] > test[off + i + 1]) {
                if (show) {
                    outArr(test, off + left, off + right);
                }
                message = "\n\n" + name + ": Array is not sorted at index " + (off + i) + ": " + test[off + i] + " " + test[off + i + 1] + ". RandomInit: " + randomInit;
                System.out.println(message);
                System.err.println(message);
                throw new RuntimeException(message);
            }
        }
        int plusCheckSum1 = 0;
        int plusCheckSum2 = 0;
        int xorCheckSum1 = 0;
        int xorCheckSum2 = 0;

        for (int i = left; i <= right; ++i) {
            plusCheckSum1 += ref[i];
            plusCheckSum2 += test[off + i];
            xorCheckSum1 ^= ref[i];
            xorCheckSum2 ^= test[off + i];
        }
        if (plusCheckSum1 != plusCheckSum2) {
            if (show) {
                outArr(test, off, right - left + off + 1);
                outArr(ref, 0);
            }
            message = "\n\n" + name + ": Array is not sorted correctly [+]. RandomInit: " + randomInit;
            System.out.println(message);
            System.err.println(message);
            throw new RuntimeException(message);
        }
        if (xorCheckSum1 != xorCheckSum2) {
            if (show) {
                outArr(test, off, right - left + off + 1);
                outArr(ref, 0);
            }
            message = "\n\n" + name + ": Array is not sorted correctly [^]. RandomInit: " + randomInit;
            System.out.println(message);
            System.err.println(message);
            throw new RuntimeException(message);
        }
    }

    public static void out() {
        if (doPrint && !WARMUP) {
            System.out.println();
        }
    }

    public static void out(Object object) {
        if (doPrint && !WARMUP) {
            System.out.println(object);
        }
    }

    private interface Sortable {

        void sortArray(int[] a, int off);
    }

//  private static final int MAX_N = 20;
// /private static final int COUNT = 12000000; // h
//  private static final int COUNT = 14000000; // w
//  private static final int MAX_N = 30;
//  private static final int COUNT =  9000000; // h
//  private static final int COUNT = 14000000; // w
//  private static final int MAX_N = 50;
//  private static final int COUNT =  5000000; // h
//  private static final int COUNT = 14000000; // w
//  private static final int MAX_N = 65;
//  private static final int COUNT =  4000000; // h
//  private static final int COUNT = 14000000; // w
//  private static final int MAX_N = 80;
//  private static final int COUNT =  3000000; // h
//  private static final int COUNT = 14000000; // w
//  private static final int MAX_N = 95;
//  private static final int COUNT = 2500000;  // h
//  private static final int COUNT = 8000000;  // w
//  private static final int MAX_N = 100;
//  private static final int COUNT = 2500000;  // h
//  private static final int COUNT = 7000000;  // w
//  private static final int MAX_N = 113;
//  private static final int COUNT = 2500000;  // h
//  private static final int COUNT = 7000000;  // w
//  private static final int MAX_N = 128;
//  private static final int COUNT = 2500000;  // h
//  private static final int COUNT = 5000000;  // w
//  private static final int MAX_N = 150;
//  private static final int COUNT = 1800000;  // h
//  private static final int COUNT = 4000000;  // w
//  private static final int MAX_N = 175;
//  private static final int COUNT = 1000000;  // h
//  private static final int COUNT = 3000000;  // w
//  private static final int MAX_N = 193;
//  private static final int COUNT =  800000;  // h
//  private static final int COUNT = 2000000;  // w
//  private static final int MAX_N = 200;
//  private static final int COUNT =  800000;  // h
//  private static final int COUNT = 2000000;  // w
//  private static final int MAX_N = 500;
//  private static final int COUNT = 250000;   // h
//  private static final int COUNT = 500000;   // w
//  private static final int MAX_N = 1000;
//  private static final int COUNT = 500000;
//  private static final int MAX_N = 1350;
//  private static final int COUNT = 300000;
//  private static final int MAX_N = 1500;
//  private static final int COUNT = 400000;
//  private static final int MAX_N = 2000;
//  private static final int COUNT =  70000;   // h
//  private static final int COUNT = 120000;   // w
//  private static final int MAX_N = 8000;
//  private static final int COUNT =  20000;   // h
//  private static final int MAX_N = 128 << 10;
//  private static final int COUNT = 1000;     // h
//  private static final int MAX_N = 256 << 10;
//  private static final int COUNT = 450;      // h
//  private static final int COUNT = 550;      // w
//  private static final int MAX_N = (4 << 10) + 20;
//  private static final int COUNT = 9000;     // h
//  private static final int MAX_N = 50000;
//  private static final int COUNT = 3000;
//  private static final int MAX_N = 100000;
//  private static final int COUNT = 1300;
//  private static final int MAX_N = 256000;
//  private static final int COUNT = 5000;
//  private static final int MAX_N = 1 << 26;
//  private static final int COUNT = 15;
//  private static final int MAX_N = 500000;
//  private static final int COUNT = 250;
//  private static final int MAX_N = 4000000;
//  private static final int COUNT = 20;
//  private static final int MAX_N = 2000000;
//  private static final int COUNT = 50;
//  private static final int COUNT =  1;
//    private static final int MAX_N = 150;
//    private static final int COUNT = 2000000;
    /*
  private static final int MAX_N = 1000;
  private static final int COUNT = 500000;
     */
 /*
  private static final int MAX_N = 30000;   // 30K
  private static final int COUNT = 10000;
     */
 /*
    private static final int MAX_N = 1000000; // 1M
    private static final int COUNT = 150;
     */

 /* short test */
    private static final int WARM_COUNT = 50;
    private static final int MAX_N = 100000;

    private static final int COUNT = (TRACING) ? 10 : (WARM_COUNT * 10);

    private static int parallelism = 0;
//  private static int parallelism = ForkJoinPool.getCommonPoolParallelism();

    private static boolean WARMUP;
    private static int randomInit;

    private static double time1;
    private static double time2;
    private static double time3;
    private static double totalTime1;
    private static double totalTime2;
    private static double totalTime3;

    private static final int LEFT = 0;
    private static final int HIGH = MAX_N - LEFT;
    private static final int RIGHT = HIGH - 1;
    private static final int SIZE = HIGH - LEFT;
    private static final Random random = new Random(777);

    private static final int[] golden = new int[MAX_N];

    // use always the same arrays to compare same memory alignment (no clone / gc overhead)
    private static final int[] test_data = new int[MAX_N + ALIGN_MAX];

    private static final boolean doPrint = true;

    private static final boolean CHECK = true || TRACING;
}
