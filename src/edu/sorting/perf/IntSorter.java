package edu.sorting.perf;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort201811;
import edu.sorting.DualPivotQuicksort201811P;
import org.marlin.MarlinSort;

/**
 * @author Jon Bentley
 */
public enum IntSorter {
    BASELINE {
        public void sort(int[] a) {
            a[0] = a[a.length - 1];
        }

        public boolean skipCheck() {
            return true;
        }

    },
    /*    
    SYSTEM {
        public void sort(int[] a) {
            Sorter.SYSTEMSORT.sort(a, 0, a.length - 1);
        }
    },
     */
    DPQ_18_11 {
        public void sort(int[] a) {
            DualPivotQuicksort201811.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_11P {
        public void sort(int[] a) {
            DualPivotQuicksort201811P.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    /*    
    DPQ_18_2 {
        public void sort(int[] a) {
            DualPivotQuicksort201802.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
     */
    DPQ_11 {
        public void sort(int[] a) {
            DualPivotQuickSort2011.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    /*    
    RADIX {
        public void sort(int[] a) {
            RadixSort.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
     */
    MARLIN {
        public void sort(int[] a) {
            MarlinSort.INSTANCE.sort(a, 0, a.length - 1);
        }
    }, /*    
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
    },
     */;

    public abstract void sort(int[] a);

    public boolean skipCheck() {
        return false;
    }
}
