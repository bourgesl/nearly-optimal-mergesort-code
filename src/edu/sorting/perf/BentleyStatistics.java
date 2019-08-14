package edu.sorting.perf;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import wildinter.net.WelfordVariance;

/**
 * @author Vladimir Yaroslavskiy
 */
public final class BentleyStatistics {

    private final static double MIN_PREC = 1e-6; // 1ns expressed in ms

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

        new BentleyStatistics().processFile(args[0]);
    }

    // members
    private final Set<Integer> lengths = new LinkedHashSet<Integer>();
    private int[] myLen;
    private final Set<String> keys = new LinkedHashSet<String>();
    private String[] myKey;
    private double[][] myTime;
    private int[] myWinners;

    private BentleyStatistics() {
        // no-op
    }

    private void processFile(String file) {
        final List<String> lines = BentleyDataParser.getLines(file);
        int size = lines.size();

        if (size > 1) {
            // parse columns
            final List<String> columns = BentleyDataParser.processColumns(lines.remove(0));
            size--;

            // skip header first columns: "Length Sub-size  Builder Tweaker"
            final int colLen = columns.size();
            final IntSorter[] sorters = new IntSorter[colLen - 4];

            for (int i = 0; i < sorters.length; i++) {
                sorters[i] = IntSorter.valueOf(columns.get(i + 4));
            }

            myLen = new int[size];
            lengths.clear();
            myKey = new String[size];
            keys.clear();
            myTime = new double[sorters.length][size];
            myWinners = new int[sorters.length];

            // parse lines:
            for (int i = 0; i < size; i++) {
                if (!processLine(sorters, lines.get(i), i)) {
                    break;
                }
            }
            doAfter(sorters);
        }
    }

    private void doAfter(final IntSorter[] sorters) {
        // TODO: use arguments for selected sorters, (sorter reference), warmup, sizes ... at least
        System.out.println(HEADER_STATS);
        System.out.println("Winners: ");

        for (int i = 0; i < sorters.length; i++) {
            // ignore BASELINE
            if (sorters[i] != IntSorter.BASELINE) {
                System.out.println(sorters[i] + " : " + myWinners[i]);
            }
        }

        for (int i = 0; i < sorters.length; i++) {
            // ignore BASELINE
            if (sorters[i] != IntSorter.BASELINE) {
                for (int j = i + 1; j < sorters.length; j++) {
                    // ignore BASELINE
                    doStats(sorters, i, j);
                }
            }
        }
        // doStats(sorters, indexOf(sorters, IntSorter.DPQ_11), indexOf(sorters, IntSorter.DPQ_18_11_21));
    }

    private void doStats(final IntSorter[] sorters, final int idxRef, final int idxTest) {
        System.out.println();
        System.out.println(HEADER_STATS);
        System.out.println("Comparing sorters " + sorters[idxRef] + " vs " + sorters[idxTest]);
        System.out.println("winners  : " + myWinners[idxRef] + " / " + myWinners[idxTest]);
        System.out.println("avg   (%): " + round(df4, mult(idxRef, idxTest) * 100.0));
        System.out.println("stats (%): " + ratioStats(idxRef, idxTest));

        System.out.println("\nStats per keys:");
        for (String key : keys) {
            System.out.println(key + " stats (%): " + ratioStats(idxRef, idxTest, key));
        }
        System.out.println("\nstats per keys and sizes:");
        for (Integer len : lengths) {
            System.out.println("- size = " + len);
            System.out.println("stats (%): " + ratioStats(idxRef, idxTest, null, len));

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
            } else {
//                System.err.println("Ignore: " + myTime[idxRef][i] + " and " + myTime[idxTest][i]);
            }
        }
        return samples;
    }

    private boolean processLine(final IntSorter[] sorters, String line, int i) {
        StringTokenizer stk = new StringTokenizer(line, " \t");
        if (stk.countTokens() < 4 + sorters.length) {
            System.err.println("Invalid line: '" + line + "'");
            return false;
        }
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
            myTime[j][i] = BentleyDataParser.parseTime(stk);
            // System.out.println("Line " + i + ": " + value + " " + myTime[j][i]);
        }

        int winnerIndex = getWinner(sorters, i);
        if (winnerIndex != -1) {
            myWinners[winnerIndex]++;
        }
        return true;
    }

    private int getWinner(final IntSorter[] sorters, int row) {
        int winnerIndex = -1;
        double winner = Double.MAX_VALUE;

        for (int k = 0; k < sorters.length; k++) {
            // ignore BASELINE
            if (sorters[k] != IntSorter.BASELINE
                    && winner > MIN_PREC
                    && myTime[k][row] > MIN_PREC
                    && myTime[k][row] < winner) {
                winnerIndex = k;
                winner = myTime[k][row];
            }
        }
        return winnerIndex;
    }

    private static String round(DecimalFormat df, double value) {
        if (Double.isNaN(value)) {
            return "NaN";
        }
        String s = df.format(value);
        for (int i = s.length(); i < 10; ++i) {
            s += " ";
        }
        return s;
    }
}
