package org.practice.fpij.usingcollections.pickingelement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PickElementElegant {

    public static void pickName(final List<String> names, final String startingLetter) {
        final Optional<String> foundName = names.stream()
                .filter(name -> name.startsWith(startingLetter))
                .findFirst();

        System.out.println(String.format("%s ile başlayan bir isim: %s",
                startingLetter, foundName.orElse("İsim bulunamadı")));
    }

    public static void main(String[] args) {
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        pickName(friends, "N");
        pickName(friends, "Z");
        /*
        Çıktı:
        N ile başlayan bir isim: Nate
        Z ile başlayan bir isim: İsim bulunamadı
         */

        /*
        PickElementImperative'e göre ne değişti?

        - filter(): koşula uyan elemanları eleyen ara işlemi kurar (lazy,
          henüz hiçbir şey çalıştırmaz).
        - findFirst(): stream'de kalan İLK elemanı alır ve akışı HEMEN
          SONLANDIRIR -- kalan elemanlara hiç bakmaz (short-circuit terminal
          işlem). PickElementImperative'teki elle yazılmış "break" ihtiyacını
          findFirst() kendi içinde, bizim için hallediyor.
        - Sonuç artık düz bir String + null kombinasyonu değil, bir
          Optional<String>. Optional, "bir değer olabilir de olmayabilir de"
          durumunu class seviyesinde temsil eden bir kap (container) --
          kitabın deyimiyle Java'nın "null deodorantı". foundName == null
          diye elle kontrol etmek yerine, Optional'ın kendi metotlarıyla
          (orElse, isPresent, ifPresent, orElseThrow) güvenli şekilde
          davranıyoruz; unutma riski derleyici seviyesinde azalıyor.
         */

        // ------------------------------------------------------------
        // Optional ile çalışmanın farklı yolları
        // ------------------------------------------------------------
        final Optional<String> maybeName = friends.stream()
                .filter(name -> name.startsWith("N"))
                .findFirst();

        // 1) orElse: değer yoksa VARSAYILAN bir değer kullan
        System.out.println("\n1) orElse:");
        System.out.println(maybeName.orElse("Bulunamadı"));

        // 2) isPresent + get: önce "değer var mı?" diye sor, sonra al
        System.out.println("\n2) isPresent + get:");
        if (maybeName.isPresent()) {
            System.out.println(maybeName.get());
        }
        /*
        get() DOĞRUDAN, isPresent() kontrolü yapmadan çağrılırsa ve Optional
        boşsa NoSuchElementException fırlatır -- yani get()'i yanlış
        kullanmak, unutulan bir null-check'in modern bir versiyonuna
        dönüşebilir. Bu yüzden JDK 10 ile orElseThrow() TERCİH EDİLEN
        alternatif olarak eklendi (niyeti daha açık: "değer yoksa patla").
         */

        // 3) orElseThrow(): değer yoksa açıkça bir exception fırlat (JDK 10+)
        System.out.println("\n3) orElseThrow:");
        try {
            String name = friends.stream()
                    .filter(n -> n.startsWith("Z"))
                    .findFirst()
                    .orElseThrow();
            System.out.println(name);
        } catch (NoSuchElementException e) {
            System.out.println("orElseThrow() fırlattı: " + e.getClass().getSimpleName());
        }

        // 4) ifPresent(): değer VARSA bir işlem çalıştır, yoksa hiçbir şey yapma
        System.out.println("\n4) ifPresent:");
        maybeName.ifPresent(name -> System.out.println("Merhaba " + name));
        /*
        ifPresent(Consumer<T>), if/else yazmadan "değer varsa şunu yap"
        demenin en okunur yolu. Değer yoksa lambda hiç çalışmaz, hiçbir
        exception da fırlamaz -- sessizce hiçbir şey yapılmaz.
         */

        /*
        Performans notu: filter().findFirst() zincirinin "önce TÜM
        elemanları filtrele, sonra ilkini al" şeklinde çalıştığını
        düşünmek YANLIŞ olur. Stream'ler LAZY olduğu için findFirst(),
        koşulu sağlayan İLK elemanı bulur bulmaz durur -- listenin geri
        kalanına hiç dokunmaz. Yani imperative "break" ile AYNI verimlilikte
        çalışır, sadece bu işi bizim yerimize Stream API üstleniyor.
         */
    }
}
