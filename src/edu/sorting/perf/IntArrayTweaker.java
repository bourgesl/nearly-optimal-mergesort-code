package edu.sorting.perf;

import edu.sorting.ArrayUtils;
import java.util.Arrays;

/**
 * @author Jon Bentley
 */
public enum IntArrayTweaker {

    IDENT_____ {
        public int[] tweak(int[] a, int out[]) {
            int[] result = ArrayUtils.clone(a, out);
            return result;
        }
    },
    REVERSE___ {
        public int[] tweak(int[] a, int out[]) {
            int[] result = ArrayUtils.clone(a, out);
            reverse(result, 0, result.length);
            return result;
        }
    },
    REVERSE_FR {
        public int[] tweak(int[] a, int out[]) {
            int[] result = ArrayUtils.clone(a, out);
            reverse(result, 0, result.length / 2);
            return result;
        }
    },
    REVERSE_BA {
        public int[] tweak(int[] a, int out[]) {
            int[] result = ArrayUtils.clone(a, out);
            reverse(result, result.length / 2, result.length);
            return result;
        }
    },
    SORT______ {
        public int[] tweak(int[] a, int out[]) {
            int[] result = ArrayUtils.clone(a, out);
            Arrays.sort(result);
            return result;
        }
    },
    DITHER____ {
        public int[] tweak(int[] a, int out[]) {
            int[] result = ArrayUtils.clone(a, out);
            for (int i = 0; i < result.length; i++) {
                result[i] += (i % 5);
            }
            return result;
        }
    };

    public abstract int[] tweak(int[] a, int out[]);

    private static void reverse(int[] result, int start, int end) {
        end -= 1;

        while (start < end) {
            int tmp = result[start];
            result[start++] = result[end];
            result[end--] = tmp;
        }
    }
}
