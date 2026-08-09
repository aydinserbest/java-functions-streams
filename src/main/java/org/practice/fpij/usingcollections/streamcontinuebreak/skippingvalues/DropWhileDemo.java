package org.practice.fpij.usingcollections.streamcontinuebreak.skippingvalues;

import java.util.List;

public class DropWhileDemo {
    public static void main(String[] args) {
        /*
        Bu bölüm Stream'lerde en çok karıştırılan konulardan biri: filter() ile dropWhile()
        ilk bakışta aynı işi yapıyor gibi görünür (ikisi de bir şarta göre eleman eler),
        ama davranışları tamamen farklıdır. Aşağıda aynı listeyi hem filter() hem de
        dropWhile() ile işleyip farkı göreceğiz.
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
        Uzunlukları:
        İsim  Uzunluk
        Brian   5
        Nate    4
        Neal    4
        Raju    4
        Sara    4
        Scott   5
         */

        /*
        filter(): "garaj kapısı" gibi davranır — her araba (eleman) geldiğinde kapı
        yeniden kontrol edilip açılır ya da kapanır. Yani şart HER eleman için
        bağımsız olarak tekrar değerlendirilir; sıra önemli değildir.

        Akış (şart: uzunluk <= 4):
        Brian(5) -> şart sağlanmıyor -> at
        Nate(4)  -> şart sağlanıyor  -> yazdır
        Neal(4)  -> şart sağlanıyor  -> yazdır
        Raju(4)  -> şart sağlanıyor  -> yazdır
        Sara(4)  -> şart sağlanıyor  -> yazdır
        Scott(5) -> şart sağlanmıyor -> at

        Sonuç: Nate, Neal, Raju, Sara (Scott en sonda olsa da uzunluğu 5 olduğu için elenir).
         */
        friends.stream()
                .filter(name -> name.length() <= 4)
                .forEach(System.out::println);

        /*
        dropWhile(): "tek yönlü turnike" gibi davranır — baştan başlayarak şart doğru
        olduğu SÜRECE elemanları atar. Şart yanlış olan ilk elemana rastladığı anda
        turnike kapanır (bir daha açılmaz) ve ondan sonraki TÜM elemanlar, şartı
        tekrar sağlasalar bile, sonuca dahil edilir. filter'ın aksine eleman eleman
        değil, listenin BAŞINDAN itibaren süreklilik esasına göre çalışır.

        Akış (şart: uzunluk > 4 olduğu sürece at):
        Brian(5) -> şart sağlanıyor  -> at (turnike hâlâ açık)
        Nate(4)  -> şart sağlanmıyor -> turnike kapanır, buradan itibaren her şey kalır
        Neal(4)  -> artık kontrol edilmez -> kalır
        Raju(4)  -> artık kontrol edilmez -> kalır
        Sara(4)  -> artık kontrol edilmez -> kalır
        Scott(5) -> artık kontrol edilmez -> kalır (uzunluğu 5 olmasına rağmen!)

        Sonuç: Nate, Neal, Raju, Sara, Scott.
        Scott'ın burada kalması filter ile aradaki temel farkı gösterir: dropWhile
        şartı bir daha denetlemez, sadece "ilk kırılma noktasını" arar.
         */
        friends.stream()
                .dropWhile(name -> name.length() > 4)
                .forEach(System.out::println);
    }
}
