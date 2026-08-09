package org.practice.streamsandfunctions.streams.flatMap.personExample;

import java.util.Arrays;
import java.util.List;

public class FlatMapExample {
    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
                new Person("Alice", Arrays.asList("123-456-7890", "987-654-3210")),
                new Person("Bob", Arrays.asList("555-555-5555")),
                new Person("Charlie", Arrays.asList("111-222-3333", "444-555-6666"))
        );
        List<List<String>> list = persons.stream()
                .map(Person::getPhoneNumbers) //person -> person.getPhoneNumbers()
                .toList();
        System.out.println("List of List of Phone Numbers: " + list);
        list.forEach(System.out::println);

        System.out.println("Flattened List of Phone Numbers:");
        List<String> flatMapResult = persons.stream()
                .flatMap(person -> person.getPhoneNumbers().stream())
                .toList();
        flatMapResult.forEach(System.out::println);
    }
}
