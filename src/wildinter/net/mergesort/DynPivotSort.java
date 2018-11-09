package wildinter.net.mergesort;

import static wildinter.net.mergesort.MarlinSort.mQuickSort;

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
        mQuickSort(A, left, right, A[right]); // last key
    }

}
