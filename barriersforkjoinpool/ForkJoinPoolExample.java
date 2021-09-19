package com.company.barriersforkjoinpool;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class ForkJoinPoolExample {
    public static void test() {
        Double[] weights = new Double[10];
//        ForkJoinTask<?> task = new WeighAnimalAction(weights, 0, weights.length);
        ForkJoinTask<Double> task = new WeighAnimalTask(weights, 0, weights.length);

        ForkJoinPool pool = new ForkJoinPool();

//        pool.invoke(task);
        Double sum = pool.invoke(task);
        System.out.println("Sum: "+sum);

        System.out.println();
        System.out.print("Weights: ");
        Arrays.asList(weights).stream().forEach(d -> System.out.print(d.intValue() + " "));

    }
}
