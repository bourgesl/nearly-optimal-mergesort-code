package edu.sorting.bench;

import static edu.sorting.bench.JMHAutoTuneDriver.HEADER_COLUMNS;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * ArraySortBenchmark data parser to convert into ASCII tables in std out:
 * #columns
 * row values (ignore low confidence)
 * where rows contains all Sorter data for the same test (transpose)
 *
 * IN:  COLUMNS:Mode	Length	Sub-size	Builder	Tweaker	Sorter	RMS	Mean	StdDev	Count	Min	Max
 * OUT: COLUMNS:Length Sub-size  Builder Tweaker	BASELINE	DPQ_11	DPQ_18_11_21	DPQ_18_11I	DPQ_18_11P	RADIX	MARLIN
 *
 * @author Laurent Bourges
 */
public final class ArraySortDataParser {

    private static final int ERR_WARNING = 25; // 25% warning on all distributions

    // true means MEAN(time) + STDDEV, false means use MIN(TIME) + STDDEV
    private final static boolean USE_RMS = false;

    private final static String PARAM_SORTER = "Parameter[tSorter]: ";

    private final static String ROW_PREFIX = ">> ";

    private final static String[] KEEP_COLS = new String[]{"Length", "Sub-size", "Builder", "Tweaker"};

    private final static DecimalFormat df;

    static {
        // Set the default locale to en-US locale (for Numerical Fields "." ",")
        Locale.setDefault(Locale.US);
        df = new DecimalFormat("0.0000000"); // 7 digits
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"/home/bourgesl/libs/nearly-optimal-mergesort-code/sort-bench/results/sort-100.log"};
        }
        // TODO: parse several result files ?
        System.out.println("Parsing " + args[0] + " ...");
        if (USE_RMS) {
            System.out.println("Timings are given in milli-seconds (rms = mean + 1 stddev)");
        } else {
            System.out.println("Timings are given in milli-seconds (min time + 1 stddev)");
        }
        new ArraySortDataParser().processFile(args[0]);
    }

    private void processFile(String file) {
        final List<String> lines = getLines(file);
        final int size = lines.size();

        if (size > 1) {
            // parse sorters
            final List<String> sorters = processSorters(lines.get(0));
            // parse columns
            final List<String> columns = processColumns(lines.get(1));

            System.err.println("Columns found: " + columns);

            final Map<String, Integer> colMapping = new LinkedHashMap<String, Integer>();
            for (int i = 0; i < columns.size(); i++) {
                colMapping.put(columns.get(i), Integer.valueOf(i));
            }

            final Set<String> keepCols = new LinkedHashSet<String>(Arrays.asList(KEEP_COLS));

            // Print header:
            final PrintStream out = System.out;
            out.print("COLUMNS:");

            int missing = 0;
            for (String col : keepCols) {
                if (!colMapping.containsKey(col)) {
                    System.err.println("Missing column: " + col);
                    missing++;
                }
                out.print(col);
                out.print('\t');
            }
            for (String col : sorters) {
                out.print(col);
                out.print('\t');
            }
            out.println();

            if (missing != 0) {
                throw new IllegalStateException("Missing column in: " + columns + " !");
            }

            final int sorterLen = sorters.size();
            final int colLen = columns.size();

            final List<String> values = new ArrayList<String>();

            int nSorter = 0;
            double timeBL = 0.0;
            boolean noBL;

            // parse lines:
            for (int i = 2; i < size; i++) {
                values.clear();
                processLine(colLen, lines.get(i), values);

                if (nSorter == 0) {
                    // parse header values
                    for (String col : keepCols) {
                        out.print(values.get(colMapping.get(col)));
                        out.print('\t');
                    }
                }
                // Parse values [RMS	Mean	StdDev	Count	Min	Max]
                double time = parseTime(colMapping, values);

                if (time > timeBL) {
                    time -= timeBL;
                    noBL = false;
                } else {
                    noBL = true;
                }

                if (Double.isNaN(time)) {
                    out.print("NaN");
                } else {
                    if (noBL) {
                        out.print('$');
                    }
                    final double relError = 100.0 * parseStddev(colMapping, values)
                            / parseMean(colMapping, values);

                    if (relError >= ERR_WARNING) {
                        out.print('!');
                    }
                    out.print(' ');
                    out.print(df.format(time));
                }
                out.print("\t");

                if (nSorter == 0) {
                    // baseline
                    timeBL = time;
                }

                if (++nSorter >= sorterLen) {
                    out.println();
                    nSorter = 0;
                    timeBL = 0.0;
                }
            }
        }
    }

    static List<String> getLines(String file) {
        final List<String> lines = new ArrayList<String>();
        String line;

        try {
            InputStream is = new FileInputStream(new File(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            boolean cols = false;
            boolean sorters = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(ROW_PREFIX)
                        && (line.contains("SAWTOTH")
                        || line.contains("_RANDOM")
                        || line.contains("STAGGER")
                        || line.contains("PLATEAU")
                        || line.contains("SHUFFLE"))) {
                    lines.add(line.substring(ROW_PREFIX.length()));
                } else if (line.startsWith(PARAM_SORTER)) {
                    if (cols) {
                        throw new IllegalStateException("Multiple sorter header found: '" + line + "' !");
                    } else {
                        sorters = true;
                    }
                    lines.add(0, line.substring(PARAM_SORTER.length()));
                } else if (line.startsWith(HEADER_COLUMNS)) {
                    if (cols) {
                        throw new IllegalStateException("Multiple column header found: '" + line + "' !");
                    } else {
                        cols = true;
                    }
                    lines.add(1, line.substring(HEADER_COLUMNS.length()));
                }
            }
            if (!cols) {
                throw new IllegalStateException("Missing column header '" + HEADER_COLUMNS + "' !");
            }
            if (!sorters) {
                throw new IllegalStateException("Missing sorter header '" + PARAM_SORTER + "' !");
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    static List<String> processSorters(String line) {
        final StringTokenizer stk = new StringTokenizer(line, " \t");

        final List<String> sorters = new ArrayList<String>();

        // Parameter[tSorter]: [BASELINE, DPQ_11, DPQ_18_11_21, DPQ_18_11_27, DPQ_18_11I, RADIX, MARLIN]
        for (int i = 0; stk.hasMoreTokens(); i++) {
            String col = stk.nextToken();
            // filter ?
            if (col.startsWith("[")) {
                col = col.substring(1);
            }
            if (col.endsWith(",") || col.endsWith("]")) {
                col = col.substring(0, col.length() - 1);
            }
            sorters.add(col);
        }
        return sorters;
    }

    static List<String> processColumns(String line) {
        final StringTokenizer stk = new StringTokenizer(line, " \t");

        final List<String> columns = new ArrayList<String>();

        for (int i = 0; stk.hasMoreTokens(); i++) {
            String col = stk.nextToken();
            // filter ?
            columns.add(col);
        }
        return columns;
    }

    private void processLine(final int colLen, String line, List<String> values) {
        final StringTokenizer stk = new StringTokenizer(line, " \t");

        if (stk.countTokens() < colLen) {
            System.err.println("Invalid line: '" + line + "'");
            return;
        }
//        System.out.println("-- '" + line + "'");

        while (stk.hasMoreTokens()) {
            values.add(stk.nextToken());
        }
    }

    static double parseTime(final Map<String, Integer> colMapping, final List<String> values) {
        // Parse values [RMS	Mean	StdDev	Count	Min	Max]
        if (USE_RMS) {
            return getDouble(values.get(colMapping.get("RMS")));
        }
        return getDouble(values.get(colMapping.get("Min")))
                + parseStddev(colMapping, values);
    }

    static double parseMean(final Map<String, Integer> colMapping, final List<String> values) {
        // Parse values [RMS	Mean	StdDev	Count	Min	Max]
        return getDouble(values.get(colMapping.get("Mean")));
    }

    static double parseStddev(final Map<String, Integer> colMapping, final List<String> values) {
        // Parse values [RMS	Mean	StdDev	Count	Min	Max]
        return getDouble(values.get(colMapping.get("StdDev")));
    }

    private static double getDouble(final String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid double '" + value + "'");
            nfe.printStackTrace();
            return Double.NaN;
        }
    }

}
