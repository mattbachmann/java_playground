package com.company.synchronization;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ReadWriteLockTest {
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private List<String> names = new ArrayList<>();

    public ReadWriteLockTest() {
        names.add("John Smith");
        names.add("Sarah Smith");
        names.add("James Johnson");
    }

    private String readNames(int i) {
        Lock lock = readWriteLock.readLock();
        try {
            lock.lock();
            System.out.println("Read Lock Obtained!");
            return names.get(i % names.size());
        } finally {
            System.out.println("Read Lock Released!");
            lock.unlock();
        }
    }

    private void addName(String name) {
        Lock lock = readWriteLock.writeLock();
        try {
            lock.lock();
            System.out.println("Write Lock Obtained!");
            Thread.sleep(1000);
            names.add(name);
        } catch (InterruptedException e) {
            // Handle thread interrupted exception 
        } finally {
            System.out.println("Write Lock Released!");
            lock.unlock();
        }
    }

    public static void test() {
        ReadWriteLockTest manager = new ReadWriteLockTest();
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(20);
            for (int i = 0; i < 100; i++) {
                final int employeeNumber = i;
                service.submit(() -> manager.readNames(employeeNumber));
            }
            service.submit(() -> manager.addName("Grace Hopper"));
            service.submit(() -> manager.addName("Josephine Davis"));
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }
}
