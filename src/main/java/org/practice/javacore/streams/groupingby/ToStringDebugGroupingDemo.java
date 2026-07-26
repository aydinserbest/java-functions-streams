package org.practice.javacore.streams.groupingby;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ToStringDebugGroupingDemo {

    public static void main(String[] args) {
        List<NewProduct> products = List.of(
                new NewProduct("Laptop", 1200),
                new NewProduct("Desk Chair", 150),
                new NewProduct("Office Chair", 150),
                new NewProduct("Dining Table", 600)
        );

        /*
         * Amaç:
         * Geliştirici olarak groupingBy() sonucunun doğru oluşup oluşmadığını
         * konsolda hızlıca kontrol etmek istiyoruz.
         *
         * Sonuç tipi:
         * Map<Integer, List<NewProduct>>
         *
         * Map yazdırılırken içindeki her NewProduct için toString() çağrılır.
         * NewProduct.toString() bulunduğu için Product@hash yerine nesnenin
         * alanlarını görebiliriz.
         *
         * Bu görünüm debug ve öğrenme için uygundur; kullanıcı ekranının nihai
         * tasarımı olarak düşünülmemelidir.
         */
        Map<Integer, List<NewProduct>> productsByPrice = products.stream()
                .collect(Collectors.groupingBy(NewProduct::getPrice));

        System.out.println(productsByPrice);
    }
}
