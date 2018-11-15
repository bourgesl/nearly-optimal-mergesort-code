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

/**
 * This class implements sorting algorithms by Vladimir Yaroslavskiy,
   such as Merging sort, the pair, nano and optimized insertion sorts,
 * counting sort and heap sort, invoked from the Dual-Pivot Quicksort.
 *
 * @author Vladimir Yaroslavskiy
 *
 * @version 2018.02.18
 * @since 10
 */
final class SortingAlgorithms201802 {

    /**
     * Prevents instantiation.
     */
    private SortingAlgorithms201802() {
    }

    /**
     * If the length of an array to be sorted is greater than this
     * constant, Merging sort is used in preference to Quicksort.
     */
    private static final int MERGING_SORT_THRESHOLD = 2048;

    /**
     * Calculates the maximum number of runs.
     *
     * @param length the array length
     * @return the maximum number of runs
     */
    public static int getMaxRunCount(int length) {
        return length > 2048000 ? 2000 : length >> 10 | 5;
    }

    /**
     * Tries to sort the specified range of the array by the Merging sort.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     * @return true if the given array is finally sorted, false otherwise
     */
    static boolean mergingSort(int[] a, int low, int high, int[] buffer, int[] run) {
        int length = high - low;

        if (length < MERGING_SORT_THRESHOLD) {
            return false;
        }
        
        /*
         * Index run[i] is the start of i-th run.
         * A run is a subsequence of elements
         * in ascending or descending order.
         */
        int max = getMaxRunCount(length);
//        int[] run = new int[max + 1]; // LBO: FIX ALLOC
        int count = 0, last = low;
        run[0] = low;

        /*
         * Check if the array is highly structured.
         */
        for (int k = low + 1; k < high && count < max;) {
            if (a[k - 1] < a[k]) {

                // Identify ascending sequence
                while (++k < high && a[k - 1] <= a[k]);

            } else if (a[k - 1] > a[k]) {

                // Identify descending sequence
                while (++k < high && a[k - 1] >= a[k]);

                // Reverse the run into ascending order
                for (int i = last - 1, j = k; ++i < --j && a[i] > a[j];) {
                    int ai = a[i];
                    a[i] = a[j];
                    a[j] = ai;
                }
            } else { // Sequence with equal elements
                for (int ak = a[k]; ++k < high && ak == a[k];);

                if (k < high) {
                    continue;
                }
            }

            if (count == 0 || a[last - 1] > a[last]) {
                ++count;
            }
            run[count] = (last = k);
        }

        if (count < max && count > 1) {
            /*
             * The array is highly structured, therefore merge all runs.
             */
            merge(a, buffer, true, low, run, 0, count);
        }
        return count < max;
    }

    /**
     * Merges the specified runs.
     *
     * @param a the source array
     * @param b the temporary buffer
     * @param src specifies the type of the target: source or buffer
     * @param offset the start index of the source, inclusive
     * @param run the start indexes of the runs, inclusive
     * @param lo the start index of the first run, inclusive
     * @param hi the start index of the last run, inclusive
     * @return the target where runs are merged
     */
    private static int[] merge(int[] a, int[] b, boolean src,
                               int offset, int[] run, int lo, int hi) {

        if (hi - lo == 1) {
            if (src) {
                return a;
            }
            for (int i = run[hi], j = i - offset, low = run[lo]; i > low;
                    b[--j] = a[--i]);
            return b;
        }
        int mi = (lo + hi) >>> 1;

        int[] a1, a2; // the left and the right halves to be merged

        a1 = merge(a, b, !src, offset, run, lo, mi);
        a2 = merge(a, b, true, offset, run, mi, hi);

        return merge(
                a1 == a ? b : a,
                a1 == a ? run[lo] - offset : run[lo],
                a1,
                a1 == b ? run[lo] - offset : run[lo],
                a1 == b ? run[mi] - offset : run[mi],
                a2,
                a2 == b ? run[mi] - offset : run[mi],
                a2 == b ? run[hi] - offset : run[hi]);
    }

    /**
     * Merges the sorted halves.
     *
     * @param dst the destination where halves are merged
     * @param k the start index of the destination, inclusive
     * @param a1 the first half
     * @param i the start index of the first half, inclusive
     * @param hi the end index of the first half, exclusive
     * @param a2 the second half
     * @param j the start index of the second half, inclusive
     * @param hj the end index of the second half, exclusive
     * @return the merged halves
     */
    private static int[] merge(int[] dst, int k,
                               int[] a1, int i, int hi, int[] a2, int j, int hj) {

        while (true) {
            dst[k++] = a1[i] < a2[j] ? a1[i++] : a2[j++];

            if (i == hi) {
                while (j < hj) {
                    dst[k++] = a2[j++];
                }
                return dst;
            }
            if (j == hj) {
                while (i < hi) {
                    dst[k++] = a1[i++];
                }
                return dst;
            }
        }
    }

    /**
     * Sorts the specified range of the array by the nano insertion sort.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    static void nanoInsertionSort(int[] a, int low, int high) {
        /*
         * In the context of Quicksort, the elements from the left part
         * play the role of sentinels. Therefore expensive check of the
         * left range on each iteration can be skipped.
         */
        for (int k; ++low < high;) {
            int ak = a[k = low];

            while (ak < a[--k]) {
                a[k + 1] = a[k];
            }
            a[k + 1] = ak;
        }
    }

    /**
     * Sorts the specified range of the array by the pair insertion sort.
     *
     * @param a the array to be sorted
     * @param left the index of the first element, inclusive, to be sorted
     * @param right the index of the last element, inclusive, to be sorted
     */
    static void pairInsertionSort(int[] a, int left, int right) {
        /*
         * Align the left boundary.
         */
        left -= (left ^ right) & 1;

        /*
         * Two elements are inserted at once on each iteration.
         * At first, we insert the greater element (a2) and then
         * insert the less element (a1), but from position where
         * the greater element was inserted. In the context of a
         * Dual-Pivot Quicksort, the elements from the left part
         * play the role of sentinels. Therefore expensive check
         * of the left range on each iteration can be skipped.
         */
        for (int k; ++left < right;) {
            int a1 = a[k = ++left];

            if (a[k - 2] > a[k - 1]) {
                int a2 = a[--k];

                if (a1 > a2) {
                    a2 = a1;
                    a1 = a[k];
                }
                while (a2 < a[--k]) {
                    a[k + 2] = a[k];
                }
                a[++k + 1] = a2;
            }
            while (a1 < a[--k]) {
                a[k + 1] = a[k];
            }
            a[k + 1] = a1;
        }
    }

    /**
     * Sorts the specified range of the array by the heap sort.
     *
     * @param a the array to be sorted
     * @param left the index of the first element, inclusive, to be sorted
     * @param right the index of the last element, inclusive, to be sorted
     */
    static void heapSort(int[] a, int left, int right) {
        for (int k = (left + 1 + right) >>> 1; k > left;) {
            pushDown(a, --k, a[k], left, right);
        }
        for (int k = right; k > left; --k) {
            int max = a[left];
            pushDown(a, left, a[k], left, k);
            a[k] = max;
        }
    }

    /**
     * Pushes specified element down during the heap sort.
     *
     * @param a the given array
     * @param p the start index
     * @param value the given element
     * @param left the index of the first element, inclusive, to be sorted
     * @param right the index of the last element, inclusive, to be sorted
     */
    private static void pushDown(int[] a, int p, int value, int left, int right) {
        for (int k;; a[p] = a[p = k]) {
            k = (p << 1) - left + 2; // the index of the right child

            if (k > right || a[k - 1] > a[k]) {
                --k;
            }
            if (k > right || a[k] <= value) {
                a[p] = value;
                return;
            }
        }
    }
}
