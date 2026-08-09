package org.practice.fpij.usingcollections.pickingelement;

import java.util.List;

public class PickElementImperative {

    public static void pickName(final List<String> names, final String startingLetter) {
        String foundName = null;
        for (String name : names) {
            if (name.startsWith(startingLetter)) {
                foundName = name;
                break;
            }
        }
        System.out.print(String.format("%s ile başlayan bir isim: ", startingLetter));
        if (foundName != null) {
            System.out.println(foundName);
        } else {
            System.out.println("İsim bulunamadı");
        }
    }

    public static void main(String[] args) {
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        pickName(friends, "N");
        pickName(friends, "Z");

        /*
        Bu metodun kokusu (code smell), yoldan geçen çöp kamyonlarıyla
        yarışacak kadar belirgin:

        1) NULL İLE BAŞLAMA: foundName = null diye başlatılıyor. Bu, kodu
           kullanan herkesi bir "null check" yapmaya mecbur bırakıyor;
           unutulursa NullPointerException riski doğar.

        2) HARİCİ (EXTERNAL) İTERATOR + break: elemanları teker teker
           for ile geziyoruz, eşleşme bulununca döngüden break ile ELLE
           çıkıyoruz. Bu, imperative (nasıl yapılacağını adım adım anlatan)
           bir stil -- "ne istiyorum" değil "nasıl yapacağım" anlatılıyor.

        3) MUTABILITY: foundName değişkeni, döngü ilerledikçe İÇERİĞİ
           DEĞİŞEBİLEN bir durum (state) taşıyor.

        4) PRIMITIVE OBSESSION: "bulundu / bulunamadı" gibi iki durumlu bir
           sonucu temsil etmek için sade bir String + null kombinasyonu
           kullanılıyor. Oysa bu durumu doğrudan temsil eden, daha zengin
           bir tip (Optional) var -- onu kullanmıyoruz.

        5) Döngüden çıktıktan SONRA ayrıca if/else ile sonucu yorumlamamız
           gerekiyor. Basit bir "ilk eşleşeni bul ve yazdır" işlemi için
           oldukça kabarık bir metot ortaya çıktı.

        Çözümü PickElementElegant class'ında görelim.
         */
    }
}
