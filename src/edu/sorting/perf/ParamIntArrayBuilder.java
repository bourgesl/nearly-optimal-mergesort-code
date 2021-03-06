package edu.sorting.perf;

import java.util.Random;

/**
 * @author Jon Bentley
 */
public enum ParamIntArrayBuilder {

    SPIRAL {
        int element(int i, int m, int n) {
            return n + i * ((i % (m + 1) > (m / 2)) ? 1 : -1);
        }
    },
    STAGGER {
        int element(int i, int m, int n) {
            return (i * m + i) % n;
        }
    },
    SAWTOTH {
        int element(int i, int m, int n) {
            return i % m;
        }
    },
    _RANDOM {
        int element(int i, int m, int n) {
            return rnd.nextInt(m);
        }
    },
    PLATEAU {
        int element(int i, int m, int n) {
            return Math.min(i, m);
        }
    },
    SHUFFLE {
        int element(int i, int m, int n) {
            return rnd.nextInt(m) > 0 ? (j += 2) : (k += 2);
        }
    };

    abstract int element(int i, int m, int n);

    public void build(int[] result, int m) {
        final int len = result.length;
        
        if (m <= 1) {
            // ensure enough data (not all zero):
            m = 2;
        }

        for (int i = 0; i < len; i++) {
            result[i] = element(i, m, len);
        }
    }

    public static void reset() {
        rnd = newRandom();
        j = 0;
        k = 1;
    }

    private static Random newRandom() {
        return new Random(0x777);
    }

    private static Random rnd = newRandom();
    private static int j = 0;
    private static int k = 1;
}
