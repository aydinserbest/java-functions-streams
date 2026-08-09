package org.practice.fpij.usingcollections.reducingtoasinglevalue;

import java.util.List;

public class SumOfLengths {
    public static void main(String[] args) {
        /*
        Konu: "Reducing a Collection to a Single Value"

        Şimdiye kadarki teknikler (filter, findFirst, map...) her elemanı
        BİRBİRİNDEN BAĞIMSIZ işliyordu -- bir eleman diğerini etkilemiyordu.
        Şimdi elemanları BİRBİRİYLE KARŞILAŞTIRIP, bir hesaplama durumunu
        (state) eleman eleman TAŞIYAN işlemlere bakıyoruz.

        En basit örnek: friends listesindeki tüm isimlerin TOPLAM karakter
        sayısını bulmak.
         */
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        System.out.println("Tüm isimlerdeki toplam karakter sayısı: " +
                friends.stream()
                        .mapToInt(name -> name.length())
                        .sum());
        // Beklenen: 5+4+4+4+4+5 = 26

        /*
        mapToInt: map()'in özelleşmiş bir versiyonu. Normal map(), bir
        Stream<T>'i başka bir Stream<R>'ye çevirir (nesneden nesneye).
        mapToInt ise her elemandan bir PRIMITIVE int üretir ve sonucu
        IntStream'e çevirir (mapToDouble/mapToLong de aynı mantıkla
        DoubleStream/LongStream üretir). Burada name.length() zaten bir
        int döndürdüğü için mapToInt kullanmak, her boyu bir Integer
        nesnesine sarmaktan (autoboxing) kaçınır.

        sum(): IntStream üzerinde tanımlı, akıştaki TÜM int değerlerini
        toplayan hazır bir terminal işlem.

        Burada gizli kalan asıl fikir MapReduce deseni: map() elemanları
        DAĞITAN (spread) adım, sum() ise bunları TEK bir değere İNDİRGEYEN
        (reduce) adımın özel bir hâli. Aslında JDK içinde sum()'ın
        implementasyonu da reduce() kullanır. sum() yerine max(), min(),
        sorted(), average() gibi başka terminal/ara işlemler de aynı
        "boy" verisi üzerinde çalıştırılabilirdi -- hangisini seçeceğimiz
        neyi bulmak istediğimize bağlı.

        Bir sonraki adımda, sum() gibi HAZIR bir metot olmadığında (örneğin
        "en uzun ismi bul" gibi bir ihtiyaçta) reduce()'ın DAHA GENEL hâlini
        nasıl kullanacağımızı görüyoruz: bkz. ReduceToLongestName.
         */
    }
}
