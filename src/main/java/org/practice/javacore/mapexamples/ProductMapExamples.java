package org.practice.javacore.mapexamples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductMapExamples {

    public static void main(String[] args) {
        Product laptop = new Product(101, "Laptop", 1200);
        Product mouse = new Product(102, "Mouse", 40);
        Product monitor = new Product(103, "Monitor", 300);
        Product keyboard = new Product(104, "Keyboard", 100);

        productLookupMap(laptop, mouse, monitor, keyboard);
        productsByWarehouseMap(laptop, mouse, monitor, keyboard);
    }

    private static void productLookupMap(Product... products) {
        /*
         * Business senaryosu:
         * Ürün servisi, ürün ID'si verildiğinde Product nesnesine doğrudan
         * erişmek istiyor.
         *
         * Key   -> Integer ürün ID'si
         * Value -> Product nesnesi
         *
         * Map<Integer, Product>
         */
        Map<Integer, Product> productById = new HashMap<>();

        for (Product product : products) {
            productById.put(product.getId(), product);
        }

        /*
         * get(key), verilen ID'ye ait Product value değerini döndürür.
         */
        Product selectedProduct = productById.get(103);

        System.out.println("\nProduct with ID 103: " + selectedProduct);

        /*
         * getOrDefault(), key bulunamadığında null yerine belirlediğimiz
         * varsayılan Product nesnesini döndürebilir.
         */
        Product unknownProduct = new Product(0, "Unknown product", 0);
        Product result = productById.getOrDefault(999, unknownProduct);

        System.out.println("Product with unknown ID: " + result);

        /*
         * Yalnızca mevcut ürün ID'lerini istiyorsak keySet() kullanırız.
         */
        System.out.println("Product IDs: " + productById.keySet());

        /*
         * Yalnızca Product nesnelerini istiyorsak values() kullanırız.
         */
        System.out.println("All products: " + productById.values());

        /*
         * ID ve Product birlikte gerekiyorsa entrySet() kullanırız.
         */
        productById.entrySet().forEach(entry -> {
            Integer productId = entry.getKey();
            Product product = entry.getValue();

            System.out.println(
                    "ID " + productId + " -> " + product.getName()
            );
        });
    }

    private static void productsByWarehouseMap(Product... products) {
        /*
         * Business senaryosu:
         * Her depo ID'sini o depoda bulunan Product listesiyle eşleştiriyoruz.
         *
         * Key   -> Integer depo ID'si
         * Value -> List<Product> ürün listesi
         *
         * Map<Integer, List<Product>>
         */
        Map<Integer, List<Product>> productsByWarehouse = new HashMap<>();

        productsByWarehouse.put(
                1,
                new ArrayList<>(List.of(products[0], products[1]))
        );
        productsByWarehouse.put(
                2,
                new ArrayList<>(List.of(products[2], products[3]))
        );

        /*
         * get(warehouseId), belirtilen deponun Product listesini döndürür.
         */
        List<Product> firstWarehouseProducts =
                productsByWarehouse.get(1);

        System.out.println(
                "\nProducts in warehouse 1: " + firstWarehouseProducts
        );

        /*
         * computeIfAbsent(key, mappingFunction):
         *
         * Depo key'i varsa mevcut List<Product> değerini döndürür.
         * Depo key'i yoksa mappingFunction ile yeni ArrayList oluşturur,
         * Map'e ekler ve bu listeyi döndürür.
         *
         * Ardından add() ile yeni ürünü ilgili deponun listesine ekliyoruz.
         */
        Product webcam = new Product(105, "Webcam", 90);

        productsByWarehouse
                .computeIfAbsent(3, warehouseId -> new ArrayList<>())
                .add(webcam);

        /*
         * entrySet() ile depo ID'sine ve o deponun ürün listesine birlikte
         * erişiyoruz.
         */
        System.out.println("\nProducts grouped by warehouse:");
        for (Map.Entry<Integer, List<Product>> entry
                : productsByWarehouse.entrySet()) {

            Integer warehouseId = entry.getKey();
            List<Product> warehouseProducts = entry.getValue();

            System.out.println("Warehouse " + warehouseId + ":");

            for (Product product : warehouseProducts) {
                System.out.println(
                        "  - " + product.getName() + " | €" + product.getPrice()
                );
            }
        }

        /*
         * Hızlı tip hatırlatması:
         *
         * Map<Integer, Product>
         * get(key) -> tek Product
         *
         * Map<Integer, List<Product>>
         * get(key) -> List<Product>
         */
    }
}
