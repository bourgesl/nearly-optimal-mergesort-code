package edu.sorting.test;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Random;
import org.openjdk.jol.vm.VM;
import org.openjdk.jol.vm.VirtualMachine;

/**
 *
 * @author bourgesl
 */
public class AllocationArrayTest {

    private final static long GC_LATENCY = 20l;
    private static final int MAXIMUM_SIZE = 50;

    private final static VirtualMachine vm = VM.current();

    private final static Random random = new Random(); // pure random

    private final static ArrayList<byte[]> paddings = new ArrayList<byte[]>(100);

    private final static ArrayList<int[]> arrays = new ArrayList<int[]>(100);

    public static synchronized int[] allocateAlignedArray(final int size) {
        int[] array;

        final int idx = arrays.size();
        // prepare slot
        arrays.add(null);
        paddings.add(null);

        while (true) {
            paddings.set(idx, new byte[random.nextInt(64)]);

            // allocate
            array = new int[size];
            arrays.set(idx, array);

            cleanup();

            long alignment = vm.addressOf(array) % 32;
            if (alignment == 0) {
                System.out.println();
                break;
            } else {
                System.out.print(alignment + " ");
                System.out.flush();
            }
        }
        
        return array;
    }

    public static void main(String[] args) throws Exception {
        final VirtualMachine vm = VM.current();
        out.println(vm.details());

        final StringBuilder result = new StringBuilder(32);

        for (int l = 0; l < 10; l++) {
            allocateAlignedArray(MAXIMUM_SIZE);
        }
        // check alignment:
        for (int l = 0; l < arrays.size(); l++) {
            int[] array = arrays.get(l);
            long address = vm.addressOf(array);

            result.setLength(0);
            result.append("Allocated array @ ");
            System.out.println(getAlignment(result, address));
        }

        for (int l = 0; l < 1000; l++) {
            int[] array = new int[MAXIMUM_SIZE];
            long address = vm.addressOf(array);

            if (true) {
                result.setLength(0);
                result.append("< ");
                System.out.println(getAlignment(result, address));
            }

            cleanup();

            address = vm.addressOf(array);

            result.setLength(0);
            result.append("> ");
            System.out.println(getAlignment(result, address));
        }
    }

    private static String getAlignment(StringBuilder result, long address) {
        result.append(address).append(" : ");
        result.append(address % 4 == 0 ? "4 " : "");
        result.append(address % 8 == 0 ? "8 " : "");
        result.append(address % 16 == 0 ? "16 " : "");
        result.append(address % 32 == 0 ? "32 " : "");
        return result.toString();
    }

    static void cleanup() {
//        final long freeBefore = Runtime.getRuntime().freeMemory();
        // Perform GC:
        for (int i = 0; i < 3; i++) {
            System.gc();
        }

        // pause for few ms :
        try {
            Thread.sleep(GC_LATENCY);
        } catch (InterruptedException ie) {
            System.err.println("thread interrupted");
        }
        /*        
        final long freeAfter = Runtime.getRuntime().freeMemory();
        System.out.println(String.format("cleanup (explicit Full GC): %,d / %,d bytes free.", freeBefore, freeAfter));
         */
    }

}
