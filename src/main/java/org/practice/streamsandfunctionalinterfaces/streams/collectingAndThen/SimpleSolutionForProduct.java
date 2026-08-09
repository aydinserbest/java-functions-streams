package org.practice.streamsandfunctionalinterfaces.streams.collectingAndThen;

import java.util.Comparator;
import java.util.List;
/*
 business amacı şudur:

  > Ürün kataloğundaki fiyatı en yüksek ürünü bul
  ve ekranda göstermek üzere yalnızca ürünün adını üret.
   Listede hiç ürün yoksa kullanıcıya "No products found" mesajını ver.

 */
public class SimpleSolutionForProduct {
    static void main() {
        List<Product> products = List.of(
                new Product("Laptop", 1200),
                new Product("Smartphone", 800),
                new Product("Tablet", 600),
                new Product("Monitor", 300),
                new Product("Keyboard", 100));

        String mostExpensiveProductName = products.stream()
                .max(Comparator.comparing(Product::getPrice))
                .map(Product::getName)
                .orElse("No products found");
        System.out.println(mostExpensiveProductName);
    }
}
