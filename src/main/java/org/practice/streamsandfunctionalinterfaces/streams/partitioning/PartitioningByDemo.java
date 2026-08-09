package org.practice.streamsandfunctionalinterfaces.streams.partitioning;

import org.practice.streamsandfunctionalinterfaces.streams.groupingby.NewProduct;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/*
 * Business ihtiyacı:
 *
 * Katalog yönetim ekranında ürünleri fiyatı 1000 euroyu aşan premium ürünler
 * ve diğer standart ürünler olarak iki ayrı bölüme ayırmak istiyoruz.
 *
 * Örnek hedef görünüm:
 *
 * Premium ürünler  -> Laptop
 * Standart ürünler -> Smartphone, Tablet, Desk Chair, Office Chair, Dining Table
 *
 * partitioningBy(), her Product için verilen Predicate'i çalıştırır.
 * Koşulu sağlayanları true, sağlamayanları false bölümünde toplar.
 */
public class PartitioningByDemo {

    /*
     * Business kuralındaki sınırı kodun farklı yerlerinde tekrar yazmamak için
     * isimlendirilmiş sabit olarak tutuyoruz.
     */
    private static final int PREMIUM_PRICE_THRESHOLD = 1000;

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
         * isPremiumProduct bir Predicate<NewProduct>'tır.
         * true anahtarında premium, false anahtarında standart ürünler bulunur.
         * partitioningBy() iki sonucu tek traversal içinde birlikte üretir.
         * Sonuç tipi Map<Boolean, List<NewProduct>> olur.
         */
        Predicate<NewProduct> isPremiumProduct =
                product -> product.getPrice() > PREMIUM_PRICE_THRESHOLD;

        Map<Boolean, List<NewProduct>> productsByPremiumStatus = products.stream()
                .collect(Collectors.partitioningBy(isPremiumProduct));

        /*
         * Map'i doğrudan yazdırmak teknik true/false anahtarlarını ve
         * NewProduct.toString() görünümünü gösterir. Kullanıcı ekranında boolean
         * anahtarları business başlıklarına çevirip gerekli alanları açıkça
         * formatlamak daha anlaşılırdır.
         */
        List<NewProduct> premiumProducts = productsByPremiumStatus.get(true);
        List<NewProduct> standardProducts = productsByPremiumStatus.get(false);

        System.out.println(
                "Premium products (> €" + PREMIUM_PRICE_THRESHOLD + "):"
        );
        premiumProducts.forEach(product ->
                System.out.println(
                        "  - " + product.getName() + " | €" + product.getPrice()
                )
        );

        System.out.println(
                "Standard products (≤ €" + PREMIUM_PRICE_THRESHOLD + "):"
        );
        standardProducts.forEach(product ->
                System.out.println(
                        "  - " + product.getName() + " | €" + product.getPrice()
                )
        );
    }
}
