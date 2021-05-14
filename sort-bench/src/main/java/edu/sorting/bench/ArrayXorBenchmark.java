package edu.sorting.bench;

import java.lang.reflect.Field;
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
import sun.misc.Unsafe;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 4, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
public class ArrayXorBenchmark {

    private final static boolean TRACE = false;

    @State(Scope.Thread)
    public static class ThreadState {

        @Param({
            //            "100000", 
            "1000000", 
//            "10000000"
        })
        int size;

        int[] source;

        int[] count4;

        // unsafe reference
        Unsafe UNSAFE = null;

        @Setup(Level.Iteration)
        public void setUp() {
            Random r = new Random(42);

            source = new int[size];

            for (int i = 0; i < size; ++i) {
                source[i] = r.nextInt();
            }

            count4 = new int[256];

            UNSAFE = null;
            try {
                final Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                UNSAFE = (Unsafe) field.get(null);
            } catch (Exception e) {
                throw new InternalError("Unable to get sun.misc.Unsafe instance", e);
            }

            // Promote all the arrays
            System.gc();
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int arrayAndOriginal(final ThreadState ts) {
        final int[] a = ts.source;
        final int size = ts.size;

        final int[] count4 = ts.count4;
        for (int i = 0; i < count4.length; ++i) {
            count4[i] = 0;
        }

        for (int i = 0; i < size; ++i) {
            count4[(a[i] >>> 24) & 0xFF]--;
        }

        if (TRACE) {
            System.out.println("arrayAndOriginal() -----------------------");
        }
        int total = 0;
        for (int i = 0; i < count4.length; ++i) {
            if (TRACE) {
                System.out.println("i: " + i + " count4: " + count4[i]);
            }
            total += count4[i];
        }
        return total;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int arrayXorOriginal(final ThreadState ts) {
        final int[] a = ts.source;
        final int size = ts.size;

        final int[] count4 = ts.count4;
        for (int i = 0; i < count4.length; ++i) {
            count4[i] = 0;
        }

        for (int i = 0; i < size; ++i) {
            count4[(a[i] >>> 24) ^ 0x80]--;
        }

        if (TRACE) {
            System.out.println("arrayXorOriginal() -----------------------");
        }
        int total = 0;
        for (int i = 0; i < count4.length; ++i) {
            if (TRACE) {
                System.out.println("i: " + i + " count4: " + count4[i]);
            }
            total += count4[i];
        }
        return total;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int arrayXor_Masked(ThreadState ts) {
        final int[] a = ts.source;
        final int size = ts.size;

        final int[] count4 = ts.count4;
        for (int i = 0; i < count4.length; ++i) {
            count4[i] = 0;
        }

        for (int i = 0; i < size; ++i) {
            count4[((a[i] >>> 24) ^ 0x80) & 0xFF]--;
        }

        if (TRACE) {
            System.out.println("arrayXor_Masked() -----------------------");
        }
        int total = 0;
        for (int i = 0; i < count4.length; ++i) {
            if (TRACE) {
                System.out.println("i: " + i + " count4: " + count4[i]);
            }
            total += count4[i];
        }
        return total;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int arrayXor_Unsafe(ThreadState ts) {
        final int[] a = ts.source;
        final int size = ts.size;

        final int[] count4 = ts.count4;
        for (int i = 0; i < count4.length; ++i) {
            count4[i] = 0;
        }

        final Unsafe U = ts.UNSAFE;
        final long off0 = Unsafe.ARRAY_INT_BASE_OFFSET;
        final long scale = Unsafe.ARRAY_INT_INDEX_SCALE;

        for (int i = 0; i < size; ++i) {
            final long offset = off0 + ((a[i] >>> 24) ^ 0x80) * scale;
            
//            final long offset = off0 + (((a[i] >>> 24) & 0xFF) ^ 0x80) * scale;
            /* count4[offset]--; */
            U.putInt(count4, offset, U.getInt(count4, offset) - 1);
        }

        if (TRACE) {
            System.out.println("arrayXor_Unsafe() -----------------------");
        }
        int total = 0;
        for (int i = 0; i < count4.length; ++i) {
            if (TRACE) {
                System.out.println("i: " + i + " count4: " + count4[i]);
            }
            total += count4[i];
        }
        return total;
    }
}
