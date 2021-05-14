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
import edu.sorting.DualPivotQuicksort20190809;
import edu.sorting.DualPivotQuicksort20191112;
import edu.sorting.DualPivotQuicksort20191112Ext;
import edu.sorting.DualPivotQuicksort20191112Fixed;
import edu.sorting.DualPivotQuicksort20210424;
import edu.sorting.DualPivotQuicksort202105;
import edu.sorting.DualPivotQuicksort202105InPlace;
import edu.sorting.InsertionSortExt;
import edu.sorting.RadixSort;
import java.util.Arrays;
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
            a[a.length - 1] = 0;
        }

        @Override
        public final boolean skipCheck() {
            return true;
        }
    },
    BASELINE_SUM {
        @Override
        public void sort(int[] a) {
            // touch all array (sum):
            int total = 0;
            for (int i = 0; i < a.length; i++) {
                total += a[i];
            }
            a[a.length - 1] = total;
        }

        @Override
        public final boolean skipCheck() {
            return true;
        }
    },
    SYSTEM {
        @Override
        public void sort(int[] a) {
            Arrays.sort(a, 0, a.length - 1);
        }
    },
    DPQS_2105_REF {
        @Override
        public void sort(int[] a) {
            edu.sorting.ref.Arrays.sort(a, 0, a.length - 1);
        }
    },
    DPQS_2105_NEW {
        @Override
        public void sort(int[] a) {
            edu.sorting.ref.ArraysNew.sort(a, 0, a.length - 1);
        }
    },
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
            DualPivotQuicksort201802.HYBRID.sort(a, 0, a.length - 1);
        }
    },
    DPQ_18_11 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201811.HYBRID.sort(a, 0, a.length - 1);
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
    DPQ_19_08_09 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20190809.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_19_11_12 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20191112.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_19_11_12_FIX {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20191112Fixed.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_21_04_24 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20210424.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_21_05 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort202105.INSTANCE.sort(a, 0, a.length - 1);
        }
    },
    DPQ_21_05_RA {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort202105.RADIX_ORIG.sort(a, 0, a.length - 1);
        }
    },
    DPQ_21_05_RA2 {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort202105.RADIX_NEW.sort(a, 0, a.length - 1);
        }
    },
    DPQ_21_05I_HYB {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort202105InPlace.HYBRID.sort(a, 0, a.length - 1);
        }
    },
    DPQ_21_05I_IN {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort202105InPlace.DPQS_ONLY.sort(a, 0, a.length - 1);
        }
    },
    DPQ_21_05I_RA {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort202105InPlace.RADIX_ONLY.sort(a, 0, a.length - 1);
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
            Qsorte.HYBRID.sort(a, 0, a.length - 1);
        }
    },
    DYN_PIVOT {
        @Override
        public void sort(int[] a) {
            DynPivotSort.HYBRID.sort(a, 0, a.length - 1);
        }
    },
    MERGE_TD {
        private final Sorter HYBRID = new TopDownMergesort(24, true);

        @Override
        public void sort(int[] a) {
            HYBRID.sort(a, 0, a.length - 1);
        }
    },
    MERGE_BU {
        private final Sorter HYBRID = new BottomUpMergesort(24, true);

        @Override
        public void sort(int[] a) {
            HYBRID.sort(a, 0, a.length - 1);
        }
    },
    TIM_TROT {
        @Override
        public void sort(int[] a) {
            TimsortTrot.HYBRID.sort(a, 0, a.length - 1);
        }
    },
    TIM_SD {
        public void sort(int[] a) {
            TimsortStrippedDown.HYBRID.sort(a, 0, a.length - 1);
        }
    },
    TIMSORT {
        public void sort(int[] a) {
            Timsort.HYBRID.sort(a, 0, a.length - 1);
        }
    },
    PEEK_SORT {
        private final Sorter HYBRID = new PeekSort(24, false);

        @Override
        public void sort(int[] a) {
            HYBRID.sort(a, 0, a.length - 1);
        }
    },
    POWER_SORT {
        private final Sorter HYBRID = new PowerSort(true, false, 24);

        public void sort(int[] a) {
            HYBRID.sort(a, 0, a.length - 1);
        }
    }
     */ // 2 arrays variants
    /*
    DPQ_18_2_E {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort201802Ext.HYBRID.sort(a, 0, a.length - 1);
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
            PairInsertionSortExt.HYBRID.sort(a, 0, a.length - 1);
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
    DPQ_19_11_E {
        @Override
        public void sort(int[] a) {
            DualPivotQuicksort20191112Ext.INSTANCE.sort(a, 0, a.length - 1);
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
