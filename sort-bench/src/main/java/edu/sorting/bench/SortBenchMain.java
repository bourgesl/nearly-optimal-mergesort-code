package edu.sorting.bench;

import java.io.IOException;
import java.util.Locale;

/**
 * Main program entry point
 *
 * @author bourgesl
 */
public class SortBenchMain {

    public static void main(String[] argv) throws IOException {
        // Pre init:
        // Set the default locale to en-US locale (for Numerical Fields "." ",")
        Locale.setDefault(Locale.US);

        if (true) {
            ArraySortBenchmark2.main(argv);
        } else {
            ArraySortBenchmark.main(argv);
        }
    }
}
