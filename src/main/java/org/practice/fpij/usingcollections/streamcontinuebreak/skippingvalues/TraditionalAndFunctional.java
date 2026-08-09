package org.practice.fpij.usingcollections.streamcontinuebreak.skippingvalues;

import java.util.Arrays;

public class TraditionalAndFunctional {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        // Traditional way
        System.out.println("Traditional:");

        for (int i = 5; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }

// Stream way
        System.out.println("\nStream:");

        Arrays.stream(numbers)
                .skip(5)
                .forEach(System.out::println);
    }
    // Traditional:
// We control HOW to iterate (index, loop, boundaries).

// Stream:
// We only describe WHAT we want (skip the first 5 elements).
// Stream controls the iteration internally.
    /*
    Traditional Java: How do I iterate? (Nasıl dolaşacağımı ben yönetirim.)
Streams (Functional Style): What do I want? (Ne istediğimi söylerim; nasıl yapılacağını Stream yönetir.)
     */
}
