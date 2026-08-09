package org.practice.streamsandfunctions.streams.groupingby;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExplicitFormattingGroupingDemo {

    public static void main(String[] args) {
        List<NewProduct> products = List.of(
                new NewProduct("Laptop", 1200),
                new NewProduct("Desk Chair", 150),
                new NewProduct("Office Chair", 150),
                new NewProduct("Dining Table", 600)
        );

        Map<Integer, List<NewProduct>> productsByPrice = products.stream()
                .collect(Collectors.groupingBy(NewProduct::getPrice));

        /*
         * Business ihtiyacı:
         * Kullanıcı ekranında sınıf adı, süslü parantez veya alan adı gibi
         * teknik detaylar görünmemelidir.
         *
         * Domain nesnelerini Map içinde koruyoruz; çünkü daha sonra Product'ın
         * başka alanlarına ihtiyaç duyabiliriz. Fakat ekrana yazarken hangi
         * alanların nasıl gösterileceğini açıkça belirliyoruz.
         *
         * Bu çıktı NewProduct.toString() metoduna bağlı değildir.
         */
        productsByPrice.forEach((price, productsAtSamePrice) -> {
            List<String> productNames = productsAtSamePrice.stream()
                    .map(NewProduct::getName)
                    .toList();

            System.out.println(
                    "€" + price + " fiyatındaki ürünler: " + productNames
            );
        });
    }
}
