package org.practice.javacore.streams.groupingby;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductSummaryGroupingDemo {

    public static void main(String[] args) {
        List<NewProduct> products = List.of(
                new NewProduct("Laptop", 1200),
                new NewProduct("Desk Chair", 150),
                new NewProduct("Office Chair", 150),
                new NewProduct("Dining Table", 600)
        );

        /*
         * Business ihtiyacı:
         * Domain nesnesini doğrudan kullanıcı arayüzüne göndermek istemiyoruz.
         * Fiyat grupları korunacak, fakat her grubun içinde ekrana özel
         * ProductSummary DTO'ları bulunacaktır.
         *
         * NewProduct -> ProductSummary dönüşümünü mapping() yapar.
         *
         * Sonuç tipi:
         * Map<Integer, List<ProductSummary>>
         */
        Map<Integer, List<ProductSummary>> summariesByPrice = products.stream()
                .collect(Collectors.groupingBy(
                        NewProduct::getPrice,
                        Collectors.mapping(
                                product -> new ProductSummary(
                                        product.getName(),
                                        "€" + product.getPrice()
                                ),
                                Collectors.toList()
                        )
                ));

        /*
         * Kullanıcı ekranı yalnızca DTO'nun bilinçli olarak hazırlanmış
         * alanlarını okur. NewProduct.toString() değişse bile bu ekran formatı
         * etkilenmez.
         */
        summariesByPrice.forEach((price, summaries) -> {
            System.out.println("Fiyat grubu: €" + price);

            summaries.forEach(summary ->
                    System.out.println(
                            "  - "
                                    + summary.displayName()
                                    + " | "
                                    + summary.formattedPrice()
                    )
            );
        });
    }
}
