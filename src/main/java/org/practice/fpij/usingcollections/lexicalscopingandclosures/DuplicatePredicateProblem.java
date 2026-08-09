package org.practice.fpij.usingcollections.lexicalscopingandclosures;

import java.util.List;
import java.util.function.Predicate;

public class DuplicatePredicateProblem {
    public static void main(String[] args) {
        /*
        Konu: "Using Lexical Scoping and Closures"

        reusinglambdas paketinde, name -> name.startsWith("N") lambda'sını
        BİR Predicate değişkeninde toplayarak tekrarı önlemiştik. Ama o
        çözüm sadece TEK bir harf ("N") için işe yarıyordu. Şimdi listede
        hem "N" hem de "B" ile başlayan isimleri de saymak istediğimizde
        ne olacağını görelim.
         */
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        // ------------------------------------------------------------
        // İki AYRI Predicate: sadece kullandıkları harf farklı
        // ------------------------------------------------------------
        final Predicate<String> startsWithN = name -> name.startsWith("N");
        final Predicate<String> startsWithB = name -> name.startsWith("B");

        final long countFriendsStartN =
                friends.stream()
                        .filter(startsWithN)
                        .count();

        final long countFriendsStartB =
                friends.stream()
                        .filter(startsWithB)
                        .count();

        System.out.println("N ile başlayan: " + countFriendsStartN);
        System.out.println("B ile başlayan: " + countFriendsStartB);

        /*
        Sorun tekrar geri geldi!

        startsWithN ve startsWithB birbirinin NEREDEYSE BİREBİR KOPYASI —
        aralarındaki TEK fark, kullandıkları harf literal'i ("N" / "B").
        Mantığın kendisi ("name bu harfle mi başlıyor?") ikisinde de aynı.

        Değişkene atayıp reusinglambdas'ta yaptığımız çözüm burada işe
        yaramıyor, çünkü orada tek bir sabit değeri (tek bir harfi) tekrar
        tekrar kullanıyorduk. Burada ise HER SEFERİNDE FARKLI bir harf
        (parametre gibi davranan bir değer) geçmemiz gerekiyor — ama
        Predicate'in test(T) metodu bize sadece "name" parametresini
        veriyor, "hangi harfle karşılaştırayım" bilgisini DIŞARIDAN alacak
        bir yolumuz yok... ya da öyle görünüyor. Çözüm: harfi bir
        PARAMETRE olarak alan ve bize bir Predicate ÜRETEN bir fonksiyon
        yazmak. Bunu StartsWithHigherOrderFunction class'ında ele alıyoruz.
         */
    }
}
