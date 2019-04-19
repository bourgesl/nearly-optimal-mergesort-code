package edu.sorting.perf;

import java.util.Arrays;

/**
 * Basic integer distribution test
 */
public final class IntArrayTest {

    private IntArrayTest() {
        // no-op
    }

    public static void main(String[] unused) {
        final int n = 100;

        final int[] input = new int[n];
        final int[] proto = new int[n];

        System.out.println("n: " + n);

        for (int m = 1, end = 2 * n; m < end; m *= 4) {
            System.out.println("\nn: " + n + " m: " + m);

            for (ParamIntArrayBuilder iab : ParamIntArrayBuilder.values()) {

                // Get new distribution sample:
                iab.build(input, m);

                System.out.println("Input(" + m + ")[" + iab.name() + "] : " + Arrays.toString(input));

                for (IntArrayTweaker iat : IntArrayTweaker.values()) {

                    // Reset tweaker to have sample initial conditions (seed):
                    ParamIntArrayBuilder.reset();

                    // tweak sample:
                    iat.tweak(input, proto);

                    System.out.println("Proto(" + m + ")[" + iab.name() + " - " + iat.name() + "] : " + Arrays.toString(proto));
                }
            }
        }
    }
}
