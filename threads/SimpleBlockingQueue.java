package com.company.threads;

public class SimpleBlockingQueue {
    final Object[] items = new Object[2];
    int putptr, takeptr, count;

    private synchronized void put(Object x) throws InterruptedException {
        while (count == items.length)
            wait();
        items[putptr] = x;
        System.out.println("Put " + x);
        if (++putptr == items.length) putptr = 0;
        ++count;
        notifyAll();
    }

    private synchronized Object take() throws InterruptedException {
        while (count == 0)
            wait();
        Object x = items[takeptr];
        if (++takeptr == items.length) takeptr = 0;
        --count;
        notifyAll();
        System.out.println("Took " + x);
        return x;
    }

    public static void test() throws InterruptedException {
        SimpleBlockingQueue queue = new SimpleBlockingQueue();
        Thread t1 = new Thread(() -> {
            try {
                queue.put("A");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
    }
}
