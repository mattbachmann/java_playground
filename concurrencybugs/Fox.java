package com.company.concurrencybugs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Fox {
    private static class Food {
    }

    private static class Water {
    }

    private void eatAndDrink(Food food, Water water) {
        synchronized (Food.class) {
            System.out.println("Got Food!");
            move();
            synchronized (Water.class) {
                System.out.println("Got Water!");
            }
        }
    }

    private void drinkAndEat(Food food, Water water) {
        synchronized (Water.class) {
            System.out.println("Got Water!");
            move();
            synchronized (Food.class) {
                System.out.println("Got Food!");
            }
        }
    }

    private void move() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Handle exception
        }
    }

    public static void testDeadLock() throws InterruptedException { // Create participants and resources
        Fox foxy = new Fox();
        Fox tails = new Fox();
        Food food = new Food();
        Water water = new Water(); // Process data
        ExecutorService service = null;
        try {
            service = Executors.newScheduledThreadPool(10);
            service.submit(() -> foxy.eatAndDrink(food, water));
            service.submit(() -> tails.drinkAndEat(food, water));
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
        service.awaitTermination(2, TimeUnit.MINUTES);
    }
}
