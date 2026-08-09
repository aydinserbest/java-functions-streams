package org.practice.fpij.usingcollections.joiningelements;

import java.util.List;
import java.util.StringJoiner;

public class StringJoinerDemo {
    public static void main(String[] args) {
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        /*
        String.join(...) ve Collectors.joining(...) perde arkasında
        java.util.StringJoiner sınıfını kullanır. Bu class'ta StringJoiner'ı
        DOĞRUDAN kullanarak, ayraç (infix) dışında BAŞLANGIÇ (prefix) ve
        BİTİŞ (suffix) dizilerini de nasıl kontrol ettiğimizi görüyoruz.
         */

        // ------------------------------------------------------------
        // 1) Sadece ayraç (infix): String.join ile aynı sonucu üretir
        // ------------------------------------------------------------
        StringJoiner simple = new StringJoiner(", ");
        for (String name : friends) {
            simple.add(name);
        }
        System.out.println("1) " + simple);
        // Çıktı: Brian, Nate, Neal, Raju, Sara, Scott

        // ------------------------------------------------------------
        // 2) Ayraç + prefix + suffix: örn. bir JSON dizisi/liste görünümü
        // ------------------------------------------------------------
        StringJoiner withBrackets = new StringJoiner(", ", "[", "]");
        for (String name : friends) {
            withBrackets.add(name);
        }
        System.out.println("2) " + withBrackets);
        // Çıktı: [Brian, Nate, Neal, Raju, Sara, Scott]

        // ------------------------------------------------------------
        // 3) Boş koleksiyon durumu: setEmptyValue ile özel bir metin
        // ------------------------------------------------------------
        StringJoiner empty = new StringJoiner(", ", "[", "]");
        empty.setEmptyValue("Kimse yok");
        System.out.println("3) " + empty);
        // Çıktı: Kimse yok
        /*
        Hiç add(...) çağrılmadıysa StringJoiner varsayılan olarak sadece
        prefix+suffix'i ("[]") döndürür. setEmptyValue(...) ile bu boş
        durumda gösterilecek TAMAMEN farklı bir metin belirleyebiliriz --
        PrintListImperative'teki "if (friends.size() > 0)" kontrolünün
        yerini alan, kütüphane seviyesinde çözülmüş bir durum.
         */

        // ------------------------------------------------------------
        // 4) merge(): iki StringJoiner'ı birleştirme
        // ------------------------------------------------------------
        StringJoiner part1 = new StringJoiner(", ");
        part1.add("Brian").add("Nate");

        StringJoiner part2 = new StringJoiner(", ");
        part2.add("Sara").add("Scott");

        part1.merge(part2);
        System.out.println("4) " + part1);
        // Çıktı: Brian, Nate, Sara, Scott
        /*
        merge(other), other'ın İÇERİĞİNİ (kendi prefix/suffix'i olmadan,
        sadece eklenmiş elemanları) mevcut StringJoiner'ın SONUNA, aradaki
        ayracı kullanarak ekler. Örneğin paralel işlenen parçaları (her
        biri kendi StringJoiner'ında biriktirilmiş) tek bir sonuçta
        toplamak istediğinizde kullanışlıdır.
         */

        /*
        Özet: String.join(...) ve Collectors.joining(...) günlük ihtiyaçlar
        için yeterli ve daha kısa. StringJoiner'ı DOĞRUDAN kullanmak, ek bir
        prefix/suffix, boş durum mesajı ya da parça parça biriktirme/birleştirme
        gibi DAHA İNCE KONTROL gerektiğinde tercih edilir.
         */
    }
}
