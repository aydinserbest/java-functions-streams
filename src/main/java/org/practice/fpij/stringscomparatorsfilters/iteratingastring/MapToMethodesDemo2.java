package org.practice.fpij.stringscomparatorsfilters.iteratingastring;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MapToMethodesDemo2 {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        Stream<Integer> integerStream = names.stream()
                .map(String::length);
        List<Integer> list = integerStream.toList();
        System.out.println("Uzunluklar: " + list);
        /*
        Stream<String>
      ↓
Stream<Integer>
         */

        IntStream intStream = names.stream()
                .mapToInt(String::length);
        int[] lengthsArray = intStream.toArray();
        System.out.println("Uzunluklar array: " + Arrays.toString(lengthsArray));
        /*
        Stream<String>
      ↓
IntStream
         */

        //neden mapToInt kullandık? Çünkü mapToInt, Stream<Integer> yerine IntStream döndürür.
        // IntStream, int değerleriyle çalışmak için optimize edilmiştir ve bazı ek yöntemler sunar.
        int sum = names.stream()
                .mapToInt(String::length)
                .sum();
        System.out.println("Toplam uzunluk: " + sum);

        double average = names.stream()
                .mapToInt(String::length)
                .average()
                .orElse(0.0);
        System.out.println("Ortalama uzunluk: " + average);

        /*
        IntStream sayesinde:
sum()
average()
min()
max()
gibi sayısal metodlara kolayca ulaşıyoruz.
         */

    }
}
/*
Method reference ile daha kısa hali
Şu:
.mapToInt(name -> name.length())
yerine:
.mapToInt(String::length)
yazabiliriz.
Yani:
int total = names.stream()
        .mapToInt(String::length)
        .sum();
Çünkü:
name -> name.length()
şunun method-reference hali:
String::length

/*
        :: nedir?
Şu:
System.out::println
method reference.
Bunu Türkçe zihninde şöyle okuyabilirsin:
"System.out nesnesinin println metodunu kullan."

içinde bulunduğumuz IterateString classında printChar metodu oluşturduk ve
method refrence ile kullandık -> .forEach(IterateString::printChar);
         */
        /*
        IterateString::printChar
kabaca:
ch -> IterateString.printChar(ch)
demek.
Yani:
.forEach(ch -> IterateString.printChar(ch));
yerine:
.forEach(IterateString::printChar);
yazıyoruz.
Tekrar aynı kural:
x -> method(x)
çoğu zaman:
Class::method
şeklinde sadeleşebilir.

 */