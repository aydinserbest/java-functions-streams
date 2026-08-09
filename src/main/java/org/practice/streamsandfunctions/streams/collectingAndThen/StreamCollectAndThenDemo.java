package org.practice.streamsandfunctions.streams.collectingAndThen;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 business amacı şudur:

  > Ürün kataloğundaki fiyatı en yüksek ürünü bul
  ve ekranda göstermek üzere yalnızca ürünün adını üret.
   Listede hiç ürün yoksa kullanıcıya "No products found" mesajını ver.

 */
//eğitim amaçlı collectingAndThen kullanıldı ancak best practice çözüm için
//SimpleSolutionForProduct classına bakkkk !!!
public class StreamCollectAndThenDemo {
    public static void main(String[] args) {
        List<Product> products = List.of(
                new Product("Laptop", 1200),
                new Product("Smartphone", 800),
                new Product("Tablet", 600),
                new Product("Monitor", 300),
                new Product("Keyboard", 100));
        String maxProduct = products.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.maxBy((Comparator.comparingInt(Product::getPrice))),
                        (productOptional -> productOptional.isPresent() ? productOptional.get().getName() : "No products found")

                ));
        System.out.println("Most expensive product: " + maxProduct);

        String minProduct = products.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.minBy((Comparator.comparingInt(Product::getPrice))),
                        (productOptional -> productOptional.isPresent() ? productOptional.get().getName() : "No products found")

                ));
        System.out.println("Most cheap product: " + minProduct + " the price is " + products.stream().min(Comparator.comparingInt(Product::getPrice)).get().getPrice());
    }
}
