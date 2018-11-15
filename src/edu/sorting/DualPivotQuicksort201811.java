/*
 * Copyright (c) 2009, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
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

import java.util.Arrays;

/**
 * This class implements powerful and fully optimized versions, both
 * sequential and parallel, of the Dual-Pivot Quicksort algorithm by
 * Vladimir Yaroslavskiy, Jon Bentley and Josh Bloch. This algorithm
 * offers O(n log(n)) performance on all data sets, and is typically
 * faster than traditional (one-pivot) Quicksort implementations.
 *
 * There are also additional algorithms such as parallel merge sort,
 * pair insertion sort, merging of runs, heap sort and counting sort
 * invoked from the Dual-Pivot Quicksort.
 *
 * @author Vladimir Yaroslavskiy
 * @author Jon Bentley
 * @author Josh Bloch
 * @author Doug Lea
 *
 * @version 2018.08.18
 *
 * @since 1.7 * 12
 */
public final class DualPivotQuicksort201811 implements wildinter.net.mergesort.Sorter {

    public final static wildinter.net.mergesort.Sorter INSTANCE = new DualPivotQuicksort201811();

    /**
     * Prevents instantiation.
     */
    private DualPivotQuicksort201811() {
    }

    // avoid alloc
    private final Sorter sorter = new Sorter();
    
    @Override
    public void sort(final int[] A, final int low, final int high) {
        // preallocation of temporary arrays into custom Sorter class
        sorter.initLength(high - low + 1);
        
        sort(sorter, A, 0, low, high + 1); // exclusive
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /* 
    From OpenJDK12 source code
     */
    /**
     * Max array size to use insertion sort.
     */
    private static final int MAX_INSERTION_SORT_SIZE = 26;

    /**
     * Max array size to use pair insertion sort.
     */
    private static final int MAX_PAIR_INSERTION_SORT_SIZE = 104;

    /**
     * Max array size to use heap sort for the leftmost part.
     */
    private static final int MAX_HEAP_SORT_SIZE = 69;

    /**
     * Min array size for performing sorting in parallel.
     */
    private static final int MIN_PARALLEL_SORT_SIZE = 1 << 12;

    /**
     * Min array size to try merging of runs.
     */
    private static final int MIN_TRY_MERGE_SIZE = 1 << 12;

    /**
     * Initial capacity of the index array for tracking runs.
     */
    private static final int INITIAL_RUN_CAPACITY = 32;

    /**
     * Min size of the first run to proceed with scanning.
     */
    private static final int MIN_FIRST_RUN_SIZE = 32;

    /**
     * Min factor for the first runs to continue scanning.
     */
    private static final int MIN_FIRST_RUNS_FACTOR = 6;

    /**
     * Min number of runs, required by parallel merging.
     */
    private static final int MIN_RUN_COUNT = 4;

    /**
     * Min array size to use parallel merging of parts.
     */
    private static final int MIN_PARALLEL_PART_MERGE_SIZE = 1 << 12;

    /**
     * Min size of a byte array to use counting sort.
     */
    private static final int MIN_BYTE_COUNTING_SORT_SIZE = 41;

    /**
     * Min size of a short or char array to use counting sort.
     */
    private static final int MIN_SHORT_OR_CHAR_COUNTING_SORT_SIZE = 2180;

    /**
     * Max double recursive partitioning depth before using heap sort.
     */
    private static final int MAX_RECURSION_DEPTH = 64 << 1;

    /**
     * Sorts the specified range of the array using parallel merge
     * sort and/or Dual-Pivot Quicksort.
     *
     * To balance the faster splitting and parallelism of merge sort
     * with the faster element partitioning of Quicksort, ranges are
     * subdivided in tiers such that, if there is enough parallelism,
     * the four-way parallel merge is started, still ensuring enough
     * parallelism to process the partitions.
     *
     * @param a the array to be sorted
     * @param processors the number of available processors
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    static void sort(int[] a, int processors, int low, int high) {
        sort(null, a, 0, low, high);
    }

    private static void sort(Sorter sorter, int[] a, int low, int high) {
        sort(sorter, a, 0, low, high);
    }
        
    /**
     * Sorts the specified array using the Dual-Pivot Quicksort and/or
     * other sorts in special-cases, possibly with parallel partitions.
     *
     * @param sorter parallel context
     * @param a the array to be sorted
     * @param bits the combination of recursion depth and bit flag, where
     *        the right bit "0" indicates that array is the leftmost part
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private static void sort(Sorter sorter, int[] a, int bits, int low, int high) {
        while (true) {
            int end = high - 1, size = high - low;

            /*
             * Run pair insertion sort on small non-leftmost parts.
             */
            if (size < MAX_PAIR_INSERTION_SORT_SIZE && (bits & 1) > 0) {
                end -= size > MAX_INSERTION_SORT_SIZE ? 3 * (size >> 2) & ~1 : 0;
                pairInsertionSort(a, low, end, size);
                return;
            }

            /*
             * Switch to heap sort on the leftmost part or
             * if the execution time is becoming quadratic.
             */
            if (size < MAX_HEAP_SORT_SIZE || (bits += 2) > MAX_RECURSION_DEPTH) {
                heapSort(a, low, end);
                return;
            }

            /*
             * Check if the whole array or non-left parts
             * are nearly sorted and then merge runs.
             */
            if ((bits == 2 || (bits & 1) > 0) && size > MIN_TRY_MERGE_SIZE
                    && tryMergeRuns(sorter, a, low, size)) {
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

            /*
             * Sort these elements in place by the combination
             * of 5-element sorting network and insertion sort.
             */
            if (a[e5] < a[e3]) { int t = a[e5]; a[e5] = a[e3]; a[e3] = t; }
            if (a[e4] < a[e2]) { int t = a[e4]; a[e4] = a[e2]; a[e2] = t; }
            if (a[e5] < a[e4]) { int t = a[e5]; a[e5] = a[e4]; a[e4] = t; }
            if (a[e3] < a[e2]) { int t = a[e3]; a[e3] = a[e2]; a[e2] = t; }
            if (a[e4] < a[e3]) { int t = a[e4]; a[e4] = a[e3]; a[e3] = t; }

            if (a[e1] > a[e2]) { int t = a[e1]; a[e1] = a[e2]; a[e2] = t;
                if (t > a[e3]) { a[e2] = a[e3]; a[e3] = t;
                    if (t > a[e4]) { a[e3] = a[e4]; a[e4] = t;
                        if (t > a[e5]) { a[e4] = a[e5]; a[e5] = t; }
                    }
                }
            }

            // Pointers
            int lower = low; // The index of the last element of the left part
            int upper = end; // The index of the first element of the right part

            /*
             * Partitioning with 2 pivots in case of different elements.
             */
            if (a[e1] < a[e2] && a[e2] < a[e3] && a[e3] < a[e4] && a[e4] < a[e5]) {

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
                        while (k > lower) {
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
                } else 
*/                    
                {
                    sort(sorter, a, bits | 1, lower + 1, upper);
                    sort(sorter, a, bits | 1, upper + 1, high);
                }

            } else { // Use single pivot in case of many equal elements

                /*
                 * Use the third of the five sorted elements as the pivot.
                 * This value is inexpensive approximation of the median.
                 */
                int pivot = a[e3];

                /*
                 * The first element to be sorted is moved to
                 * the location formerly occupied by the pivot.
                 * When partitioning is completed, the pivot is
                 * swapped back into its final position, and
                 * excluded from the next subsequent sorting.
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
                } else 
*/                
                {
                    sort(sorter, a, bits | 1, upper, high);
                }
            }
            high = lower; // Iterate along the left part
        }
    }

    /**
     * Sorts the specified range of the array by pair insertion sort.
     *
     * In the context of Quicksort, the pivot element between given
     * parts plays the role of sentinel. Therefore, expensive check
     * of the left range on each iteration can be skipped unless it
     * is the leftmost call. For initial array up to threshold, use
     * plain insertion sort. For remainder, insert two elements per
     * iteration, first, the greater element then the lesser, but
     * from position where the greater element was inserted.
     *
     * @param a the array to be sorted
     * @param left the index of the first element, inclusive, to be sorted
     * @param end the index of the last element for classic insertion sort
     * @param size the array size
     */
    private static void pairInsertionSort(int[] a, int left, int end, int size) {
        int last = left + size;

        /*
         * Start with classic insertion sort on tiny part.
         */
        for (int k; left < end; ) {
            int ak = a[k = ++left];

            while (ak < a[--k]) {
                a[k + 1] = a[k];
            }
            a[k + 1] = ak;
        }

        /*
         * Continue with pair insertion sort on remain part.
         */
        for (int k; ++left < last; ) {
            int a1 = a[k = left], a2 = a[++left];

            if (a1 > a2) {

                while (a1 < a[--k]) {
                    a[k + 2] = a[k];
                }
                a[++k + 1] = a1;

                while (a2 < a[--k]) {
                    a[k + 1] = a[k];
                }
                a[k + 1] = a2;

            } else if (a1 < a[k - 1]) {

                while (a2 < a[--k]) {
                    a[k + 2] = a[k];
                }
                a[++k + 1] = a2;

                while (a1 < a[--k]) {
                    a[k + 1] = a[k];
                }
                a[k + 1] = a1;
            }
        }
    }

// ...
    /**
     * Sorts the specified range of the array using heap sort.
     *
     * @param a the array to be sorted
     * @param left the index of the first element, inclusive, to be sorted
     * @param right the index of the last element, inclusive, to be sorted
     */
    private static void heapSort(int[] a, int left, int right) {
        for (int k = (left + 1 + right) >>> 1; k > left; ) {
            pushDown(a, --k, a[k], left, right);
        }
        for (int k = right; k > left; --k) {
            int max = a[left];
            pushDown(a, left, a[k], left, k);
            a[k] = max;
        }
    }

    /**
     * Pushes specified element down during heap sort.
     *
     * @param a the given array
     * @param p the start index
     * @param value the given element
     * @param left the index of the first element, inclusive, of the range
     * @param right the index of the last element, inclusive, of the range
     */
    private static void pushDown(int[] a, int p, int value, int left, int right) {
        for (int k ;; a[p] = a[p = k]) {
            k = (p << 1) - left + 2; // Index of the right child

            if (k > right || a[k - 1] > a[k]) {
                --k;
            }
            if (k > right || a[k] <= value) {
                a[p] = value;
                return;
            }
        }
    }

    /**
     * Calculates the max number of runs.
     *
     * @param size the array size
     * @return the max number of runs
     */
    private static int getMaxRunCount(int size) {
        return size > 2048000 ? 2000 : size >> 10 | 5;
    }

    /**
     * Tries to sort the specified range of the array.
     *
     * @param sorter parallel context
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param size the array size
     * @return true if finally sorted, false otherwise
     */
    private static boolean tryMergeRuns(Sorter sorter, int[] a, int low, int size) {
        /*
         * The run array is constructed only if initial runs are
         * long enough to continue, run[i] then holds start index
         * of the i-th sequence of elements in non-descending order.
         */
        int[] run = null;
        int high = low + size;
        int count = 1, last = low;
        int max = getMaxRunCount(size);

        /*
         * Identify all possible runs.
         */
        for (int k = low + 1; k < high && count < max; ) {

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
            } else { // Identify equal elements
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

//                System.out.println("alloc run");
//                run = new int[INITIAL_RUN_CAPACITY];

                run = sorter.run; // LBO: prealloc
                run[0] = low;

            } else if (a[last - 1] > a[last]) {

                if (count > (k - low) >> MIN_FIRST_RUNS_FACTOR) {

                    /*
                     * The first runs are not long
                     * enough to continue scanning.
                     */
                    return false;
                }

                if (++count == run.length) {
                    run = Arrays.copyOf(run, count << 1);
                }
            }
            run[count] = (last = k);
        }

        /*
         * Check if array is highly structured and then merge runs.
         */
        if (count < max && count > 1) {
            int[] b; int offset = low;

            // LBO: prealloc
            if (sorter == null || (b = sorter.b) == null || b.length < size) {
//                System.out.println("alloc b: "+size);
                b = new int[size];
//            } else {
                // offset = sorter.offset;
            }
            mergeRuns(a, b, offset, 1, sorter != null, run, 0, count);
        }
        return count < max;
    }

    /**
     * Merges the specified runs.
     *
     * @param a the source array
     * @param b the temporary buffer used in merging
     * @param offset the start index in the source, inclusive
     * @param aim specifies merging: to source ( > 0), buffer ( < 0) or any ( == 0)
     * @param parallel indicates whether merging is performed in parallel
     * @param run the start indexes of the runs, inclusive
     * @param lo the start index of the first run, inclusive
     * @param hi the start index of the last run, inclusive
     * @return the destination where runs are merged
     */
    private static int[] mergeRuns(int[] a, int[] b, int offset,
            int aim, boolean parallel, int[] run, int lo, int hi) {

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
         * Merge the left and the right parts.
         */
        int[] a1, a2;
/*
        if (parallel && hi - lo > MIN_RUN_COUNT) {
            RunMerger merger = new RunMerger(a, b, offset, 0, run, mi, hi).forkMe();
            a1 = mergeRuns(a, b, offset, -aim, true, run, lo, mi);
            a2 = (int[]) merger.getDestination();
        } else 
*/        
        {
            a1 = mergeRuns(a, b, offset, -aim, false, run, lo, mi);
            a2 = mergeRuns(a, b, offset,    0, false, run, mi, hi);
        }

        int[] dst = a1 == a ? b : a;

        int k   = a1 == a ? run[lo] - offset : run[lo];
        int lo1 = a1 == b ? run[lo] - offset : run[lo];
        int hi1 = a1 == b ? run[mi] - offset : run[mi];
        int lo2 = a2 == b ? run[mi] - offset : run[mi];
        int hi2 = a2 == b ? run[hi] - offset : run[hi];

/*
        if (parallel) {
            new Merger(null, dst, k, a1, lo1, hi1, a2, lo2, hi2).invoke();
        } else 
*/        
        {
            mergeParts(null, dst, k, a1, lo1, hi1, a2, lo2, hi2);
        }
        return dst;
    }

    /**
     * Merges the sorted parts.
     *
     * @param merger parallel context
     * @param dst the destination where parts are merged
     * @param k the start index of the destination, inclusive
     * @param a1 the first part
     * @param lo1 the start index of the first part, inclusive
     * @param hi1 the end index of the first part, exclusive
     * @param a2 the second part
     * @param lo2 the start index of the second part, inclusive
     * @param hi2 the end index of the second part, exclusive
     */
    private static void mergeParts(Merger merger, int[] dst, int k,
            int[] a1, int lo1, int hi1, int[] a2, int lo2, int hi2) {

        if (false && merger != null && a1 == a2) {

            while (true) {
                /*
                 * The first part must be larger.
                 */
                if (hi1 - lo1 < hi2 - lo2) {
                    int lo = lo1; lo1 = lo2; lo2 = lo;
                    int hi = hi1; hi1 = hi2; hi2 = hi;
                }

                /*
                 * Small parts will be merged sequentially.
                 */
                if (hi1 - lo1 < MIN_PARALLEL_PART_MERGE_SIZE) {
                    break;
                }

                /*
                 * Find the median of the larger part.
                 */
                int mi1 = (lo1 + hi1) >>> 1;
                int key = a1[mi1];
                int mi2 = hi2;

                /*
                 * Partition the smaller part.
                 */
                for (int loo = lo2; loo < mi2; ) {
                    int t = (loo + mi2) >>> 1;

                    if (key > a2[t]) {
                        loo = t + 1;
                    } else {
                        mi2 = t;
                    }
                }

                int d = mi2 - lo2 + mi1 - lo1;

                /*
                 * Merge the right parts in parallel.
                 */
/*                
                merger.forkMerger(dst, k + d, a1, mi1, hi1, a2, mi2, hi2);
*/
                /*
                 * Process the left parts.
                 */
                hi1 = mi1;
                hi2 = mi2;
            }
        }

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
// ...
    private class Sorter{
        final int[] run;
        int[] b;
        boolean runInit;
        
        Sorter() {
            // preallocate max runs:
            final int max = getMaxRunCount(Integer.MAX_VALUE) + 1;
            run = new int[max];
        }
        
        void initLength(int length) {
            if (b == null || b.length < length) {
                b = new int[length];
            }
            runInit = true;
        }
    }
    
    private class Merger{}
}
