package org.practice.streamsandfunctions.comparator;

import java.util.Comparator;
import java.util.List;

public class PersonComparableDemo {

    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("Mehmet", 35),
                new Person("Alice", 28),
                new Person("John", 42)
        );

        /*
         * Person, Comparable<Person> implement ettiği için sınıfın bir doğal
         * sırası vardır. Aşağıdaki sorted() parametre istemez ve Person içindeki
         * compareTo() metodunu kullanır.
         *
         * Bu örnekte doğal sıra ada göre belirlenmiştir.
         */
        List<Person> naturalOrder = people.stream()
                .sorted()
                .toList();

        System.out.println("Natural order by name: " + naturalOrder);

        /*
         * Comparable yalnızca bir temel/doğal sıra tanımlar.
         * Fakat farklı bir ekranda kişileri yaşa göre sıralamak isteyebiliriz.
         * Bunun için Person sınıfını değiştirmek yerine dışarıdan Comparator
         * veririz.
         */
        Comparator<Person> byAge =
                Comparator.comparingInt(Person::getAge);

        List<Person> peopleByAge = people.stream()
                .sorted(byAge)
                .toList();

        System.out.println("Alternative order by age: " + peopleByAge);
    }

    /*
     * Comparable bir class değildir, interface'tir.
     * Bu nedenle "extends Comparable" değil "implements Comparable<Person>"
     * yazarız.
     *
     * compareTo(this, other) sonucunun anlamı:
     * negatif -> this önce gelir
     * sıfır   -> sıralama bakımından eşit
     * pozitif -> this sonra gelir
     */
    static class Person implements Comparable<Person> {
        private final String name;
        private final int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        String getName() {
            return name;
        }

        int getAge() {
            return age;
        }

        @Override
        public int compareTo(Person other) {
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + " (" + age + ")";
        }
    }
}
