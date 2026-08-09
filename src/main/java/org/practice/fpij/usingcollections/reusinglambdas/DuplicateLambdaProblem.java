package org.practice.fpij.usingcollections.reusinglambdas;

import java.util.List;

public class DuplicateLambdaProblem {
    public static void main(String[] args) {
        /*
        Konu: "Reusing Lambda Expressions" (Lambda İfadelerini Yeniden Kullanma)

        Lambda ifadeleri o kadar kısa ve pratik yazılıyor ki, fark etmeden
        aynı lambda'yı birden fazla yere KOPYALA-YAPIŞTIR yapmak çok kolay
        oluyor. Bu class, o tuzağı gösteriyor: aynı filtreleme mantığını
        3 farklı listede AYNI LAMBDA'yı 3 kez yazarak uyguluyoruz.
         */
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        final List<String> editors =
                List.of("Brian", "Jackie", "John", "Mike");

        final List<String> comrades =
                List.of("Kate", "Ken", "Nick", "Paula", "Zach");

        /*
        Üç listede de "N harfiyle başlayan isim sayısı"nı bulmak istiyoruz.
        Naif yaklaşım: her stream'de aynı lambda'yı tekrar tekrar yazmak.
         */
        final long countFriendsStartN =
                friends.stream()
                        .filter(name -> name.startsWith("N"))
                        .count();

        final long countEditorsStartN =
                editors.stream()
                        .filter(name -> name.startsWith("N"))
                        .count();

        final long countComradesStartN =
                comrades.stream()
                        .filter(name -> name.startsWith("N"))
                        .count();

        System.out.println("friends'te N ile başlayan: " + countFriendsStartN);
        System.out.println("editors'te N ile başlayan: " + countEditorsStartN);
        System.out.println("comrades'te N ile başlayan: " + countComradesStartN);

        /*
        Sorun nerede?

        name -> name.startsWith("N") ifadesi üç yerde AYNEN tekrar ediyor.
        Kod kısa göründüğü için bu tekrar göze çok batmıyor, ama gerçek bir
        projede şu sorunlar ortaya çıkar:

        1) BAKIM SORUNU: Yarın "N" yerine "K" ile başlayanları da filtrelemek
           ya da büyük/küçük harf duyarsız (case-insensitive) kontrol
           eklemek isterseniz, bu değişikliği kod tabanındaki HER
           kopyada tek tek yapmanız gerekir. Birini unutursanız, uygulamanın
           bir kısmı eski davranışta kalır — tutarsız (inconsistent) bir
           sistem elde edersiniz.

        2) PERFORMANS/ANALİZ SORUNU: Bu filtreleme mantığının davranışını
           incelemek veya iyileştirmek istediğinizde (örneğin performans
           profili çıkarmak), mantık TEK bir yerde toplanmadığı için kod
           tabanında dağınık kopyaları ayrı ayrı incelemeniz gerekir.

        Kısacası: Lambda ifadeleri kısalığıyla bizi rahatlatıyor ama
        "DRY" (Don't Repeat Yourself) prensibini çiğnemeyi de bir o kadar
        kolaylaştırıyor. Çözümü ReusableLambdaSolution class'ında.
         */
    }
}
