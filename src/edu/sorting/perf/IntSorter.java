package edu.sorting.perf;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort201802;
import edu.sorting.DualPivotQuicksort201802Ext;
import edu.sorting.DualPivotQuicksort201811;
import edu.sorting.DualPivotQuicksort201811P;
import edu.sorting.RadixSort;
import org.marlin.MarlinMergeSort;
import org.marlin.MarlinSort;
import org.marlin.Qsorte;
import wildinter.net.mergesort.BottomUpMergesort;
import wildinter.net.mergesort.PeekSort;
import wildinter.net.mergesort.Sorter;
import wildinter.net.mergesort.TimsortTrot;

/**
 * @author Jon Bentley
 */
public enum IntSorter {
    /* baseline must stay at index 0 (first) */
    BASELINE {
        @Override
        public void sort(int[] a) {
            a[0] = a[a.length - 1];
        }

        @Override
        public final boolean skipCheck() {
            return true;
        }
    },
    /*    
    SYSTEM {
        @Override
        public void sort(int[] a) {
            Sorter.SYSTEMSORT.sort(a, 0, a.length - 1);
        }
    },
     */
    DPQ_18_11 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201811.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_11P {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201811P.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_2 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201802.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_11 {
        @Override
        public void sort(int[] a) {
            DualPivotQuickSort2011.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    RADIX {
        @Override
        public void sort(int[] a) {
            RadixSort.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    MARLIN {
        @Override
        public void sort(int[] a) {
            MarlinSort.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    QSORTE {
        @Override
        public void sort(int[] a) {
            Qsorte.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    /*    
    DYN_PIVOT {
        @Override
        public void sort(int[] a) {
            DynPivotSort.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    MERGE_TD {
        private final Sorter INSTANCE = new TopDownMergesort(24, true);

        @Override
        public void sort(int[] a) {
            INSTANCE.sort(a, 0, a.length - 1);
        }
    },
     */
    MERGE_BU {
        private final Sorter INSTANCE = new BottomUpMergesort(24, true);

        @Override
        public void sort(int[] a) {
            INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    TIM_TROT {
        @Override
        public void sort(int[] a) {
            TimsortTrot.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    /*    
    TIM_SD {
        public void sort(int[] a) {
            TimsortStrippedDown.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    TIMSORT {
        public void sort(int[] a) {
            Timsort.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
     */
    PEEK_SORT {
        private final Sorter INSTANCE = new PeekSort(24, false);

        @Override
        public void sort(int[] a) {
            INSTANCE.sort(a, 0, a.length - 1);
        }
    }, /*    
    POWER_SORT {
        private final Sorter INSTANCE = new PowerSort(true, false, 24);

        public void sort(int[] a) {
            INSTANCE.sort(a, 0, a.length - 1);
        }
    }    
     */
    // 2 arrays variants
    DPQ_18_11_E {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201802Ext.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    MARLIN_M2 {
        @Override
        public void sort(int[] a) {
            MarlinMergeSort.INSTANCE.sort(a, 0, a.length - 1);
        }
    };

    public abstract void sort(int[] a);

    public boolean skipCheck() {
        return false;
    }
}
