package org.practice.fpij.introduction;

import java.util.Arrays;
import java.util.List;

/**
 * Business açısından burada ne yapılıyor?
 *
 * Bir uygulama, hizmet verdiği şehirler arasında Chicago'nun bulunup
 * bulunmadığını kontrol edip sonucu ekranda göstermek istiyor.
 */
public class Cities {
    public static void main(String[] args) {
        // Uygulamanın hizmet verdiği şehirler.
        List<String> cities = Arrays.asList("Albany", "Boulder", "Chicago", "Denver", "Eugene");

        // Aynı arama ihtiyacı iki farklı yaklaşımla çalıştırılıyor.
        findChicagoDeclarative(cities);
        findChicagoImperative(cities);
        /*
        System.out.println("Found chicago?:" + cities.contains("Chicago"));
        boolean chicago = cities.contains("Chicago");
        System.out.println("Found chicago?:" + chicago);
         */
    }

    private static void findChicagoDeclarative(List<String> cities) {
        // contains(), listenin Chicago değerini içerip içermediğini doğrudan söyler.
        System.out.println("Found chicago?:" + cities.contains("Chicago"));
    }

    private static void findChicagoImperative(List<String> cities) {
        // Aramaya başlamadan önce Chicago'nun bulunmadığını varsayıyoruz.
        boolean found = false;

        // Şehirleri sırayla kontrol ediyoruz.
        for (String city : cities) {
            if (city.equals("Chicago")) {
                // Chicago bulunduğu anda sonucu true yapıp gereksiz aramayı durduruyoruz.
                found = true;
                break;
            }
        }

        // Arama tamamlandığında bulunan sonucu kullanıcıya gösteriyoruz.
        System.out.println("Found chicago?:" + found);
    }
}
