package com.company.synchronization;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SheepManager {
    private int sheepCount = 0;
    private AtomicInteger atomicSheepCount = new AtomicInteger(0);
    private final Object lock = new Object();

    private void incrementAndReport() {
        System.out.print((++sheepCount) + " ");
    }

    private void atomicIncrementAndReport() {
        System.out.print(atomicSheepCount.incrementAndGet() + " ");
    }

    private synchronized void synchronizedIncrementAndReport() {
        System.out.print((++sheepCount) + " ");
    }
//    Same as: (locks this)
//    private void synchronizedIncrementAndReport() {
//        synchronized (this) {
//            System.out.print((++sheepCount) + " ");
//        }SheepManager
//    }

    private void lockSynchronizedIncrementAndReport() {
        synchronized (lock) { // won't lock manager
            System.out.print((++sheepCount) + " ");
        }
    }

    public static synchronized void printDaysWork() {
        System.out.print("Finished work");
    }
//    same as:
//    public static void printDaysWork() {
//        synchronized (SheepManager.class) {
//            System.out.print("Finished work");
//        }
//    }

    public static void testSheepCount() throws InterruptedException {
        ExecutorService service = null;
        SheepManager manager = new SheepManager();
//        Runnable countAndReport = manager::incrementAndReport;
//        Runnable countAndReport = manager::atomicIncrementAndReport;
        Runnable countAndReport = manager::synchronizedIncrementAndReport;
        try {
            service = Executors.newFixedThreadPool(20);
            for (int i = 0; i < 20; i++) {
                service.submit(countAndReport);
            }
        } finally {
            if (service != null) {
                service.shutdown();
                service.awaitTermination(1, TimeUnit.MINUTES);
                System.out.println("Total: " + manager.getSheepCount());
            }
        }
    }

    private int getSheepCount() {
        return sheepCount;
    }
}
