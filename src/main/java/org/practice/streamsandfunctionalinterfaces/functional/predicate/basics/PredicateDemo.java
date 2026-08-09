package org.practice.streamsandfunctionalinterfaces.functional.predicate.basics;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Locale.filter;

public class PredicateDemo {
    public static void main(String[] args) {
        Predicate<Integer> isEven = number -> number % 2 == 0;
        System.out.println(isEven.test(4)); // true
        System.out.println(isEven.test(5)); // false

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> evenNumbers = numbers.stream()
                .filter(isEven)
                //lambdayı burda da tanımlayabiliriz: .filter(number -> number % 2 == 0)
                .toList();
        System.out.println(evenNumbers); // [2, 4, 6, 8]

        List<Integer> bigNumbers = numbers.stream()
                .filter(number -> number > 5)
                .toList();
        System.out.println(bigNumbers); // [6, 7, 8, 9]

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        List<String> startWith = names.stream()
                .filter(name-> name.startsWith("A"))
                .toList();
        System.out.println(startWith); // [Alice]

        List<String> users = List.of("Alice", "Bob", "Charlie", "David");
        //tek bir değeri test ederek boolean sonuç alıyor.
        //Burada oluşturduğun users listesi kullanılmıyor. "Alice" değerini doğrudan kendin veriyorsun:
        Predicate<String> startsWith = name -> name.startsWith("A");
        System.out.println(startsWith.test("Alice")); // true

        //istersen tanımladığın startWith Predicate'ini listeyi filtrelemek için de kullanılabilir:
        List<String> usersStartWithA = names.stream()
                .filter(startsWith)
                .toList();
        System.out.println(usersStartWithA);

        //listedeki tüm değerleri test ederek koşulu sağlayanlardan yeni bir liste oluşturuyor.
        //filter() burada koşulu sağlayan elemanları seçerek yeni stream oluşturur.
        // toList() da bu elemanları yeni bir listeye toplar.


        List<String> filteredUsers = users.stream()
                .filter(user -> user.length() > 3)
                .toList();
        System.out.println(filteredUsers); // [Alice, Charlie, David]

    }
}
