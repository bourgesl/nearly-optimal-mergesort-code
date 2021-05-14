/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/
package edu.sorting.bench;

/**
 *
 * @author bourgesl
 */
public class Test {

    public static void main(String[] unused) {
        final ArrayXorBenchmark test = new ArrayXorBenchmark();

        final ArrayXorBenchmark.ThreadState state = new ArrayXorBenchmark.ThreadState();
        state.size = 1000;
        state.setUp();

        System.out.println("arrayXor_Unsafe: " + test.arrayXor_Unsafe(state));
        System.out.println("arrayXor_Masked: " + test.arrayXor_Masked(state));
        System.out.println("arrayXorOriginal: " + test.arrayXorOriginal(state));
    }
}
