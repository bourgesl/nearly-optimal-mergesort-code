package edu.sorting;

/**
 * Few Array utility functions
 * @author bourgesl
 */
public final class ArrayUtils {

    private ArrayUtils() {
        // forbidden
    }
    
    public static int[] clone(final int[] in, final int[] out) {
        if ((out != null) && (out.length == in.length)) {
            System.arraycopy(in, 0, out, 0, in.length);
            return out;
        }
        if (true) {
            throw new IllegalStateException("given output array is invalid !");
        }
        System.out.println("clone: "+ in.length);
        return in.clone();
    }
}
