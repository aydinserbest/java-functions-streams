package org.practice.streamsandfunctionalinterfaces.streams.groupingby;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Business ihtiyacı:
 *
 * Fiyat karşılaştırma ekranında aynı fiyata sahip ürünleri ortak bir fiyat
 * başlığı altında göstermek istiyoruz.
 *
 * Örnek hedef görünüm:
 *
 * 150 euro -> [Desk Chair, Office Chair]
 * 600 euro -> [Dining Table]
 *
 * groupingBy(), her Product için getPrice() değerini grup anahtarı yapar.
 * Aynı fiyatı döndüren ürünler aynı Map entry'sinin değer listesinde toplanır.
 */
public class GroupingByDemo {
    public static void main(String[] args) {
        List<NewProduct> products = Arrays.asList(
                new NewProduct("Laptop", 1200),
                new NewProduct("Smartphone", 800),
                new NewProduct("Tablet", 400),
                new NewProduct("Desk Chair", 150),
                new NewProduct("Office Chair", 150),
                new NewProduct("Dining Table", 600)
        );

        /*
         * map(NewProduct::getPrice), Product nesnelerini fiyat değerlerine dönüştürür ve ürün bilgisini kaybeder.
         * groupingBy(NewProduct::getPrice), fiyatı yalnızca Map anahtarı yapar ve Product nesnelerini gruplarda korur.
         * Bu nedenle map() sonucu List<Integer>, groupingBy() sonucu Map<Integer, List<NewProduct>> olur.
         * Yalnızca fiyatlar isteniyorsa map(), ürünler fiyat başlıkları altında isteniyorsa groupingBy() kullanılır.
         */

        /*
         * 1. map(NewProduct::getPrice)
         *
         * Business ihtiyacı:
         * Ürünlerin fiyatlarından oluşan düz bir liste istiyoruz.
         *
         * map(), her NewProduct nesnesini Integer fiyat değerine dönüştürür.
         * Dönüşümden sonra Stream içinde Product nesnesi kalmaz.
         *
         * Stream<NewProduct> -> Stream<Integer> -> List<Integer>
         *
         * Sonuç:
         * [1200, 800, 400, 150, 150, 600]
         *
         * Bu sonuçta 150 fiyatının hangi ürünlere ait olduğu bilgisi kaybolur.
         */
        List<Integer> prices = products.stream()
                .map(NewProduct::getPrice)
                .toList();

        System.out.println("Price list: " + prices);

        /*
         * 2. groupingBy(NewProduct::getPrice)
         *
         * Business ihtiyacı:
         * Ürünleri kaybetmeden, aynı fiyata sahip olanları ortak fiyat başlığı
         * altında göstermek istiyoruz.
         *
         * NewProduct::getPrice burada Product'ı Integer'a dönüştürüp atmaz.
         * Her Product'ın hangi Map anahtarına ait olduğunu belirler.
         * Product nesnesi ilgili fiyatın List değerinde korunur.
         *
         * Stream<NewProduct>
         *      -> Map<Integer, List<NewProduct>>
         *
         * Sonuç:
         * 150 -> [Desk Chair, Office Chair]
         */
        Map<Integer, List<NewProduct>> productsByPrice = products.stream()
                .collect(Collectors.groupingBy(
                        NewProduct::getPrice // product -> product.getPrice()
                ));

        /*
         * Map doğrudan yazdırılınca her NewProduct için toString() çağrılır.
         * Bu çıktı grupları geliştirici olarak kontrol etmek ve debug yapmak içindir.
         * Kullanıcıya her domain alanını veya teknik sınıf görünümünü göstermemeliyiz.
         * Gerçek ekran çıktısı gerekli alanlar seçilerek açıkça formatlanmalı veya DTO ile hazırlanmalıdır.
         */
        System.out.println("Products grouped by price: " + productsByPrice);

        /*
         * 3. groupingBy() + mapping()
         *
         * Business ihtiyacı:
         * Fiyat grupları korunsun, fakat her grubun içinde Product nesnesinin
         * tamamı yerine yalnızca ekranda gösterilecek ürün adları bulunsun.
         *
         * NewProduct::getPrice -> Map anahtarını belirler.
         * NewProduct::getName  -> Grubun içine konulacak değeri belirler.
         *
         * Sonuç tipi:
         * Map<Integer, List<String>>
         */
        Map<Integer, List<String>> productNamesByPrice = products.stream()
                .collect(Collectors.groupingBy(
                        NewProduct::getPrice,
                        Collectors.mapping(
                                NewProduct::getName,
                                Collectors.toList()
                        )
                ));

        productNamesByPrice.forEach((price, names) ->
                System.out.println(
                        "Price: €" + price + ", Products: " + names
                )
        );
    }
}
