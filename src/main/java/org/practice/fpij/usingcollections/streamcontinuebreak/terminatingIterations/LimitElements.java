package org.practice.fpij.usingcollections.streamcontinuebreak.terminatingIterations;

import java.util.List;

public class LimitElements {
    public static void main(String[] args) {
        /*
        Konu: "Terminating Iterations" (Yinelemeyi Erken Sonlandırma)

        skip() ve dropWhile() koleksiyonun BAŞINDAN eleman ATLAMAK için
        kullanılıyordu. limit() ve takeWhile() ise bunun tam tersini yapar:
        koleksiyonun sonuna kadar gitmeden yinelemeyi ERKEN DURDURUR.

        - Belirli SAYIDA elemanı işlemek istiyorsak: limit()
        - Bir şart sağlandığı SÜRECE işlemek, şart bozulunca hemen durmak
          istiyorsak: takeWhile() (JDK 9 ile geldi)
         */
        List<String> friends = List.of(
                "Brian",
                "Nate",
                "Neal",
                "Raju",
                "Sara",
                "Scott"
        );

        /*
        limit(3): Sadece ilk 3 elemanı işle, sonra dur.
        skip()/dropWhile() gibi bunlar da bir "kapı" gibi davranır ama farklı
        şekilde: skip/dropWhile başta KAPALI bir kapıdır, şart bozulunca açılır.
        limit/takeWhile ise başta AÇIK bir kapıdır; şart bozulunca (ya da
        istenen sayıya ulaşılınca) bir daha AÇILMAMAK üzere kapanır.

        Akış:
        Brian -> işlenir (1. eleman)
        Nate  -> işlenir (2. eleman)
        Neal  -> işlenir (3. eleman) -> limit doldu, kapı kapanır
        Raju, Sara, Scott -> hiç değerlendirilmez, akış burada tamamen biter

        Beklenen çıktı: BRIAN, NATE, NEAL
         */
        friends.stream()
                .limit(3)
                .map(String::toUpperCase)
                .forEach(System.out::println);

        /*
        takeWhile(): Belirli bir SAYI yerine bir ŞART verilir. Şart doğru
        olduğu SÜRECE elemanlar işlenir; şartı SAĞLAMAYAN ilk elemanla
        karşılaşılır karşılaşılmaz kapı kapanır ve yineleme HEMEN durur.
        dropWhile'ın aksine (o, şart bozulana kadar ATAR, sonrasını TAMAMEN alır),
        takeWhile şart bozulana kadar ALIR, sonrasını TAMAMEN atar.

        Şart: name.length() > 4 (uzunluk 4'ten büyük olduğu sürece devam et)

        Akış:
        Brian (5) -> şart sağlanıyor -> işlenir
        Nate  (4) -> şart sağlanmıyor -> kapı hemen kapanır, yineleme durur
        Neal, Raju, Sara, Scott -> hiç değerlendirilmez (dizinin sonunda
        Scott'ın uzunluğu da 5 olsa, yani şartı sağlasa bile, artık kapı
        kapandığı için bir daha kontrol edilmez)

        Beklenen çıktı: sadece BRIAN
         */
        friends.stream()
                .takeWhile(name -> name.length() > 4)
                .map(String::toUpperCase)
                .forEach(System.out::println);

        /*
        Özet - dört fonksiyonun "kapı" benzetmesi:

        skip(n)        : başta kapalı kapı, n eleman geçince açılır (sayıya göre)
        dropWhile(pred) : başta kapalı kapı, pred yanlış olan ilk elemanda açılır
        limit(n)        : başta açık kapı, n eleman geçince sonsuza dek kapanır
        takeWhile(pred) : başta açık kapı, pred yanlış olan ilk elemanda sonsuza dek kapanır

        Her dördü de listeyi baştan sona TEK TEK, sırayla dolaşır; filter()
        gibi her elemanı birbirinden bağımsız değerlendirmez, "kapının o an
        açık mı kapalı mı olduğuna" bakar.
         */
    }
}
