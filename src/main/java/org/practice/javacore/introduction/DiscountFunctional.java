package org.practice.javacore.introduction;

import java.util.List;

/**
 * Business açısından burada ne yapılıyor?
 *
 * Bir mağaza, fiyatı 20'den yüksek ürünlere yüzde 10 indirim uygulayıp
 * müşterinin bu ürünler için ödeyeceği toplam tutarı hesaplamak istiyor.
 */
public class DiscountFunctional {
    public static void main(String[] args) {
        // Mağazadaki ürünlerin indirimsiz fiyatları.
        List<Integer> prices = List.of(10, 30, 17, 20, 12, 45, 18);

        // Fiyat listesinden bir işlem akışı başlatılıyor.
        double totalOfDiscountedPrices = prices.stream()
                // Kampanyaya yalnızca fiyatı 20'den yüksek ürünler dahil ediliyor.
                .filter(price -> price > 20)
                // Seçilen her fiyata yüzde 10 indirim uygulanıyor.
                .mapToDouble(price -> price * 0.9)
                // İndirimli fiyatlar toplanarak müşterinin ödeyeceği tutar bulunuyor.
                .sum();

        // Hesaplanan toplam tutar ekranda gösteriliyor.
        System.out.println("Total of discounted prices: " + totalOfDiscountedPrices);
    }
}
