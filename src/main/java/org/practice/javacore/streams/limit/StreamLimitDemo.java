package org.practice.javacore.streams.limit;

import java.util.Random;
import java.util.stream.Stream;

public class StreamLimitDemo {
    public static void main(String[] args) {
        Stream.generate(new Random()::nextInt)
                .limit(10)
                .forEach(System.out::println);
    }
}
