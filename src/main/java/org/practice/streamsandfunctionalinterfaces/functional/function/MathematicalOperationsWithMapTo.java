package org.practice.streamsandfunctionalinterfaces.functional.function;

import java.util.List;
import java.util.stream.IntStream;

public class MathematicalOperationsWithMapTo {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");

        IntStream intStream = names.stream()
                .mapToInt(String::length);

        // sum();
        int sum = intStream.sum();
        /*
        Burada dikkat:
.sum()
doğrudan:
int
döndürür.
Bu yüzden:
int total = ...
yazabiliyoruz.
         */

        //average();
        double average = names.stream()
                .mapToDouble(String::length)
                .average()
                .orElse(0.0);

        /*
        .orElse(0)
"Neden sum()da yoktu ama average()da var?"
Çünkü average() doğrudan double döndürmüyor.
Şunu döndürüyor:
OptionalDouble
Neden?
Liste boş olabilir:
List<String> names = List.of();
O zaman:
Ortalama nedir?
diye sorduğumuzda cevap yok.
Çünkü:
toplam / eleman sayısı

0 / 0
anlamlı bir ortalama değil.
Java bu yüzden:
.average()
sonucunu OptionalDouble içerisinde verir.
Biz de:
.orElse(0)
diyoruz:
Eğer ortalama varsa onu ver, yoksa 0 ver.

Şimdilik Optional konusuna fazla girmeyelim; ayrıca öğreniriz.
         */

        // min();
        //En kısa ismin kaç karakter olduğunu bulalım:
        names.stream()
                .mapToInt(String::length)
                .min()
                .ifPresent(min -> System.out.println("Minimum uzunluk: " + min));

        // max();
        //En uzun ismin kaç karakter olduğunu bulalım:
        int max = names.stream()
                .mapToInt(String::length)
                .max()
                .orElseThrow();

        System.out.println("Toplam: " + sum);
        System.out.println("Ortalama: " + average);
        
        System.out.println("Maximum: " + max);

    }
}
