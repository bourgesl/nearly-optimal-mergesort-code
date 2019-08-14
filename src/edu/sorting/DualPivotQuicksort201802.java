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

import wildinter.net.mergesort.Sorter;


/**
 * This class implements powerful and fully optimized versions, both
 * sequential and parallel, of the Dual-Pivot Quicksort algorithm by
 * Vladimir Yaroslavskiy, Jon Bentley and Josh Bloch. This algorithm
 * offers O(n log(n)) performance on all data sets, and is typically
 * faster than traditional (one-pivot) Quicksort implementations.
 *
 * @author Vladimir Yaroslavskiy
 * @author Jon Bentley
 * @author Josh Bloch
 *
 * @version 2018.02.18
 * @since 1.7
 */
public final class DualPivotQuicksort201802 implements Sorter {

    public final static Sorter INSTANCE = new DualPivotQuicksort201802();

    /**
     * Prevents instantiation.
     */
    private DualPivotQuicksort201802() {
    }

    // avoid alloc
    private int[] aux = null;
    private int[] run = null;

    @Override
    public void sort(final int[] A, final int low, final int high) {
        final int length = high - low + 1;
        if (aux == null || aux.length < length) {
            aux = new int[length];
        }
        int max = SortingAlgorithms201802.getMaxRunCount(length) + 1;
        if (run == null || run.length < max) {
            run = new int[max];
        }

        sort(A, LEFTMOST_BITS, low, high + 1, aux, run);
    }

    /**
     * Sorts the specified range of the array.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    public static void sortORIG(int[] a, int low, int high) {
        sort(a, LEFTMOST_BITS, low, high, null, null); // FAIL
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    /*
    From OpenJDK12 source code
     */

    /**
     * If the length of the leftmost part to be sorted is less than
     * this constant, heap sort is used in preference to Quicksort.
     */
    private static final int HEAP_SORT_THRESHOLD = 69;

    /**
     * If the length of non-leftmost part to be sorted is less than this
     * constant, nano insertion sort is used in preference to Quicksort.
     */
    private static final int NANO_INSERTION_SORT_THRESHOLD = 36;

    /**
     * If the length of non-leftmost part to be sorted is less than this
     * constant, pair insertion sort is used in preference to Quicksort.
     */
    private static final int PAIR_INSERTION_SORT_THRESHOLD = 88;

    /**
     * if the depth of the Quicksort recursion exceeds this
     * constant, heap sort is used in preference to Quicksort.
     */
    private static final int MAX_RECURSION_DEPTH = 100;

    /**
     * This constant is combination of maximum Quicksort recursion depth
     * and binary flag, where the right bit "0" indicates that the whole
     * array is considered as the leftmost part.
     */
    private static final int LEFTMOST_BITS = MAX_RECURSION_DEPTH << 1;

    /**
     * Sorts the specified range of the array by the Dual-Pivot Quicksort.
     *
     * @param a the array to be sorted
     * @param bits the recursion depth of Quicksort and the leftmost flag
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private static void sort(int[] a, int bits, int low, int high, int[] buffer, int[] run) {
        int end = high - 1, length = high - low;

        /*
         * Run insertion sorts on non-leftmost part.
         */
        if ((bits & 1) > 0) {

            /*
             * Use nano insertion sort on tiny part.
             */
            if (length < NANO_INSERTION_SORT_THRESHOLD) {
                SortingAlgorithms201802.nanoInsertionSort(a, low, high);
                return;
            }

            /*
             * Use pair insertion sort on small part.
             */
            if (length < PAIR_INSERTION_SORT_THRESHOLD) {
                SortingAlgorithms201802.pairInsertionSort(a, low, end);
                return;
            }
        }

        /*
         * Switch to heap sort on the leftmost part or
         * if the execution time is becoming quadratic.
         */
        if (length < HEAP_SORT_THRESHOLD || (bits -= 2) < 0) {
            SortingAlgorithms201802.heapSort(a, low, end);
            return;
        }

        /*
         * Check if the array is nearly sorted
         * and then try to sort it by Merging sort.
         */
        if (SortingAlgorithms201802.mergingSort(a, low, high, buffer, run)) {
            return;
        }

        /*
         * The splitting of the array, defined by the following
         * step, is related to the inexpensive approximation of
         * the golden ratio.
         */
        int step = (length >> 3) * 3 + 3;

        /*
         * Five elements around (and including) the central element in
         * the array will be used for pivot selection as described below.
         * The unequal choice of spacing these elements was empirically
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
        if (a[e5] < a[e3]) {
            int t = a[e5];
            a[e5] = a[e3];
            a[e3] = t;
        }
        if (a[e4] < a[e2]) {
            int t = a[e4];
            a[e4] = a[e2];
            a[e2] = t;
        }
        if (a[e5] < a[e4]) {
            int t = a[e5];
            a[e5] = a[e4];
            a[e4] = t;
        }
        if (a[e3] < a[e2]) {
            int t = a[e3];
            a[e3] = a[e2];
            a[e2] = t;
        }
        if (a[e4] < a[e3]) {
            int t = a[e4];
            a[e4] = a[e3];
            a[e3] = t;
        }

        if (a[e1] > a[e2]) {
            int t = a[e1];
            a[e1] = a[e2];
            a[e2] = t;
            if (t > a[e3]) {
                a[e2] = a[e3];
                a[e3] = t;
                if (t > a[e4]) {
                    a[e3] = a[e4];
                    a[e4] = t;
                    if (t > a[e5]) {
                        a[e4] = a[e5];
                        a[e5] = t;
                    }
                }
            }
        }

        // Pointers
        int lower = low; // The index of the last element of the left part
        int upper = end; // The index of the first element of the right part

        if (a[e1] < a[e2] && a[e2] < a[e3] && a[e3] < a[e4] && a[e4] < a[e5]) {

            /*
             * Use the first and the fifth elements as the pivots.
             * These values are inexpensive approximation of tertiles.
             * Note, that pivot1 < pivot2.
             */
            int pivot1 = a[e1];
            int pivot2 = a[e5];

            /*
             * The first and the last elements to be sorted are moved to the
             * locations formerly occupied by the pivots. When partitioning
             * is completed, the pivots are swapped back into their final
             * positions, and excluded from subsequent sorting.
             */
            a[e1] = a[lower];
            a[e5] = a[upper];

            /*
             * Skip elements, which are less or greater than the pivots.
             */
            while (a[++lower] < pivot1);
            while (a[--upper] > pivot2);

            /*
             * Backwards 3-interval partitioning
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
            for (int $ = --lower, k = ++upper; --k > lower;) {
                int ak = a[k];

                if (ak < pivot1) { // Move a[k] to the left side
                    while (a[++lower] < pivot1);

                    if (lower > k) {
                        lower = k;
                        break;
                    }
                    if (a[lower] > pivot2) { // a[lower] >= pivot1
                        a[k] = a[--upper];
                        a[upper] = a[lower];
                    } else { // pivot1 <= a[lower] <= pivot2
                        a[k] = a[lower];
                    }
                    a[lower] = ak;
                } else if (ak > pivot2) { // Move a[k] to the right side
                    a[k] = a[--upper];
                    a[upper] = ak;
                }
            }

            /*
             * Swap the pivots into their final positions.
             */
            a[low] = a[lower];
            a[lower] = pivot1;
            a[end] = a[upper];
            a[upper] = pivot2;

            /*
             * Sort all parts recursively, excluding known pivots.
             */
            sort(a, bits | 1, upper + 1, high, buffer, run);
            sort(a, bits, low, lower, buffer, run);
            sort(a, bits | 1, lower + 1, upper, buffer, run);
        } else { // Partitioning with one pivot

            /*
             * Use the third element as the pivot. This value
             * is inexpensive approximation of the median.
             */
            int pivot = a[e3];

            /*
             * The first element to be sorted is moved to the location
             * formerly occupied by the pivot. When partitioning is
             * completed, the pivot is swapped back into its final
             * position, and excluded from subsequent sorting.
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
            for (int k = ++upper; --k > lower;) {
                if (a[k] == pivot) {
                    continue;
                }
                int ak = a[k];

                if (ak < pivot) { // Move a[k] to the left side
                    while (a[++lower] < pivot);

                    if (lower > k) {
                        lower = k;
                        break;
                    }
                    a[k] = pivot;

                    if (a[lower] > pivot) {
                        a[--upper] = a[lower];
                    }
                    a[lower] = ak;
                } else { // ak > pivot - Move a[k] to the right side
                    a[k] = pivot;
                    a[--upper] = ak;
                }
            }

            /*
             * Swap the pivot into its final position.
             */
            a[low] = a[lower];
            a[lower] = pivot;

            /*
             * Sort the left and the right parts recursively, excluding
             * known pivot. All elements from the central part are equal
             * and, therefore, already sorted.
             */
            sort(a, bits | 1, upper, high, buffer, run);
            sort(a, bits, low, lower, buffer, run);
        }
    }
}
