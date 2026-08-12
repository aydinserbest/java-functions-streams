package org.practice.fpij.stringscomparatorsfilters.comparatorinterface;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SortByAgeDescendingDuplication {

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

        printPeople("Artan yaşa göre sıralı:",
                people.stream()
                        .sorted((person1, person2) -> person1.ageDifference(person2))
                        .collect(toList()));

        printPeople("\nAzalan yaşa göre sıralı:",
                people.stream()
                        .sorted((person1, person2) -> person2.ageDifference(person1))
                        .collect(toList()));
        /*
        Azalan sıralama için parametreleri TERSTEN çağırdık:
        person2.ageDifference(person1). Bu çalışır, ama iki sorunu var:

        1) METHOD REFERENCE ARTIK KULLANILAMAZ: Person::ageDifference
           kısayolu "ilk parametre = hedef, ikinci parametre = argüman"
           kuralına dayanıyordu. Burada ilk parametre (person1) HEDEF
           değil, ARGÜMAN olarak kullanılıyor -- kalıp uymuyor, derleyici
           bu yönlendirmeyi otomatik yapamıyor; elle lambda yazmak zorunda
           kalıyoruz.

        2) DRY İHLALİ: Artan ve azalan sıralamanın karşılaştırma MANTIĞI
           aslında AYNI (yaş farkına bakmak) -- sadece yönü ters. Ama iki
           AYRI lambda yazarak bu mantığı KOPYALADIK. Yaş karşılaştırma
           kuralı değişse (örn. doğum tarihine göre kıyaslamaya geçilse),
           İKİ yeri de güncellememiz gerekecek -- unutma riski doğar.

        Çözümü SortByAgeReusingComparator'da görelim: JDK'nın Comparator'a
        eklediği reversed() default metoduyla bu tekrarı ortadan
        kaldıracağız.
         */
    }
}
