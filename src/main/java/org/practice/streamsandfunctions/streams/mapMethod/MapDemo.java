package org.practice.streamsandfunctions.streams.mapMethod;

import java.util.ArrayList;
import java.util.List;

public class MapDemo {
    public static void main(String[] args) {
        List<String> cities = new ArrayList<>();
        cities.add("amsterdam");
        cities.add("alaska");
        cities.add("new york");
        cities.add("paris");

        cities.stream()
                .map(String::toUpperCase) //c -> c.toUpperCase())
                .forEach(System.out::println); //word -> System.out.println(word))

        /*
        stream operations do not change original collection, they return new collection
         */
        //intermediate operation ??? map
        //terminal operation ??? forEach, collect

        cities.stream()
                .map(String::toUpperCase)
                .map(word -> "Hi: " + word)
                .forEach(System.out::println);
    }
}
