package org.practice.javacore.comparator.styles;

import java.util.Comparator;

/*
 * KLASİK YAKLAŞIM 2: Comparator ayrı bir class olarak yazılır.
 *
 * PlainProduct değişmez. Fiyata göre sıralama davranışı bu sınıfta tutulur.
 * Başka bir business sırası gerekirse başka bir Comparator yazılabilir.
 */
public class ProductPriceComparator implements Comparator<PlainProduct> {

    @Override
    public int compare(PlainProduct first, PlainProduct second) {
        return Integer.compare(first.getPrice(), second.getPrice());
    }
}
