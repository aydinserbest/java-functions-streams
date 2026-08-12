package org.practice.fpij.stringscomparatorsfilters.comparatorinterface;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SortByNameAndFindMinMax {

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

        System.out.println("1) İsme göre artan alfabetik sıralama:");
        printPeople("İsme göre sıralı:",
                people.stream()
                        .sorted((person1, person2) ->
                                person1.getName().compareTo(person2.getName()))
                        .collect(toList()));
        /*
        Şimdiye kadar yaş (age) üzerinden karşılaştırdık; burada sadece
        lambda İÇİNDEKİ mantığı değiştirdik (String'in kendi compareTo'sunu
        kullanarak) -- sorted() çağrısının GENEL YAPISI aynı kaldı. Bu,
        Comparator'ın esnekliğini gösteriyor: aynı sorted() iskeleti,
        farklı karşılaştırma kurallarıyla yeniden kullanılabiliyor.
         */

        System.out.println("\n2) En genç kişiyi bulma (min):");
        people.stream()
                .min(Person::ageDifference)
                .ifPresent(youngest -> System.out.println("En genç: " + youngest));
        /*
        "En genç kişiyi bul" demek için TÜM listeyi yaşa göre sıralayıp
        ilk elemanı almaya gerek YOK -- Stream'in min() metodu bunu
        doğrudan yapar ve bir Comparator kabul eder (Person::ageDifference
        burada da geçerli, çünkü min de "hedef.ageDifference(argüman)"
        kalıbını kullanıyor).

        min(), bir Optional<Person> döner -- çünkü liste BOŞ olabilir, bu
        durumda "en genç" diye bir şey yoktur. ifPresent(...) ile SADECE
        sonuç varsa yazdırıyoruz (pickingelement paketinden tanıdık bir
        kalıp).
         */

        System.out.println("\n3) En yaşlı kişiyi bulma (max):");
        people.stream()
                .max(Person::ageDifference)
                .ifPresent(eldest -> System.out.println("En yaşlı: " + eldest));
        /*
        max() da aynı mantıkla çalışır, sadece karşılaştırmanın "en büyük"
        ucunu arar. min() ile max() arasındaki TEK fark kullanım amacı --
        ikisi de aynı Comparator'ı kullanarak listeyi baştan sona TEK
        geçişte tarar; sıralamaya gerek YOKTUR.
         */
    }
}
