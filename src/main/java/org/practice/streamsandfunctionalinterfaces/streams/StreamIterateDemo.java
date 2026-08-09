package org.practice.streamsandfunctionalinterfaces.streams;

import java.util.stream.Stream;

public class StreamIterateDemo {
    public static void main(String[] args) {
        /*
         * Stream.iterate(seed, rule)
         *
         * seed: Stream'in ilk değeri
         * rule: Mevcut değerden bir sonraki değeri üretme kuralı
         *
         * İki parametreli iterate() sonsuz bir Stream üretir.
         * Bu nedenle aşağıdaki örneklerde eleman sayısını limit() ile sınırlıyoruz.
         */

        // 1 ile başla; her adımda mevcut sayıya 1 ekle.
        // Üretilen değerler: 1, 2, 3, 4, 5
        Stream.iterate(1, number -> number + 1)
                .limit(5)
                .forEach(System.out::println);

        // 0 ile başla; her adımda mevcut sayıya 2 ekle.
        // Üretilen değerler: 0, 2, 4, 6, 8
        Stream.iterate(0, number -> number + 2)
                .limit(5)
                .forEach(System.out::println);

        // 1 ile başla; her adımda mevcut sayıyı 2 ile çarp.
        // Üretilen değerler: 1, 2, 4, 8, 16, 32
        Stream.iterate(1, number -> number * 2)
                .limit(6)
                .forEach(System.out::println);

        // 10 ile başla; her adımda mevcut sayıdan 1 çıkar.
        // Üretilen değerler: 10, 9, 8, 7, 6
        Stream.iterate(10, number -> number - 1)
                .limit(5)
                .forEach(System.out::println);

        // iterate() yalnızca sayılarla çalışmaz.
        // "Java" ile başla; her adımda metnin sonuna bir ünlem ekle.
        // Üretilen değerler: Java, Java!, Java!!, Java!!!
        Stream.iterate("Java", text -> text + "!")
                .limit(4)
                .forEach(System.out::println);

        /*
         * Üç parametreli iterate(seed, condition, rule) sonlu bir Stream oluşturabilir:
         *
         * 1                         -> başlangıç değeri
         * number -> number <= 5     -> devam etme koşulu (Predicate)
         * number -> number + 1      -> sonraki değeri üretme kuralı
         *
         * Koşul false olduğunda üretim duracağı için ayrıca limit() kullanmak gerekmez.
         */
        Stream.iterate(
                1,
                number -> number <= 5,
                number -> number + 1
        ).forEach(System.out::println);
    }
}
