package org.practice.streamsandfunctionalinterfaces.comparator;

import java.util.Comparator;
import java.util.List;

public class ProductComparatorDemo {

    public static void main(String[] args) {
        List<Product> products = List.of(
                new Product("Mouse", 40),
                new Product("Laptop", 1200),
                new Product("Keyboard", 100),
                new Product("Monitor", 300)
        );

        /*
         * Product için Java kendiliğinden "doğal sıra" seçemez.
         *
         * Product hangi alana göre sıralanmalı?
         * - Ada göre mi?
         * - Fiyata göre mi?
         *
         * Business ihtiyacına göre dışarıdan Comparator<Product> tanımlarız.
         * Bir sınıf için yalnızca bir Comparator olmak zorunda değildir.
         */
        Comparator<Product> byPrice =
                Comparator.comparingInt(Product::price);

        Comparator<Product> byName =
                Comparator.comparing(Product::name);

        List<Product> productsByPrice = products.stream()
                .sorted(byPrice)
                .toList();

        List<Product> productsByName = products.stream()
                .sorted(byName)
                .toList();

        /*
         * Business örneği:
         * Katalog ekranı ürünleri ucuzdan pahalıya gösterebilir.
         * Yönetim ekranı ise ürünleri alfabetik sırada gösterebilir.
         * Aynı Product sınıfı iki farklı Comparator ile kullanılabilir.
         */
        System.out.println("By price: " + productsByPrice);
        System.out.println("By name: " + productsByName);

        /*
         * En pahalı ürünü bulmak için fiyat Comparator'ını değiştirmeden
         * Stream.max() metoduna veririz.
         */
        Product mostExpensive = products.stream()
                .max(byPrice)
                .orElseThrow();

        System.out.println("Most expensive: " + mostExpensive);
    }

    record Product(String name, int price) {
    }
}
