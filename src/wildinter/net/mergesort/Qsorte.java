package wildinter.net.mergesort;

import static wildinter.net.mergesort.MarlinSort.qsorte;

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
        qsorte(A, left, right, (left + right) >> 1);
    }

}
