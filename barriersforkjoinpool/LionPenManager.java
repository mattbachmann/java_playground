package com.company.barriersforkjoinpool;

import java.util.concurrent.*;

public class LionPenManager {
    private void removeAnimals() {
        System.out.println("Removing animals");
    }

    private void cleanPen() {
        System.out.println("Cleaning the pen");
    }

    private void addAnimals() {
        System.out.println("Adding animals");
    }

    private void performTask(CyclicBarrier c1, CyclicBarrier c2) {
        removeAnimals();
        try {
            c1.await();
            cleanPen();
            c2.await();
            addAnimals();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public static void test() throws InterruptedException {
        ExecutorService service = null;
        try {
            int numJobs = 4;
            service = Executors.newFixedThreadPool(12); // workers >= jobs
            LionPenManager manager = new LionPenManager();
            CyclicBarrier c1 = new CyclicBarrier(numJobs, () -> System.out.println("*** Animals removed!"));
            CyclicBarrier c2 = new CyclicBarrier(numJobs, () -> System.out.println("*** Pen Cleaned!"));
            for (int i = 0; i < numJobs; i++) {
                service.submit(() -> manager.performTask(c1, c2));
            }
        } finally {
            if (service != null) {
                service.shutdown();
                service.awaitTermination(1, TimeUnit.MINUTES);
                service.shutdownNow();
            }
        }
    }
}
