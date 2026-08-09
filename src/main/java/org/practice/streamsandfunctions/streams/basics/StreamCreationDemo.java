package org.practice.streamsandfunctions.streams.basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamCreationDemo {
    static void main() {
        List<String> department = new ArrayList<>();
        department.add("HR");
        department.add("Supply");
        department.add("Engineer");
        department.add("Computer Science");
        department.add("Marketing");
        department.add("Finance");
        department.add("Sales");

        Stream<String> departmentStream = department.stream();
        //iterate all the elements and display on the console

        //foreach consumer bekler
        //consumerdan kasıt kendisine atanabailecek lambda ifadesidir-davranış
        //ör:
        /*
        s -> System.out.println(s) bir davranış
        atanırken
        Consumer<String> sout = s -> System.out.println(s)
        sout.apply("John);
         */
        departmentStream.forEach(s -> System.out.println(s));
        //method reference ile
        //class instance'ı :: metot ismi:
        // departmentStream.forEach(System.out::println);

        // Collection veya array oluşturmadan, verilen değerlerden doğrudan Stream oluşturabiliriz.
        Stream<String> stringStream = Stream.of("human resources", "management");
        stringStream.forEach(s -> System.out.println(s));

        // Array, Collection değildir; bu nedenle arraysOfWord.stream() yazamayız.
        // Bir array'den Stream oluşturmak için Arrays.stream(array) kullanırız.
        String[] arraysOfWord = {"apple", "banana"};
        // arraysOfWord.stream(); // Derleme hatası: String[] içinde stream() metodu yoktur.
        Stream<String> streamOfWords = Arrays.stream(arraysOfWord);
        streamOfWords.forEach(System.out::println);

    }
}
