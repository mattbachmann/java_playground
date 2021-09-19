package com.company.barriersforkjoinpool;

import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class WeighAnimalTask extends RecursiveTask<Double> {
    private int start;
    private int end;
    private Double[] weights;

    WeighAnimalTask(Double[] weights, int start, int end) {
        this.start = start;
        this.end = end;
        this.weights = weights;
    }

    protected Double compute() {
        if (end - start <= 3) {
            double sum = 0;
            for (int i = start; i < end; i++) {
                weights[i] = weigh();
                System.out.println("Animal Weighed: " + i);
                sum += weights[i];
            }
            return sum;
        } else {
            int middle = start + ((end - start) / 2);
            System.out.println("Recursions [start=" + start + ",middle=" + middle + ",end=" + end + "]");
            RecursiveTask<Double> otherTask = new WeighAnimalTask(weights,start,middle);
            otherTask.fork();
//            Double otherResult = otherTask.fork().join(); // idle waiting for other thread to complete - first should calculate before waiting
            return new WeighAnimalTask(weights,middle,end).compute() + otherTask.join();
        }
    }

    private Double weigh() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (double) new Random().nextInt(100);
    }
}
