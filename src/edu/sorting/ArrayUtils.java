package edu.sorting;

/**
 * Few Array utility functions
 * @author bourgesl
 */
public final class ArrayUtils {

    private final static boolean USE_MANUAL_COPY = true;

    private ArrayUtils() {
        // forbidden
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
}
