package org.practice.streamsandfunctionalinterfaces.comparator.styles;

/*
 * Bu Product yalnızca veri taşır.
 *
 * Comparable implement etmez ve içinde compareTo() bulunmaz.
 * Sıralama kuralları Product sınıfının dışından verilecektir.
 */
public class PlainProduct {

    private final String name;
    private final int price;

    public PlainProduct(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " (€" + price + ")";
    }
}
