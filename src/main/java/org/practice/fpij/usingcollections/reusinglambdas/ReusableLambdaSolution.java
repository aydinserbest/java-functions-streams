package org.practice.fpij.usingcollections.reusinglambdas;

import java.util.List;
import java.util.function.Predicate;

public class ReusableLambdaSolution {
    public static void main(String[] args) {
        /*
        DuplicateLambdaProblem'daki tekrarı (name -> name.startsWith("N")
        ifadesinin 3 kez yazılmasını) burada ortadan kaldırıyoruz.
         */
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        final List<String> editors =
                List.of("Brian", "Jackie", "John", "Mike");

        final List<String> comrades =
                List.of("Kate", "Ken", "Nick", "Paula", "Zach");

        // ------------------------------------------------------------
        // Lambda'yı BİR KERE, açık tipli bir değişkende oluşturuyoruz
        // ------------------------------------------------------------
        final Predicate<String> startsWithN = name -> name.startsWith("N");
        /*
        filter() metodunun beklediği parametre tipi java.util.function.
        Predicate<String>'tir (tek bir elemanı alıp true/false döndüren bir
        fonksiyonel arayüz — test(T) metodunu içerir).

        DuplicateLambdaProblem'da lambda'yı doğrudan filter(...) çağrısının
        İÇİNE yazdığımızda, derleyici o lambda'dan arka planda bir Predicate
        implementasyonu ÜÇ AYRI yerde (her çağrı noktasında) sentezliyordu.
        Burada ise aynı işi TEK BİR yerde yapıyoruz: lambda'yı, tipini açıkça
        belirttiğimiz (Predicate<String>) bir DEĞİŞKENE atıyoruz. Böylece
        artık elimizde normal bir NESNE REFERANSI var — tıpkı bir String ya
        da bir Integer referansı gibi, bu referansı istediğimiz kadar
        parametre olarak geçebiliriz.
         */

        // ------------------------------------------------------------
        // Aynı Predicate'i üç farklı stream'de YENİDEN KULLANIYORUZ
        // ------------------------------------------------------------
        final long countFriendsStartN =
                friends.stream()
                        .filter(startsWithN)
                        .count();

        final long countEditorsStartN =
                editors.stream()
                        .filter(startsWithN)
                        .count();

        final long countComradesStartN =
                comrades.stream()
                        .filter(startsWithN)
                        .count();

        System.out.println("friends'te N ile başlayan: " + countFriendsStartN);
        System.out.println("editors'te N ile başlayan: " + countEditorsStartN);
        System.out.println("comrades'te N ile başlayan: " + countComradesStartN);

        /*
        Ne kazandık?

        - Filtreleme KURALI (hangi harfle başlıyor) artık TEK bir satırda
          yaşıyor. Kuralı değiştirmek istersek (örn. "N" yerine "K", ya da
          case-insensitive kontrol eklemek), SADECE startsWithN
          tanımını güncellemek yeterli — üç filter() çağrısına dokunmuyoruz.
        - Kod, NE yapıldığını (isim "N" ile mi başlıyor) isimlendirilmiş bir
          değişken üzerinden okunur hâle geldi; "startsWithN" ismi, satır
          içine gömülü bir lambda'dan daha açıklayıcı.
        - filter() çağrılarının kendisi artık birbirinin BİREBİR KOPYASI
          değil, aynı davranışı PAYLAŞIYORLAR.

        Not: startsWithN aynı zamanda test() metodu üzerinden başka
        yerlerde de (örn. anyMatch(startsWithN), noneMatch(startsWithN))
        kullanılabilir — Predicate bir NESNE olduğu için filter()'a özel
        değildir, Predicate bekleyen HER YERDE geçerlidir.
         */
        System.out.println("\nBonus - aynı Predicate başka bir yerde de kullanılabilir:");
        System.out.println("friends içinde N ile başlayan biri var mı? "
                + friends.stream().anyMatch(startsWithN));
    }
}
