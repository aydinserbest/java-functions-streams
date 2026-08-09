package org.practice.fpij.usingcollections.streamcontinuebreak.skippingvalues;

import java.util.Arrays;

public class SkipAndFilter {
    public static void main(String[] args) {
        //1. example:
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        // Traditional way
        System.out.println("Traditional:");
        for (int number : numbers) {
            if (number % 2 == 0) {
                System.out.println(number);
            }
        }

        // Stream way
        System.out.println("\nStream:");
        Arrays.stream(numbers)
                .filter(n -> n % 2 == 0)
                .forEach(System.out::println);

        //2. example
    /*
    continue'ın asıl kullanım amacı
continue sadece "ilk N elemanı atla" için kullanılmaz.
Mesela
Negatif sayıları işleme.
     */
        int[] nums = {5, -2, 8, -1, 10};
        for (int num : nums) {
            if (num < 0 ) {
                continue;
            }
            System.out.println(num);
        }
        // Stream way
        Arrays.stream(nums)
                .filter(n -> n >= 0)
                .forEach(System.out::println);
    }
    /*
    Burada
continue
yerine
filter()
geldi.
     */
    /*
    Traditional (Imperative) style'da
    yani aslında bütün elemanlara tek tek uğruyorsun.
    Hepsine bakılıyor.
Sonra
"Bunu işleyeyim mi?"

kararı veriliyor.
Stream (Functional) style'da
Burada düşünce tamamen değişiyor.
continue
yok.
Yerine
filter

↓

forEach
var.
Çok önemli nokta
Aslında Stream'in içinde de bütün elemanlar tek tek okunuyor.
Yani Java yine
5
-2
8
-1
10
elemanlarını sırayla inceliyor.
Ama bu iş senin yazdığın for döngüsüyle yapılmıyor.
Bu işi Stream API yapıyor.
Sen sadece diyorsun ki
"Şu şartı sağlayanları bana bırak."

Yani iteration hâlâ var, fakat kontrolü sende değil, Stream'de.
Sen artık karar vermiyorsun.
Sadece işlem yapıyorsun (forEach).

O yüzden şu cümle çok doğru
Traditional:
Iterate → Decide → Continue → Process

Functional (Stream):
Select → Process

Bir teknik düzeltme
Sadece şunu söylemek daha doğru olur:
"Önce istediğin elemanları seç."

Bu ifade kavramsal olarak doğru olsa da teknik olarak Stream'de aslında elemanların hepsi yine sırayla değerlendirilir (lazy evaluation ile). filter() tüm diziyi önceden kopyalayıp yeni bir liste oluşturmaz. Her eleman akış içinde sırayla gelir, filtrelenir ve uygunsa bir sonraki operasyona (forEach) iletilir.
Yani teknik akış şöyledir:

5  ----> filter ----> geçti ----> forEach

-2 ----> filter ----> elendi

8  ----> filter ----> geçti ----> forEach

-1 ----> filter ----> elendi

10 ----> filter ----> geçti ----> forEach
Bu nedenle asıl fark elemanların okunup okunmaması değil, kontrolün kimde olduğudur:
Traditional: Sen döngüyü yazarsın, if ve continue ile akışı yönetirsin.
Stream (Functional): Sen sadece dönüşüm kurallarını (filter, map, skip...) tanımlarsın; döngüyü ve akışı Stream API yönetir.
İşte Stream'lerin temel felsefesi de budur.
     */
    /*
    skip() ile filter() arasındaki fark
Bunlar çok karıştırılır.
skip()
Pozisyona göre atlar.
1 2 3 4 5 6 7

skip(3)

↓

4 5 6 7
Elemanın değerine bakmaz.
filter()
Şarta göre atlar.
1 2 3 4 5 6 7

filter(x -> x > 3)

↓

4 5 6 7
Burada pozisyon değil değer önemlidir.
     */
}
