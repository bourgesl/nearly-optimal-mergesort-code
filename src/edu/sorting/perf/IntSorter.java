package edu.sorting.perf;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort201802;
import edu.sorting.DualPivotQuicksort201811;
import edu.sorting.RadixSort;
import org.marlin.MarlinSort;
import wildinter.net.mergesort.BottomUpMergesort;
import wildinter.net.mergesort.Sorter;
import wildinter.net.mergesort.TopDownMergesort;

/**
 * @author Jon Bentley
 */
public enum IntSorter {
/*    
    SYSTEM {
        public void sort(int[] a) {
            Sorter.SYSTEMSORT.sort(a, 0, a.length - 1);
        }
    },
*/    
    DPQ_11 {
        public void sort(int[] a) {
            DualPivotQuickSort2011.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_11 {
        public void sort(int[] a) {
            DualPivotQuicksort201811.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_2 {
        public void sort(int[] a) {
            DualPivotQuicksort201802.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    RADIX {
        public void sort(int[] a) {
            RadixSort.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    MARLIN {
        public void sort(int[] a) {
            MarlinSort.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    MERGE_TD {
        private final Sorter INSTANCE = new TopDownMergesort(24, true);

        public void sort(int[] a) {
            INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    MERGE_BU {
        private final Sorter INSTANCE = new BottomUpMergesort(24, true);

        public void sort(int[] a) {
            INSTANCE.sort(a, 0, a.length - 1);
        }
    };

    public abstract void sort(int[] a);
}
