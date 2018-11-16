package edu.sorting.perf;

import static edu.sorting.perf.BentleyBasher.MIN_PREC;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import wildinter.net.WelfordVariance;

/**
 * @author Vladimir Yaroslavskiy
 */
public final class Statistic {

    private final static boolean IGNORE_LOW_CONFIDENCE = true;

    private final static String HEADER_STATS = "--- STATS ---";

    private final static DecimalFormat df4;

    static {
        // Set the default locale to en-US locale (for Numerical Fields "." ",")
        Locale.setDefault(Locale.US);
        df4 = new DecimalFormat("0.0000");
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"/home/bourgesl/libs/nearly-optimal-mergesort-code/basher-results.out"};
        }

        new Statistic().processFile(args[0]);
    }

    private void processFile(String file) {
        final List<String> lines = getLines(file);
        final int size = lines.size();

        if (size > 0) {
            final IntSorter[] sorters = IntSorter.values();
            myLen = new int[size];
            lengths.clear();
            myKey = new String[size];
            keys.clear();
            myTime = new double[sorters.length][size];
            myWinners = new int[sorters.length];

            for (int i = 0; i < size; i++) {
                processLine(sorters, lines.get(i), i);
            }
            doAfter();
        }
    }

    private void doAfter() {
        // TODO: use arguments for selected sorters, (sorter reference), warmup, sizes ... at least
        doStats(IntSorter.DPQ_11.ordinal(), IntSorter.DPQ_18_11.ordinal());
        doStats(IntSorter.DPQ_18_2.ordinal(), IntSorter.DPQ_18_11.ordinal());
        doStats(IntSorter.DPQ_18_11.ordinal(), IntSorter.RADIX.ordinal());
        doStats(IntSorter.DPQ_18_11.ordinal(), IntSorter.MARLIN.ordinal());
    }

    private void doStats(final int idxRef, final int idxTest) {
        final IntSorter[] sorters = IntSorter.values();
        System.out.println();
        System.out.println(HEADER_STATS);
        System.out.println("Comparing sorters " + sorters[idxRef] + " vs " + sorters[idxTest]);
        System.out.println("winners  : " + myWinners[idxRef] + " / " + myWinners[idxTest]);
        System.out.println("avg   (%): " + round(df4, mult(idxRef, idxTest) * 100.0));
        System.out.println("stats (%): " + ratioStats(idxRef, idxTest));

        System.out.println("\n\nStats per keys:");
        for (String key : keys) {
            System.out.println(key + " stats (%): " + ratioStats(idxRef, idxTest, key));
        }
        System.out.println("\n\nstats per keys and sizes:");
        for (Integer len : lengths) {
            System.out.println("- size = " + len);
            for (String key : keys) {
                System.out.println(key + " stats (%): " + ratioStats(idxRef, idxTest, key, len));
            }
            System.out.println("\n");
        }
    }

    private double mult(final int idxRef, final int idxTest) {
        double mult = 1.0d;
        final int length = myTime[idxRef].length;

        int count = 0;
        for (int i = 0; i < length; i++) {
            if (myTime[idxRef][i] > MIN_PREC && myTime[idxTest][i] > MIN_PREC) {
                count++;
                if (Math.pow(mult * (myTime[idxRef][i] / myTime[idxTest][i]), 1.0 / (count + 1)) == 0.0) {
                    break;
                }
                mult *= (myTime[idxRef][i] / myTime[idxTest][i]);
            }
        }
        if (count < 2) {
            return Double.NaN;
        }
        return Math.pow(mult, 1.0 / count);
    }

    private WelfordVariance ratioStats(final int idxRef, final int idxTest) {
        return ratioStats(idxRef, idxTest, null);
    }

    private WelfordVariance ratioStats(final int idxRef, final int idxTest,
                                       final String selectedKey) {
        return ratioStats(idxRef, idxTest, selectedKey, null);
    }

    private WelfordVariance ratioStats(final int idxRef, final int idxTest,
                                       final String selectedKey, final Integer selectedLen) {

        final WelfordVariance samples = new WelfordVariance();
        final int length = myTime[idxRef].length;

        for (int i = 0; i < length; i++) {
            if (selectedKey != null && !selectedKey.equals(myKey[i])) {
                continue;
            }
            if (selectedLen != null && selectedLen.intValue() != myLen[i]) {
                continue;
            }
            if (myTime[idxRef][i] > MIN_PREC && myTime[idxTest][i] > MIN_PREC) {
                samples.add(100.0 * myTime[idxRef][i] / myTime[idxTest][i]);
            }
        }
        return samples;
    }

    private List<String> getLines(String file) {
        List<String> lines = new ArrayList<String>();
        String line;

        try {
            InputStream is = new FileInputStream(new File(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while ((line = reader.readLine()) != null) {
                if (line.contains("SAWTOTH")
                        || line.contains("_RANDOM")
                        || line.contains("STAGGER")
                        || line.contains("PLATEAU")
                        || line.contains("SHUFFLE")) {
                    lines.add(line/*.replace(',', '.')*/);
                }
                if (line.startsWith(HEADER_STATS)) {
                    System.err.println("STATS already processed; skipping");
                    lines.clear();
                    break;
                }
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private void processLine(final IntSorter[] sorters, String line, int i) {
        StringTokenizer stk = new StringTokenizer(line, " \t");
        String value;
//        System.out.println("-- '" + line + "'");
        value = stk.nextToken(); // length
        myLen[i] = Integer.parseInt(value);

        lengths.add(Integer.valueOf(myLen[i]));

        value = stk.nextToken(); // sub-size
        value = stk.nextToken(); // type
        myKey[i] = value;

        value = stk.nextToken(); // variant
        myKey[i] += ":" + value;

        keys.add(myKey[i]);

// Data parsing:
        for (int j = 0; j < sorters.length; j++) {

            value = stk.nextToken();
            myTime[j][i] = getDouble(value);
//System.out.print("Line " + i + ": " + value + " " + myTime[j][i]);
        }

        int winnerIndex = getWinner(sorters.length, i);
        myWinners[winnerIndex]++;
    }

    private int getWinner(int nSorters, int row) {
        int winnerIndex = 0;
        double winner = myTime[0][row];

        for (int k = 1; k < nSorters; k++) {
            if (winner > MIN_PREC && myTime[k][row] > MIN_PREC && myTime[k][row] < winner) {
                winnerIndex = k;
                winner = myTime[k][row];
            }
        }
        return winnerIndex;
    }

    private double getDouble(String value) {
        if (value.startsWith("!")) {
            if (IGNORE_LOW_CONFIDENCE) {
                return -0.0d;
            } else {
                value = value.substring(1);
            }
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return -0.0d;
        }
    }

    private static String round(DecimalFormat df, double value) {
        if (Double.isNaN(value)) {
            return "NaN";
        }
        String s = df.format(value);
        for (int i = s.length(); i < 10; ++i) {
            s += " ";
        }
        /*        
        String s = String.valueOf(Math.round(value * 10000.0) / 10000.0);
        int k = s.length() - s.indexOf(".");

        for (int i = k; i <= 4; ++i) {
            s += "0";
        }
         */
        return s;
    }

    private final Set<Integer> lengths = new LinkedHashSet<Integer>();
    private int[] myLen;
    private final Set<String> keys = new LinkedHashSet<String>();
    private String[] myKey;
    private double[][] myTime;
    private int[] myWinners;
}
