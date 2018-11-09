package wildinter.net.mergesort;

import java.util.Arrays;
import java.util.Random;
import static wildinter.net.mergesort.MergesAndRuns.mergeRuns;

/**
 * Simple top-down mergesort implementation.
 *
 * Recursion is stopped at subproblems of sizes at most insertionsortThreshold;
 * those are sorted by straight insertion sort.
 * If doSortedCheck is true, we check if two runs are by chance already
 * in sorted order before two runs are merged (compare last of left run with
 * first of right run).
 *
 * @author Sebastian Wild (wild@uwaterloo.ca)
 */
public class TopDownMergesort implements Sorter {

    private final int myInsertionsortThreshold;
    private final boolean doSortedCheck;

    public TopDownMergesort(final int insertionsortThreshold, final boolean doSortedCheck) {
        this.myInsertionsortThreshold = insertionsortThreshold;
        this.doSortedCheck = doSortedCheck;
    }

    // avoid alloc
    private int[] aux = null;

    @Override
    public void sort(final int[] A, final int left, final int right) {
//        insertionsortThreshold = myInsertionsortThreshold;
//		int[] buffer = new int[(right - left + 1)];
        if (aux == null || aux.length < (right - left + 1)) {
            aux = new int[(right - left + 1)];
        }
        //makeRunsAscending(A, left, right);
        if (doSortedCheck) {
            mergesortCheckSorted(A, left, right, aux);
        } else {
            mergesort(A, left, right, aux);
        }
    }

    private static final int insertionsortThreshold = 24;

    public static void mergesortCheckSorted(final int[] A, final int left, final int right, final int[] buffer) {
        final int n = right - left + 1;
        if (n <= insertionsortThreshold) {
            Insertionsort.insertionsort(A, left, right);
        } else {
            final int m = left + (n >> 1);
            mergesortCheckSorted(A, left, m - 1, buffer);
            mergesortCheckSorted(A, m, right, buffer);

            if (A[m - 1] > A[m]) {
                mergeRuns(A, left, m, right, buffer);
            }
        }
    }

    public static void mergesort(int[] A, int left, int right, final int[] buffer) {
        int n = right - left + 1;
        if (n <= insertionsortThreshold) {
            Insertionsort.insertionsort(A, left, right);
            return;
        }
        int m = left + (n >> 1);
        mergesort(A, left, m - 1, buffer);
        mergesort(A, m, right, buffer);
        mergeRuns(A, left, m, right, buffer);
    }

    private static void makeRunsAscending(final int[] A, final int lo, final int hi) {
//        System.out.println("makeRunsAscending: " + Arrays.toString(A));
        int runLo = lo;
        int runHi = lo + 1;
//        int c = 0;
        while (++runHi <= hi) {
//            System.out.println("A[runHi]: " + A[runHi]);
            // Find end of run, and reverse range if descending
            if (A[runHi] < A[runLo]) { // Descending
//                System.out.println("Down: " + A[runHi]);
                while (runHi <= hi && A[runHi] < A[runHi - 1]) {
//                    System.out.println("Down: " + A[runHi]);
                    runHi++;
                }
//                System.out.println("reverse: [" + runLo + " - " + runHi + "]");
                reverseRange(A, runLo, runHi);
            } else {
                // Ascending
                while (runHi <= hi && A[runHi] >= A[runHi - 1]) {
                    runHi++;
                }
            }
            runLo = runHi;
//            System.out.println("A[runLo]: " + A[runLo]);
//            c++;
        }
//        System.out.println("makeRunsAscending: " + c + " = " + Arrays.toString(A));
    }

    /**
     * Reverse the specified range of the specified array.
     *
     * @param A the array in which a range is to be reversed
     * @param lo the index of the first element in the range to be reversed
     * @param hi the index after the last element in the range to be reversed
     */
    private static void reverseRange(final int[] A, int lo, int hi) {
        hi--;
        while (lo < hi) {
            int t = A[lo];
            A[lo++] = A[hi];
            A[hi--] = t;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "+iscutoff=" + myInsertionsortThreshold + "+checkSorted=" + doSortedCheck;
    }

    public static void main(String[] args) {
        final Random random = new Random();
        final TopDownMergesort sort = new TopDownMergesort(24, true);

        int[] A = Inputs.randomPermutation(30, random);
//		A = new int[] {3, 7, 1, 2, 6, 9, 5, 8, 4};
        System.out.println(Arrays.toString(A));
        sort.sort(A, 0, A.length - 1);
        System.out.println(Arrays.toString(A));

        A = new int[417];
        Inputs.fillWithTimsortDrag(A, 32, random);
        System.out.println(Arrays.toString(A));
        sort.sort(A, 0, A.length - 1);
        System.out.println(Arrays.toString(A));
    }
}
