package org.practice.fpij.usingcollections.reducingtoasinglevalue;

import java.util.List;

public class ReduceWithIdentity {
    public static void main(String[] args) {
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        /*
        ReduceToLongestName'deki reduce() bir Optional<String> döndürüyordu,
        çünkü liste BOŞ olabilirdi ve o durumda geri verecek hiçbir eleman
        yoktu. Eğer BAŞLANGIÇ (identity/base) DEĞERİ olarak kullanılacak bir
        varsayılanımız varsa, reduce()'ın bu değeri PARAMETRE olarak alan
        farklı (overload edilmiş) bir versiyonunu kullanabiliriz.

        Örnek: "Steve" ismini bir taban (base) değer olarak veriyoruz. Eğer
        listede Steve'den daha UZUN bir isim varsa o seçilecek; yoksa
        sonuç doğrudan "Steve" olacak.
         */
        final String steveOrLonger =
                friends.stream()
                        .reduce("Steve", (name1, name2) ->
                                name1.length() >= name2.length() ? name1 : name2);

        System.out.println("Sonuç: " + steveOrLonger);
        // Steve 5 harfli; Brian ve Scott da 5 harfli ama "Steve" başlangıç
        // değeri olarak name1 konumunda geldiği için >= şartıyla önde kalıyor.
        // Beklenen: Steve

        /*
        Bu sürümde reduce() ilk çağrıyı

            name1 = "Steve" (bizim verdiğimiz taban değer)
            name2 = "Brian" (listenin ilk elemanı)

        şeklinde başlatıyor, sonra normal reduce mantığıyla (bir önceki
        sonuç + bir sonraki eleman) listenin geri kalanını gezmeye devam
        ediyor.

        Neden artık Optional DÖNMÜYOR?

        Çünkü liste TAMAMEN BOŞ olsa bile, elimizde zaten bir taban değer
        ("Steve") var -- "sonuç yok" diye bir durum SÖZ KONUSU DEĞİL,
        sonuç en kötü ihtimalle taban değerin kendisi olur. Bu yüzden bu
        overload düz bir String (genel olarak T) döndürür, Optional<String>
        değil -- PickElementElegant'taki orElse(varsayılanDeğer) ile aynı
        SONUCU, farklı bir YOLDAN elde ediyoruz: orElse() sonradan Optional
        üzerinde varsayılan atarken, bu reduce() overload'u varsayılanı
        EN BAŞTAN işin içine katıyor.
         */

        // Karşılaştırma için: listede "Steve"den daha uzun kimse yoksa yine "Steve" kazanır.
        final List<String> shortNames = List.of("Al", "Bo", "Cy");
        final String result = shortNames.stream()
                .reduce("Steve", (name1, name2) ->
                        name1.length() >= name2.length() ? name1 : name2);
        System.out.println("Kısa isimler arasında taban değer kazanır: " + result);
        // Beklenen: Steve
    }
}
