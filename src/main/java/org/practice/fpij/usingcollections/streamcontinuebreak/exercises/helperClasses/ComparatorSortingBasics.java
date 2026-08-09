package org.practice.fpij.usingcollections.streamcontinuebreak.exercises.helperClasses;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class ComparatorSortingBasics {
    public static void main(String[] args) {
        /*
        Bu class, Q01Pagination'daki

            .sorted(Comparator.comparing(Product::addDate))

            satırını sorudan bağımsız, sade örneklerle açıklıyor.
         */

        record Product(String name, LocalDate addDate, double price) {}

        List<Product> products = List.of(
                new Product("Laptop", LocalDate.of(2026, 3, 10), 25000),
                new Product("TV", LocalDate.of(2026, 1, 5), 15000),
                new Product("USB", LocalDate.of(2026, 2, 20), 150),
                new Product("Phone", LocalDate.of(2026, 1, 5), 30000)
        );

        // ------------------------------------------------------------
        // 1) sorted() PARAMETRESİZ neden ÇALIŞMAZ?
        // ------------------------------------------------------------
        System.out.println("1) sorted() parametresiz neden derlenmez:");
        /*
        products.stream().sorted().forEach(...);  // DERLEME HATASI

        Parametresiz sorted(), akıştaki elemanların "doğal sıralamasını"
        (natural ordering) kullanır. Bunun çalışması için eleman tipinin
        Comparable arayüzünü implemente etmesi gerekir (String, Integer,
        LocalDate gibi hazır tipler zaten Comparable'dır). Product bir
        record; alanları (name, addDate, price) Comparable olsa bile
        Product'ın KENDİSİ Comparable DEĞİL — derleyici "hangi alana göre
        sıralayacağımı bilmiyorum" der ve hata verir. Bu yüzden ona
        SÖYLEMEMİZ gerekir: "hangi alana göre sırala?" İşte bunu
        Comparator.comparing(...) ile yapıyoruz.
         */

        // ------------------------------------------------------------
        // 2) Comparator.comparing(keyExtractor): tek alana göre sıralama
        // ------------------------------------------------------------
        System.out.println("\n2) Comparator.comparing(Product::addDate):");
        products.stream()
                .sorted(Comparator.comparing(Product::addDate))
                .forEach(p -> System.out.println(p.name() + " - " + p.addDate()));
        /*
        Comparator.comparing(...) bir "key extractor" (anahtar çıkarıcı)
        fonksiyon alır: elemanı (Product) verirsin, o da sıralamada
        kullanılacak KARŞILAŞTIRILABİLİR bir değer (LocalDate) döndürür.

        Product::addDate bir METOD REFERANSI'dır ve şu lambda'nın kısa
        yazımıdır:
            product -> product.addDate()

        Yani "her Product'tan addDate alanını çıkar, KARŞILAŞTIRMAYI SEN
        (Comparator) o tarihler üzerinden yap" demiş oluyoruz. LocalDate
        zaten Comparable olduğu için (hangi tarihin diğerinden önce/sonra
        geldiğini bilir), Comparator.comparing bu bilgiyi kullanarak
        Product'ları sıralayabiliyor. Varsayılan yön KÜÇÜKTEN BÜYÜĞE'dir
        (LocalDate için: eskiden yeniye).
         */

        // ------------------------------------------------------------
        // 3) .reversed(): sıralama yönünü tersine çevirme
        // ------------------------------------------------------------
        System.out.println("\n3) .reversed() -> yeniden eskiye:");
        products.stream()
                .sorted(Comparator.comparing(Product::addDate).reversed())
                .forEach(p -> System.out.println(p.name() + " - " + p.addDate()));
        /*
        Comparator.comparing(...) bir Comparator NESNESİ döndürdüğü için
        üzerine .reversed() gibi başka metotlar zincirleyebiliriz.
         */

        // ------------------------------------------------------------
        // 4) thenComparing(): eşitlik durumunda İKİNCİL sıralama alanı
        // ------------------------------------------------------------
        System.out.println("\n4) Önce tarihe, tarih eşitse fiyata göre sırala:");
        products.stream()
                .sorted(Comparator.comparing(Product::addDate)
                        .thenComparing(Product::price))
                .forEach(p -> System.out.println(p.name() + " - " + p.addDate() + " - " + p.price()));
        /*
        Yukarıdaki listede "TV" ve "Phone" AYNI tarihte (2026-01-05).
        Sadece Comparator.comparing(Product::addDate) kullansaydık, bu
        ikisinin arasındaki sıra BELİRSİZ kalırdı (hangisi önce geleceği
        garanti edilmez). thenComparing(...) ile "tarihler eşitse bu
        SEFER de fiyata bak" demiş oluyoruz; böylece sıralama tam olarak
        belirlenmiş (deterministic) hâle geliyor.
         */

        // ------------------------------------------------------------
        // 5) Sayısal alanlar için comparingInt / comparingDouble
        // ------------------------------------------------------------
        System.out.println("\n5) Fiyata göre sırala (comparingDouble):");
        products.stream()
                .sorted(Comparator.comparingDouble(Product::price))
                .forEach(p -> System.out.println(p.name() + " - " + p.price()));
        /*
        price bir "double" (primitive). Comparator.comparing(Product::price)
        de teknik olarak ÇALIŞIR ama her double'ı gizlice Double nesnesine
        çevirir (autoboxing) — bu, gereksiz nesne oluşturduğu için biraz
        verimsizdir. int/long/double alanlarda özel olarak
        comparingInt/comparingLong/comparingDouble kullanmak hem daha
        performanslı hem de niyeti daha net anlatır.
         */

        /*
        Özet:
        - sorted()                              -> sadece Comparable tipler için (String, Integer, LocalDate...)
        - sorted(Comparator.comparing(X::alan))  -> herhangi bir nesneyi, belirttiğin BİR ALANA göre sıralar
        - .reversed()                            -> yönü tersine çevirir
        - .thenComparing(...)                    -> eşitlik durumunda ikincil kriter ekler
        - comparingInt/Long/Double(...)          -> primitive alanlarda autoboxing'den kaçınmak için tercih edilir
         */
    }
}
