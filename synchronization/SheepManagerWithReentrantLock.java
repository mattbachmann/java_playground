package com.company.synchronization;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SheepManagerWithReentrantLock {
    private int sheepCount = 0;
    private Lock lock = new ReentrantLock();

    private Void incrementAndReport() throws InterruptedException {
        if (lock.tryLock(1, TimeUnit.NANOSECONDS)) {
            try {
                lock.lock(); // This will raise the lock counter - have to unlock twice
//                System.out.println("Counted: " + (++sheepCount)); // takes ~20 ms
                ++sheepCount;
            } finally {
                lock.unlock();
                lock.unlock();
            }
        } else {
            System.out.println("Could not finish counting within work time. So went home.");
        }
        return null;
    }

    public static void testSheepCount() throws Exception {
        ExecutorService service = null;
        SheepManagerWithReentrantLock manager = new SheepManagerWithReentrantLock();
        Callable<Void> countAndReport = manager::incrementAndReport;

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
