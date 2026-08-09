package org.practice.streamsandfunctionalinterfaces.mapexamples;

import java.util.HashMap;
import java.util.Map;

public class BasicMapOperationsDemo {

    public static void main(String[] args) {
        /*
         * Business senaryosu:
         * Bir mağazada ürün kodunu mevcut stok miktarıyla eşleştiriyoruz.
         *
         * Key   -> String ürün kodu
         * Value -> Integer stok miktarı
         *
         * Map<K, V> içinde K key tipini, V value tipini gösterir.
         */
        Map<String, Integer> stockByProductCode = new HashMap<>();

        /*
         * put(key, value):
         * Map'e yeni key-value eşleşmesi ekler.
         * Aynı key tekrar verilirse eski value yeni value ile değiştirilir.
         */
        stockByProductCode.put("LAPTOP", 5);
        stockByProductCode.put("MOUSE", 20);
        stockByProductCode.put("KEYBOARD", 8);

        stockByProductCode.put("LAPTOP", 6);
        System.out.println("Updated laptop stock: " + stockByProductCode);

        /*
         * get(key):
         * Verilen key'e ait value değerini döndürür.
         * Key bulunamazsa null döndürür.
         */
        Integer mouseStock = stockByProductCode.get("MOUSE");
        Integer unknownStock = stockByProductCode.get("MONITOR");

        System.out.println("Mouse stock: " + mouseStock);
        System.out.println("Unknown monitor stock: " + unknownStock);

        /*
         * getOrDefault(key, defaultValue):
         * Key varsa gerçek value, yoksa verdiğimiz varsayılan değer döner.
         * Map'e yeni kayıt eklemez.
         */
        int monitorStock = stockByProductCode.getOrDefault("MONITOR", 0);
        System.out.println("Monitor stock with default: " + monitorStock);

        /*
         * containsKey(key)   -> Map'te bu key var mı?
         * containsValue(v)   -> Map'te bu value var mı?
         */
        boolean hasLaptop = stockByProductCode.containsKey("LAPTOP");
        boolean hasProductWithStock20 = stockByProductCode.containsValue(20);

        System.out.println("Has LAPTOP key: " + hasLaptop);
        System.out.println("Has stock value 20: " + hasProductWithStock20);

        /*
         * putIfAbsent(key, value):
         * Key yoksa kaydı ekler, key zaten varsa mevcut değeri korur.
         */
        stockByProductCode.putIfAbsent("MOUSE", 100);
        stockByProductCode.putIfAbsent("MONITOR", 4);

        /*
         * replace(key, value):
         * Key varsa value değerini değiştirir; key yoksa yeni kayıt oluşturmaz.
         */
        stockByProductCode.replace("KEYBOARD", 10);

        /*
         * computeIfPresent():
         * Key varsa mevcut value kullanılarak yeni value hesaplanır.
         * Bir Mouse satıldığında stok miktarını bir azaltıyoruz.
         */
        stockByProductCode.computeIfPresent(
                "MOUSE",
                (code, stock) -> stock > 0 ? stock - 1 : 0
        );

        /*
         * remove(key):
         * Key-value eşleşmesini Map'ten kaldırır ve eski value değerini döndürür.
         */
        Integer removedStock = stockByProductCode.remove("MONITOR");

        System.out.println("Removed monitor stock: " + removedStock);
        System.out.println("Final stock map: " + stockByProductCode);

        /*
         * size()    -> Map'teki key-value entry sayısı
         * isEmpty() -> Map boş mu?
         */
        System.out.println("Entry count: " + stockByProductCode.size());
        System.out.println("Is map empty: " + stockByProductCode.isEmpty());
    }
}
