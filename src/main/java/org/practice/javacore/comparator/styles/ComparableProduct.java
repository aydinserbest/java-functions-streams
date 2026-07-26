package org.practice.javacore.comparator.styles;

/*
 * KLASİK YAKLAŞIM 1: POJO, Comparable'ı kendisi implement eder.
 *
 * Bu Product sınıfı kendi doğal sırasının fiyata göre olduğuna karar verir.
 * compareTo() Product sınıfının içindedir.
 *
 * Artısı:
 * Comparator vermeden Collections.sort() veya sorted() kullanılabilir.
 *
 * Dikkat:
 * Bir sınıfın compareTo() ile tanımlanmış temel olarak tek doğal sırası vardır.
 * Daha sonra ada veya stoka göre farklı sıralamalar için yine Comparator gerekir.
 */
public class ComparableProduct implements Comparable<ComparableProduct> {

    private final String name;
    private final int price;

    public ComparableProduct(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    /*
     * this.price küçükse negatif, eşitse 0, büyükse pozitif sonuç oluşur.
     * Böylece doğal sıra ucuz üründen pahalı ürüne doğrudur.
     */
    @Override
    public int compareTo(ComparableProduct other) {
        return Integer.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return name + " (€" + price + ")";
    }
}
