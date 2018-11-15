package org.marlin;

import wildinter.net.mergesort.Insertionsort;
import wildinter.net.mergesort.MergesAndRuns;
import wildinter.net.mergesort.Sorter;

/**
 *
 * @author bourgesl
 */
public final class MarlinSort implements Sorter {

    public final static Sorter INSTANCE = new MarlinSort();

    /**
     * Prevents instantiation.
     */
    private MarlinSort() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    // insertion sort threshold
    private static final int INSERTION_SORT_THRESHOLD = 14;

    // avoid alloc
    private int[] aux = null;

    @Override
    public void sort(final int[] A, final int left, final int right) {
        if (aux == null || aux.length < A.length) {
            aux = new int[A.length];
        }
        mergeSortNoCopy(A, aux, right + 1);
    }

    /**
     * Modified merge sort:
     * Input arrays are in both auxX/auxY (sorted: 0 to insertionSortIndex)
     *                     and x/y (unsorted: insertionSortIndex to toIndex)
     * Outputs are stored in x/y arrays
     */
    static void mergeSortNoCopy(final int[] x,
                                final int[] auxX,
                                final int toIndex) {

        if ((toIndex > x.length)
                || (toIndex > auxX.length)) {
            // explicit check to avoid bound checks within hot loops (below):
            throw new ArrayIndexOutOfBoundsException("bad arguments: toIndex="
                    + toIndex);
        }

        // Original's Marlin merge sort:
        // sort second part only using merge / insertion sort
        // in auxiliary storage (auxX/auxY)
        mergeSort(x, x, auxX, 0, toIndex);

        // final pass to merge both
        // Merge sorted parts (auxX/auxY) into x/y arrays
        // 34 occurences
        // no initial left part or both sublists (auxX, auxY) are sorted:
        // copy back data into (x, y):
        if (MergesAndRuns.COUNT_MOVE_COSTS) {
            MergesAndRuns.totalMoveCosts += toIndex;
        }
        System.arraycopy(auxX, 0, x, 0, toIndex);
    }

    /**
     * Src is the source array that starts at index 0
     * Dest is the (possibly larger) array destination with a possible offset
     * low is the index in dest to start sorting
     * high is the end index in dest to end sorting
     */
    private static void mergeSort(final int[] refX,
                                  final int[] srcX, final int[] dstX,
                                  final int low, final int high) {
        final int length = high - low;

        /*
         * Tuning parameter: list size at or below which insertion sort
         * will be used in preference to mergesort.
         */
        if (length <= INSERTION_SORT_THRESHOLD) {
            // Insertion sort on smallest arrays
            if (MergesAndRuns.COUNT_MOVE_COSTS) {
                MergesAndRuns.totalMoveCosts += 1;
            }
            dstX[low] = refX[low];

            for (int i = low + 1, j, v; i < high; ++i) {
                v = refX[i];
                for (j = i - 1; v < dstX[j];) {
                    if (MergesAndRuns.COUNT_MOVE_COSTS) {
                        MergesAndRuns.totalMoveCosts += 1;
                    }
                    dstX[j + 1] = dstX[j];
                    if (--j < low) {
                        break;
                    }
                }
                if (MergesAndRuns.COUNT_MOVE_COSTS) {
                    MergesAndRuns.totalMoveCosts += 1;
                }
                dstX[j + 1] = v;
            }
            return;
        }

        // Recursively sort halves of dest into src
        // note: use signed shift (not >>>) for performance
        // as indices are small enough to exceed Integer.MAX_VALUE
        final int mid = (low + high) >>> 1;

        mergeSort(refX, dstX, srcX, low, mid);
        mergeSort(refX, dstX, srcX, mid, high);

        // If arrays are inverted ie all(A) > all(B) do swap A and B to dst
        if (srcX[high - 1] <= srcX[low]) {
            // 1561 occurences
            final int left = mid - low;
            final int right = high - mid;
            final int off = (left != right) ? 1 : 0;
            // swap parts:
            if (MergesAndRuns.COUNT_MOVE_COSTS) {
                MergesAndRuns.totalMoveCosts += left + right;
            }
            System.arraycopy(srcX, low, dstX, mid + off, left);
            System.arraycopy(srcX, mid, dstX, low, right);
            return;
        }

        // If arrays are already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (srcX[mid - 1] <= srcX[mid]) {
            // 14 occurences
            if (MergesAndRuns.COUNT_MOVE_COSTS) {
                MergesAndRuns.totalMoveCosts += length;
            }
            System.arraycopy(srcX, low, dstX, low, length);
            return;
        }

        // Merge sorted halves (now in src) into dest
        for (int i = low, p = low, q = mid; i < high; i++) {
            if ((q >= high) || ((p < mid) && (srcX[p] <= srcX[q]))) {
                if (MergesAndRuns.COUNT_MOVE_COSTS) {
                    MergesAndRuns.totalMoveCosts += 1;
                }
                dstX[i] = srcX[p];
                p++;
            } else {
                if (MergesAndRuns.COUNT_MOVE_COSTS) {
                    MergesAndRuns.totalMoveCosts += 1;
                }
                dstX[i] = srcX[q];
                q++;
            }
        }
    }

    /* From Mainwright 1987
    Quicksort algorithms with an early exit for sorted subfiles
     */
    public static void qsorte(final int[] srcX,
                              final int m, final int n, int pivot_loc) {

        final int len = n - m;
        if (len > 0) {
            if (len <= INSERTION_SORT_THRESHOLD) {
                // Insertion sort on smallest arrays
                if (true) {
                    Insertionsort.insertionsort(srcX, m, n);
                } else {
                    for (int i = m + 1, j, x, curx = srcX[m]; i <= n; i++) {
                        x = srcX[i];

                        if (x < curx) {
                            j = i - 1;
                            for (;;) {
                                // swap element
                                srcX[j + 1] = srcX[j];
                                if ((j-- == m) || (x >= srcX[j])) {
                                    break;
                                }
                            }
                            srcX[j + 1] = x;
                        } else {
                            curx = x;
                        }
                    }
                }
            } else {
                boolean flag = true;

                int pivot = srcX[pivot_loc];
                int i = m;
                int j = n;
                boolean lsorted = true, rsorted = true;
                int t;

                while (flag) {
                    while (srcX[i] < pivot) {
                        if (lsorted) {
                            if (i > m) {
                                if (srcX[i] < srcX[i - 1]) {
                                    lsorted = false;
                                }
                            }
                        }
                        i += 1;
                    }

                    while ((j >= m) && (srcX[j] >= pivot)) {
                        if (rsorted) {
                            if (j < n) {
                                if (srcX[j] > srcX[j + 1]) {
                                    rsorted = false;
                                }
                            }
                        }
                        j -= 1;
                    }

                    if (i < j) {
                        // swap elements i and j
                        t = srcX[i];
                        srcX[i] = srcX[j];
                        srcX[j] = t;

                        if (i == pivot_loc) {
                            pivot_loc = j;
                        }

                        if (lsorted) {
                            if (i > m) {
                                if (srcX[i] < srcX[i - 1]) {
                                    lsorted = false;
                                }
                            }
                        }
                        if (rsorted) {
                            if (j < n) {
                                if (srcX[j] > srcX[j + 1]) {
                                    rsorted = false;
                                }
                            }
                        }
                    } else {
                        flag = false;
                    }
                } // while flag

                if (!rsorted) {
                    // swap elements i and pivot_loc
                    t = srcX[i];
                    srcX[i] = srcX[pivot_loc];
                    srcX[pivot_loc] = t;

                    i += 1;
                }

                if (!lsorted) {
                    qsorte(srcX, m, j, (m + j) >> 1);
                }
                if (!rsorted) {
                    qsorte(srcX, i, n, (i + n) >> 1);
                }
            }
        }
    }

    /*
    From Wulfenia journal:
    Enhancing Quicksort algorithm using a dynamic pivot selection technique
     */
    public static void mQuickSort(final int[] srcX,
                                  final int low, final int high, final int pivot) {

        final int len = high - low;
        if (len > 0) {
            if (len <= INSERTION_SORT_THRESHOLD) {
                // Insertion sort on smallest arrays
                if (true) {
                    Insertionsort.insertionsort(srcX, low, high);
                } else {
                    for (int i = low + 1, j, x, curx = srcX[low]; i <= high; i++) {
                        x = srcX[i];

                        if (x < curx) {
                            j = i - 1;
                            for (;;) {
                                // swap element
                                srcX[j + 1] = srcX[j];
                                if ((j-- == low) || (x >= srcX[j])) {
                                    break;
                                }
                            }
                            srcX[j + 1] = x;
                        } else {
                            curx = x;
                        }
                    }
                }
            } else {
                boolean sorted = true; // n
                int i = low;
                int j = high;

                int countLess = 0, countLarger = 0;
                long sumLess = 0L, sumLarger = 0L;
                int k = srcX[high];
                int t;

                while (i <= j) {
                    if (srcX[i] <= pivot) {
                        countLess++;
                        sumLess += srcX[i];

                        if (sorted && (k >= (pivot - srcX[i]))) {
                            // k increasing => elements are increasing in sorted order
                            k = (pivot - srcX[i]);
                        } else {
                            sorted = false;
                        }
                        i++;
                    } else {
                        countLarger++;
                        sumLarger += srcX[i];

                        // swap elements i and j
                        t = srcX[i];
                        srcX[i] = srcX[j];
                        srcX[j] = t;
                        j--;
                    }
                }
                if (countLess != 0) {
                    if (!sorted) {
                        // subarray is not sorted
                        mQuickSort(srcX, low, i - 1, (int) (sumLess / countLess));
                    }
                }
                if (countLarger != 0) {
                    mQuickSort(srcX, i, high, (int) (sumLarger / countLarger));
                }
            }
        }
    }
}
