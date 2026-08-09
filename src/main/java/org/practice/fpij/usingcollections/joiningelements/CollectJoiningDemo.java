package org.practice.fpij.usingcollections.joiningelements;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class CollectJoiningDemo {
    public static void main(String[] args) {
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        /*
        StringJoinDemo'daki String.join, elemanları DÖNÜŞTÜRMEDEN olduğu
        gibi birleştiriyordu. Ama burada önce her ismi BÜYÜK HARFE çevirip
        (map), SONRA virgülle birleştirmek (join) istiyoruz. Bunun için
        Stream'in collect() metodunu kullanıyoruz.
         */
        System.out.println(
                friends.stream()
                        .map(String::toUpperCase)
                        .collect(joining(", ")));
        // Çıktı: BRIAN, NATE, NEAL, RAJU, SARA, SCOTT

        /*
        Burada neler oluyor?

        - map(String::toUpperCase): her ismi büyük harfli hâline
          DÖNÜŞTÜREN bir ara işlem (henüz hiçbir şey birleştirmiyor).
        - collect(joining(", ")): DÖNÜŞTÜRÜLMÜŞ elemanları alıp TEK bir
          String'e indirgeyen (reduce eden) terminal işlem.

        collect() aslında reduce()'ın ÖZEL bir biçimidir: elemanları
        gezip biriktirmesi gerektiğini bilir, ama BİRİKTİRME İŞİNİN
        NASIL yapılacağını (hedefin bir String mi, bir List mi, bir Map
        mi olacağını) kendisi bilmez -- bu sorumluluğu, kendisine
        verilen bir COLLECTOR nesnesine devreder. Burada Collectors
        sınıfının static joining(ayrac) metodu, "elemanları String'e,
        aralarına şu ayracı koyarak biriktir" davranışını temsil eden bir
        Collector üretiyor.

        Neden reduce() değil de collect(joining(...)) kullanıyoruz?
        Teorik olarak reduce() ile de String birleştirebilirdik, ama:

        - Ayraç mantığını (ilk elemanda ayraç KOYMA, aradakilerde koy)
          elle yönetmemiz gerekirdi -- tam olarak PrintListImperative'te
          çektiğimiz sıkıntının aynısı.
        - String birleştirmede reduce() her adımda YENİ bir String nesnesi
          oluşturabilir (String immutable olduğu için), bu da collect()'in
          içeride StringBuilder/StringJoiner gibi DEĞİŞTİRİLEBİLİR bir
          ara yapı kullanmasına göre daha VERİMSİZ olabilir.

        joining()'in üç farklı overload'u vardır:

        - joining()                      -> araya hiçbir şey koymadan birleştirir
        - joining(delimiter)             -> sadece ayraç koyar (yukarıdaki örnek)
        - joining(delimiter, prefix, suffix) -> ayrıca başa/sona ekleme yapar
         */
        System.out.println(
                friends.stream()
                        .collect(joining(", ", "[", "]")));
        // Çıktı: [Brian, Nate, Neal, Raju, Sara, Scott]

        /*
        Bir sonraki class'ta (StringJoinerDemo), joining(...)'in perde
        arkasında kullandığı StringJoiner sınıfını DOĞRUDAN ele alıp
        prefix/suffix/infix üzerinde daha ince kontrolü gösteriyoruz.
         */
    }
}
