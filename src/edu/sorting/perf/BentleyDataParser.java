package edu.sorting.perf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * BentleyBasher data parser to convert into ASCII tables in std out:
 * #columns
 * row values (ignore low confidence)
 * @author Laurent Bourges
 */
public final class BentleyDataParser {

    private final static boolean IGNORE_LOW_CONFIDENCE = false; // TODO

    private final static DecimalFormat df;

    static {
        // Set the default locale to en-US locale (for Numerical Fields "." ",")
        Locale.setDefault(Locale.US);
        df = new DecimalFormat("0.0000000"); // 7 digits
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"/home/bourgesl/libs/nearly-optimal-mergesort-code/basher-results.out"};
        }
        System.err.println("Parsing " + args[0] + " ...");
        new BentleyDataParser().processFile(args[0]);
    }

    private void processFile(String file) {
        final List<String> lines = getLines(file);
        final int size = lines.size();

        if (size > 1) {
            // parse columns
            final List<String> columns = processColumns(lines.get(0));

            // Print header:
            final PrintStream out = System.out;
            out.print("# ");
            for (String col : columns) {
                out.print(col);
                out.print('\t');
            }
            out.println();

            System.err.println("Columns found: " + columns);

            // parse lines:
            for (int i = 1; i < size; i++) {
                processLine(columns, lines.get(i));
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

            while ((line = reader.readLine()) != null) {
                if (line.contains("STAGGER")
                        || line.contains("SAWTOTH")
                        || line.contains("_RANDOM")
                        || line.contains("PLATEAU")
                        || line.contains("SHUFFLE")
                        || line.contains("SPIRAL")) {
                    lines.add(line);
                } else if (line.startsWith(BentleyBasher.HEADER_COLUMNS)) {
                    if (cols) {
                        throw new IllegalStateException("Multiple column header found: '" + line + "' !");
                    } else {
                        cols = true;
                    }
                    lines.add(0, line.substring(BentleyBasher.HEADER_COLUMNS.length()));
                }
            }
            if (!cols) {
                throw new IllegalStateException("Missing column header '" + BentleyBasher.HEADER_COLUMNS + "' !");
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    static List<String> processColumns(String line) {
        final StringTokenizer stk = new StringTokenizer(line, " \t");

        final List<String> columns = new ArrayList<String>();

        for (int i = 0; stk.hasMoreTokens(); i++) {
            String col = stk.nextToken();
            if (col.startsWith("[")) {
                // Ignore last col '[A vs B]'
                break;
            }
            columns.add(col);
        }
        return columns;
    }

    private void processLine(final List<String> columns, String line) {
        final PrintStream out = System.out;

        final int colLen = columns.size();

        final StringTokenizer stk = new StringTokenizer(line, " \t");

        if (stk.countTokens() < colLen) {
            System.err.println("Invalid line: '" + line + "'");
            return;
        }
        System.out.println("-- '" + line + "'");

        // header first columns: "Length Sub-size  Builder Tweaker"
        String value;
        value = stk.nextToken(); // Length (int)
        out.print(value);
        out.print("\t");

        value = stk.nextToken(); // Sub-size (int)
        out.print(value);
        out.print("\t");

        value = stk.nextToken(); // Builder (str)
        out.print("\"");
        out.print(value);
        out.print(":");
        value = stk.nextToken(); // Tweaker (str)
        out.print(value);
        out.print("\"\t");

        // Data parsing:
        for (int j = 0, last = colLen - 4; j < last; j++) {
            final double v = parseTime(stk);
            if (Double.isNaN(v)) {
                out.print("NaN");
            } else {
                out.print(df.format(v));
            }
            out.print("\t");
        }
        out.println();
    }

    static double parseTime(final StringTokenizer stk) {
        String value = stk.nextToken();
        if (value.contains("[")) {
            // skip [...] distribution flags
//                System.err.println("skip value '" + value + "'");
            do {
                value = stk.nextToken();
//                    System.err.println("skip value '" + value + "'");
            } while (!value.contains("]"));

            value = stk.nextToken();
        }
        double v = Double.NaN;
        if (value.startsWith("$") || value.startsWith("!")) {
            if (IGNORE_LOW_CONFIDENCE) {
//                System.err.println("Flag '" + value + "'");
            }
            value = stk.nextToken();
            if (IGNORE_LOW_CONFIDENCE) {
//                System.err.println("skip flagged value '" + value + "'");
                value = null;
            }
        }
        if (value != null) {
            v = getDouble(value);
        }
        return v;
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
