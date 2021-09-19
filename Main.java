package com.company;

import com.company.barriersforkjoinpool.ForkJoinPoolExample;
import com.company.barriersforkjoinpool.LionPenManager;
import com.company.classstuff.A;
import com.company.concurrencybugs.Fox;
import com.company.executor.ThreadPoolExecutorExample;
import com.company.parallelstreams.ParallelStreamsTester;
import com.company.synchronization.ReadWriteLockTest;
import com.company.synchronization.SheepManager;
import com.company.synchronization.SheepManagerWithReentrantLock;
import com.company.threadfactory.CustomThreadFactory;
import com.company.threads.SimpleBlockingQueue;

import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main {

    public static void main(String[] args) throws Exception {
//        testStaticBlock();

//        executorServiceTest();
//        testSynchronization();

//        CollectionTester.testMapAndQueues();
//        CollectionTester.testCopyOnWriteArrayList();
//        CollectionTester.testSynchronizedCollections();

//        ParallelStreamsTester.test();
//        WhaleDataCalculator.test();

//        testBarriersAndForkJoin();
//        CompletableFutureTest.test();
//        Fox.testDeadLock();
//        ParallelStreamsTester.testConcurrentGroupBy();
//        SimpleBlockingQueue.test();

//        SheepManagerWithReentrantLock.testSheepCount();
//        ReadWriteLockTest.test();
//        CustomThreadFactory.test();
        ThreadPoolExecutorExample.test();
    }


    private static void testBarriersAndForkJoin() throws InterruptedException {
        LionPenManager.test();
        ForkJoinPoolExample.test();
    }

    private static void testSynchronization() throws InterruptedException {
        SheepManager.testSheepCount();
    }

    private static void testStaticBlock() {
        A a = new A();
        A a1 = new A();
        A a2 = new A();
    }

    private static void executorServiceTest() throws InterruptedException {
        int processors = Runtime.getRuntime().availableProcessors();
        int numCalcJobs = 8;
        int numIoJobs = 50;

        ExecutorService cachedService = Executors.newCachedThreadPool();
//        System.out.println(String.format("Cached service does %d calc jobs.", numCalcJobs));
//        doWork(cachedService, numCalcJobs, () -> {calcStuff(); return null;});

        System.out.println(String.format("Cached service does %d io jobs.", numIoJobs));
        doWork(cachedService, numIoJobs, () -> {
            simulateIo();
            return null;
        });

        ExecutorService cpuService = Executors.newFixedThreadPool(processors);

//        System.out.println(String.format("CPU service with %d cores does %d calc jobs.", processors, numCalcJobs));
//        doWork(cpuService, numCalcJobs, () -> {calcStuff(); return null;});

        System.out.println(String.format("CPU service with %d cores does %d io jobs.", processors, numIoJobs));
        doWork(cpuService, numIoJobs, () -> {
            simulateIo();
            return null;
        });
    }

    private static void doWork(ExecutorService service, int numJobs, Callable<Void> work) throws InterruptedException {
        long startTime = System.nanoTime();

        try {
            IntStream.range(0, numJobs).forEach(i -> {
                service.submit(() -> {
                    work.call();
                    System.out.println(String.format("Done job %d", i));
                    return i;
                });
            });
        } finally {
            service.shutdown();
        }
        service.awaitTermination(20, TimeUnit.MINUTES);
        // Check whether all tasks are finished
        if (service.isTerminated()) {
            System.out.println("All tasks finished");
        } else {
            System.out.println("At least one task is still running");
        }
        long time = System.nanoTime() - startTime;
        System.out.println("Time for doWork [ms]: " + time / 1e6);
    }

    static private void calcStuff() {
        long startTime = System.nanoTime();
        IntStream.range(0, 20).forEach(j -> {
            LongStream.range(1, 2000000000)
                .reduce(1, (long x, long y) -> x * y);
        });
        long time = System.nanoTime() - startTime;
        System.out.println("Time for calcStuff [ms]: " + time / 1e6);
    }

    static private void simulateIo() throws InterruptedException {
        Thread.sleep(500);
    }
}
