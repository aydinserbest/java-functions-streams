package org.practice.streamsandfunctions.comparator.styles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComparableProductDemo {

    public static void main(String[] args) {
        List<ComparableProduct> products = new ArrayList<>(List.of(
                new ComparableProduct("Laptop", 1200),
                new ComparableProduct("Mouse", 40),
                new ComparableProduct("Monitor", 300)
        ));

        /*
         * Comparator vermiyoruz.
         * Collections.sort(), ComparableProduct içindeki compareTo() metodunu
         * kullanarak doğal fiyat sıralamasını uygular.
         */
        Collections.sort(products);

        System.out.println("Comparable natural order: " + products);
    }
}
