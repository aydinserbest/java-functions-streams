package org.practice.streamsandfunctionalinterfaces.functional.predicate.exercises;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateExercises {
    static void main() {
        Predicate<Integer> isEven = number -> number % 2 == 0;
        Predicate<Integer> isGreaterThan18 = number -> number > 18;
        System.out.println(isEven.or(isGreaterThan18).test(20)); // true
        System.out.println(isEven.and(isGreaterThan18).test(18)); // false

        System.out.println(checkNumber(20, isEven)); // true
        System.out.println(checkNumber(20, isGreaterThan18)); // true
        System.out.println(checkNumber(15, isEven)); // false

        System.out.println(isEven.negate().test(15)); // true

        System.out.println(Predicate.not(isEven).test(15)); // true
        boolean test = Predicate.not(isEven).test(15);// true
        System.out.println(test);
        Predicate<Integer> not = Predicate.not(isEven);
        System.out.println(not.test(15));

        Predicate<String> compareEquality = Predicate.isEqual("John");
        System.out.println(compareEquality.test("John"));
        System.out.println(compareEquality.test("Madame"));

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        list.stream()
                .filter(isEven)
                .toList()
                .forEach(System.out::println);

    }
    public static boolean checkNumber(int number, Predicate<Integer> predicate) {
        return predicate.test(number);
    }
}
