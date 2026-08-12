package org.practice.fpij.stringscomparatorsfilters.comparatorinterface;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SortByAgeReusingComparator {

    private static void printPeople(final String message, final List<Person> people) {
        System.out.println(message);
        people.forEach(System.out::println);
    }

    public static void main(String[] args) {
        final List<Person> people = Arrays.asList(
                new Person("John", 20),
                new Person("Sara", 21),
                new Person("Jane", 21),
                new Person("Greg", 35));

        final Comparator<Person> compareAscending =
                (person1, person2) -> person1.ageDifference(person2);
        final Comparator<Person> compareDescending = compareAscending.reversed();
        /*
        reversed(), Comparator arayüzüne JDK tarafından eklenmiş bir
        DEFAULT metottur (Java 8+). Var olan bir Comparator'ı alır ve
        karşılaştırma parametrelerinin sırasını İÇERİDE TERSİNE ÇEVİREN
        YENİ bir Comparator DÖNDÜRÜR -- orijinal compareAscending'i
        DEĞİŞTİRMEZ (yan etkisiz). Bu yüzden reversed() bir HIGHER-ORDER
        metot sayılır: bir fonksiyonel ifadeden başka bir fonksiyonel
        ifade üretir.

        Artık karşılaştırma mantığını (yaş farkı) sadece BİR yerde
        yazdık; azalan sıralama bunun üzerine "tersini al" diyerek elde
        ediliyor -- SortByAgeDescendingDuplication'daki DRY ihlali ortadan
        kalktı.
         */

        printPeople("Artan yaşa göre sıralı:",
                people.stream().sorted(compareAscending).collect(toList()));
        printPeople("\nAzalan yaşa göre sıralı:",
                people.stream().sorted(compareDescending).collect(toList()));
    }
}
