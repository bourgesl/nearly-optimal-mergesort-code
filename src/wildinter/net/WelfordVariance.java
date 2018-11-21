package wildinter.net;

/**
 * Simple implementation of Welford's algorithm for
 * online-computation of the variance of a stream.
 *
 * see http://jonisalonen.com/2013/deriving-welfords-method-for-computing-variance/
 *
 * @author Sebastian Wild (wild@uwaterloo.ca)
 */
public final class WelfordVariance {

    long nSamples;
    double min, max, mean, squaredError;

    public WelfordVariance() {
        reset();
    }

    public void copy(WelfordVariance other) {
        nSamples = other.nSamples;
        min = other.min;
        max = other.max;
        mean = other.mean;
        squaredError = other.squaredError;
    }

    public void reset() {
        nSamples = 0L;
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
        mean = 0.0;
        squaredError = 0.0;
    }

    public void add(double x) {
        if (x < min) {
            min = x;
        }
        if (x > max) {
            max = x;
        }
        ++nSamples;
        final double oldMean = mean;
        mean += (x - mean) / nSamples;
        squaredError += (x - mean) * (x - oldMean);
    }

    public double min() {
        if (nSamples == 0L) {
            return Double.NaN;
        }
        return min;
    }

    public double max() {
        if (nSamples == 0L) {
            return Double.NaN;
        }
        return max;
    }

    public double mean() {
        if (nSamples == 0L) {
            return Double.NaN;
        }
        return mean;
    }

    public long nSamples() {
        return nSamples;
    }

    public double variance() {
        if (nSamples == 0L) {
            return Double.NaN;
        }
        return squaredError / (nSamples - 1L);
    }

    public double stddev() {
        if (nSamples == 0L) {
            return Double.NaN;
        }
        return Math.sqrt(variance());
    }

    public double rms() {
        if (nSamples == 0L) {
            return Double.NaN;
        }
        return mean() + stddev();
    }

    public int errorPercent() {
        if (nSamples == 0L) {
            return 1000;
        }
        return (int) Math.round(rawErrorPercent());
    }

    public double rawErrorPercent() {
        return Math.abs(100.0 * stddev() / mean());
    }

    @Override
    public String toString() {
        return "[" + nSamples()
                + ": µ=" + (float) mean()
                + " σ=" + (float) stddev()
                + " rms=" + (float) rms()
                + " min=" + (float) min()
                + " max=" + (float) max()
                + "]";
    }

    public static void main(String[] args) {
        WelfordVariance v = new WelfordVariance();
        for (int i : new int[]{1, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 8, 89, 10000, 100001, 00, 101,}) {
            v.add(i);
        }
        System.out.println("v.mean() = " + v.mean());
        System.out.println("v.variance() = " + v.variance());
        System.out.println("v.stdev() = " + v.stddev());
        System.out.println("stats() = " + v);
    }

}
