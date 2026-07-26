package org.practice.javacore.mapexamples;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapTraversalDemo {

    public static void main(String[] args) {
        /*
         * Business senaryosu:
         * Kullanıcı adını tamamladığı sipariş sayısıyla eşleştiriyoruz.
         *
         * LinkedHashMap, kayıtların eklenme sırasını koruduğu için örnek çıktısı
         * her çalıştırmada Alice, Mehmet, John sırasıyla görünür.
         */
        Map<String, Integer> completedOrdersByUser = new LinkedHashMap<>();
        completedOrdersByUser.put("Alice", 12);
        completedOrdersByUser.put("Mehmet", 8);
        completedOrdersByUser.put("John", 15);

        /*
         * keySet():
         * Yalnızca key değerlerine erişir.
         *
         * Dönüş tipi:
         * Set<String>
         *
         * Business ihtiyacı:
         * Raporun yalnızca kullanıcı adlarını göstermesi.
         */
        System.out.println("\nOnly keys with keySet():");
        for (String username : completedOrdersByUser.keySet()) {
            System.out.println(username);
        }

        /*
         * values():
         * Yalnızca value değerlerine erişir.
         *
         * Dönüş tipi:
         * Collection<Integer>
         *
         * Business ihtiyacı:
         * Kullanıcı adlarından bağımsız sipariş adetlerini incelemek.
         */
        System.out.println("\nOnly values with values():");
        for (Integer orderCount : completedOrdersByUser.values()) {
            System.out.println(orderCount);
        }

        /*
         * entrySet():
         * Key ve value değerini birlikte taşıyan Map.Entry nesnelerine erişir.
         *
         * entry.getKey()   -> entry'nin key değeri
         * entry.getValue() -> entry'nin value değeri
         *
         * Key ve value birlikte gerekiyorsa keySet() içinde tekrar get(key)
         * çağırmak yerine entrySet() kullanmak daha doğrudandır.
         */
        System.out.println("\nKeys and values with entrySet():");
        for (Map.Entry<String, Integer> entry
                : completedOrdersByUser.entrySet()) {

            String username = entry.getKey();
            Integer orderCount = entry.getValue();

            System.out.println(
                    username + " completed " + orderCount + " orders"
            );
        }

        /*
         * Map.forEach((key, value) -> ...):
         * Key ve value değerini lambda parametreleri olarak birlikte verir.
         * entrySet() döngüsünün kısa ve modern alternatifidir.
         */
        System.out.println("\nKeys and values with Map.forEach():");
        completedOrdersByUser.forEach((username, orderCount) ->
                System.out.println(username + " -> " + orderCount)
        );

        /*
         * Hızlı hatırlatma:
         *
         * map.keySet()   -> yalnızca key'ler
         * map.values()   -> yalnızca value'lar
         * map.entrySet() -> key ve value birlikte
         * map.get(key)   -> belirli bir key'in value değeri
         */
    }
}
