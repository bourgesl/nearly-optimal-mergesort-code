package edu.sorting.perf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import wildinter.net.WelfordVariance;

/**
 * @author Vladimir Yaroslavskiy
 */
public class Statistic {

    private final static String HEADER_STATS = "--- STATS ---";
    private final static double MIN_PREC = 1e-5; // nanosecond

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
        System.out.println("avg   (%): " + round(mult() * 100.0));
        System.out.println("stats (%): " + ratioStats(null));

        System.out.println("stats per keys:");
        for (String key : keys) {
            System.out.println(key + " stats (%): " + ratioStats(key));
        }
    }

    protected void processFile(String file, String number, String name) {
        List<String> lines = getLines(file);
        final int size = lines.size();

        if (size > 0) {
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

    protected WelfordVariance ratioStats(String selectedKey) {
        final WelfordVariance samples = new WelfordVariance();
        final int length = myTime[0].length;

        for (int i = 0; i < length; i++) {
            if (selectedKey != null && !selectedKey.equals(myKey[i])) {
                continue;
            }
            if (myTime[0][i] > MIN_PREC && myTime[1][i] > MIN_PREC) {
                samples.addSample(100.0 * myTime[0][i] / myTime[1][i]);
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
        value = stk.nextToken(); // sub-size
        value = stk.nextToken(); // type
        myKey[i] = value;
        value = stk.nextToken(); // variant
        myKey[i] += ":" + value;

        keys.add(myKey[i]);

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
            if (myTime[k][row] < winner) {
                winnerIndex = k;
                winner = myTime[k][row];
            }
        }
        return winnerIndex;
    }

    private double getDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -0.0d;
        }
    }

    private static String round(double value) {
        if (Double.isNaN(value)) {
            return "NaN";
        }
        String s = String.valueOf(Math.round(value * 10000.0) / 10000.0);
        int k = s.length() - s.indexOf(".");

        for (int i = k; i <= 4; ++i) {
            s += "0";
        }
        return s;
    }

    private Set<String> keys = new LinkedHashSet<String>();
    private String[] myKey;
    private double[][] myTime;
    private int[] myWinners = new int[2];
}
