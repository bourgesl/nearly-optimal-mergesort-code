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
        System.out.println("arrayXorOriginal: " + test.arrayXorOriginal(state));
        System.out.println("arrayXor_Masked: " + test.arrayXor_Masked(state));
        System.out.println("arrayXor_Masked_2: " + test.arrayXor_Masked_2(state));
        System.out.println("arrayXor_Masked_3: " + test.arrayXor_Masked_3(state));
        System.out.println("arrayXor_Masked_4: " + test.arrayXor_Masked_4(state));

        System.out.println("array4_Original: " + test.array4_Original(state));
        System.out.println("array4_Unsafe: " + test.array4_Unsafe(state));
        System.out.println("array4_Unsafe2: " + test.array4_Unsafe2(state));
    }
}
