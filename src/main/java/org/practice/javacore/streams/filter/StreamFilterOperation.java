package org.practice.javacore.streams.filter;

import java.util.List;

public class StreamFilterOperation {
    public static void main(String[] args) {
        List<String> cities = List.of("Amsterdam", "Alaska", "New York", "Paris");

        System.out.println("Cities starting with 'A':");
        cities.stream()
                .filter(city -> city.startsWith("A"))
                .forEach(System.out::println);

        cities.stream()
                .map(String::toUpperCase)
                .filter(city -> city.startsWith("A"))
                .forEach(System.out::println);
    }
}
