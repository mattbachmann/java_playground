package com.company.barriersforkjoinpool;

import java.util.*;
import java.util.concurrent.*;

class WeighAnimalAction extends RecursiveAction {
    private int start;
    private int end;
    private Double[] weights;

    WeighAnimalAction(Double[] weights, int start, int end) {
        this.start = start;
        this.end = end;
        this.weights = weights;
    }

    protected void compute() {
        if (end - start <= 3) {
            for (int i = start; i < end; i++) {
                weights[i] = (double) new Random().nextInt(100);
                System.out.println("Animal Weighed: " + i);
            }
        } else {
            int middle = start + ((end - start) / 2);
            System.out.println("Recursions [start=" + start + ",middle=" + middle + ",end=" + end + "]");
            invokeAll(new WeighAnimalAction(weights, start, middle), new WeighAnimalAction(weights, middle, end)); // recursion
        }
    }

}
