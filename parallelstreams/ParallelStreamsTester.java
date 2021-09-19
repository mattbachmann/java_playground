package com.company.parallelstreams;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParallelStreamsTester {
    public static void test() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        Stream<Integer> stream = list.stream();
//        Stream<Integer> parallelStream = stream.parallel(); // Will convert also stream to parallel stream! And using/closing stream also closes parallelStream
        Stream<Integer> parallelStream2 = list.parallelStream();

        System.out.println("Serial stream forEach: ");
        stream.forEach(System.out::println);
        System.out.println("Parallel stream forEach:");
        parallelStream2.forEach(System.out::println);
        System.out.println("Parallel stream forEachOrdered:");
        list.parallelStream().forEachOrdered(System.out::println);

        System.out.println();
        Arrays.asList("jackal", "kangaroo", "lemur")
            .parallelStream()
            .map(s -> {
                System.out.println(s);
                return s.toUpperCase();
            })
            .forEach(System.out::println);

        avoidStatefulExpressions();
        reduceMustBeAssociativeAndStateless();
        testConcurrentCollectors();
    }

    private static void testConcurrentCollectors() {
        Stream<String> stream = Stream.of("w", "o", "l", "f").parallel();
        SortedSet<String> set = stream.collect(ConcurrentSkipListSet::new, Set::add, Set::addAll); // collector must be unordered and concurrent
//        Set<String> set = stream.collect(Collectors.toSet()); // not concurrent
        System.out.println(set); // [f, l, o, w]

        Stream<String> ohMy = Stream.of("lions", "tigers", "bears").parallel();
        ConcurrentMap<Integer, String> map = ohMy.collect(Collectors.toConcurrentMap(String::length, k -> k, (s1, s2) -> s1 + "," + s2));
//        ConcurrentMap<Integer, String> map = ohMy.collect(Collectors.toConcurrentMap(String::length, k -> k)); // Duplicate key exception
        System.out.println(map); // {5=lions,bears, 6=tigers}
        System.out.println(map.getClass()); // java.util.concurrent.ConcurrentHashMap
    }

    private static void reduceMustBeAssociativeAndStateless() {
        System.out.println();
        System.out.println(Arrays.asList(1, 2, 3, 4, 5, 6)
            .parallelStream()
//            .stream() // subtracts all from 0 as expected = -21
//            .reduce(0, (a, b) -> (a - b)); // NOT AN ASSOCIATIVE ACCUMULATOR - sum and product are, but not subtraction and division.
//            Default combiner cannot deal with non-associative subtraction result is wrong!
//            The parallel combination creates intermediate results - so combiner must be associative.
            .reduce(0, (a, b) -> (a - b), (a, b) -> a + b)); // Nonassociative accumulator, but Associative combiner - seems to work also with parallel stream.
        // 4-(2-1) = 3
        // (4-2)-1 = 1

        System.out.println(Arrays.asList("w", "o", "l", "f")
            .parallelStream()
            .reduce("", String::concat)); // order is kept!

        System.out.println(Arrays.asList("w", "o", "l", "f")
            .parallelStream()
            .reduce("X", String::concat)); // But parallel accumulator applies identity to each element in parallel
    }

    private static void avoidStatefulExpressions() {
        List<Integer> data = Collections.synchronizedList(new ArrayList<>());
//        List<Integer> data = new ArrayList<>(); // concurrent write - elements may be lost or null
        IntStream.range(0, 11)
            .parallel()
            .map(i -> {
                data.add(i);
                return i + 1 - 1;
            }) // AVOID STATEFUL LAMBDA EXPRESSIONS!
            .forEachOrdered(i -> System.out.print(i + " "));

        System.out.println();
        for (Integer e : data) {
            System.out.print(e + " ");
        }
    }

    public static void testConcurrentGroupBy() {
        Stream<String> cats = Stream.of("leopard", "lynx", "ocelot", "puma").parallel();
        Stream<String> bears = Stream.of("panda", "grizzly", "polar").parallel();
        ConcurrentMap<Boolean, List<String>> data = Stream.of(cats, bears)
            .flatMap(s -> s)
            .collect(Collectors.groupingByConcurrent(s -> !s.startsWith("p")));
        System.out.println(data.get(false).size() + " " + data.get(true).size());
    }
}
