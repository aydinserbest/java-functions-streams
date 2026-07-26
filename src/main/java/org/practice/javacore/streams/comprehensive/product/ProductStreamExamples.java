package org.practice.javacore.streams.comprehensive.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductStreamExamples {

    public static void main(String[] args) {
        List<Product> products = createProducts();

        filterSellableProducts(products);
        mapProductsToCatalogLabels(products);
        flatMapProductTags(products);
        sortProductsForCatalog(products);
        createSecondCatalogPage(products);
        countStockProblems(products);
        calculateInventoryValues(products);
        collectProductsIntoReports(products);
        createCampaignReport(products);
        findCheapestAndMostExpensiveProducts(products);
    }

    private static List<Product> createProducts() {
        return List.of(
                new Product(101, "Laptop", "Electronics", 1200, 5, true,
                        List.of("computer", "portable", "premium")),
                new Product(102, "Mouse", "Accessories", 40, 25, true,
                        List.of("computer", "wireless")),
                new Product(103, "Monitor", "Electronics", 300, 0, true,
                        List.of("display", "computer")),
                new Product(104, "Keyboard", "Accessories", 100, 12, true,
                        List.of("computer", "wireless")),
                new Product(105, "Desk", "Furniture", 450, 3, true,
                        List.of("office", "wood")),
                new Product(106, "Chair", "Furniture", 250, 7, false,
                        List.of("office", "ergonomic")),
                new Product(107, "Webcam", "Accessories", 90, 18, true,
                        List.of("video", "computer")),
                new Product(108, "Smartphone", "Electronics", 800, 4, true,
                        List.of("mobile", "portable", "premium"))
        );
    }

    private static void filterSellableProducts(List<Product> products) {
        /*
         * Business ihtiyacı:
         * Müşteri kataloğunda yalnızca aktif ve stokta bulunan ürünler görünür.
         *
         * Predicate<Product>, tek ürün için satışa uygunluk kararını taşır.
         * filter() intermediate, toList() terminal operation'dır.
         */
        Predicate<Product> isSellable =
                product -> product.isActive() && product.getStock() > 0;

        List<Product> sellableProducts = products.stream()
                .filter(isSellable)
                .toList();

        System.out.println("\n1. Sellable products:");

        /*
         * forEach(), her sonuç için Consumer<Product> çalıştıran terminal
         * operation'dır.
         */
        sellableProducts.forEach(System.out::println);
    }

    private static void mapProductsToCatalogLabels(List<Product> products) {
        /*
         * Business ihtiyacı:
         * Katalog kartı tam Product nesnesi yerine okunabilir bir etiket ister.
         *
         * map() dönüşümü:
         * Product -> String
         */
        List<String> catalogLabels = products.stream()
                .filter(Product::isActive)
                .map(product -> "%s - €%.2f"
                        .formatted(product.getName(), product.getPrice()))
                .toList();

        System.out.println("\n2. Catalog labels: " + catalogLabels);
    }

    private static void flatMapProductTags(List<Product> products) {
        /*
         * Business ihtiyacı:
         * Arama servisi bütün ürün kartlarındaki etiketlerden benzersiz bir
         * arama sözlüğü oluşturmak istiyor.
         *
         * Her Product bir List<String> taşır.
         * flatMap(), bu iç listeleri tek Stream<String> içinde birleştirir.
         */
        Set<String> searchTags = products.stream()
                .flatMap(product -> product.getTags().stream())
                .map(tag -> tag.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        System.out.println("\n3. Search tags: " + searchTags);
    }

    private static void sortProductsForCatalog(List<Product> products) {
        /*
         * Business ihtiyacı:
         * Yönetim kataloğunda ürünler önce kategoriye, aynı kategoride fiyatı
         * yüksekten düşüğe, fiyat da aynıysa isme göre gösterilir.
         */
        Comparator<Product> catalogOrder =
                Comparator.comparing(Product::getCategory)
                        .thenComparing(
                                Comparator.comparingDouble(Product::getPrice)
                                        .reversed()
                        )
                        .thenComparing(Product::getName);

        List<Product> sortedProducts = products.stream()
                .sorted(catalogOrder)
                .toList();

        System.out.println("\n4. Products in catalog order:");
        sortedProducts.forEach(System.out::println);
    }

    private static void createSecondCatalogPage(List<Product> products) {
        /*
         * Business ihtiyacı:
         * Aktif ürün kataloğunda sayfa boyutu 3'tür. İkinci sayfa için önce
         * aktif ürünler seçilir ve isim sırası sabitlenir; ardından ilk üç ürün
         * atlanıp sonraki üç ürün alınır.
         *
         * filter -> sorted -> skip -> limit sırası business sonucunu belirler.
         */
        List<Product> secondPage = products.stream()
                .filter(Product::isActive)
                .sorted(Comparator.comparing(Product::getName))
                .skip(3)
                .limit(3)
                .toList();

        System.out.println("\n5. Second catalog page: " + secondPage);
    }

    private static void countStockProblems(List<Product> products) {
        /*
         * Business ihtiyacı:
         * Satın alma paneli aktif olduğu halde stoku tükenen ürün çeşidi
         * sayısını gösterecek.
         *
         * count() pipeline'da kalan eleman sayısını long olarak döndürür.
         */
        long outOfStockCount = products.stream()
                .filter(product -> product.isActive()
                        && product.getStock() == 0)
                .count();

        System.out.println("\n6. Active out-of-stock count: " + outOfStockCount);
    }

    private static void calculateInventoryValues(List<Product> products) {
        /*
         * Business ihtiyacı:
         * Finans raporunda her ürünün stok değeri:
         *
         * price * stock
         *
         * olarak hesaplanır ve bütün aktif ürünler için toplanır.
         */
        double totalInventoryValue = products.stream()
                .filter(Product::isActive)
                .mapToDouble(product ->
                        product.getPrice() * product.getStock())
                .sum();

        /*
         * Aynı toplamı reduce() ile de hesaplayebiliriz.
         * map(), Product'ı stok değerine dönüştürür;
         * reduce(), bütün değerleri tek toplamda birleştirir.
         */
        double totalWithReduce = products.stream()
                .filter(Product::isActive)
                .map(product -> product.getPrice() * product.getStock())
                .reduce(0.0, Double::sum);

        /*
         * Fiyat özeti count, min, max, sum ve average değerlerini tek geçişte
         * üretir.
         */
        DoubleSummaryStatistics priceStatistics = products.stream()
                .filter(Product::isActive)
                .mapToDouble(Product::getPrice)
                .summaryStatistics();

        System.out.println("\n7. Total inventory value: €"
                + totalInventoryValue);
        System.out.println("Total with reduce: €" + totalWithReduce);
        System.out.println("Average active product price: €"
                + priceStatistics.getAverage());
    }

    private static void collectProductsIntoReports(List<Product> products) {
        /*
         * Business ihtiyacı 1:
         * Katalog yönetiminde ürünler kategori başlıkları altında gösterilir.
         */
        Map<String, List<Product>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));

        /*
         * Business ihtiyacı 2:
         * Dashboard kategori başına satılabilir ürün sayısını gösterir.
         */
        Map<String, Long> sellableCountByCategory = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.filtering(
                                product -> product.isActive()
                                        && product.getStock() > 0,
                                Collectors.counting()
                        )
                ));

        /*
         * Business ihtiyacı 3:
         * Sipariş servisi ürünleri ID üzerinden hızlı bulmak için lookup Map'i
         * ister.
         *
         * Function.identity(), Product'ın kendisini Map değeri yapar.
         */
        Map<Long, Product> productById = products.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        Function.identity()
                ));

        /*
         * Business ihtiyacı 4:
         * Kampanya hazırlığında aktif ve pasif ürünler iki gruba ayrılır.
         */
        Map<Boolean, List<Product>> productsByActiveStatus = products.stream()
                .collect(Collectors.partitioningBy(Product::isActive));

        System.out.println("\n8. Products by category: " + productsByCategory);
        System.out.println("Sellable count by category: "
                + sellableCountByCategory);
        System.out.println("Product with ID 101: " + productById.get(101L));
        System.out.println("Products by active status: "
                + productsByActiveStatus);
    }

    private static void createCampaignReport(List<Product> products) {
        /*
         * Business ihtiyacı:
         * Premium kampanya raporunda fiyatı 500 euroyu aşan aktif ürün adları
         * tek bir cümlede gösterilir.
         *
         * joining() isimleri birleştirir.
         * collectingAndThen() oluşan metne business başlığını ekler.
         *
         * toCollection(ArrayList::new) örneği de değiştirilebilir bir kampanya
         * çalışma listesi üretir.
         */
        String premiumReport = products.stream()
                .filter(product -> product.isActive()
                        && product.getPrice() > 500)
                .map(Product::getName)
                .collect(Collectors.collectingAndThen(
                        Collectors.joining(", "),
                        names -> "Premium campaign products: " + names
                ));

        List<Product> mutableCampaignList = products.stream()
                .filter(product -> product.isActive()
                        && product.getStock() > 0)
                .limit(3)
                .collect(Collectors.toCollection(ArrayList::new));

        System.out.println("\n9. " + premiumReport);
        System.out.println("Mutable campaign list: " + mutableCampaignList);
    }

    private static void findCheapestAndMostExpensiveProducts(
            List<Product> products
    ) {
        Comparator<Product> byPrice =
                Comparator.comparingDouble(Product::getPrice);

        /*
         * Business ihtiyacı:
         * Katalog özeti satılabilir ürünler içindeki en ucuz ve en pahalı ürünü
         * gösterir. Aynı fiyat Comparator'ı iki hesapta tekrar kullanılır.
         */
        Optional<Product> cheapest = products.stream()
                .filter(product -> product.isActive()
                        && product.getStock() > 0)
                .min(byPrice);

        Optional<Product> mostExpensive = products.stream()
                .filter(product -> product.isActive()
                        && product.getStock() > 0)
                .max(byPrice);

        /*
         * minBy(), Collector tabanlı aynı minimum seçimini yapar.
         */
        Optional<Product> cheapestWithMinBy = products.stream()
                .filter(product -> product.isActive()
                        && product.getStock() > 0)
                .collect(Collectors.minBy(byPrice));

        System.out.println("\n10. Cheapest sellable product: "
                + cheapest.map(Product::getName).orElse("None"));
        System.out.println("Most expensive sellable product: "
                + mostExpensive.map(Product::getName).orElse("None"));
        System.out.println("Cheapest with minBy: "
                + cheapestWithMinBy.map(Product::getName).orElse("None"));
    }
}
