package org.practice.fpij.usingcollections.streamcontinuebreak.exercises.helperClasses;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBasics {
    public static void main(String[] args) {
        /*
        Bu class, Q01Pagination'da kullandığımız Random / nextInt() konusunu
        sorudan bağımsız, sade örneklerle gösteriyor.
         */

        // ------------------------------------------------------------
        // 1) Random nesnesini oluşturma: NEDEN TEK SEFER, DIŞARIDA?
        // ------------------------------------------------------------
        Random random = new Random();
        System.out.println("1) Random nesnesi oluşturuldu: " + random);
        /*
        Random nesnesi, her çağrıldığında bir önceki üretilen sayıya göre
        (dahili bir "seed"/durum değeri üzerinden) yeni bir sayı üretir.
        Bu yüzden Random'ı DÖNGÜ/LAMBDA İÇİNDE her seferinde "new Random()"
        ile yeniden oluşturmak yanlıştır: özellikle çok hızlı art arda
        (örneğin bir stream'in mapToObj'u içinde) çağrılırsa, sistem saati
        tabanlı varsayılan seed'ler birbirine çok yakın/aynı çıkabilir ve
        ürettiğiniz "rastgele" sayılar birbirine benzeyip rastgeleliğini
        kaybedebilir. Doğrusu: Random nesnesini BİR KERE oluşturup, aynı
        nesneyi tekrar tekrar kullanmaktır (Q01Pagination'da yaptığımız gibi).
         */

        // ------------------------------------------------------------
        // 2) nextInt(): sınırsız, TÜM int aralığında rastgele sayı
        // ------------------------------------------------------------
        System.out.println("\n2) nextInt() -> herhangi bir int (negatif de olabilir):");
        for (int i = 0; i < 3; i++) {
            System.out.println(random.nextInt());
        }
        /*
        Parametresiz nextInt(), int'in alabileceği TÜM değer aralığından
        (yaklaşık -2 milyar ile +2 milyar arası) rastgele bir sayı üretir.
        Günlük hayattaki çoğu senaryo için (örn. "0-9 arası bir sayı") bu
        çok geniş bir aralık; genelde aşağıdaki sınırlı hâli kullanılır.
         */

        // ------------------------------------------------------------
        // 3) nextInt(bound): 0 (dahil) ile bound (HARİÇ) arası
        // ------------------------------------------------------------
        System.out.println("\n3) nextInt(6) -> 0,1,2,3,4,5 arası (6 HARİÇ):");
        for (int i = 0; i < 5; i++) {
            System.out.println(random.nextInt(6));
        }
        /*
        Bu, gerçek hayatta en çok kullanılan biçimdir: bir dizinin/listenin
        indeksini rastgele seçmek için (Q01Pagination'daki
        productNames[random.nextInt(productNames.length)] örneği gibi),
        ya da zar atmak (nextInt(6) -> 0..5, +1 eklersen 1..6 zar sonucu)
        gibi durumlarda kullanılır. Üst sınır (bound) SONUCA DAHİL DEĞİLDİR.
         */

        // ------------------------------------------------------------
        // 4) nextInt(origin, bound): origin (dahil) ile bound (HARİÇ) arası
        //    (Java 17 ile geldi)
        // ------------------------------------------------------------
        System.out.println("\n4) nextInt(10, 20) -> 10 ile 19 arası:");
        for (int i = 0; i < 5; i++) {
            System.out.println(random.nextInt(10, 20));
        }
        /*
        0'dan değil, belirlediğiniz bir alt sınırdan (origin) başlayan bir
        aralık istiyorsanız kullanışlıdır. Örneğin "18 ile 65 yaş arası
        rastgele bir yaş üret" gibi bir ihtiyaçta nextInt(18, 66) yazardınız.
         */

        // ------------------------------------------------------------
        // 5) Diğer temel üretim metotları: nextDouble / nextBoolean / nextLong
        // ------------------------------------------------------------
        System.out.println("\n5) Diğer tipler:");
        System.out.println("nextDouble()  -> 0.0 ile 1.0 arası ondalık: " + random.nextDouble());
        System.out.println("nextBoolean() -> true/false: " + random.nextBoolean());
        System.out.println("nextLong()    -> geniş aralıkta long: " + random.nextLong());

        // ------------------------------------------------------------
        // 6) Sabit "seed" ile TEKRAR ÜRETİLEBİLİR (deterministic) rastgelelik
        // ------------------------------------------------------------
        System.out.println("\n6) Aynı seed -> her çalıştırmada AYNI sayı dizisi:");
        Random seeded1 = new Random(42);
        Random seeded2 = new Random(42);
        System.out.println("seeded1: " + seeded1.nextInt(100) + ", " + seeded1.nextInt(100));
        System.out.println("seeded2: " + seeded2.nextInt(100) + ", " + seeded2.nextInt(100));
        /*
        new Random() her çalıştırmada FARKLI bir başlangıç durumu (genelde
        sistem saatine dayalı) kullanır, bu yüzden her çalıştırmada farklı
        sayılar üretir. new Random(42) gibi SABİT bir seed verirseniz, o
        Random nesnesinden üretilecek TÜM sayı dizisi her çalıştırmada
        BİREBİR AYNI olur. Bu, testler yazarken ("bu senaryoyu her seferinde
        aynı rastgele veriyle tekrar üretebilmek") çok işe yarar.
         */

        // ------------------------------------------------------------
        // 7) Stream ile toplu rastgele sayı üretimi: random.ints(...)
        // ------------------------------------------------------------
        System.out.println("\n7) random.ints(adet, min, max) -> hazır bir IntStream:");
        random.ints(5, 1, 100) // 5 adet, 1(dahil)-100(hariç) arası
                .forEach(System.out::println);
        /*
        Q01Pagination'da IntStream.range(0, 40).mapToObj(i -> ...) ile
        "sayaç" üretip, içeride random.nextInt(...) çağırarak rastgele
        değerler üretmiştik. Random sınıfının kendi ints()/doubles()/
        longs() metotları da doğrudan bir stream üretebilir; adet, alt ve
        üst sınırı parametre olarak verirsiniz. Basit durumlarda (sadece
        rastgele sayı listesi lazımsa) bu daha kısadır; Q01'de olduğu gibi
        her sayıdan KENDİ NESNENİZİ (Product) üretmeniz gerekiyorsa,
        mapToObj() ile kendi lambda'nızı yazmanız gerekir.
         */

        // ------------------------------------------------------------
        // 8) Notlar: thread-safety ve güvenlik
        // ------------------------------------------------------------
        System.out.println("\n8) Notlar:");
        System.out.println("ThreadLocalRandom.current().nextInt(10): "
                + ThreadLocalRandom.current().nextInt(10));
        /*
        - java.util.Random TEK BİR NESNE birden fazla thread tarafından
          aynı anda kullanılırsa performans kaybına yol açabilir (dahili
          durumu güncellerken thread'ler birbirini bekletir). Çok
          thread'li kodda bunun yerine ThreadLocalRandom.current() tercih
          edilir; her thread'e özel bir Random örneği verir.
        - java.util.Random KRİPTOGRAFİK OLARAK GÜVENLİ DEĞİLDİR (şifre,
          token, güvenlik anahtarı gibi hassas değerler için kullanılmaz).
          O tür ihtiyaçlarda java.security.SecureRandom kullanılır. Bizim
          alıştırmalarımızdaki (rastgele tarih/isim üretme gibi) kullanım
          için java.util.Random tamamen yeterlidir.
         */
    }
}
