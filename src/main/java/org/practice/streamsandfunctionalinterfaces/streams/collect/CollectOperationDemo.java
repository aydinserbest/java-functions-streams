package org.practice.streamsandfunctionalinterfaces.streams.collect;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CollectOperationDemo {
    public static void main(String[] args) {
        List<String> cities = Arrays.asList("amsterdam", "paris", "berlin", "new york");

        /*
         * map() bir intermediate operation'dır:
         * Her şehri büyük harfli yeni bir String'e dönüştürür.
         *
         * toList() bir terminal operation'dır:
         * Stream'i çalıştırır ve dönüştürülen elemanları yeni bir Listede toplar.
         * Stream.toList() ile oluşan liste değiştirilemez (unmodifiable).
         *
         * Aynı toplama işlemi collect(Collectors.toList()) ile de yazılabilir;
         * ancak bu iki yöntemin döndürdüğü listenin değiştirilebilirlik garantisi
         * aynı değildir.
         */
        List<String> uppercaseCities = cities.stream()
                .map(String::toUpperCase)
                .toList();

        uppercaseCities.forEach(System.out::println);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        /*
         * filter() yalnızca çift sayıları Stream'de bırakır.
         * count() kalan elemanları sayan ve long döndüren terminal operation'dır.
         *
         * count(), collect() değildir. Benzer sonuç
         * collect(Collectors.counting()) ile de alınabilir; yalnızca sayma
         * yapılıyorsa doğrudan count() daha sade bir seçimdir.
         */
        long count = numbers.stream()
                .filter(num -> num % 2 == 0)
                .count();

        System.out.println("Count of even numbers: " + count);

        List<String> names = List.of("John", "Jane", "Jack", "Meddy", "Mark", "Selly");

        /*
         * collect() bir terminal operation'dır. Stream'deki elemanları
         * Collector'ın belirlediği sonuç yapısında biriktirir.
         *
         * Collectors.toCollection(TreeSet::new):
         * 1. TreeSet::new ile boş bir TreeSet oluşturur.
         * 2. J ile başlayan isimleri bu Set içine ekler.
         * 3. TreeSet tekrarları kabul etmez ve elemanları doğal sırada tutar.
         *
         * Sonuç tipi Set<String> olsa da çalışma zamanındaki nesne TreeSet'tir.
         */
        Set<String> namesWithStartJ = names.stream()
                .filter(name -> name.startsWith("J"))
                .collect(Collectors.toCollection(TreeSet::new));

        System.out.println("Names starting with 'J': " + namesWithStartJ);
    }
}
