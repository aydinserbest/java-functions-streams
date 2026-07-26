package org.practice.javacore.streams.comprehensive.product;

import java.util.List;

/*
 * Product POJO'su katalog, stok ve fiyat raporlarını temsil eder.
 *
 * tags alanındaki iç liste, bütün ürün etiketlerini flatMap() ile tek akışta
 * birleştirebilmemiz için eklenmiştir.
 */
public class Product {

    private final long id;
    private final String name;
    private final String category;
    private final double price;
    private final int stock;
    private final boolean active;
    private final List<String> tags;

    public Product(
            long id,
            String name,
            String category,
            double price,
            int stock,
            boolean active,
            List<String> tags
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.active = active;
        this.tags = List.copyOf(tags);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean isActive() {
        return active;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", active=" + active +
                ", tags=" + tags +
                '}';
    }
}
