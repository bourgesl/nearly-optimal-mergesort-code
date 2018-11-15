package org.marlin;

import wildinter.net.mergesort.Sorter;

/**
 *
 * @author bourgesl
 */
public final class DynPivotSort implements Sorter {

    public DynPivotSort() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public void sort(final int[] A, final int left, final int right) {
        MarlinSort.mQuickSort(A, left, right, A[right]); // last key
    }

}
