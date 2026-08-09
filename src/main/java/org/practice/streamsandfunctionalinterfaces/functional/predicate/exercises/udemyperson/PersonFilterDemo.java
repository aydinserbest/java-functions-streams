package org.practice.streamsandfunctionalinterfaces.functional.predicate.exercises.udemyperson;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PersonFilterDemo {
    public static void main(String[] args) {
        List<Person> personsOlderThan29 = filterByAge(29);
        System.out.println("29 yaşından büyük kişi sayısı: " + personsOlderThan29.size());
        personsOlderThan29.forEach(System.out::println);

        System.out.println();

        List<Person> personsFromNewYork = filterByCity("New York");
        System.out.println("New York'ta yaşayan kişi sayısı: " + personsFromNewYork.size());
        personsFromNewYork.forEach(System.out::println);
    }

    public static List<Person> filterByAge(int age) {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John", 28, "New York"));
        persons.add(new Person("Jane", 22, "Los Angeles"));
        persons.add(new Person("Jack", 30, "New York"));
        persons.add(new Person("Jill", 24, "Chicago"));


        // Write your code below
        Predicate<Person> isOlder = person -> person.getAge() > age;
        return filter(persons, isOlder);
    }

    public static List<Person> filterByCity(String city) {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John", 28, "New York"));
        persons.add(new Person("Jane", 22, "Los Angeles"));
        persons.add(new Person("Jack", 30, "New York"));
        persons.add(new Person("Jill", 24, "Chicago"));
        // Write your code below
        Predicate<Person> livesInCity = person -> person.getCity().equals(city);
        return filter(persons, livesInCity);

    }

    public static List<Person> filter(List<Person> persons, Predicate<Person> predicate) {
        List<Person> filteredPersons = new ArrayList<>();
        for (Person person : persons) {
            if (predicate.test(person)) {
                filteredPersons.add(person);
            }
        }
        return filteredPersons;
    }
}
