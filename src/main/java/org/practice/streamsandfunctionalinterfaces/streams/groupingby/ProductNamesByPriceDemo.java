package org.practice.streamsandfunctionalinterfaces.streams.groupingby;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductNamesByPriceDemo {

    public static void main(String[] args) {
        List<NewProduct> products = List.of(
                new NewProduct("Laptop", 1200),
                new NewProduct("Desk Chair", 150),
                new NewProduct("Office Chair", 150),
                new NewProduct("Dining Table", 600)
        );

        /*
         * Business ihtiyacı:
         * Raporun sonraki aşamasında Product nesnesinin tamamına ihtiyacımız
         * yoktur; yalnızca aynı fiyattaki ürün adları gösterilecektir.
         *
         * groupingBy(NewProduct::getPrice):
         * Her ürünün hangi fiyat grubuna gireceğini belirler.
         *
         * mapping(NewProduct::getName, toList()):
         * Grubun içine NewProduct yerine yalnızca String ürün adını koyar.
         *
         * Tip farkı:
         * Map<Integer, List<NewProduct>> yerine
         * Map<Integer, List<String>> oluşur.
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
