package edu.sorting.perf;

import java.util.Random;

public enum ParamIntArrayBuilder {

    STAGGER { int element(int i, int m, int n) { return (i * m + i) % n; }},
    SAWTOTH { int element(int i, int m, int n) { return i % m; }},
    _RANDOM { int element(int i, int m, int n) { return rnd.nextInt(m); }},
    PLATEAU { int element(int i, int m, int n) { return Math.min(i, m); }},
    SHUFFLE { int element(int i, int m, int n) { return rnd.nextInt(m) > 0 ? (j += 2) : (k += 2); }};

    abstract int element(int i, int m, int n);

    public int[] build(int len, int m) {
        int[] result = new int[len];

        for (int i = 0; i < len; i++) {
            result[i] = element(i, m, len);
        }
        return result;
    }

    private static int j = 0, k = 1;
    private static Random rnd = new Random(0x777);
}
