package org.practice.fpij.usingcollections.lexicalscopingandclosures;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateVsFunction {
    public static void main(String[] args) {
        /*
        Bu bölümde hem Predicate<T> hem de Function<T, R> kullandık.
        Aralarındaki farkı netleştirelim.
         */
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        // ------------------------------------------------------------
        // Predicate<T>: T al, boolean döndür -- bir EVET/HAYIR kararı
        // ------------------------------------------------------------
        final Predicate<String> isLongName = name -> name.length() > 4;
        System.out.println("Predicate<String> -> test(name): tek bir isim uzun mu?");
        System.out.println("'Brian' uzun mu? " + isLongName.test("Brian"));
        System.out.println("'Ken' uzun mu?   " + isLongName.test("Ken"));
        /*
        Predicate<T>'nin soyut metodu test(T) -> boolean'dır. "Bu elemanı
        kabul et mi, etme mi?" tarzı bir karar temsil eder. filter() gibi,
        elemanları elemek/seçmek için karar veren metotlar Predicate alır.
         */

        // ------------------------------------------------------------
        // Function<T, R>: T al, R döndür -- bir DÖNÜŞÜM
        // ------------------------------------------------------------
        final Function<String, Integer> nameLength = String::length;
        System.out.println("\nFunction<String, Integer> -> apply(name): ismin uzunluğu");
        System.out.println("'Brian' uzunluğu: " + nameLength.apply("Brian"));
        /*
        Function<T, R>'nin soyut metodu apply(T) -> R'dir. Predicate'ten
        daha GENELDİR: her zaman boolean değil, İSTEDİĞİN TÜRDE bir sonuç
        döndürebilir (burada String'i Integer'a çeviriyoruz). map() gibi,
        bir değeri BAŞKA bir değere dönüştüren metotlar Function alır.

        Aslında Predicate<T>, "R'si her zaman Boolean olan özel bir
        Function<T, R>" gibi düşünülebilir (Java'da doğrudan böyle miras
        almasa da, kavramsal olarak ilişki bu).
         */

        // ------------------------------------------------------------
        // Function<String, Predicate<String>>: bir Predicate ÜRETEN Function
        // ------------------------------------------------------------
        final Function<String, Predicate<String>> startsWithLetter =
                letter -> name -> name.startsWith(letter);

        System.out.println("\nFunction<String, Predicate<String>> -> apply(letter) bir Predicate döndürür");
        final Predicate<String> startsWithN = startsWithLetter.apply("N");
        System.out.println("startsWithLetter.apply(\"N\") bize bir Predicate<String> verdi.");
        System.out.println("'Nate' N ile başlıyor mu? " + startsWithN.test("Nate"));

        System.out.println("\nDoğrudan filter() içinde kullanımı (CurriedFunctionRefactor'daki gibi):");
        long countStartN = friends.stream().filter(startsWithLetter.apply("N")).count();
        System.out.println("N ile başlayan sayısı: " + countStartN);
        /*
        Burada iki kavram iç içe: startsWithLetter bir Function (apply ile
        çağrılıyor) ve DÖNDÜRDÜĞÜ ŞEY bir Predicate (test ile çağrılıyor,
        ya da doğrudan filter()'a veriliyor). "Bir fonksiyonun başka bir
        fonksiyon üretmesi" fikri, CurriedFunctionRefactor'da gördüğümüz
        curry (körleme) yaklaşımının özünü oluşturuyor.
         */
    }
}
