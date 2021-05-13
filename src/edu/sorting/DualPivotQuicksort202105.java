/*
 * Copyright (c) 2009, 2021, Oracle and/or its affiliates. All rights reserved.
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
package edu.sorting;
// package java.util; // TODO

import java.util.Arrays; // TODO
// import java.util.concurrent.CountedCompleter;
// import java.util.concurrent.RecursiveTask;

/**
 * This class implements powerful and fully optimized versions, both
 * sequential and parallel, of the Dual-Pivot Quicksort algorithm by
 * Vladimir Yaroslavskiy, Jon Bentley and Josh Bloch. This algorithm
 * offers O(n log(n)) performance on all data sets, and is typically
 * faster than traditional (one-pivot) Quicksort implementations.
 *
 * There are also additional algorithms, invoked from the Dual-Pivot
 * Quicksort, such as mixed insertion sort, merging of runs and heap
 * sort, radix sort, counting sort and parallel merge sort.
 *
 * @author Vladimir Yaroslavskiy
 * @author Jon Bentley
 * @author Josh Bloch
 * @author Doug Lea
 *
 * @version 2021.05.06
 *
 * @since 1.7 * 14 & 17
 */
/* Vladimir's version: final DualPivotQuicksort_6K_B.java */
public final class DualPivotQuicksort202105 implements wildinter.net.mergesort.Sorter {

    private final static boolean TRACE = false;
    private final static boolean TRACE_ALLOC = false;

    public final static wildinter.net.mergesort.Sorter INSTANCE = new DualPivotQuicksort202105();

    /**
     * Prevents instantiation.
     */
    private DualPivotQuicksort202105() {
    }

    // avoid alloc
    private final Sorter sorter = new Sorter();

    @Override
    public void sort(final int[] A, final int low, final int high) {
        // preallocation of temporary arrays into custom Sorter class
        sorter.initBuffers(high - low + 1, low);

        sort(sorter, A, 0, low, high + 1); // exclusive
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /*
    From Vladimir's source code:
     */
    /**
     * Max array size to use mixed insertion sort.
     */
    private static final int MAX_MIXED_INSERTION_SORT_SIZE = 65;

    /**
     * Max array size to use insertion sort.
     */
    private static final int MAX_INSERTION_SORT_SIZE = 44;

    /**
     * Min array size to try merging of runs.
     */
    private static final int MIN_TRY_MERGE_SIZE = 4 << 10;

    /**
     * Min size of the first run to continue with scanning.
     */
    private static final int MIN_FIRST_RUN_SIZE = 16;

    /**
     * Min factor for the first runs to continue scanning.
     */
    private static final int MIN_FIRST_RUNS_FACTOR = 7;

    /**
     * Max capacity of the index array for tracking runs.
     */
    private static final int MAX_RUN_CAPACITY = 5 << 10;

    /**
     * Min array size to use radix sort.
     */
    private static final int MIN_RADIX_SORT_SIZE = 6 << 10;

    /**
     * Threshold of mixed insertion sort is increased by this value.
     */
    private static final int DEPTH = 3 << 1;

    /**
     * Min depth to invoke radix sort.
     */
    private static final int MIN_RADIX_SORT_DEPTH = DEPTH << 2;

    /**
     * Max recursive partitioning depth before using heap sort.
     */
    private static final int MAX_RECURSION_DEPTH = 64 * DEPTH;

    /**
     * Sorts the specified array using the Dual-Pivot Quicksort and/or
     * other sorts in special cases, possibly with parallel partitions.
     *
     * @param sorter parallel context
     * @param a the array to be sorted
     * @param bits the combination of recursion depth and bit flag, where
     *        the right bit "0" indicates that array is the leftmost part
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    static void sort(Sorter sorter, int[] a, int bits, int low, int high) {
        while (true) {
            if (TRACE) {
                System.out.println("DPQSsort[" + a.length + "] bits = " + bits + " in [" + low + " - " + high + "]");
            }

            int end = high - 1, size = high - low;

            /*
             * Run mixed insertion sort on small non-leftmost parts.
             */
            if (size < MAX_MIXED_INSERTION_SORT_SIZE + bits && (bits & 1) > 0) {
                mixedInsertionSort(a, low, high - 3 * ((size >> 5) << 3), high);
                return;
            }

            /*
             * Invoke insertion sort on small leftmost part.
             */
            if (size < MAX_INSERTION_SORT_SIZE) {
                insertionSort(a, low, high);
                return;
            }

            /*
             * Check if the whole array or large non-leftmost
             * parts are nearly sorted and then merge runs.
             */
            if ((bits == 0 || size > MIN_TRY_MERGE_SIZE && (bits & 1) > 0)
                    && tryMergeRuns(sorter, a, low, size)) {
                return;
            }

            /*
             * Switch to heap sort, if execution
             * time is becoming quadratic.
             */
            if ((bits += DEPTH) > MAX_RECURSION_DEPTH) {
                heapSort(a, low, high);
                return;
            }

            /*
             * Use an inexpensive approximation of the golden ratio
             * to select five sample elements and determine pivots.
             */
            int step = (size >> 3) * 3 + 3;

            /*
             * Five elements around (and including) the central element
             * will be used for pivot selection as described below. The
             * unequal choice of spacing these elements was empirically
             * determined to work well on a wide variety of inputs.
             */
            int e1 = low + step;
            int e5 = end - step;
            int e3 = (e1 + e5) >>> 1;
            int e2 = (e1 + e3) >>> 1;
            int e4 = (e3 + e5) >>> 1;
            int a3 = a[e3];

            /*
             * Sort these elements in place by the combination
             * of 4-element sorting network and insertion sort.
             *
             *    5 ------o-----------o------------
             *            |           |
             *    4 ------|-----o-----o-----o------
             *            |     |           |
             *    2 ------o-----|-----o-----o------
             *                  |     |
             *    1 ------------o-----o------------
             */
            if (a[e5] < a[e2]) { int t = a[e5]; a[e5] = a[e2]; a[e2] = t; }
            if (a[e4] < a[e1]) { int t = a[e4]; a[e4] = a[e1]; a[e1] = t; }
            if (a[e5] < a[e4]) { int t = a[e5]; a[e5] = a[e4]; a[e4] = t; }
            if (a[e2] < a[e1]) { int t = a[e2]; a[e2] = a[e1]; a[e1] = t; }
            if (a[e4] < a[e2]) { int t = a[e4]; a[e4] = a[e2]; a[e2] = t; }

            if (a3 < a[e2]) {
                if (a3 < a[e1]) {
                    a[e3] = a[e2]; a[e2] = a[e1]; a[e1] = a3;
                } else {
                    a[e3] = a[e2]; a[e2] = a3;
                }
            } else if (a3 > a[e4]) {
                if (a3 > a[e5]) {
                    a[e3] = a[e4]; a[e4] = a[e5]; a[e5] = a3;
                } else {
                    a[e3] = a[e4]; a[e4] = a3;
                }
            }

            // Pointers
            int lower = low; // The index of the last element of the left part
            int upper = end; // The index of the first element of the right part

            /*
             * Partitioning with two pivots in case of different elements.
             */
            if (a[e1] < a[e2] && a[e2] < a[e3] && a[e3] < a[e4] && a[e4] < a[e5]) {

                if (TRACE) {
                    // Challenge: debug heuristic to estimate if almost sorted vs random data:
                    System.out.println("Challenge: ");
                    System.out.println("1: i[" + e1 + "] => [" + a[e1] + "]");
                    System.out.println("2: i[" + e2 + "] => [" + a[e2] + "]");
                    System.out.println("3: i[" + e3 + "] => [" + a[e3] + "]");
                    System.out.println("4: i[" + e4 + "] => [" + a[e4] + "]");
                    System.out.println("5: i[" + e5 + "] => [" + a[e5] + "]");

                    System.out.println("5-1: d = [" + (a[e5] - a[e1]) + "]");

                    System.out.println("1-2: d = [" + (a[e2] - a[e1]) + "]");
                    System.out.println("2-3: d = [" + (a[e3] - a[e2]) + "]");
                    System.out.println("3-4: d = [" + (a[e4] - a[e3]) + "]");
                    System.out.println("4-5: d = [" + (a[e5] - a[e4]) + "]");
                }
                
                /*
                 * Invoke radix sort on large array.
                 */
                // LBO: use radixSort for sequential sort (no parallelism):
                if ((true /* || sorter == null*/ || bits > MIN_RADIX_SORT_DEPTH) && size > MIN_RADIX_SORT_SIZE) {
                    radixSort(sorter, a, low, high);
                    return;
                }

                /*
                 * Use the first and fifth of the five sorted elements as
                 * the pivots. These values are inexpensive approximation
                 * of tertiles. Note, that pivot1 < pivot2.
                 */
                int pivot1 = a[e1];
                int pivot2 = a[e5];

                /*
                 * The first and the last elements to be sorted are moved
                 * to the locations formerly occupied by the pivots. When
                 * partitioning is completed, the pivots are swapped back
                 * into their final positions, and excluded from the next
                 * subsequent sorting.
                 */
                a[e1] = a[lower];
                a[e5] = a[upper];

                /*
                 * Skip elements, which are less or greater than the pivots.
                 */
                while (a[++lower] < pivot1);
                while (a[--upper] > pivot2);

                /*
                 * Backward 3-interval partitioning
                 *
                 *   left part                 central part          right part
                 * +------------------------------------------------------------+
                 * |  < pivot1  |   ?   |  pivot1 <= && <= pivot2  |  > pivot2  |
                 * +------------------------------------------------------------+
                 *             ^       ^                            ^
                 *             |       |                            |
                 *           lower     k                          upper
                 *
                 * Invariants:
                 *
                 *              all in (low, lower] < pivot1
                 *    pivot1 <= all in (k, upper)  <= pivot2
                 *              all in [upper, end) > pivot2
                 *
                 * Pointer k is the last index of ?-part
                 */
                for (int unused = --lower, k = ++upper; --k > lower; ) {
                    int ak = a[k];

                    if (ak < pivot1) { // Move a[k] to the left side
                        while (lower < k) {
                            if (a[++lower] >= pivot1) {
                                if (a[lower] > pivot2) {
                                    a[k] = a[--upper];
                                    a[upper] = a[lower];
                                } else {
                                    a[k] = a[lower];
                                }
                                a[lower] = ak;
                                break;
                            }
                        }
                    } else if (ak > pivot2) { // Move a[k] to the right side
                        a[k] = a[--upper];
                        a[upper] = ak;
                    }
                }

                /*
                 * Swap the pivots into their final positions.
                 */
                a[low] = a[lower]; a[lower] = pivot1;
                a[end] = a[upper]; a[upper] = pivot2;

                /*
                 * Sort non-left parts recursively (possibly in parallel),
                 * excluding known pivots.
                 */
/*
                if (size > MIN_PARALLEL_SORT_SIZE && sorter != null) {
                    sorter.forkSorter(bits | 1, lower + 1, upper);
                    sorter.forkSorter(bits | 1, upper + 1, high);
                } else {
*/
                    sort(sorter, a, bits | 1, lower + 1, upper);
                    sort(sorter, a, bits | 1, upper + 1, high);
//                }

            } else { // Use single pivot in case of many equal elements

                /*
                 * Use the third of the five sorted elements as the pivot.
                 * This value is inexpensive approximation of the median.
                 */
                int pivot = a[e3];

                /*
                 * The first element to be sorted is moved to the
                 * location formerly occupied by the pivot. After
                 * completion of partitioning the pivot is swapped
                 * back into its final position, and excluded from
                 * the next subsequent sorting.
                 */
                a[e3] = a[lower];

                /*
                 * Traditional 3-way (Dutch National Flag) partitioning
                 *
                 *   left part                 central part    right part
                 * +------------------------------------------------------+
                 * |   < pivot   |     ?     |   == pivot   |   > pivot   |
                 * +------------------------------------------------------+
                 *              ^           ^                ^
                 *              |           |                |
                 *            lower         k              upper
                 *
                 * Invariants:
                 *
                 *   all in (low, lower] < pivot
                 *   all in (k, upper)  == pivot
                 *   all in [upper, end] > pivot
                 *
                 * Pointer k is the last index of ?-part
                 */
                for (int k = ++upper; --k > lower; ) {
                    int ak = a[k];

                    if (ak != pivot) {
                        a[k] = pivot;

                        if (ak < pivot) { // Move a[k] to the left side
                            while (a[++lower] < pivot);

                            if (a[lower] > pivot) {
                                a[--upper] = a[lower];
                            }
                            a[lower] = ak;
                        } else { // ak > pivot - Move a[k] to the right side
                            a[--upper] = ak;
                        }
                    }
                }

                /*
                 * Swap the pivot into its final position.
                 */
                a[low] = a[lower]; a[lower] = pivot;

                /*
                 * Sort the right part (possibly in parallel), excluding
                 * known pivot. All elements from the central part are
                 * equal and therefore already sorted.
                 */
/*
                if (size > MIN_PARALLEL_SORT_SIZE && sorter != null) {
                    sorter.forkSorter(bits | 1, upper, high);
                } else {
*/
                    sort(sorter, a, bits | 1, upper, high);
//                }
            }
            high = lower; // Iterate along the left part
        }
    }

    /**
     * Sorts the specified range of the array using mixed insertion sort.
     *
     * Mixed insertion sort is combination of simple insertion sort,
     * pin insertion sort and pair insertion sort.
     *
     * In the context of Dual-Pivot Quicksort, the pivot element
     * from the left part plays the role of sentinel, because it
     * is less than any elements from the given part. Therefore,
     * expensive check of the left range can be skipped on each
     * iteration unless it is the leftmost call.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param end the index of the last element for simple insertion sort
     * @param high the index of the last element, exclusive, to be sorted
     */
    private static void mixedInsertionSort(int[] a, int low, int end, int high) {
        if (TRACE) {
            System.out.println("mixedInsertionSort[" + a.length + "] in [" + low + " - " + high + "]");
        }
        if (end == high) {

            /*
             * Invoke simple insertion sort on tiny array.
             */
            for (int i; ++low < end; ) {
                int ai = a[i = low];

                while (ai < a[--i]) {
                    a[i + 1] = a[i];
                }
                a[i + 1] = ai;
            }
        } else {

            /*
             * Start with pin insertion sort on small part.
             *
             * Pin insertion sort is extended simple insertion sort.
             * The main idea of this sort is to put elements larger
             * than an element called pin to the end of array (the
             * proper area for such elements). It avoids expensive
             * movements of these elements through the whole array.
             */
            int pin = a[end];

            for (int i, p = high; ++low < end; ) {
                int ai = a[i = low];

                if (ai < a[i - 1]) { // Small element

                    /*
                     * Insert small element into sorted part.
                     */
                    a[i] = a[--i];

                    while (ai < a[--i]) {
                        a[i + 1] = a[i];
                    }
                    a[i + 1] = ai;

                } else if (p > i && ai > pin) { // Large element

                    /*
                     * Find element smaller than pin.
                     */
                    while (a[--p] > pin);

                    /*
                     * Swap it with large element.
                     */
                    if (p > i) {
                        ai = a[p];
                        a[p] = a[i];
                    }

                    /*
                     * Insert small element into sorted part.
                     */
                    while (ai < a[--i]) {
                        a[i + 1] = a[i];
                    }
                    a[i + 1] = ai;
                }
            }

            /*
             * Continue with pair insertion sort on remain part.
             */
            for (int i; low < high; ++low) {
                int a1 = a[i = low], a2 = a[++low];

                /*
                 * Insert two elements per iteration: at first, insert the
                 * larger element and then insert the smaller element, but
                 * from the position where the larger element was inserted.
                 */
                if (a1 > a2) {

                    while (a1 < a[--i]) {
                        a[i + 2] = a[i];
                    }
                    a[++i + 1] = a1;

                    while (a2 < a[--i]) {
                        a[i + 1] = a[i];
                    }
                    a[i + 1] = a2;

                } else if (a1 < a[i - 1]) {

                    while (a2 < a[--i]) {
                        a[i + 2] = a[i];
                    }
                    a[++i + 1] = a2;

                    while (a1 < a[--i]) {
                        a[i + 1] = a[i];
                    }
                    a[i + 1] = a1;
                }
            }
        }
    }

    /**
     * Sorts the specified range of the array using insertion sort.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private static void insertionSort(int[] a, int low, int high) {
        if (TRACE) {
            System.out.println("insertionSort[" + a.length + "] in [" + low + " - " + high + "]");
        }
        for (int i, k = low; ++k < high; ) {
            int ai = a[i = k];

            if (ai < a[i - 1]) {
                while (--i >= low && ai < a[i]) {
                    a[i + 1] = a[i];
                }
                a[i + 1] = ai;
            }
        }
    }

    /**
     * Sorts the specified range of the array using heap sort.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private static void heapSort(int[] a, int low, int high) {
        if (TRACE) {
            System.out.println("heapSort[" + a.length + "] in [" + low + " - " + high + "]");
        }
        for (int k = (low + high) >>> 1; k > low; ) {
            pushDown(a, --k, a[k], low, high);
        }
        while (--high > low) {
            int max = a[low];
            pushDown(a, low, a[high], low, high);
            a[high] = max;
        }
    }

    /**
     * Pushes specified element down during heap sort.
     *
     * @param a the given array
     * @param p the start index
     * @param value the given element
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private static void pushDown(int[] a, int p, int value, int low, int high) {
        for (int k ;; a[p] = a[p = k]) {
            k = (p << 1) - low + 2; // Index of the right child

            if (k > high) {
                break;
            }
            if (k == high || a[k] < a[k - 1]) {
                --k;
            }
            if (a[k] <= value) {
                break;
            }
        }
        a[p] = value;
    }

    /**
     * Sorts the specified range of the array using radix sort.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     * @return true if finally sorted, false otherwise
     */
    static boolean radixSort(Sorter sorter, int[] a, int low, int high) {
        if (TRACE) {
            System.out.println("radixSort[" + a.length + "] in [" + low + " - " + high + "]");
        }
        int[] b; int offset = low;

        if (sorter == null || (b = sorter.b) == null) {
            if (TRACE_ALLOC) {
                System.out.println("radixSort: alloc b: " + (high - low));
            }
            b = (int[]) tryAllocate(a, high - low);

            if (b == null) {
                return false;
            }
        } else {
            offset = sorter.offset;
        }

        int start = low - offset;
        int last = high - offset;

        final int[] count1; final int[] count2;
        final int[] count3; final int[] count4;

        if (sorter != null) {
            sorter.resetRadixBuffers();
            count1 = sorter.count1;
            count2 = sorter.count2;
            count3 = sorter.count3;
            count4 = sorter.count4;
        } else {
            if (TRACE_ALLOC) {
                System.out.println("radixSort: alloc buffers (4x256)");
            }
            count1 = new int[256];
            count2 = new int[256];
            count3 = new int[256];
            count4 = new int[256];
        }

        for (int i = low; i < high; ++i) {
            count1[ a[i]         & 0xFF]--;
            count2[(a[i] >>>  8) & 0xFF]--;
            count3[(a[i] >>> 16) & 0xFF]--;
            count4[(a[i] >>> 24) ^ 0x80]--;
        }

        boolean passLevel4 = passLevel(count4, 255, low - high, high);
        boolean passLevel3 = passLevel(count3, 255, low - high, high);
        boolean passLevel2 = passLevel(count2, 255, low - high, high);
        boolean passLevel1 = passLevel(count1, 255, low - high, high);

        if (passLevel1) {
            for (int i = low; i < high; ++i) {
                b[count1[a[i] & 0xFF]++ - offset] = a[i];
            }
        }

        if (passLevel2) {
            if (passLevel1) {
                for (int i = start; i < last; ++i) {
                    a[count2[(b[i] >>> 8) & 0xFF]++] = b[i];
                }
            } else {
                for (int i = low; i < high; ++i) {
                    b[count2[(a[i] >>> 8) & 0xFF]++ - offset] = a[i];
                }
            }
        }

        if (passLevel3) {
            if (passLevel1 ^ passLevel2) {
                for (int i = start; i < last; ++i) {
                    a[count3[(b[i] >>> 16) & 0xFF]++] = b[i];
                }
            } else {
                for (int i = low; i < high; ++i) {
                    b[count3[(a[i] >>> 16) & 0xFF]++ - offset] = a[i];
                }
            }
        }

        if (passLevel4) {
            if (passLevel1 ^ passLevel2 ^ passLevel3) {
                for (int i = start; i < last; ++i) {
                    a[count4[(b[i] >>> 24) ^ 0x80]++] = b[i];
                }
            } else {
                for (int i = low; i < high; ++i) {
                    b[count4[(a[i] >>> 24) ^ 0x80]++ - offset] = a[i];
                }
            }
        }

        if (passLevel1 ^ passLevel2 ^ passLevel3 ^ passLevel4) {
            System.arraycopy(b, low - offset, a, low, high - low);
        }
        return true;
    }

    /**
     * Scans count array and creates histogram.
     *
     * @param count the count array
     * @param last the index of the last count
     * @param size the array size
     * @param high the index of the last element, exclusive
     * @return false if the level can be skipped, true otherwise
     */
    private static boolean passLevel(int[] count, int last, int size, int high) {
        for (int c : count) {
            if (c == 0) {
                continue;
            }
            if (c == size) { // All elements are equal
                return false;
            }
            break;
        }

        /*
         * Compute histogram.
         */
        count[last] += high;

        for (int i = last; i > 0; --i) {
            count[i - 1] += count[i];
        }
        return true;
    }

    /**
     * Tries to sort the specified range of the array.
     *
     * @param sorter parallel context
     * @param a the array to be sorted
     * @param low the index of the first element to be sorted
     * @param size the array size
     * @return true if finally sorted, false otherwise
     */
    private static boolean tryMergeRuns(Sorter sorter, int[] a, int low, int size) {
        if (TRACE) {
            System.out.println("tryMergeRuns[" + a.length + "] in [" + low + " - " + (low + size) + "]");
        }
        /*
         * The run array is constructed only if initial runs are
         * long enough to continue, run[i] then holds start index
         * of the i-th sequence of elements in non-descending order.
         */
        int[] run = null;
        int high = low + size;
        int count = 1, last = low;

        /*
         * Identify all possible runs.
         */
        for (int k = low + 1; k < high; ) {

            /*
             * Find the end index of the current run.
             */
            if (a[k - 1] < a[k]) {

                // Identify ascending sequence
                while (++k < high && a[k - 1] <= a[k]);

            } else if (a[k - 1] > a[k]) {

                // Identify descending sequence
                while (++k < high && a[k - 1] >= a[k]);

                // Reverse into ascending order
                for (int i = last - 1, j = k; ++i < --j && a[i] > a[j]; ) {
                    int ai = a[i]; a[i] = a[j]; a[j] = ai;
                }
            } else { // Identify constant sequence
                for (int ak = a[k]; ++k < high && ak == a[k]; );

                if (k < high) {
                    continue;
                }
            }

            /*
             * Check special cases.
             */
            if (sorter.runInit || run == null) {
                sorter.runInit = false; // LBO

                if (k == high) {
                    /*
                     * The array is monotonous sequence,
                     * and therefore already sorted.
                     */
                    return true;
                }

                if (k - low < MIN_FIRST_RUN_SIZE) {
                    /*
                     * The first run is too small
                     * to proceed with scanning.
                     */
                    return false;
                }

                if (false && TRACE_ALLOC) {
                    System.out.println("tryMergeRuns: alloc runs: " + (((size >> 10) | 0x7F) & 0x3FF));
                    // min 127, max 1023, ext 5120
                    run = new int[((size >> 10) | 0x7F) & 0x3FF];
                } else {
                    run = sorter.run; // LBO: prealloc
                }
                run[0] = low;

            } else if (a[last - 1] > a[last]) { // Start new run

                if (count > (k - low) >> MIN_FIRST_RUNS_FACTOR) {
                    /*
                     * The first runs are not long
                     * enough to continue scanning.
                     */
                    return false;
                }

                if (++count == MAX_RUN_CAPACITY) {
                    /*
                     * Array is not highly structured.
                     */
                    return false;
                }

                if (false && count == run.length) { // LBO: prealloc
                    /*
                     * Increase capacity of index array.
                     */
//                  System.out.println("alloc run (resize)");
                    run = Arrays.copyOf(run, count << 1);
                }
            }
            run[count] = (last = k);

            if (++k == high) {
                /*
                 * There is a single-element run at the end.
                 */
                --k;
            }
        }

        /*
         * Merge runs of highly structured array.
         */
        if (count > 1) {
            if (TRACE) {
                System.out.println("Merge runs:");
                int prev = run[0];
                for (int i = 1, end; i <= count; i++) {
                    end = run[i] - 1;
                    System.out.println("run " + i + " in [" + prev + " - " + end + "] <=> [" + a[prev] + " - " + a[end] + "]");
                    prev = run[i];
                }
            }
            int[] b; int offset = low;

            if (sorter == null || (b = sorter.b) == null) {
                if (TRACE_ALLOC) {
                    System.out.println("tryMergeRuns: alloc b: " + size);
                }
                b = (int[]) tryAllocate(a, size);

                if (b == null) {
                    return false;
                }
            } else {
                offset = sorter.offset;
            }
            mergeRuns(a, b, offset, 1, /*sorter != null,*/ run, 0, count);
        }
        return true;
    }

    /**
     * Merges the specified runs.
     *
     * @param a the source array
     * @param b the temporary buffer used in merging
     * @param offset the start index in the source, inclusive
     * @param aim specifies merging: to source ( > 0), buffer ( < 0) or any ( == 0)
     * @param run the start indexes of the runs, inclusive
     * @param lo the start index of the first run, inclusive
     * @param hi the start index of the last run, inclusive
     * @return the destination where runs are merged
     */
    private static int[] mergeRuns(int[] a, int[] b, int offset,
            int aim, /*boolean parallel,*/ int[] run, int lo, int hi) {
        
        if (TRACE) {
            System.out.println("mergeRuns[" + a.length + "] in [" + run[lo] + " - " + run[hi] + "]");
        }

        if (hi - lo == 1) {
            if (aim >= 0) {
                return a;
            }
            for (int i = run[hi], j = i - offset, low = run[lo]; i > low;
                b[--j] = a[--i]
            );
            return b;
        }

        /*
         * Split into approximately equal parts.
         */
        int mi = lo, rmi = (run[lo] + run[hi]) >>> 1;
        while (run[++mi + 1] <= rmi);

        /*
         * Merge the left and right parts.
         */
        int[] a1, a2;
/*
        if (parallel && hi - lo > MIN_RUN_COUNT) {
            RunMerger merger = new RunMerger(a, b, offset, 0, run, mi, hi).forkMe();
            a1 = mergeRuns(a, b, offset, -aim, true, run, lo, mi);
            a2 = (int[]) merger.getDestination();
        } else {
*/
            a1 = mergeRuns(a, b, offset, -aim, /*false,*/ run, lo, mi);
            a2 = mergeRuns(a, b, offset,    0, /*false,*/ run, mi, hi);
//        }

        int[] dst = a1 == a ? b : a;

        int k   = a1 == a ? run[lo] - offset : run[lo];
        int lo1 = a1 == b ? run[lo] - offset : run[lo];
        int hi1 = a1 == b ? run[mi] - offset : run[mi];
        int lo2 = a2 == b ? run[mi] - offset : run[mi];
        int hi2 = a2 == b ? run[hi] - offset : run[hi];

/*
        if (parallel) {
            new Merger(null, dst, k, a1, lo1, hi1, a2, lo2, hi2).invoke();
        } else {
*/
            mergeParts(/*null,*/dst, k, a1, lo1, hi1, a2, lo2, hi2);
//        }
        return dst;
    }

    /**
     * Merges the sorted parts.
     *
     * @param dst the destination where parts are merged
     * @param k the start index of the destination, inclusive
     * @param a1 the first part
     * @param lo1 the start index of the first part, inclusive
     * @param hi1 the end index of the first part, exclusive
     * @param a2 the second part
     * @param lo2 the start index of the second part, inclusive
     * @param hi2 the end index of the second part, exclusive
     */
    private static void mergeParts(/*Merger merger,*/int[] dst, int k,
            int[] a1, int lo1, int hi1, int[] a2, int lo2, int hi2) {
        
        if (TRACE) {
            System.out.println("mergeParts[" + dst.length + "] in [" + lo1 + " - " + hi1 + "] and  [" + lo2 + " - " + hi2 + "]");
        }
        // ...
        /*
         * Merge small parts sequentially.
         */
        while (lo1 < hi1 && lo2 < hi2) {
            dst[k++] = a1[lo1] < a2[lo2] ? a1[lo1++] : a2[lo2++];
        }
        if (dst != a1 || k < lo1) {
            while (lo1 < hi1) {
                dst[k++] = a1[lo1++];
            }
        }
        if (dst != a2 || k < lo2) {
            while (lo2 < hi2) {
                dst[k++] = a2[lo2++];
            }
        }
    }

    static final class Sorter {

        final int[] run;
        int[] b;
        int offset;
        boolean runInit;

        int[] count1 = null;
        int[] count2 = null;
        int[] count3 = null;
        int[] count4 = null;

        Sorter() {
            // preallocate max runs:
            if (TRACE_ALLOC) {
                System.out.println("Sorter: pre-alloc runs: " + MAX_RUN_CAPACITY);
            }
            run = new int[MAX_RUN_CAPACITY];
        }

        void initBuffers(final int length, final int offset) {
            if ((b == null) || (b.length < length)) {
                if (TRACE_ALLOC) {
                    System.out.println("Sorter: alloc b: " + length);
                }
                b = new int[length];
            }
            this.runInit = true;
            this.offset = offset;
        }

        void resetRadixBuffers() {
            if (count1 == null) {
                if (TRACE_ALLOC) {
                    System.out.println("Sorter: alloc buffers (4x256)");
                }
                count1 = new int[256];
                count2 = new int[256];
                count3 = new int[256];
                count4 = new int[256];
            } else {
                Arrays.fill(count1, 0);
                Arrays.fill(count2, 0);
                Arrays.fill(count3, 0);
                Arrays.fill(count4, 0);
            }
        }
    }

    private static Object tryAllocate(Object a, int size) {
        try {
            if (a instanceof int[]) {
                return new int[size];
            }
            if (a instanceof long[]) {
                return new long[size];
            }
            if (a instanceof float[]) {
                return new float[size];
            }
            if (a instanceof double[]) {
                return new double[size];
            }
            throw new IllegalArgumentException(
                "Unknown type of array: " + a.getClass().getName());
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}
