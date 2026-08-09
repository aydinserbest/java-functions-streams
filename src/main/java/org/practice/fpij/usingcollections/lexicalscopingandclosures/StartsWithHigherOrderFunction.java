package org.practice.fpij.usingcollections.lexicalscopingandclosures;

import java.util.List;
import java.util.function.Predicate;

public class StartsWithHigherOrderFunction {

    /*
    checkIfStartsWith: bir String (letter) alıyor ve bir Predicate<String>
    DÖNDÜRÜYOR. Şimdiye kadar gördüğümüz higher-order fonksiyonlar (filter,
    map...) fonksiyonu PARAMETRE olarak alıyordu; bu ise fonksiyonu SONUÇ
    olarak döndürüyor — bu da higher-order function'ın diğer yüzü.
     */
    public static Predicate<String> checkIfStartsWith(final String letter) {
        return name -> name.startsWith(letter);
    }

    public static void main(String[] args) {
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        /*
        checkIfStartsWith("N") çağrısı bize bir Predicate<String> döndürüyor
        ve bunu doğrudan filter()'a veriyoruz. İki çağrı da AYNI kodu
        (checkIfStartsWith metodunu) kullanıyor, sadece verdiğimiz harf
        farklı — DuplicatePredicateProblem'daki tekrar burada YOK.
         */
        final long countFriendsStartN =
                friends.stream()
                        .filter(checkIfStartsWith("N"))
                        .count();

        final long countFriendsStartB =
                friends.stream()
                        .filter(checkIfStartsWith("B"))
                        .count();

        System.out.println("N ile başlayan: " + countFriendsStartN);
        System.out.println("B ile başlayan: " + countFriendsStartB);

        /*
        Peki checkIfStartsWith içindeki

            return name -> name.startsWith(letter);

        satırında "letter" kime ait? Bu lambda'nın TEK parametresi "name" —
        "letter" bu lambda'nın kendi parametre listesinde yok. Java, bu
        durumda lambda'nın TANIMLANDIĞI çevreleyen kapsama (checkIfStartsWith
        metodunun parametre kapsamına) bakıyor ve orada "letter"ı buluyor.
        Bir fonksiyonun, kendi tanımlandığı kapsamdaki değişkenlere bu
        şekilde erişebilmesine LEXICAL SCOPING (sözcüksel kapsam) denir.

        Lambda, tanımlandığı kapsamı "üzerine kapattığı" (closes over) için
        buna aynı zamanda CLOSURE (kapanış) da denir. checkIfStartsWith("N")
        çağrıldığında dönen Predicate, "letter = N" bilgisini YANINDA
        TAŞIYARAK filter()'a gidiyor; checkIfStartsWith metodu çoktan geri
        dönmüş olsa bile, döndürdüğü lambda "letter" değerini unutmuyor.
        Bu yüzden aynı iki filter() çağrısı farklı sonuçlar (N vs B) verse
        de kodun kendisi tekrar etmiyor; tekrar eden tek şey bir DEĞER
        (harf), MANTIK değil.
         */

        // ------------------------------------------------------------
        // Lexical scoping'in kısıtı: final ya da effectively final
        // ------------------------------------------------------------
        /*
        Lambda'lar tanımlandıkları anda çalıştırılmayabilir; sonradan (hatta
        başka bir thread'de) çalıştırılabilirler. Bu yüzden Java, lambda
        içinden erişilen yerel (local) değişkenlerin, tanımlandıktan SONRA
        bir daha DEĞİŞTİRİLMEMESİNİ (final ya da "effectively final" olmasını)
        şart koşar — aksi halde lambda çalıştığı anda değişkenin hangi
        değere sahip olduğu belirsizleşir (race condition riski).

        checkIfStartsWith'teki "letter" parametresi zaten final işaretli.
        final işareti KOYMASAK bile, letter metot içinde bir daha
        DEĞİŞTİRİLMEDİĞİ için yine de "effectively final" sayılırdı ve
        derlenirdi. Ama aşağıdaki gibi bir şey yapmaya kalksak:

            public static Predicate<String> broken(String letter) {
                letter = letter.toUpperCase(); // <- letter'ı DEĞİŞTİRDİK
                return name -> name.startsWith(letter);
            }

        letter artık effectively final DEĞİL (metot içinde yeniden atama
        yapıldı) ve bu, lambda içinde letter'a erişmeyi DERLEME HATASI
        yapardı. (İsterseniz yukarıdaki satırları class'a ekleyip deneyin.)
         */

        /*
        Performans notu: "name -> name.startsWith(letter)" gibi dış bir
        değişkeni (letter) YAKALAYAN (capture eden) lambda'lar, hiçbir dış
        değişken yakalamayan (stateless) lambda'lara göre biraz daha
        maliyetlidir — stateless bir lambda derleyici tarafından tek bir
        sabit (runtime constant) gibi ele alınabilirken, durum yakalayan
        (stateful/capturing) bir lambda her çağrıldığında o durumu taşıyan
        yeni bir örnek oluşturulmasını gerektirebilir.
         */
    }
}
