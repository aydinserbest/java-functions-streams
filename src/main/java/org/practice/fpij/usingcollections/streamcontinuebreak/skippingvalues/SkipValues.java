package org.practice.fpij.usingcollections.streamcontinuebreak.skippingvalues;

import java.util.Arrays;

public class SkipValues {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        /*
        Amacımız:
İlk 5 elemanı atla, kalanları yazdır.
Eski Java'da bunu kendimiz yönetiyoruz.
         */
        for (int i = 5; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }
        /*
        Burada programcı olarak biz karar veriyoruz.
        nereden başlayacağım?
        kaç kere döneceğim?
        index kaç olacak?
        Yani bütün iteration (dolaşma) kontrolü bize ait.
         */
        /*
        Aynısını CONTINUE ile de yazabilirdik
Mesela illa 0'dan başlamak istiyorsak:
         */
        for (int i = 0; i < numbers.length; i++) {
            if (i < 5) {
                continue;
            }
            System.out.println(numbers[i]);
        }
        /*
        Akış şöyle:
        i = 0
continue
↓

i = 1
continue
↓

i = 2
continue
↓

i = 3
continue
↓

i = 4
continue
↓

i = 5
print 6
↓

i = 6
print 7
...
continue ne yaptı?
"Bu elemanı işleme, sonraki iterasyona geç."

Yani
1
2
3
4
5
görülüyor ama işlenmiyor.
         */

        /*
        Stream (Functional Style)
Aynı işi Stream'de şöyle yazıyoruz.
         */
        Arrays.stream(numbers)
                .skip(5)
                .forEach(System.out::println);
    }
    /*
    Burada hiçbir yerde
i
if
continue
yok.
Biz sadece diyoruz ki
İlk 5 taneyi atla.

Gerisini Stream hallediyor.
Yani nasıl döneceğini değil, ne istediğimizi söylüyoruz.
Bu yüzden buna DECLARATIVE PROGRAMMING deniyor.
     */
    /*
    Peki ebook neden
In the functional style, we don't use if or continue

diyor?
Çünkü Functional Programming'de mantık şöyle:
Eski yaklaşım:
Bütün elemanları dolaş.

↓

İşleyip işlemeyeceğine karar ver.

↓

continue ile bazılarını atla.
Yani önce dolaşıyorsun, sonra karar veriyorsun.
Functional yaklaşım ise

Önce istediğin elemanları seç.

↓

Sonra sadece onlar üzerinde çalış.
Bu çok önemli fark.
     */
}
