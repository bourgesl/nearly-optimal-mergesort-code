package edu.sorting.perf;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort201802;
import edu.sorting.DualPivotQuicksort201811;

/**
 * @author Jon Bentley
 */
public enum IntSorter {
/*
    DPQ_2011 {
        public void sort(int[] a) {
            DualPivotQuickSort2011.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
*/
    DPQ_2018_2 {
        public void sort(int[] a) {
            DualPivotQuicksort201802.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_2018_11 {
        public void sort(int[] a) {
            DualPivotQuicksort201811.INSTANCE.sort(a, 0, a.length - 1);
        }
    };

    public abstract void sort(int[] a);
}
