package edu.sorting.perf;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort201802;

public enum IntSorter {

    DPQ_1 {
        public void sort(int[] a) {
            DualPivotQuickSort2011.sortORIG(a, 0, a.length);
        }
    },
    DPQ_2 {
        public void sort(int[] a) {
            DualPivotQuicksort201802.sortORIG(a, 0, a.length);
        }
    };

    public abstract void sort(int[] a);
}
