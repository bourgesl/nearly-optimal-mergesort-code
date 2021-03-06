package edu.sorting;

import java.util.Arrays;
import wildinter.net.mergesort.Sorter;

public final class RadixSort implements Sorter {

    public final static Sorter INSTANCE = new RadixSort();

    /**
     * Prevents instantiation.
     */
    private RadixSort() {
    }

    // avoid alloc
    private int[] aux = null;
    private final int[] run = new int[0x400];

    @Override
    public void sort(int[] array, int offset, int end) {
        if (offset < 0 || end < offset || end >= array.length) {
            throw new IllegalArgumentException();
        }
        final int length = end - offset + 1;
        /* extra right increment ? */
        if (aux == null || aux.length < length) {
            aux = new int[length];
        }
        tryMerge(array, offset, end, aux, run);
    }

    @Override
    public String toString() {
        return "RadixSort";
    }

    private static final int INSERTION_SORT_THRESHOLD = 64;
    /**
     * The maximum number of runs in merge sort.
     */
    private static final int MAX_RUN_COUNT = 15;

    private static void insertionSort(int[] array, int offset, int end) {
        for (int x = offset; x < end; ++x) {
            for (int y = x; y > offset && array[y - 1] > array[y]; y--) {
                int temp = array[y];
                array[y] = array[y - 1];
                array[y - 1] = temp;
            }
        }
    }

    // Sorts byte#0 (least significant byte): unstable (stability is unnecessary here)
    // uses buf[0..0xFF]
    private static void sort3(int[] array, int offset, int end, int[] buf) {
        Arrays.fill(buf, 0, 0x100, 0);
        for (int x = offset; x < end; ++x) {
            ++buf[array[x] & 0xFF];
        }

        int pref = array[offset] & 0xFFFFFF00;
        for (int x = 0; x < 0x100; x++) {
            for (int c = buf[x]; c > 0; c--) {
                array[offset++] = pref | x;
            }
            if (offset == end) {
                return;
            }
        }
    }

    // Sorts byte#1
    // uses buf[0..0xFF] & buf[0x300..0x3FF]
    private static void sort2(int[] array, int offset, int end, int[] buf) {
        Arrays.fill(buf, 0, 0x100, 0);
        for (int x = offset; x < end; ++x) {
            ++buf[(array[x] >> 8) & 0xFF];
        }

        buf[0] += offset;
        buf[0x300] = offset;
        for (int x = 1; x < 0x100; ++x) {
            int off = buf[x - 1];
            buf[x + 0x300] = off;
            buf[x] += off;
        }

        for (int x = 0; x < 0x100; ++x) {
            int off = buf[x + 0x300];
            while (off != buf[x]) {
                int value = array[off];
                int y = (value >> 8) & 0xff;
                if (x == y) {
                    off++;
                } else {
                    while (x != y) {
                        int temp = array[buf[y + 0x300]];
                        array[buf[y + 0x300]++] = value;
                        value = temp;
                        y = (value >> 8) & 0xff;
                    }
                    array[off++] = value;
                }
            }
            buf[x + 0x300] = off;
        }
        for (int x = 0; x < 0x100; ++x) {
            int off = buf[x + 0x300];
            int size = x > 0 ? off - buf[x + 0x2FF] : off - offset;
            if (size > INSERTION_SORT_THRESHOLD) {
                sort3(array, off - size, off, buf);
            } else if (size > 1) {
                insertionSort(array, off - size, off);
            }
        }
    }

    // Sorts byte#2
    // uses buf[0..0xFF] & buf[0x200..0x2FF]
    private static void sort1(int[] array, int offset, int end, int[] buf) {
        Arrays.fill(buf, 0, 0x100, 0);
        for (int x = offset; x < end; ++x) {
            ++buf[(array[x] >> 16) & 0xFF];
        }

        buf[0] += offset;
        buf[0x200] = offset;
        for (int x = 1; x < 0x100; ++x) {
            int off = buf[x - 1];
            buf[x + 0x200] = off;
            buf[x] += off;
        }

        for (int x = 0; x < 0x100; ++x) {
            int off = buf[x + 0x200];
            while (off != buf[x]) {
                int value = array[off];
                int y = (value >> 16) & 0xff;
                if (x == y) {
                    off++;
                } else {
                    while (x != y) {
                        int temp = array[buf[y + 0x200]];
                        array[buf[y + 0x200]++] = value;
                        value = temp;
                        y = (value >> 16) & 0xff;
                    }
                    array[off++] = value;
                }
            }
            buf[x + 0x200] = off;
        }
        for (int x = 0; x < 0x100; ++x) {
            int off = buf[x + 0x200];
            int size = x > 0 ? off - buf[x + 0x1FF] : off - offset;
            if (size > INSERTION_SORT_THRESHOLD) {
                sort2(array, off - size, off, buf);
            } else if (size > 1) {
                insertionSort(array, off - size, off);
            }
        }
    }

    // Sorts byte#3 (most significant byte), taking sign into account
    // uses buf[0..0x1FF]
    private static void sort0(int[] array, int offset, int end, int[] buf) {
        Arrays.fill(buf, 0, MAX_RUN_COUNT + 1, 0);
        for (int x = offset; x < end; ++x) {
            ++buf[(array[x] >> 24) + 0x80];
        }

        buf[0] += offset;
        buf[0x100] = offset;
        for (int x = 1; x < 0x100; ++x) {
            int off = buf[x - 1];
            buf[x + 0x100] = off;
            buf[x] += off;
        }

        for (int x = 0; x < 0x100; ++x) {
            int off = buf[x + 0x100];
            while (off != buf[x]) {
                int value = array[off];
                int y = (value >> 24) + 0x80;
                if (x == y) {
                    off++;
                } else {
                    while (x != y) {
                        int temp = array[buf[y + 0x100]];
                        array[buf[y + 0x100]++] = value;
                        value = temp;
                        y = (value >> 24) + 0x80;
                    }
                    array[off++] = value;
                }
            }
            buf[x + 0x100] = off;
        }
        for (int x = 0; x < 0x100; ++x) {
            int off = buf[x + 0x100];
            int size = x > 0 ? off - buf[x + 0xFF] : off - offset;
            if (size > INSERTION_SORT_THRESHOLD) {
                sort1(array, off - size, off, buf);
            } else if (size > 1) {
                insertionSort(array, off - size, off);
            }
        }
    }

    private static void tryMerge(int[] a, final int left, int right, final int[] aux, final int[] run) {
        /*
         * Index run[i] is the start of i-th run
         * (ascending or descending sequence).
         */
//        int[] run = new int[0x400];
//        run[0] = left;
        Arrays.fill(run, 0);
        int count = 0;


        // Check if the array is nearly sorted
        for (int k = left; k < right; run[count] = k) {
            // Equal items in the beginning of the sequence
            while (k < right && a[k] == a[k + 1]) {
                k++;
            }
            if (k == right) {
                break;  // Sequence finishes with equal items
            }
            if (a[k] < a[k + 1]) { // ascending
                while (++k <= right && a[k - 1] <= a[k]);
            } else if (a[k] > a[k + 1]) { // descending
                while (++k <= right && a[k - 1] >= a[k]);
                // Transform into an ascending sequence
                for (int lo = run[count] - 1, hi = k; ++lo < --hi; ) {
                    int t = a[lo]; a[lo] = a[hi]; a[hi] = t;
                }
            }

            // Merge a transformed descending sequence followed by an
            // ascending sequence
            if (run[count] > left && a[run[count]] >= a[run[count] - 1]) {
                count--;
            }

            /*
             * The array is not highly structured,
             * use Quicksort instead of merge sort.
             */
            if (++count == MAX_RUN_COUNT) {
                sort0(a, left, right + 1, run);
                return;
            }
        }

        // These invariants should hold true:
        //    run[0] = 0
        //    run[<last>] = right + 1; (terminator)

        if (count == 0) {
            // A single equal run
            return;
        } else if (count == 1 && run[count] > right) {
            // Either a single ascending or a transformed descending run.
            // Always check that a final run is a proper terminator, otherwise
            // we have an unterminated trailing run, to handle downstream.
            return;
        }
        right++;
        if (run[count] < right) {
            // Corner case: the final run is not a terminator. This may happen
            // if a final run is an equals run, or there is a single-element run
            // at the end. Fix up by adding a proper terminator at the end.
            // Note that we terminate with (right + 1), incremented earlier.
            run[++count] = right;
        }

        // Determine alternation base for merge
        byte odd = 0;
        for (int n = 1; (n <<= 1) < count; odd ^= 1);

        // Use or create temporary array b for merging
        int[] b;                 // temp array; alternates with a
        int ao, bo;              // array offsets from 'left'
        int blen = right - left; // space needed for b

        final int[] work = (aux.length < blen) ? new int[blen] : aux; // LBO: avoid alloc
        int workBase = 0;
        if (odd == 0) {
            System.arraycopy(a, left, work, workBase, blen);
            b = a;
            bo = 0;
            a = work;
            ao = workBase - left;
        } else {
            b = work;
            ao = 0;
            bo = workBase - left;
        }

        // Merging
        for (int last; count > 1; count = last) {
            for (int k = (last = 0) + 2; k <= count; k += 2) {
                int hi = run[k], mi = run[k - 1];
                for (int i = run[k - 2], p = i, q = mi; i < hi; ++i) {
                    if (q >= hi || p < mi && a[p + ao] <= a[q + ao]) {
                        b[i + bo] = a[p++ + ao];
                    } else {
                        b[i + bo] = a[q++ + ao];
                    }
                }
                run[++last] = hi;
            }
            if ((count & 1) != 0) {
                for (int i = right, lo = run[count - 1]; --i >= lo;
                    b[i + bo] = a[i + ao]
                );
                run[++last] = right;
            }
            int[] t = a; a = b; b = t;
            int o = ao; ao = bo; bo = o;
        }
    }
}
