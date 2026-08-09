package org.practice.streamsandfunctionalinterfaces.streams.collectingAndThen;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MaxMinAndMaxByMinByDemo {

    public static void main(String[] args) {
        List<Product> products = List.of(
                new Product("Laptop", 1200),
                new Product("Smartphone", 800),
                new Product("Tablet", 600),
                new Product("Monitor", 300),
                new Product("Keyboard", 100)
        );

        /*
         * Bütün örneklerde aynı karşılaştırma kuralını kullanıyoruz:
         *
         * Ürünleri price alanlarına göre karşılaştır.
         *
         * Comparator<Product>:
         * Product alır ve Product nesnelerinin hangi kurala göre
         * karşılaştırılacağını tanımlar.
         */
        Comparator<Product> compareByPrice =
                Comparator.comparingInt(Product::getPrice);

        /*
         * 1. Stream.max()
         *
         * Business ihtiyacı:
         * E-ticaret yönetim ekranında katalogdaki en pahalı ürünü göstermek
         * istiyoruz. Sonuçta yalnızca fiyatı değil, ürünün tamamını istiyoruz;
         * çünkü ürünün hem adına hem fiyatına ihtiyacımız var.
         *
         * max(), Stream içindeki en büyük elemanı verilen Comparator'a göre
         * bulur. Liste boş olabileceği için Optional<Product> döndürür.
         *
         * Stream<Product>
         *      ↓ max(compareByPrice)
         * Optional<Product>
         */
        Optional<Product> mostExpensiveOptional = products.stream()
                .max(compareByPrice);

        Product mostExpensive = mostExpensiveOptional.orElseThrow(
                () -> new IllegalStateException("No products found")
        );

        System.out.println(
                "Most expensive product with max(): "
                        + mostExpensive.getName()
                        + ", price: "
                        + mostExpensive.getPrice()
        );

        /*
         * 2. Collectors.maxBy()
         *
         * Business ihtiyacı:
         * Ürünleri bir Collector akışı içinde işliyoruz ve toplama işleminin
         * sonucunda fiyatı en yüksek ürünü almak istiyoruz.
         *
         * maxBy(), doğrudan Stream metodu değildir. Bir Collector üretir ve
         * bu Collector collect() metoduna verilir.
         *
         * Stream.max():
         * products.stream().max(compareByPrice)
         *
         * Collectors.maxBy():
         * products.stream().collect(Collectors.maxBy(compareByPrice))
         *
         * İki kullanım da bu örnekte Optional<Product> üretir.
         * maxBy() özellikle groupingBy() gibi başka Collector'ların içinde
         * kullanılacağı zaman faydalıdır.
         */
        Optional<Product> mostExpensiveWithMaxBy = products.stream()
                .collect(Collectors.maxBy(compareByPrice));

        Product maxByProduct = mostExpensiveWithMaxBy.orElseThrow(
                () -> new IllegalStateException("No products found")
        );

        System.out.println(
                "Most expensive product with maxBy(): "
                        + maxByProduct.getName()
                        + ", price: "
                        + maxByProduct.getPrice()
        );

        /*
         * 3. Stream.min()
         *
         * Business ihtiyacı:
         * Müşteriye katalogdaki en ekonomik ürünü göstermek istiyoruz.
         * Ürün nesnesini koruduğumuz için sonuçtan hem adı hem fiyatı
         * okuyabiliriz.
         *
         * min(), verilen Comparator'a göre en küçük elemanı bulur.
         * Liste boş olabileceği için Optional<Product> döndürür.
         */
        Optional<Product> cheapestOptional = products.stream()
                .min(compareByPrice);

        Product cheapest = cheapestOptional.orElseThrow(
                () -> new IllegalStateException("No products found")
        );

        System.out.println(
                "Cheapest product with min(): "
                        + cheapest.getName()
                        + ", price: "
                        + cheapest.getPrice()
        );

        /*
         * 4. Collectors.minBy()
         *
         * Business ihtiyacı:
         * Ürünleri Collector tabanlı bir raporlama akışında toplarken fiyatı
         * en düşük ürünü sonuç olarak almak istiyoruz.
         *
         * minBy(), en küçük elemanı seçen bir Collector üretir.
         * Asıl terminal operation collect() metodudur.
         *
         * Sonuç yine Optional<Product>'tır:
         *
         * Stream<Product>
         *      ↓ collect(Collectors.minBy(compareByPrice))
         * Optional<Product>
         */
        Optional<Product> cheapestWithMinBy = products.stream()
                .collect(Collectors.minBy(compareByPrice));

        Product minByProduct = cheapestWithMinBy.orElseThrow(
                () -> new IllegalStateException("No products found")
        );

        System.out.println(
                "Cheapest product with minBy(): "
                        + minByProduct.getName()
                        + ", price: "
                        + minByProduct.getPrice()
        );

        /*
         * Kısa karşılaştırma:
         *
         * max()   -> Stream'in doğrudan terminal operation'ıdır.
         * min()   -> Stream'in doğrudan terminal operation'ıdır.
         *
         * maxBy() -> collect() içinde kullanılan bir Collector üretir.
         * minBy() -> collect() içinde kullanılan bir Collector üretir.
         *
         * Yalnızca tek bir en büyük veya en küçük eleman aranıyorsa
         * max() ve min() genellikle daha sade ve okunaklıdır.
         *
         * groupingBy(), collectingAndThen() gibi Collector'larla birlikte
         * çalışılıyorsa maxBy() ve minBy() kullanışlıdır.
         */
    }
}
