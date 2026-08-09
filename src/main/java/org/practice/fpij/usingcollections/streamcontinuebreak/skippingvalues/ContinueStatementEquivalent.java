package org.practice.fpij.usingcollections.streamcontinuebreak.skippingvalues;

import java.util.List;

public class ContinueStatementEquivalent {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        /*
        Kitaptaki cümle şunu söylüyor:
        "Belirli sayıda elemanı atlamak ya da bir şart sağlanana kadar atlamak
        istediğimizde, geleneksel for döngüsünde if + continue kullanırız.
        Fonksiyonel stilde ise if/continue kullanmayız; iterasyonu skip() veya
        dropWhile() fonksiyonlarıyla kontrol ederiz."

        Aşağıda bu iki senaryoyu -- (1) sabit SAYIDA eleman atlama ve
        (2) bir ŞART sağlandığı sürece atlama -- hem geleneksel if/continue
        stiliyle hem de fonksiyonel skip()/dropWhile() stiliyle yazıyoruz.
         */

        // ------------------------------------------------------------
        // 1) Belirli SAYIDA elemanı atla (ilk 5 elemanı atla)
        // ------------------------------------------------------------

        System.out.println("Geleneksel (continue + if, sayaçla):");
        int index = -1;
        for (int number : numbers) {
            index++;
            if (index < 5) {
                continue; // ilk 5 elemanı atla, döngü gövdesinin geri kalanını çalıştırma
            }
            System.out.println(number);
        }
        /*
        Burada "kaç eleman atlanacağını" (index < 5) BİZ hesaplıyoruz ve
        continue ile döngü gövdesinin geri kalanını elle atlıyoruz. Sayacı
        tutmak ve ne zaman durdurulacağına karar vermek bizim sorumluluğumuz.
         */

        System.out.println("\nFonksiyonel (skip):");
        numbers.stream()
                .skip(5) // ilk 5 elemanı atla; sayacı biz tutmuyoruz, Stream tutuyor
                .forEach(System.out::println);

        // ------------------------------------------------------------
        // 2) Bir ŞART sağlandığı SÜRECE atla (değer <= 5 olduğu sürece atla)
        // ------------------------------------------------------------

        System.out.println("\nGeleneksel (continue + if, şartla):");
        boolean stillDropping = true;
        for (int number : numbers) {
            if (stillDropping && number <= 5) {
                continue; // şart hâlâ doğru, bu elemanı atla
            }
            stillDropping = false; // şart bir kez bozulunca bir daha kontrol edilmemeli
            System.out.println(number);
        }
        /*
        Burada "şart hâlâ geçerli mi" bilgisini (stillDropping bayrağı) BİZ elde
        tutmak zorundayız. Şart bir kez false olsa bile continue mantığının
        yanlışlıkla tekrar devreye girmemesi için bu bayrağı manuel olarak
        kapatıyoruz. Bu, dropWhile'ın "tek yönlü turnike" davranışını
        (bkz. DropWhileDemo) elle taklit etmek demektir; hataya açık ve
        kalabalık bir yol.
         */

        System.out.println("\nFonksiyonel (dropWhile):");
        numbers.stream()
                .dropWhile(number -> number <= 5) // şart doğru olduğu sürece at, sonra hepsini bırak
                .forEach(System.out::println);

        /*
        Özet:
        - if/continue ile bizim elle YAZDIĞIMIZ "sayaç" veya "bayrak" mantığını,
          fonksiyonel tarafta skip()/dropWhile() bizim yerimize yönetir.
        - Traditional: "Nasıl atlayacağımı ben kontrol ederim." (index, flag, continue)
        - Functional : "Neyi atlamak istediğimi söylerim." (skip(n) / dropWhile(predicate));
          iterasyonun kendisini Stream yönetir.
         */
    }
}
