package edu.sorting.bench;

import java.io.IOException;
import java.util.Locale;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.runner.RunnerException;

/**
 * Main program entry point
 *
 * @author bourgesl
 */
public class SortBenchMain {

    public static void main(String[] argv) throws RunnerException, IOException {

        // Pre init:
        // Set the default locale to en-US locale (for Numerical Fields "." ",")
        Locale.setDefault(Locale.US);

        Main.main(argv);
    }
}
