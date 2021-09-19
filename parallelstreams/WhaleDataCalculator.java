package com.company.parallelstreams;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class WhaleDataCalculator {
    private int processRecord(int input) {
        System.out.println("Task " + input + " started.");
        try {
            Thread.sleep(10); // milliseconds
        } catch (InterruptedException e) { // Handle interrupted exception
            System.out.println("Task " + input + " interrupted.");
        }
        System.out.println("Task " + input + " done.");
        return input + 1;
    }

    private int processAllData(List<Integer> data) {
        return data.parallelStream()
//        return data.stream()
            .map(this::processRecord)
            .reduce(0, (Integer x, Integer y) -> {
                return x + y;
            });
    }

    public static void test() {
        WhaleDataCalculator calculator = new WhaleDataCalculator(); // Define the data
        List<Integer> data = IntStream.range(0, 4001)
            .boxed()
            .collect(Collectors.toList());

        long start = System.nanoTime();
        // Process the data
        int result = calculator.processAllData(data);

        double time = (System.nanoTime() - start) / 1e6; // Report results
        System.out.println("\nTasks completed in: " + time + " ms with: " + result);
    }
}
