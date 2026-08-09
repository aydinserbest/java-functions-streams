package org.practice.streamsandfunctions.streams;

import java.util.Random;
import java.util.stream.Stream;

public class StreamIterateAndGenerateDemo {

    public static void main(String[] args) {
        /*
         * iterate():
         * Bir başlangıç değeri alır. Her yeni değer, bir önceki değer kullanılarak üretilir.
         *
         * 1 ile başla; her adımda önceki değere 1 ekle.
         * Sonuç: 1, 2, 3, 4, 5
         */
        System.out.println("Stream.iterate():");
        Stream.iterate(1, number -> number + 1)
                .limit(5)
                .forEach(System.out::println);

        /*
         * generate():
         * Başlangıç değerine ihtiyaç duymaz. Her eleman Supplier tarafından üretilir.
         * Önceden üretilen değer, yeni değeri hesaplamak için kullanılmaz.
         *
         * Her ihtiyaç olduğunda bağımsız bir rastgele sayı üret.
         */
        System.out.println("Stream.generate():");
        Random random = new Random();
        Stream.generate(random::nextInt)
                .limit(5)
                .forEach(System.out::println);

        /*
         * generate() sabit bir değer de üretebilir.
         * Supplier parametre almaz ve her çağrıldığında "Java" döndürür.
         * Sonuç: Java, Java, Java
         */
        System.out.println("Stream.generate() ile sabit değer:");
        Stream.generate(() -> "Java")
                .limit(3)
                .forEach(System.out::println);

        /*
         * Kısa fark:
         *
         * iterate(seed, rule) -> Başlangıç değeri vardır ve sonraki değer önceki değere bağlıdır.
         * generate(supplier)  -> Başlangıç değeri yoktur; her değer Supplier tarafından üretilir.
         *
         * Bu kullanımlarda iki Stream de sonsuzdur.
         * Bu nedenle kaç eleman kullanılacağını limit() ile belirliyoruz.
         */
    }
}
