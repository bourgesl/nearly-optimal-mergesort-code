package edu.sorting.perf;

import java.util.Arrays;

public enum IntArrayTweaker {

    IDENT_____ { public int[] tweak(int[] a) { int[] result = a.clone();                                                               return result; }},
    REVERSE___ { public int[] tweak(int[] a) { int[] result = a.clone(); reverse(result, 0, result.length);                            return result; }},
    REVERSE_FR { public int[] tweak(int[] a) { int[] result = a.clone(); reverse(result, 0, result.length / 2);                        return result; }},
    REVERSE_BA { public int[] tweak(int[] a) { int[] result = a.clone(); reverse(result, result.length / 2, result.length);            return result; }},
    SORT______ { public int[] tweak(int[] a) { int[] result = a.clone(); Arrays.sort(result);                                          return result; }},
    DITHER____ { public int[] tweak(int[] a) { int[] result = a.clone(); for (int i = 0; i < result.length; i++) result[i] += (i % 5); return result; }};

    public abstract int[] tweak(int[] a);

    private static void reverse(int[] result, int start, int end) {
        end -= 1;

        while (start < end) {
            int tmp = result[start];
            result[start++] = result[end];
            result[end--] = tmp;
        }
    }
}
