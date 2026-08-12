package org.practice.fpij.stringscomparatorsfilters.comparatorinterface;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SortByAgeAscending {

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

        System.out.println("1) Lambda expression ile artan yaşa göre sıralama:");
        final List<Person> ascendingAgeLambda = people.stream()
                .sorted((person1, person2) -> person1.ageDifference(person2))
                .collect(toList());
        printPeople("Artan yaşa göre sıralı:", ascendingAgeLambda);
        /*
        NEDEN List'in kendi sort() metodunu DEĞİL stream().sorted()'ı
        kullandık?

        List.sort(Comparator) bir void metottur -- ÇAĞRILDIĞI listeyi
        YERİNDE (in place) değiştirir. Orijinal listeyi korumak isteseydik
        önce bir KOPYASINI çıkarıp sort'u kopya üzerinde çağırmamız
        gerekirdi -- fazladan bir adım. stream().sorted(...) ise YENİ,
        sıralı bir koleksiyon DÖNDÜRÜR; orijinal `people` listesine hiç
        dokunmaz.

        sorted()'ın çalışma mantığı, önceki bölümlerden tanıdığımız
        reduce()'a benziyor: reduce() listeyi TEK bir değere indirger;
        sorted() ise verilen Comparator'ın karşılaştırma sonucunu
        kullanarak elemanları SIRALAR. Comparator bir functional interface
        olduğu için buraya rahatça bir lambda expression geçirebiliyoruz.

        Comparator'ın soyut compareTo metodu iki parametre alır ve bir int
        döner: negatifse ilk parametre önce gelir, pozitifse sonra gelir,
        sıfırsa eşit demektir. Person'daki ageDifference metodu tam olarak
        bu sözleşmeye uyacak şekilde tasarlandı.
         */

        System.out.println("\n2) Method reference ile aynı sıralama:");
        final List<Person> ascendingAgeMethodRef = people.stream()
                .sorted(Person::ageDifference)
                .collect(toList());
        printPeople("Artan yaşa göre sıralı:", ascendingAgeMethodRef);
        /*
        (person1, person2) -> person1.ageDifference(person2) lambda'sında
        aslında sadece parametre YÖNLENDİRMESİ yapıyoruz: ilk parametre
        HEDEF (target), ikinci parametre ARGÜMAN. Bu, method reference'ların
        instance-metodu kalıbıyla BİREBİR örtüşüyor -- derleyici bu
        yönlendirmeyi bizim yerimize yapabilir: Person::ageDifference.

        DİKKAT: Bu kısayol sadece parametre sırası "ilk parametre = hedef,
        kalanlar = argüman" kuralına uyduğunda çalışır. Azalan (descending)
        sıralamada bu kural BOZULUYOR -- bunu SortByAgeDescendingDuplication
        class'ında göreceğiz.
         */
    }
}
