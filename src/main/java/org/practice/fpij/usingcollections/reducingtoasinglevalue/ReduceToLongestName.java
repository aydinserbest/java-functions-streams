package org.practice.fpij.usingcollections.reducingtoasinglevalue;

import java.util.List;
import java.util.Optional;

public class ReduceToLongestName {
    public static void main(String[] args) {
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        /*
        Amaç: listedeki EN UZUN ismi bulmak. Birden fazla isim aynı (en
        uzun) boya sahipse, İLK karşılaşılanı almak istiyoruz.

        Naif yol: önce en uzun boyu bul, sonra o boydaki ilk elemanı
        tekrar ara -- ama bu, listeyi İKİ KEZ gezmek demek, verimsiz.
        reduce() bunu TEK GEÇİŞTE yapmamızı sağlıyor.
         */
        final Optional<String> aLongName =
                friends.stream()
                        .reduce((name1, name2) ->
                                name1.length() >= name2.length() ? name1 : name2);

        aLongName.ifPresent(name ->
                System.out.println(String.format("En uzun isimlerden biri: %s", name)));
        // Beklenen: Brian (Brian ve Scott ikisi de 5 harfli, İLK karşılaşılan -Brian- kazanıyor)

        /*
        reduce() nasıl çalıştı?

        Verdiğimiz lambda iki parametre alıyor (name1, name2) ve ikisinden
        birini (uzun olanı) geri döndürüyor. reduce() metodu, listeyi
        gezerken bu lambda'yı SIRAYLA şöyle çağırıyor:

        1. çağrı: name1 = "Brian", name2 = "Nate"   -> sonuç: "Brian" (5>=4)
        2. çağrı: name1 = "Brian", name2 = "Neal"   -> sonuç: "Brian" (5>=4)
        3. çağrı: name1 = "Brian", name2 = "Raju"   -> sonuç: "Brian" (5>=4)
        4. çağrı: name1 = "Brian", name2 = "Sara"   -> sonuç: "Brian" (5>=4)
        5. çağrı: name1 = "Brian", name2 = "Scott"  -> sonuç: "Brian" (5>=5, >= olduğu için İLK GELEN kazanıyor)

        Her çağrıda name1, BİR ÖNCEKİ çağrının SONUCUdur; name2 ise
        listedeki bir SONRAKİ elemandır. Yani reduce(), bir "TAŞINAN
        SONUÇ" (accumulator) fikriyle çalışıyor -- filter/map'in aksine,
        elemanlar birbirinden bağımsız değil, sonuç eleman eleman
        BİRİKTİRİLİYOR (carry-forward).

        Bu lambda'nın imzası (T, T) -> T, yani JDK'daki BinaryOperator<T>
        fonksiyonel arayüzüne uyuyor -- reduce() metodunun beklediği
        parametre tipi de tam olarak bu. reduce()'ın kendisi "en uzun ismi
        nasıl bulacağını" BİLMİYOR; bu bilgi (karşılaştırma mantığı) lambda
        ifadesine ayrılmış -- Strategy tasarım deseninin hafif bir
        uygulaması gibi düşünülebilir.

        Neden Optional<String> dönüyor?

        - Liste BOŞ olabilir -- o zaman "en uzun isim" diye bir şey yok,
          reduce() elinde hiçbir eleman bulamaz ve Optional.empty() döner.
        - Liste TEK elemanlıysa, reduce() lambda'yı HİÇ ÇAĞIRMADAN o tek
          elemanı doğrudan döndürür (karşılaştıracak ikinci bir eleman
          olmadığı için).
        - Liste birden fazla elemanlıysa yukarıdaki gibi eleman eleman
          karşılaştırma yapılır.

        Bu üç durumu TEK bir dönüş tipiyle (null döndürüp elle kontrol
        etmek yerine) güvenli şekilde ifade edebilmek için sonuç Optional
        olarak sarılıyor -- PickElementElegant'ta findFirst()'ün Optional
        döndürmesiyle birebir aynı gerekçe.
         */
    }
}
