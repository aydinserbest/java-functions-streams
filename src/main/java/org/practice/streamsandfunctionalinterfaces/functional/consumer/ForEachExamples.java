package org.practice.streamsandfunctionalinterfaces.functional.consumer;

import java.util.List;

public class ForEachExamples {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");

        names.forEach(System.out::println);
        /*
        forEach() sadece Stream'e ait bir metot değil. List de doğrudan forEach() metoduna sahip.
        names bir List.
List zaten forEach() kullanabiliyor
Şunu yazdığında:
names.forEach(System.out::println);
kabaca şunu söylüyorsun:
names listesindeki her elemanı al ve println'e gönder.
         */
        /*
        Bunun lambda hali
Method reference'ı açarsak:
names.forEach(System.out::println);
şunun kısa hali:
names.forEach(name -> System.out.println(name));
Bunu da eski for mantığına yaklaştırırsak:
for (String name : names) {
    System.out.println(name);
}
Üçü de burada aynı temel işi yapıyor:
for (String name : names) {
    System.out.println(name);
}
names.forEach(name -> System.out.println(name));
names.forEach(System.out::println);
Sonuncusu en kısa.
         */
        /*
        Peki neden sürekli .stream() kullanıyoruz?
Çünkü stream() bize Stream işlemlerini kullanma imkânı veriyor.
Mesela sadece yazdıracaksam:
         */
        names.forEach(System.out::println); // yeterli
        /*
        Ama şöyle bir şey yapmak istiyorsam:
Uzunluğu 4'ten büyük isimleri seç → büyük harfe çevir → yazdır.

Stream çok uygun:
         */
        names.stream()                          // stream()  → Listeyi Stream'e çevirir
                .filter(name -> name.length() > 4) // filter()  → SEÇER      | Predicate<T> gerekir
                .map(String::toUpperCase)           // map()     → DÖNÜŞTÜRÜR | Function<T,R> gerekir
                .forEach(System.out::println);      // forEach() → UYGULAR    | Consumer<T> gerekir

        /*
        Senin örnekte özellikle:
.filter(name -> name.length() > 4)
Predicate<String>:
String → boolean

"Alice" → true
"Bob"   → false
.map(String::toUpperCase)
Function<String, String>:
String → String

"Alice" → "ALICE"
.forEach(System.out::println)
Consumer<String>:
String → void

"ALICE" → ekrana yazdır → geriye değer döndürmez
En kullanışlı ezberin şu olsun:
Predicate → SORAR       → true/false
Function  → DÖNÜŞTÜRÜR → başka değer
Consumer  → TÜKETİR    → sonuç döndürmez
Yani kodu gördüğünde:
stream → BAŞLAT
filter → SEÇ
map → DÖNÜŞTÜR
forEach → UYGULA
         */
    }
}
