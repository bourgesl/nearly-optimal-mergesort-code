package edu.sorting.bench;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 4, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
public class ArrayCopyBenchmark {

    @State(Scope.Thread)
    public static class ThreadState {

        @Param({"50", "100", "500", "1000", "5000", "10000", "50000", "100000", "500000", "1000000"})
        int size;

        int[] source, destination;

        @Setup(Level.Iteration)
        public void setUp() {
            Random r = new Random(42);

            source = new int[size];
            destination = new int[size];

            for (int i = 0; i < size; ++i) {
                source[i] = r.nextInt();
            }

            // Promote all the arrays
            System.gc();
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int arraycopy(ThreadState ts) {
        final int[] src = ts.source;
        final int[] dst = ts.destination;
        final int size = ts.size;

        System.arraycopy(src, 0, dst, 0, size);

        return dst[0];
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int manualcopy(ThreadState ts) {
        final int[] src = ts.source;
        final int[] dst = ts.destination;
        final int size = ts.size;

        for (int i = 0; i < size; i++) {
            dst[i] = src[i];
        }

        return dst[0];
    }
}
