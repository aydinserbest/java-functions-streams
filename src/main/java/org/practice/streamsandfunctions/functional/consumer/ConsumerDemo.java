package org.practice.streamsandfunctions.functional.consumer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerDemo {
    public static void main(String[] args) {
        Consumer<String> convertAndDisplay = input -> System.out.println(input.toUpperCase());
        convertAndDisplay.accept("hello world");

        Consumer<Integer> squareOf = num -> System.out.println(num * num);
        //Arrays.asList(1, 2, 3, 4, 5).forEach(squareOf);
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.forEach(squareOf);

        Consumer<String> printLength = str -> System.out.println(str.length());
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        names.forEach(printLength);

        Consumer<String> appendInput = str -> System.out.println("New value after appending is Hello: " + str);
        appendInput.andThen(convertAndDisplay).accept("world");
    }
}
