package org.practice.fpij.usingcollections.transformingcollection;

import java.util.List;
import java.util.stream.Stream;

public class TransformStreamExample {
    public static void main(String[] args) {
        List<String> friends = List.of("Alice", "Bob", "Charlie", "David");
        Stream<String> stringStream = friends.stream()
                .map(String::toUpperCase);
        stringStream.forEach(System.out::println);


        Stream<String> lengthString = friends.stream()
                .filter(name -> name.length() > 3);
        lengthString.forEach(System.out::println);
    }
}
