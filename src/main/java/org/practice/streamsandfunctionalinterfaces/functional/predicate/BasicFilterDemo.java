package org.practice.streamsandfunctionalinterfaces.functional.predicate;

import java.util.List;
import java.util.function.Predicate;

public class BasicFilterDemo {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //5'ten büyük olan sayıları filtrele ve yazdır
        numbers.stream()
                .filter(number -> number > 5) // SEÇER | Predicate<Integer>
                .forEach(System.out::println); // UYGULAR | Consumer<Integer>

        //Çift sayıları seçelim
        numbers.stream()
                .filter(number -> number %2 == 0) // SEÇER | Predicate<Integer>
                .forEach(System.out::println); // UYGULAR | Consumer<Integer>

        //filter()ın yaptığı tek şey:
        //Predicate true döndürüyorsa geçir.

        //String: uzunluğu 4'ten büyük isimleri seç
        List<String> names = List.of("Ali", "Ayşe", "Mehmet", "Fatma");
        names.stream()
                .filter(name -> name.length() > 4) // SEÇER | Predicate<String>
                .forEach(System.out::println); // UYGULAR | Consumer<String>

        //A harfiyle başlayanları seç
        names.stream()
                .filter(name -> name.startsWith("A")) // SEÇER | Predicate<String>
                .forEach(System.out::println); // UYGULAR | Consumer<String>

        //Birden fazla şart verebiliriz
        //Mesela:
        //A ile başlasın VE uzunluğu 4'ten büyük olsun.
        names.stream()
                .filter(name -> name.startsWith("A") && name.length() > 4) // SEÇER | Predicate<String>
                .forEach(System.out::println); // UYGULAR | Consumer<String>

        //İki tane filter() da kullanabilirsin
        //Aynı şeyi şöyle de yazabiliriz:
        names.stream()
                .filter(name -> name.startsWith("A")) // SEÇER | Predicate<String>
                .filter(name -> name.length() > 4) // SEÇER | Predicate<String>
                .forEach(System.out::println); // UYGULAR | Consumer<String>

        //filter ile map farkını tekrar netleştirelim
        //filter sadece seçer, elemanları değiştirmez. map ise elemanları dönüştürür.
        //Örnek:
        names.stream()
                .filter(name -> name.startsWith("A")) // SEÇER | Predicate<String>
                .map(String::toUpperCase) // DÖNÜŞTÜRÜR | Function<String, String>
                .forEach(System.out::println); // UYGULAR | Consumer

        //Predicate'i ayrı değişken olarak da yazabiliriz
        //Mesela:
        Predicate<String> startsWithA = name -> name.startsWith("A");
        names.stream()
                .filter(startsWithA) // SEÇER | Predicate<String>
                .forEach(System.out::println); // UYGULAR | Consumer<String>

        Predicate<String> uzunMu =
                name -> name.length() > 4;

        names.stream()
                .filter(uzunMu)
                .forEach(System.out::println);

        /*
        Bu örnek filter() neden Predicate istiyor sorusunu çok güzel gösteriyor.
Çünkü filterın ihtiyacı şu:
"Bana her eleman için true veya false
 söyleyebilecek bir şey ver."
Predicate tam olarak bunu yapıyor:
Predicate<T>

T → boolean
         */
        List<Integer> sayilar = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Predicate<Integer> ciftMi = sayi -> sayi % 2 == 0;
        sayilar.stream()
                .filter(ciftMi)
                .forEach(System.out::println);

        List<Integer> çiftSayilar = sayilar.stream()
                .filter(ciftMi.negate()) //negate() ile tersini alabiliriz
                .toList();
        System.out.println(çiftSayilar);
        /*
        filter, her elemanı Predicate'e gönderir; true dönen elemanı geçirir, false dönen elemanı eler.

Ve kısa formül:
filter = SEÇER
Predicate = SORAR

Predicate: "Kalsın mı?"
true  → EVET, geçir
false → HAYIR, ele
         */
    }

/*
filter = SEÇ / ELE
Tek mantığı şu:
Her elemanı tek tek al
        ↓
Predicate'e sor
        ↓
    true / false
     ↙       ↘
  KALSIN    ELENSİN
Yani filter() elemanı değiştirmez. Sadece kalacak mı, elenecek mi? kararını verir.
 */

}
