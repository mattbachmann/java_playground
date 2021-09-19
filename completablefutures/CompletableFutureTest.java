package com.company.completablefutures;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CompletableFutureTest {
    public static void test() throws InterruptedException, ExecutionException {
        CompletableFuture<Double> cExpenseReports = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                return new Random().nextDouble();
            } catch (InterruptedException e) {
                throw new CompletionException(e);
            }
        }, Executors.newCachedThreadPool());

        cExpenseReports.thenAccept(expenseInvoices -> {
            System.out.println("Result: " + expenseInvoices);
        });

        cExpenseReports.exceptionally(e -> {
            System.out.println("Failed: " + e.getCause());
            return -1.;
        });

        System.out.println("Started calculation.");
        Thread.sleep(2000); // keep main thread alive for async task to finish

//        double result = cExpenseReports.get(); // blocking wait
    }
}
