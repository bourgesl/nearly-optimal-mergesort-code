package edu.sorting;

import java.util.Arrays;
import wildinter.net.mergesort.Sorter;

/**
 * Few Array utility functions
 * @author bourgesl
 */
public final class ArrayUtils {

    private final static boolean USE_MANUAL_COPY = true;

    private ArrayUtils() {
        // forbidden
    }

    static {
        if (Sorter.DO_CHECK) {
            System.out.println("DO_CHECKS enabled for Sorter(2 arrays) !");
        }
    }

    public static int[] clone(final int[] in, final int[] out) {
        if ((out != null) && (out.length == in.length)) {
            if (USE_MANUAL_COPY) {
                // may have smaller variance than calling native System.arraycopy()
                for (int i = out.length - 1; i >= 0; i--) {
                    out[i] = in[i];
                }
            } else {
                System.arraycopy(in, 0, out, 0, in.length);
            }
            return out;
        }
        if (true) {
            throw new IllegalStateException("given output array is invalid !");
        }
        System.out.println("clone: " + in.length);
        return in.clone();
    }

    static void checkSorted(String msg, int[] A, int[] B, int[] A_REF, int[] B_REF, int low, int high) {
        for (int i = low; i < high - 1; i++) {
            if (A[i + 1] < A[i]) {
                System.err.println(msg + " A NOT SORTED at " + i + " :: " + Arrays.toString(A));
                return;
            }
            if (A_REF[B[i]] != A[i]) {
                System.err.println(msg + " A NOT EQUAL TO A[B] at " + i);
                return;
            }
            if (A_REF[B[i + 1]] < A_REF[B[i]]) {
                System.err.println(msg + " B NOT SORTED at " + i);
                return;
            }
        }
        check(A, A_REF, low, high + 1);
        check(B, B_REF, low, high + 1);
//        System.out.println(msg + " checkSorted OK");
    }

    private static void check(int[] a, int[] ref, int low, int high) {
        long plusCheckSum1 = 0;
        long plusCheckSum2 = 0;
        long xorCheckSum1 = 0;
        long xorCheckSum2 = 0;

        for (int i = low; i < high; i++) {
            plusCheckSum1 += a[i];
            plusCheckSum2 += ref[i];
            xorCheckSum1 ^= a[i];
            xorCheckSum2 ^= ref[i];
        }
        if (plusCheckSum1 != plusCheckSum2) {
            System.out.println("A: " + Arrays.toString(a));
            System.out.println("R: " + Arrays.toString(ref));
            throw new RuntimeException("!!! Array is not sorted correctly [+].");
        }
        if (xorCheckSum1 != xorCheckSum2) {
            throw new RuntimeException("!!! Array is not sorted correctly [^].");
        }
    }
}
