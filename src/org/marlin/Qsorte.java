package org.marlin;

import wildinter.net.mergesort.Sorter;

/**
 *
 * @author bourgesl
 */
public final class Qsorte implements Sorter {

    public Qsorte() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    // insertion sort threshold
    public static final int INSERTION_SORT_THRESHOLD = 24;

    @Override
    public void sort(final int[] A, final int left, final int right) {
        MarlinSort.qsorte(A, left, right, (left + right) >> 1);
    }

}
