package org.practice.fpij.usingcollections.streamcontinuebreak.exercises.helperClasses;

import java.util.List;
import java.util.stream.IntStream;

public class MapVsMapToObj {
    public static void main(String[] args) {
        /*
        Bu class, Q01Pagination'da kullandığımız map() / mapToObj() ayrımını
        sorudan bağımsız, sade örneklerle gösteriyor. Amaç: "hangisini ne
        zaman kullanırım" sorusuna kafanda net bir cevap oluşturmak.

        Kısa kural:
        - map()       : akıştaki tipi AYNI TÜRDE tutarak dönüştürür
                         (int -> int, ya da Object -> Object)
        - mapToObj()   : SAYISAL bir akıştaki (IntStream/LongStream/DoubleStream)
                         primitive değerleri NESNEYE çevirir
                         (int -> Object), akışın türünü değiştirir
         */

        // ------------------------------------------------------------
        // 1) IntStream.map(): int -> int (aynı tür kalır, hâlâ IntStream)
        // ------------------------------------------------------------
        System.out.println("1) IntStream.map() -> int'ten int'e:");
        IntStream.range(1, 6) // 1,2,3,4,5
                .map(n -> n * 2) // her sayıyı 2 ile çarp, sonuç yine int
                .forEach(System.out::println);
        /*
        Girdi: 1,2,3,4,5 (int)
        Çıktı: 2,4,6,8,10 (int)
        map()'in lambda'sı bir int alıp bir int döndürüyor. Akış IntStream
        olarak kalmaya devam ediyor, hiçbir nesne (Object) üretilmedi.
         */

        // ------------------------------------------------------------
        // 2) Stream<T>.map(): Object -> Object (aynı tür kalır, hâlâ Stream)
        // ------------------------------------------------------------
        System.out.println("\n2) Stream<String>.map() -> String'ten String'e:");
        List<String> words = List.of("elma", "armut", "muz");
        words.stream()
                .map(String::toUpperCase) // her String'i büyük harfe çevir, sonuç yine String
                .forEach(System.out::println);
        /*
        Girdi: String nesneleri
        Çıktı: yine String nesneleri
        Burada da tür değişmiyor (Object -> Object), sadece içerik dönüşüyor.
        Bu yüzden buna map() yeterli; mapToObj() gerekmiyor çünkü zaten
        elimizde primitive değil, nesne var.
         */

        // ------------------------------------------------------------
        // 3) IntStream.mapToObj(): int -> Object (tür DEĞİŞİR, IntStream'den
        //    Stream<T>'e geçilir) -- Q01'de kullandığımız asıl kısım burası
        // ------------------------------------------------------------
        System.out.println("\n3) IntStream.mapToObj() -> int'ten String'e:");
        IntStream.range(1, 6)
                .mapToObj(n -> "Ürün-" + n) // her int'i bir String'e (nesneye) çevir
                .forEach(System.out::println);
        /*
        Girdi: 1,2,3,4,5 (int, primitive)
        Çıktı: "Ürün-1","Ürün-2",... (String, nesne)
        Burada map() KULLANAMAZDIK: IntStream.map()'in imzası int -> int
        bekler, "Ürün-" + n gibi bir String döndüremezsin (derleme hatası
        alırsın). Tür DEĞİŞTİĞİ (int'ten Object'e) için mapToObj() gerekir.
        Sonuç artık IntStream değil, Stream<String>.
         */

        // ------------------------------------------------------------
        // 4) mapToObj() ile int'i KENDİ nesnemize çevirmek
        //    (Q01Pagination'daki new Product(...) ile birebir aynı mantık)
        // ------------------------------------------------------------
        System.out.println("\n4) IntStream.mapToObj() -> int'ten kendi nesnemize (Point):");
        record Point(int index, int square) {}

        IntStream.range(1, 5) // 1,2,3,4
                .mapToObj(n -> new Point(n, n * n)) // her sayıdan bir Point nesnesi üret
                .forEach(p -> System.out.println(p.index() + " -> " + p.square()));
        /*
        Girdi: 1,2,3,4 (int)
        Çıktı: Point(1,1), Point(2,4), Point(3,9), Point(4,16) (nesne)
        Q01Pagination'daki
            .mapToObj(i -> new Product(rastgeleİsim, rastgeleTarih))
        satırı da tam olarak bunu yapıyor: elindeki "sayaç" (int i) değerini
        kullanarak (ya da hiç kullanmayarak) yeni bir nesne üretiyor ve
        akışı IntStream'den Stream<Product>'a çeviriyor.
         */

        /*
        Özet:

        IntStream/LongStream/DoubleStream üzerinde:
          .map(int -> int)        -> hâlâ IntStream/LongStream/DoubleStream
          .mapToObj(int -> T)     -> artık Stream<T>  (PRIMITIVE'DEN NESNEYE geçiş)

        Stream<T> üzerinde:
          .map(T -> R)            -> hâlâ Stream<R>   (NESNEDEN NESNEYE dönüşüm)

        Kısacası: elindeki değer zaten bir nesne ise map() yeter. Elindeki
        değer primitive (int/long/double) ve onu bir nesneye çevirmen
        gerekiyorsa mapToObj() kullanırsın.
         */
    }
}
