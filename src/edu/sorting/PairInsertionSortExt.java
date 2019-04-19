/*
 * Copyright (c) 2009, 2019, Oracle and/or its affiliates. All rights reserved.
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

import static edu.sorting.DualPivotQuicksort20181121Ext.checkSorted;
import static edu.sorting.DualPivotQuicksort20190210Ext.pairInsertionSort;

/**
* InsertionSort only
*/
public final class PairInsertionSortExt implements wildinter.net.mergesort.Sorter {

    private final static boolean DO_CHECK = false;
        
    public final static wildinter.net.mergesort.Sorter INSTANCE = new PairInsertionSortExt();

    /**
     * Prevents instantiation.
     */
    private PairInsertionSortExt() {
    }

    // avoid alloc
    // fake B (ancillary data ie indices)
    private int[] B = null;
    private int[] auxA = null;
    private int[] auxB = null;
    
    // checks only
    private static int[] A_REF = null;
    private static int[] B_REF = null;

    @Override
    public void sort(final int[] A, final int low, final int high) {
        final int length = high - low + 1;
        if (B == null || B.length < length) {
            B = new int[length];
        }

        if (DO_CHECK) {
            if (A_REF == null || A_REF.length < length) {
                A_REF = new int[length];
            }
            if (B_REF == null || B_REF.length < length) {
                B_REF = new int[length];
            }
            for (int i = 0; i < length; i++) {
                B[i] = i;
                A_REF[i] = A[i];
                B_REF[i] = B[i];
            }
        }

        pairInsertionSort(A, B, low, high + 1, high + 1); // exclusive

        if (DO_CHECK) {
            checkSorted("sort(root)", A, B, low, high);
        }        
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
