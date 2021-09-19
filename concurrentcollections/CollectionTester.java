package com.company.concurrentcollections;

import java.util.*;
import java.util.concurrent.*;

public class CollectionTester {

    public static void testMapAndQueues() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.put("zebra", 52);
        map.put("elephant", 10);
        System.out.println(map.get("elephant"));

        Queue<Integer> queue = new ConcurrentLinkedQueue<>(); // FIFO
        queue.offer(31);
        System.out.println(queue.peek());
        System.out.println(queue.poll());

        Deque<Integer> deque = new ConcurrentLinkedDeque<>(); // double ended
        deque.offer(10);
        deque.push(4);
        System.out.println(deque.peek());
        System.out.println(deque.pop());

        // When talking about LIFO (stack), people say push/poll/peek. When talking about FIFO (single-ended queue), people say offer/poll/peek.
        Deque<Integer> dQueue = new ArrayDeque<>();
        System.out.println(dQueue.offer(10)); // true
        System.out.println(dQueue.offer(4));
        System.out.println(dQueue.peek()); // 10
        System.out.println(dQueue.poll()); // 10
        System.out.println(dQueue.poll()); // 4
        System.out.println(dQueue.peek()); // null
        dQueue.push(5);
        System.out.println(dQueue.peek()); // 5
        System.out.println(dQueue.pop()); // 5

        try {
            BlockingDeque<Integer> blockingDeque = new LinkedBlockingDeque<>();
            blockingDeque.offer(91);
            blockingDeque.offerFirst(5, 2, TimeUnit.MINUTES);
            blockingDeque.offerLast(47, 100, TimeUnit.MICROSECONDS);
            blockingDeque.offer(3, 4, TimeUnit.SECONDS);
            System.out.println(blockingDeque.poll());
            System.out.println(blockingDeque.poll(950, TimeUnit.MILLISECONDS));
            System.out.println(blockingDeque.pollFirst(200, TimeUnit.NANOSECONDS));
            System.out.println(blockingDeque.pollLast(1, TimeUnit.SECONDS));
        } catch (InterruptedException e) { // Handle interruption }
        }

        try {
            BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
            blockingQueue.offer(91);
            blockingQueue.offer(3, 4, TimeUnit.SECONDS);
            System.out.println(blockingQueue.poll());
            System.out.println(blockingQueue.poll(950, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) { // Handle interruption }
        }
    }

    public static void testCopyOnWriteArrayList() {
        List<Integer> list = new CopyOnWriteArrayList<>(Arrays.asList(4, 3, 52));
//        List<Integer> list = new ArrayList<>(Arrays.asList(4, 3, 52)); // ConcurrentModificationException
        for (Integer item : list) {
            System.out.print(item + " ");
            list.add(9); // new array created for each insertion - while iterator sees original
        }
        System.out.println();
        System.out.println("Size: " + list.size());
    }

    public static void testSynchronizedCollections() throws InterruptedException {
        final List<Integer> list = Collections.synchronizedList(new ArrayList<>(Arrays.asList(4, 3, 52)));
        ExecutorService cachedService = Executors.newCachedThreadPool();
        cachedService.submit(() -> list.add(33));
        cachedService.submit(() -> list.add(44));
        cachedService.submit(() -> list.add(55));
        cachedService.submit(() -> list.add(77));
        cachedService.submit(() -> list.add(66));
        cachedService.submit(() -> list.add(88));
        cachedService.submit(() -> list.add(99));
        cachedService.shutdown();
        synchronized (list) { // without this will get ConcurrentModificationException
            for (int data : list) {
                System.out.println(data + " ");
            }
        }
        cachedService.awaitTermination(1, TimeUnit.MINUTES);

        Map<String, Object> foodData = new HashMap<String, Object>();
        foodData.put("penguin", 1);
        foodData.put("flamingo", 2);
        Map<String, Object> synchronizedFoodData = Collections.synchronizedMap(foodData);
        for (String key : synchronizedFoodData.keySet()) {
//            synchronizedFoodData.remove(key); // ConcurrentModificationException

        }
    }
}
