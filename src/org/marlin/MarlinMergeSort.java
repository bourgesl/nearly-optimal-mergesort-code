/*
 * Copyright (c) 2009, 2018, Oracle and/or its affiliates. All rights reserved.
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
package org.marlin;

import java.util.Arrays;
import wildinter.net.mergesort.Sorter;

/**
 * MergeSort adapted from (OpenJDK 8) java.util.Array.legacyMergeSort(Object[])
 * to swap two arrays at the same time (x & y)
 * and use external auxiliary storage for temporary arrays
 */
public final class MarlinMergeSort implements Sorter {

    public final static Sorter INSTANCE = new MarlinMergeSort();

    /**
     * Prevents instantiation.
     */
    private MarlinMergeSort() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    // avoid alloc
    // fake B (ancillary data ie indices)
    private int[] B = null;
    private int[] auxX = null;
    private int[] auxY = null;

    @Override
    public void sort(final int[] A, final int left, final int right) {
        if (auxX == null || auxX.length < A.length) {
            auxX = new int[A.length];
        }
        if (auxY == null || auxY.length < A.length) {
            auxY = new int[A.length];
        }
        if (B == null || B.length < A.length) {
            B = new int[A.length];
        }
        mergeSortNoCopy(A, B, auxX, auxY, right + 1, 0);
    }

    /**
     * Modified merge sort:
     * Input arrays are in both auxX/auxY (sorted: 0 to insertionSortIndex)
     *                     and x/y (unsorted: insertionSortIndex to toIndex)
     * Outputs are stored in x/y arrays
     */
    static void mergeSortNoCopy(final int[] x, final int[] y,
                                final int[] auxX, final int[] auxY,
                                final int toIndex,
                                final int insertionSortIndex) {
/*
        if ((toIndex > x.length) || (toIndex > y.length)
                || (toIndex > auxX.length) || (toIndex > auxY.length)) {
            // explicit check to avoid bound checks within hot loops (below):
            throw new ArrayIndexOutOfBoundsException("bad arguments: toIndex="
                    + toIndex);
        }
*/
        // Original's Marlin merge sort:
        // sort second part only using merge / insertion sort
        // in auxiliary storage (auxX/auxY)
        mergeSort(x, y, x, auxX, y, auxY, insertionSortIndex, toIndex);

        // avoid extra copy to mimic proper array swaps (minimize loops):
        if (false) {
            return;
        }

        // final pass to merge both
        // Merge sorted parts (auxX/auxY) into x/y arrays
        if ((insertionSortIndex == 0)
                || (auxX[insertionSortIndex - 1] <= auxX[insertionSortIndex])) {
            // 34 occurences
            // no initial left part or both sublists (auxX, auxY) are sorted:
            // copy back data into (x, y):
            System.arraycopy(auxX, 0, x, 0, toIndex);
            System.arraycopy(auxY, 0, y, 0, toIndex);

            if (DO_CHECK) {
                checkRange(x, 0, toIndex);
            }
            return;
        }

        for (int i = 0, p = 0, q = insertionSortIndex; i < toIndex; i++) {
            if ((q >= toIndex) || ((p < insertionSortIndex)
                    && (auxX[p] <= auxX[q]))) {
                x[i] = auxX[p];
                y[i] = auxY[p];
                p++;
            } else {
                x[i] = auxX[q];
                y[i] = auxY[q];
                q++;
            }
        }

        if (DO_CHECK) {
            // validate:
            checkRange(x, 0, toIndex);
        }
    }

    // insertion sort threshold for MergeSort()
    private static final int INSERTION_SORT_THRESHOLD = 14;

    /**
     * Src is the source array that starts at index 0
     * Dest is the (possibly larger) array destination with a possible offset
     * low is the index in dest to start sorting
     * high is the end index in dest to end sorting
     */
    private static void mergeSort(final int[] refX, final int[] refY,
                                  final int[] srcX, final int[] dstX,
                                  final int[] srcY, final int[] dstY,
                                  final int low, final int high) {
        final int length = high - low;

        /*
         * Tuning parameter: list size at or below which insertion sort
         * will be used in preference to mergesort.
         */
        if (length <= INSERTION_SORT_THRESHOLD) {
            // Insertion sort on smallest arrays
            dstX[low] = refX[low];
            dstY[low] = refY[low];

            for (int i = low + 1, j = low, x, y; i < high; j = i++) {
                x = refX[i];
                y = refY[i];

                while (dstX[j] > x) {
                    // swap element
                    dstX[j + 1] = dstX[j];
                    dstY[j + 1] = dstY[j];
                    if (j-- == low) {
                        break;
                    }
                }
                dstX[j + 1] = x;
                dstY[j + 1] = y;
            }
            return;
        }

        // Recursively sort halves of dest into src
        // note: use signed shift (not >>>) for performance
        // as indices are small enough to exceed Integer.MAX_VALUE
        final int mid = (low + high) >> 1;

        mergeSort(refX, refY, dstX, srcX, dstY, srcY, low, mid);
        mergeSort(refX, refY, dstX, srcX, dstY, srcY, mid, high);

        // If arrays are inverted ie all(A) > all(B) do swap A and B to dst
        if (srcX[high - 1] <= srcX[low]) {
            // 1561 occurences
            final int left = mid - low;
            final int right = high - mid;
            final int off = (left != right) ? 1 : 0;
            // swap parts:
            System.arraycopy(srcX, low, dstX, mid + off, left);
            System.arraycopy(srcX, mid, dstX, low, right);
            System.arraycopy(srcY, low, dstY, mid + off, left);
            System.arraycopy(srcY, mid, dstY, low, right);
            return;
        }

        // If arrays are already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (srcX[mid - 1] <= srcX[mid]) {
            // 14 occurences
            System.arraycopy(srcX, low, dstX, low, length);
            System.arraycopy(srcY, low, dstY, low, length);
            return;
        }

        // Merge sorted halves (now in src) into dest
        for (int i = low, p = low, q = mid; i < high; i++) {
            if ((q >= high) || ((p < mid) && (srcX[p] <= srcX[q]))) {
                dstX[i] = srcX[p];
                dstY[i] = srcY[p];
                p++;
            } else {
                dstX[i] = srcX[q];
                dstY[i] = srcY[q];
                q++;
            }
        }
    }

    private static void checkRange(int[] x, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++) {
            if (x[i - 1] > x[i]) {
                System.out.println("Bad sorted x [" + (i - 1) + "]" + Arrays.toString(x));
                return;
            }
        }
    }
}
