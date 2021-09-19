package com.company.executor;

import com.company.threadfactory.CustomThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolExecutorExample {

    public static void test() throws InterruptedException {

        BlockingQueue<Runnable> worksQueue = new ArrayBlockingQueue<Runnable>(10);
        RejectedExecutionHandler rejectionHandler = new RejectedExecutionHandelerImpl();
        CustomThreadFactory factory = new CustomThreadFactory("CustomThreadFactory");
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 10, TimeUnit.SECONDS, worksQueue, rejectionHandler);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 10, TimeUnit.SECONDS, worksQueue, factory, rejectionHandler);

        executor.prestartAllCoreThreads();

        worksQueue.add(new TaskOne());
        worksQueue.add(new TaskTwo());
        worksQueue.add(new TaskThree());

        System.out.print("All Threads are created now\n\n");
        System.out.print("Give me CustomThreadFactory stats:\n\n" + factory.getStats());

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        executor.shutdownNow();
    }

    private static class RejectedExecutionHandelerImpl implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(
            Runnable runnable,
            ThreadPoolExecutor executor) {
            System.out.println(runnable.toString() + " : I've been rejected ! ");
        }
    }

    private static class TaskOne implements Runnable {
        @Override
        public void run() {
            System.out.println("Executing Task One");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class TaskTwo implements Runnable {
        @Override
        public void run() {
            System.out.println("Executing Task Two");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class TaskThree implements Runnable {
        @Override
        public void run() {
            System.out.println("Executing Task Three");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

