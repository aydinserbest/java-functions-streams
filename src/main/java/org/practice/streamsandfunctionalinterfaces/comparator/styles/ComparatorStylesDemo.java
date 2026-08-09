package org.practice.streamsandfunctionalinterfaces.comparator.styles;

import java.util.Comparator;
import java.util.List;

public class ComparatorStylesDemo {

    public static void main(String[] args) {
        List<PlainProduct> products = List.of(
                new PlainProduct("Laptop", 1200),
                new PlainProduct("Mouse", 40),
                new PlainProduct("Monitor", 300)
        );

        /*
         * KLASİK YAKLAŞIM 2:
         *
         * Comparator davranışı ayrı bir class içindedir.
         * ProductPriceComparator implements Comparator<PlainProduct> yazar.
         */
        Comparator<PlainProduct> classicByPrice =
                new ProductPriceComparator();

        List<PlainProduct> classicResult = products.stream()
                .sorted(classicByPrice)
                .toList();

        System.out.println("Separate Comparator class: " + classicResult);

        /*
         * MODERN YAKLAŞIM:
         *
         * PlainProduct hiçbir interface implement etmez.
         * Ayrı ProductPriceComparator classı yazmak yerine Comparator'ı hazır
         * factory metot ve method reference ile oluştururuz.
         *
         * ProductPriceComparator ile aşağıdaki byPrice aynı işi yapar.
         */
        Comparator<PlainProduct> byPrice =
                Comparator.comparingInt(PlainProduct::getPrice);

        /*
         * Aynı POJO için ikinci bir business sırası kolayca tanımlanabilir.
         * Product sınıfına veya ayrı bir Comparator classına dokunmuyoruz.
         */
        Comparator<PlainProduct> byName =
                Comparator.comparing(PlainProduct::getName);

        List<PlainProduct> modernByPrice = products.stream()
                .sorted(byPrice)
                .toList();

        List<PlainProduct> modernByName = products.stream()
                .sorted(byName)
                .toList();

        System.out.println("Modern Comparator by price: " + modernByPrice);
        System.out.println("Modern Comparator by name: " + modernByName);

        /*
         * Özet:
         *
         * ComparableProduct implements Comparable
         * -> Doğal sıra sınıfın içinde, compareTo() kullanılır.
         *
         * ProductPriceComparator implements Comparator
         * -> Sıralama ayrı bir class içinde, compare() kullanılır.
         *
         * Comparator.comparingInt(...)
         * -> Modern kısa kullanım; POJO hiçbir interface implement etmez.
         */
    }
}
