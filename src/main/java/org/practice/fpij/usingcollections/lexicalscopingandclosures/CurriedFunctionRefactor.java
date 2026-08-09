package org.practice.fpij.usingcollections.lexicalscopingandclosures;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class CurriedFunctionRefactor {
    public static void main(String[] args) {
        /*
        StartsWithHigherOrderFunction'da checkIfStartsWith'i STATIC bir
        metot olarak yazdık. Ama gelecekte her önbelleğe almak (cache)
        istediğimiz değer için ayrı bir static metot eklemek class'ı
        kirletir (pollute eder). Oysa bu fonksiyona sadece bir metodun
        İÇİNDE, ihtiyaç duyulduğu yerde ihtiyacımız var. Onu bir
        java.util.function.Function değişkenine taşıyarak KAPSAMINI
        DARALTABİLİRİZ (narrow scope) — class seviyesinden metot
        seviyesine indirebiliriz.

        Aşağıda AYNI davranışı üç farklı yazım biçimiyle gösteriyoruz;
        üçü de birbirinin eşdeğeri, sadece kısalık dereceleri farklı.
         */
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        // ------------------------------------------------------------
        // 1) En açık (verbose) hâli: gövdeli lambda, Predicate'i adlandırıp döndürüyor
        // ------------------------------------------------------------
        final Function<String, Predicate<String>> startsWithLetterVerbose =
                (String letter) -> {
                    Predicate<String> checkStarts = (String name) -> name.startsWith(letter);
                    return checkStarts;
                };
        /*
        startsWithLetterVerbose bir Function<String, Predicate<String>>:
        "bir String (harf) al, bana bir Predicate<String> ÜRET" demek. Gövde
        içinde önce checkStarts adında bir Predicate oluşturuyoruz, sonra
        onu return ediyoruz. checkIfStartsWith static metoduyla TAMAMEN
        aynı işi yapıyor, sadece bir DEĞİŞKEN olarak var; class'ın static
        alanı değil, main()'in yerel (local) bir değişkeni.
         */

        // ------------------------------------------------------------
        // 2) Ara adım: gövde yerine tek ifadeli iç içe lambda
        // ------------------------------------------------------------
        final Function<String, Predicate<String>> startsWithLetterShort =
                (String letter) -> (String name) -> name.startsWith(letter);
        /*
        checkStarts adlı ara değişkene gerek yok: "letter -> (name -> ...)"
        şeklinde LAMBDA DÖNDÜREN BİR LAMBDA yazabiliriz. Dıştaki lambda
        (letter alan) çalıştığında, içteki lambda (name alan) bir NESNE
        olarak üretilip geri döner. Bu, "curry" (körleme) adı verilen bir
        tekniğin görünümüdür: iki parametreli bir işlemi (letter VE name),
        "önce letter'ı al, sonra name bekleyen bir fonksiyon döndür"
        şeklinde TEK TEK parametre alan bir fonksiyon zincirine çeviriyoruz.
         */

        // ------------------------------------------------------------
        // 3) En kısa hâli: parametre TİPLERİNİ de derleyiciye bırakıyoruz
        // ------------------------------------------------------------
        final Function<String, Predicate<String>> startsWithLetterConcise =
                letter -> name -> name.startsWith(letter);
        /*
        (String letter) -> (String name) -> ... yazımındaki tipleri
        kaldırdık. Java, startsWithLetterConcise değişkeninin bildirilen
        tipinden (Function<String, Predicate<String>>) yola çıkarak
        letter'ın String, iç lambda'nın da bir Predicate<String>
        (name'in de String) olduğunu KENDİSİ ÇIKARIYOR (type inference).
        Okumaya alışana kadar biraz göz yorucu olabilir, ama üç örnek de
        birebir aynı şeyi ifade ediyor.
         */

        // Üçünün de aynı sonucu ürettiğini görelim:
        System.out.println("verbose  -> N: " + friends.stream().filter(startsWithLetterVerbose.apply("N")).count());
        System.out.println("short    -> N: " + friends.stream().filter(startsWithLetterShort.apply("N")).count());
        System.out.println("concise  -> N: " + friends.stream().filter(startsWithLetterConcise.apply("N")).count());

        // ------------------------------------------------------------
        // Kullanım: startsWithLetterConcise.apply(harf) bize bir Predicate verir
        // ------------------------------------------------------------
        final long countFriendsStartN =
                friends.stream()
                        .filter(startsWithLetterConcise.apply("N"))
                        .count();

        final long countFriendsStartB =
                friends.stream()
                        .filter(startsWithLetterConcise.apply("B"))
                        .count();

        System.out.println("\nN ile başlayan: " + countFriendsStartN);
        System.out.println("B ile başlayan: " + countFriendsStartB);
        /*
        startsWithLetterConcise bir Function olduğu için çağrılırken
        () değil, .apply(...) kullanılır (Function arayüzünün soyut
        metodu "apply"dır; Predicate'in soyut metodu ise "test"tir —
        bu yüzden StartsWithHigherOrderFunction'da filter(checkIfStartsWith("N"))
        derken ".apply" yazmıyorduk, çünkü checkIfStartsWith zaten normal
        bir METOT çağrısıydı, sonucu (Predicate) doğrudan filter'a
        veriyorduk. Burada ise önce startsWithLetterConcise.apply("N") ile
        Predicate'i ÇIKARIYORUZ, sonra onu filter'a veriyoruz).
         */
    }
}
