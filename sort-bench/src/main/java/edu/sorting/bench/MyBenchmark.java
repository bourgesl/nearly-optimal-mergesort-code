package edu.sorting.bench;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(batchSize = 10, iterations = 10, time = 3, timeUnit = TimeUnit.SECONDS)
public class MyBenchmark {

    @State(Scope.Benchmark)
    public static class Data {

        @Param({"100", "500", "1000", "2000", "5000"})
        public int size;

        private int[] input;

        @Setup
        public void setupAttributes() {
            // TODO: define variables
        }

        @TearDown
        public void down() {
            // cleanup
        }
    }

    @Benchmark
    public int testMethod(final Data data) {
        // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
        // Put your benchmark code here.

        return 0;
    }

}
