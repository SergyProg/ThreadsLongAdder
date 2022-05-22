package ru.netology;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.LongAdder;

//import static java.util.Arrays.stream;
import static java.util.concurrent.ThreadLocalRandom.current;

public class Main {
    static int MIN_REVENUE = 100;
    static int MAX_REVENUE = 1000000;

    static int MIN_NUMBERS_OF_SAILS = 3;
    static int MAX_NUMBERS_OF_SAILS = 1000;

    public static void main(String[] args) throws Exception {
        int[] shop1 = randomIntArray();
        int[] shop2 = randomIntArray();
        int[] shop3 = randomIntArray();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Integer>> futures = executorService.invokeAll(List.of(parallelSum(shop1), parallelSum(shop2), parallelSum(shop3)));
        executorService.shutdown();

        long revenue = 0;

        for (Future<Integer> future : futures) {
            revenue += future.get();
        }
        System.out.println("Общая выручка трёх магазинов составила: " + revenue + " рублей");
    }

    private static int[] randomIntArray() {
        return current().ints(MIN_REVENUE, MAX_REVENUE).limit(randomLimit()).toArray();
    }

    private static long randomLimit() {
        return current().nextLong(MIN_NUMBERS_OF_SAILS, MAX_NUMBERS_OF_SAILS);
    }

    private static Callable<Integer> parallelSum(int[] revenueArray) {
        return (() -> {
            LongAdder stat = new LongAdder();
            for (int i = 0; i < revenueArray.length; i++) {
                stat.add(revenueArray[i]);
            }
            return (int) stat.sum();
        });
        //return () -> stream(transactions).sum();
    }
}
