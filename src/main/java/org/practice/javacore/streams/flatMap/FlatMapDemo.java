package org.practice.javacore.streams.flatMap;

import java.util.Arrays;
import java.util.stream.Stream;

public class FlatMapDemo {
    static void main() {
        String[] arraysOfWords = {"hello", "world", "how", "are", "you"};
        // Array, Collection değildir; bu nedenle arraysOfWords.stream() yazamayız.
        // Array'in elemanlarından Stream oluşturmanın 1. yolu:
        Stream<String> arraysOfWords1 = Stream.of(arraysOfWords);
        // Array'in elemanlarından Stream oluşturmanın 2. yolu:
        Stream<String> streamOfArray = Arrays.stream(arraysOfWords);
        streamOfArray.map(word -> word.split("")) // Her kelimeyi String[] içindeki harflere ayırır.
                .flatMap(Arrays::stream) // Her String[] dizisini Stream<String> yapıp tek Stream'de düzleştirir.
                .forEach(System.out::println);




    }
}
