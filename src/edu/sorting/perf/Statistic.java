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
public class Statistic {

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

        new Statistic().processFile(args[0], null, null);
    }

    protected void doAfter(String number, String name) {
        System.out.println();
        System.out.println(HEADER_STATS);
        System.out.println("winners  : " + myWinners[0] + " / " + myWinners[1]);
        System.out.println("avg   (%): " + round(df4, mult() * 100.0));
        System.out.println("stats (%): " + ratioStats(null, null));

        System.out.println("\n\nStats per keys:");
        for (String key : keys) {
            System.out.println(key + " stats (%): " + ratioStats(key, null));
        }
        System.out.println("\n\nstats per keys and sizes:");
        for (Integer len : lengths) {
            System.out.println("- size = " + len);
            for (String key : keys) {
                System.out.println(key + " stats (%): " + ratioStats(key, len));
            }
            System.out.println("\n");
        }
    }

    protected void processFile(String file, String number, String name) {
        List<String> lines = getLines(file);
        final int size = lines.size();

        if (size > 0) {
            myLen = new int[size];
            lengths.clear();
            myKey = new String[size];
            myTime = new double[2][size];
            keys.clear();

            for (int i = 0; i < size; i++) {
                processLine(lines.get(i), i);
            }
            doAfter(number, name);
        }
    }

    protected double mult() {
        double mult = 1.0d;
        final int length = myTime[0].length;

        int count = 0;
        for (int i = 0; i < length; i++) {
            if (myTime[0][i] > MIN_PREC && myTime[1][i] > MIN_PREC) {
                count++;
                if (Math.pow(mult * (myTime[0][i] / myTime[1][i]), 1.0 / (count + 1)) == 0.0) {
                    break;
                }
                mult *= (myTime[0][i] / myTime[1][i]);
            }
        }
        if (count < 2) {
            return Double.NaN;
        }
        return Math.pow(mult, 1.0 / count);
    }

    protected WelfordVariance ratioStats(final String selectedKey, final Integer selectedLen) {
        final WelfordVariance samples = new WelfordVariance();
        final int length = myTime[0].length;

        for (int i = 0; i < length; i++) {
            if (selectedKey != null && !selectedKey.equals(myKey[i])) {
                continue;
            }
            if (selectedLen != null && selectedLen.intValue() != myLen[i]) {
                continue;
            }
            if (myTime[0][i] > MIN_PREC && myTime[1][i] > MIN_PREC) {
                samples.add(100.0 * myTime[0][i] / myTime[1][i]);
            }
        }
        return samples;
    }

    protected List<String> getLines(String file) {
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

    protected void processLine(String line, int i) {
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

// First 2 (customize that selection):        
        value = stk.nextToken();
        myTime[0][i] = getDouble(value);
//System.out.print("Line " + i + ": " + value + " " + myTime[0][i]);

        value = stk.nextToken();
        myTime[1][i] = getDouble(value);
//System.out.println(" " + value + " " + myTime[1][i]);

        int winnerIndex = getWinner(i);
        myWinners[winnerIndex]++;
    }

    private int getWinner(int row) {
        int winnerIndex = 0;
        double winner = myTime[0][row];

        for (int k = 1; k < 2; k++) {
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
    private int[] myWinners = new int[2];
}
