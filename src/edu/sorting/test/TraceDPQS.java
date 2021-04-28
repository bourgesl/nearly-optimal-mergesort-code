/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/
package edu.sorting.test;

import edu.sorting.DualPivotQuickSort2011;
import edu.sorting.DualPivotQuicksort20210424;
import edu.sorting.perf.IntArrayTweaker;
import edu.sorting.perf.ParamIntArrayBuilder;
import java.util.Arrays;

/**
 *
 * @author bourgesl
 */
public class TraceDPQS {

    private final static int M = 1000000;
    private final static int N = 1;

    private final static int SUBSIZE = 1;

    public static void main(String[] args) {
        test(ParamIntArrayBuilder.SHUFFLE, IntArrayTweaker.DITHER____, Impl.DPQS_24);
    }

    public static void test(ParamIntArrayBuilder iab, IntArrayTweaker iat, Impl impl) {
        final int[] input = new int[M];
        final int[] proto = new int[M];

        // Get new distribution sample:
        iab.build(input, SUBSIZE);

        // Reset tweaker to have sample initial conditions (seed):
        ParamIntArrayBuilder.reset();

        // tweak sample:
        iat.tweak(input, proto);

        final int[] data = proto;
        // copy
        final int[] copy = Arrays.copyOf(data, M);

        if (true) {
            System.out.println("data[0-512]: " + Arrays.toString(Arrays.copyOfRange(data, 0, 512)));
            System.out.println("data[-100:0]: " + Arrays.toString(Arrays.copyOfRange(data, M - 100, M)));
        }

        System.out.println("Test[" + iab + " | " + iat + " | " + impl + "] M=" + M + "----------------------------------------------------------");
        System.arraycopy(data, 0, copy, 0, copy.length);
        impl.sort(data);
        Arrays.sort(copy);

        if (!Arrays.equals(data, copy)) {
            for (int j = 0; j < M; j++) {
                if (data[j] != copy[j]) {
                    System.out.println("Mismatch[" + j + "]: " + data[j] + " != " + copy[j]);
                }
            }
            System.out.flush();
            throw new IllegalStateException("Bad sort : " + impl);
        }
    }

    enum Impl {

        DPQS_24 {
            @Override
            void sort(int[] data) {
                DualPivotQuicksort20210424.INSTANCE.sort(data);
            }
        },
        DPQS_11 {
            @Override
            void sort(int[] data) {
                DualPivotQuickSort2011.INSTANCE.sort(data);
            }
        };

        abstract void sort(int[] data);
    }
}
