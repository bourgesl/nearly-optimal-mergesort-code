package wildinter.net.mergesort;

import java.util.Arrays;

public class RadixSort implements Sorter {
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
        for (int x=0; x<0x100; x++) {
            for(int c = buf[x]; c > 0; c--) 
                array[offset++] = pref | x;
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
                if(x == y) {
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
                if(x == y) {
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
        Arrays.fill(buf, 0, MAX_RUN_COUNT+1, 0);
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
                if(x == y) {
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

    private static void tryMerge(int[] a, int left, int right) {
        /*
         * Index run[i] is the start of i-th run
         * (ascending or descending sequence).
         */
        int[] run = new int[0x400];
        int count = 0; run[0] = left;

        // Check if the array is nearly sorted
        for (int k = left; k < right; run[count] = k) {
            while (k < right && a[k] == a[k + 1]) k++;
            if (a[k] > a[k + 1]) { // descending
                while (++k <= right && a[k - 1] >= a[k]) ;
                for (int lo = run[count] - 1, hi = k; ++lo < --hi && a[lo] != a[hi]; ) {
                    int t = a[lo];
                    a[lo] = a[hi];
                    a[hi] = t;
                }
            } else {
                while (++k <= right && a[k - 1] <= a[k]) ;
            }

            /*
             * The array is not highly structured,
             * use Radixsort instead of merge sort.
             */
            if (++count == MAX_RUN_COUNT) {
                sort0(a, left, right+1, run);
                return;
            }
        }

        // Check special cases
        // Implementation note: variable "right" is increased by 1.
        if (run[count] == right++) { // The last run contains one element
            run[++count] = right;
        } else if (count == 1) { // The array is already sorted
            return;
        }

        // Determine alternation base for merge
        byte odd = 0;
        for (int n = 1; (n <<= 1) < count; odd ^= 1);

        // Use or create temporary array b for merging
        int[] b;                 // temp array; alternates with a
        int ao, bo;              // array offsets from 'left'
        int blen = right - left; // space needed for b
        int[] work = new int[blen];
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

    public void sort(int[] array, int offset, int end) {
        if (offset < 0 || end < offset || end >= array.length) {
            throw new IllegalArgumentException();
        }
        tryMerge(array, offset, end);
    }

    public void sort(int[] array) {
        tryMerge(array, 0, array.length-1);
    }

    @Override
    public String toString() {
        return "RadixSort";
    }

    public static void main(String[] args) {
        
    }
}