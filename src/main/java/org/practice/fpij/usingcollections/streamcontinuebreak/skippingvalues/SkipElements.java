package org.practice.fpij.usingcollections.streamcontinuebreak.skippingvalues;

import java.util.Arrays;
import java.util.List;

public class SkipElements {
    public static void main(String[] args) {
        // Create an array of integers
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        // Skip the first 5 elements and print the rest
        System.out.println("Skipping the first 5 elements:");
        for (int i = 5; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }
        /*
         Array'ler Collection değildir ve List interface'ini implement etmez.
         Bu nedenle numbers.stream() yazılamaz.

         Metodun doğru adı stream()'dir; streams() değildir. List, Set ve Queue
         gibi Collection türleri, Collection interface'inden gelen stream()
         metodunu doğrudan kullanabilir:

         names.stream()
         products.stream()

         Map de Collection değildir. Map verisi Stream'e çevrilecekse hangi
         bölümün işleneceği seçilir:

         map.keySet().stream()
         map.values().stream()
         map.entrySet().stream()

         Array'ler için java.util.Arrays sınıfındaki static Arrays.stream(...)
         metodu kullanılır. numbers bir int[] olduğu için bu çağrı IntStream
         üretir. skip(5) ilk beş int değerini akışta atlar; kaynak array'i silmez
         veya değiştirmez. forEach terminal işlemi kalan değerleri yazdırır.
        */
        Arrays.stream(numbers)
                .skip(5)
                .forEach(System.out::println);

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve", "Frank");
        names.stream()
                .skip(3)
                .map(String::toUpperCase) //function - method reference
                //.map(name -> name.toUpperCase()) -lambda
                .forEach(System.out::println);
    }
}
/*
// Bir array'den Stream oluşturmak için Arrays.stream(array) kullanırız.

String[] arraysOfWord = {"apple", "banana"};

  // arraysOfWord.stream(); // Derleme hatası: String[] içinde stream() metodu yoktur.

  Stream<String> streamOfWords = Arrays.stream(arraysOfWord);

  buradaki arraysOfWord, List veya başka bir Collection değil; Java’nın yerleşik array yapısıdır. Array’in kendi .stream() metodu yoktur:

  arraysOfWord.stream(); // Hatalı

  Bu yüzden yardımcı Arrays sınıfını kullanırız:

  Arrays.stream(arraysOfWord);

NOT:
Stream.of(a, b, c);    // Değerleri doğrudan Stream'e verme
 */