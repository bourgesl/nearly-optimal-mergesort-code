package edu.sorting.perf;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort20181121;
import edu.sorting.DualPivotQuicksort20181121Ext;
import edu.sorting.DualPivotQuicksort20181121I;
import edu.sorting.DualPivotQuicksort20181127;
import edu.sorting.DualPivotQuicksort201811P;
import edu.sorting.DualPivotQuicksort201901;
import edu.sorting.DualPivotQuicksort20190105;
import edu.sorting.DualPivotQuicksort20190210;
import edu.sorting.DualPivotQuicksort20190210Ext;
import edu.sorting.DualPivotQuicksort20190405;
import edu.sorting.DualPivotQuicksort20190501;
import edu.sorting.DualPivotQuicksort20190501Ext;
import edu.sorting.InsertionSortExt;
import edu.sorting.RadixSort;
import org.marlin.MarlinMergeSort;
import org.marlin.MarlinSort;

/**
 * @author Jon Bentley
 */
public enum IntSorter {
    /* baseline must stay at index 0 (first) */
    BASELINE {
        @Override
        public void sort(int[] a) {
            // touch array (minimal unitary operation):
            a[0] = 0;
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
    DPQ_11 {
        @Override
        public void sort(int[] a) {
            DualPivotQuickSort2011.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    /*    
    DPQ_18_2 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201802.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_11 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201811.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
     */
    DPQ_18_11_21 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20181121.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_11_27 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20181127.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_11I {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20181121I.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_11P {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201811P.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_19_01 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201901.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_19_01_05 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20190105.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_19_02_10 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20190210.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_19_04_05 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20190405.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_19_05_01 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20190501.INSTANCE.sort(a, 0, a.length - 1);
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

        @Override
        public final boolean skipCheck() {
            return true;
        }
    }, /*
    QSORTE {
        @Override
        public void sort(int[] a) {
            Qsorte.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
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
    PEEK_SORT {
        private final Sorter INSTANCE = new PeekSort(24, false);

        @Override
        public void sort(int[] a) {
            INSTANCE.sort(a, 0, a.length - 1);
        }
    }, 
    POWER_SORT {
        private final Sorter INSTANCE = new PowerSort(true, false, 24);

        public void sort(int[] a) {
            INSTANCE.sort(a, 0, a.length - 1);
        }
    }    
     */ // 2 arrays variants
    /*
    DPQ_18_2_E {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201802Ext.INSTANCE.sort(a, 0, a.length - 1);
        }
    }, */
    // Straight Insertion-Sort (very slow on large size)
    ISORT_E {
        @Override
        public void sort(int[] a) {
            InsertionSortExt.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    /*
    // Buggy
    P_ISORT_E {
        @Override
        public void sort(int[] a) {
            PairInsertionSortExt.INSTANCE.sort(a, 0, a.length - 1);
        }
    },    
     */
    DPQ_18_11_E {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20181121Ext.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_19_02_E {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20190210Ext.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_19_05_E {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20190501Ext.INSTANCE.sort(a, 0, a.length - 1);
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
