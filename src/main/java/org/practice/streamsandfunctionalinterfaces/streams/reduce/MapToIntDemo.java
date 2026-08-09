package org.practice.streamsandfunctionalinterfaces.streams.reduce;

import java.util.Arrays;
import java.util.List;

public class MapToIntDemo {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        /*
         * numbers.stream() bize Stream<Integer> verir.
         *
         * Integer bir nesne tipidir. mapToInt(), Stream içindeki her Integer
         * değerini primitive int değerine dönüştürür ve IntStream üretir:
         *
         * List<Integer>
         *      ↓ stream()
         * Stream<Integer>
         *      ↓ mapToInt()
         * IntStream
         *
         * num -> num ifadesinde num bir Integer'dır. Java, dönen Integer
         * değerini otomatik unboxing ile int değerine dönüştürür.
         *
         * mapToInt() bir intermediate operation'dır. Stream'i o anda
         * dolaşmaz; dönüşüm kuralını pipeline'a ekler.
         *
         * sum() bir terminal operation'dır. sum() çağrıldığında Stream
         * dolaşılır, Integer değerleri int'e dönüştürülür ve toplanır.
         */
        int result = numbers.stream()
                .mapToInt(num -> num)
                .sum();

        System.out.println("Sum: " + result);

        /*
         * num -> num lambdasını daha açık biçimde num.intValue() kullanarak
         * yazabiliriz. intValue(), Integer nesnesinin primitive int değerini
         * verir.
         */
        int sumWithIntValue = numbers.stream()
                .mapToInt(num -> num) //num -> num.intValue()
                .sum();

        System.out.println("Sum with intValue(): " + sumWithIntValue);

        /*
         * Integer::intValue, num -> num.intValue() lambdasının
         * method-reference karşılığıdır.
         */
        int sumWithMethodReference = numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("Sum with method reference: " + sumWithMethodReference);

        /*
         * Genel Stream<Integer> üzerinde doğrudan sum(), average(), min() ve
         * max() metotları bulunmaz. mapToInt() ile IntStream elde ettiğimizde
         * bu hazır sayısal terminal operation'ları kullanabiliriz.
         *
         * average(), min() ve max() boş bir Stream'de sonuç bulamayabileceği
         * için Optional döndürür. orElse(0), sonuç yoksa kullanılacak değeri
         * belirtir.
         */
        double average = numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);

        int minimum = numbers.stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);

        int maximum = numbers.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        System.out.println("Average: " + average);
        System.out.println("Minimum: " + minimum);
        System.out.println("Maximum: " + maximum);

        /*
         * reduce() ile mapToInt().sum() bu örnekte aynı toplamı üretir.
         *
         * reduce(): Birleştirme kuralını biz veririz.
         * sum():     IntStream'in hazır toplama işlemini kullanırız.
         *
         * 0, toplama işleminin identity (başlangıç) değeridir.
         */
        int sumWithReduce = numbers.stream()
                .reduce(0, Integer::sum);

        System.out.println("Sum with reduce: " + sumWithReduce);

        /*
         * reduce() yalnızca toplama için kullanılmaz. Burada başlangıç değeri
         * 1 olan özel bir çarpma kuralıyla bütün sayıları çarpıyoruz.
         *
         * 1 çarpmanın identity değeridir:
         * 1 * herhangi bir sayı = aynı sayı
         */
        int product = numbers.stream()
                .reduce(1, (a, b) -> a * b);

        System.out.println("Product: " + product);

        //with filter- and - max
        numbers.stream()
                .filter(num -> num % 2 == 0)
                .mapToInt(num -> num).max()
                .ifPresent(max -> System.out.println("Maximum even number: " + max));
    }
}
