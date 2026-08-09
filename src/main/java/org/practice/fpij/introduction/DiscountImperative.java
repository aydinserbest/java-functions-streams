package org.practice.fpij.introduction;

import java.util.Arrays;
import java.util.List;

/**
 * Business açısından burada ne yapılıyor?
 *
 * Bir mağaza, fiyatı 20'den yüksek ürünlere yüzde 10 indirim uygulayıp
 * müşterinin bu ürünler için ödeyeceği toplam tutarı hesaplamak istiyor.
 */
public class DiscountImperative {
    public static void main(String[] args) {
        // Mağazadaki ürünlerin indirimsiz fiyatları.
        List<Integer> prices = Arrays.asList(10, 30, 17, 20, 12, 45, 18);

        // Uygun ürünlerin indirimli fiyatları bu değişkende biriktirilecek.
        double totalOfDiscountedPrices = 0.0;

        // Listedeki fiyatlar sırayla kontrol ediliyor.
        for (int price : prices) {
            // Kampanyaya yalnızca fiyatı 20'den yüksek ürünler dahil ediliyor.
            if (price > 20) {
                // Fiyata yüzde 10 indirim uygulanıp mevcut toplama ekleniyor.
                totalOfDiscountedPrices += price * 0.9;
            }
        }

        // Bütün fiyatlar kontrol edildikten sonra müşterinin ödeyeceği tutar gösteriliyor.
        System.out.println("Total of discounted prices: " + totalOfDiscountedPrices);
    }
}
