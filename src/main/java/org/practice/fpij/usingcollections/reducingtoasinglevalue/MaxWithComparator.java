package org.practice.fpij.usingcollections.reducingtoasinglevalue;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MaxWithComparator {
    public static void main(String[] args) {
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        /*
        ReduceToLongestName'de reduce() ile elle yazdığımız "hangisi daha
        uzun" karşılaştırmasını, "en büyüğü/en uzunu bulmak" ÖZEL bir
        ihtiyaç olduğu için Stream'in hazır max() metoduyla da
        yapabiliriz. max() bir Comparator ister -- "büyük" ne demek,
        onu tarif etmemiz gerekir.
         */
        final Optional<String> aLongName =
                friends.stream()
                        .max(Comparator.comparing(String::length));

        aLongName.ifPresent(name ->
                System.out.println(String.format("En uzun isimlerden biri: %s", name)));
        // Beklenen: Brian (ReduceToLongestName ile AYNI sonuç)

        /*
        Comparator.comparing(String::length): her String'i, uzunluğuna
        (Integer) bakarak karşılaştıran bir Comparator üretir -- daha önce
        ComparatorSortingBasics'te sorted() ile kullandığımız
        Comparator.comparing(...) ile birebir aynı yapı, burada max()'a
        veriliyor.

        reduce() ile max() arasındaki fark NE?

        - reduce((name1, name2) -> ...) : GENEL AMAÇLI bir indirgeme.
          "iki elemandan birini nasıl seçeceğimi" TAMAMEN biz tarif
          ediyoruz (uzunluk, alfabetik sıra, herhangi bir kural olabilir).
        - max(Comparator) : ÖZEL AMAÇLI, niyeti daha AÇIK bir metot.
          Sadece "hangisi büyük" sorusunu Comparator'a soruyor; kodu
          okuyan kişi "en büyüğü arıyoruz" niyetini reduce()'a göre çok
          daha hızlı anlıyor.

        İkisi de aynı işi (max() dahili olarak reduce() kullanılarak
        yazılmıştır) yapıyor, ama max() + Comparator kombinasyonu, "en
        büyük/en küçük eleman" ihtiyacı için reduce()'a göre daha KISA ve
        daha OKUNAKLI. Benzer şekilde min(Comparator) ile en KISA ismi
        bulabiliriz.
         */
        final Optional<String> aShortName =
                friends.stream()
                        .min(Comparator.comparing(String::length));

        aShortName.ifPresent(name ->
                System.out.println(String.format("En kısa isimlerden biri: %s", name)));
    }
}
