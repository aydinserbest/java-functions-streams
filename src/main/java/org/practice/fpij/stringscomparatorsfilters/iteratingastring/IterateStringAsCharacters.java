package org.practice.fpij.stringscomparatorsfilters.iteratingastring;

public class IterateStringAsCharacters {

    private static void printChar(final int aChar) {
        System.out.println((char) aChar);
    }

    public static void main(String[] args) {
        final String str = "w00t";
        /*
        Bu String'in karakterlerini tek tek yazdırmak istiyoruz.
Klasik yöntem:
         */
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
        }
        /*
        Burada Java'ya bütün işi biz tarif ediyoruz:
i oluştur.
0'dan başla.
String uzunluğuna kadar git.
i'yi artır.
charAt(i) ile karakteri bul.
sonra yazdır.

Buna imperative style deniyor.
Yani:
NASIL yapılacağını biz anlatıyoruz.
         */
        /*
        Functional style neyi değiştiriyor?
Modern yaklaşımda şöyle düşüneceğiz:
"String'in karakterlerini bana ver ve her biri için şu işlemi yap."
str.chars()
forEach:
Her eleman için bunu yap.

demek.
ch -> System.out.println(ch)
şu demek:
Her ch geldiğinde onu yazdır.
Ama bir problem var.
Neden w yerine 119 çıktı?
final String str = "w00t";

str.chars()
   .forEach(ch -> System.out.println(ch));
   çıktı beklediğimizin aksine:
   119
48
48
116
Çünkü:
str.chars()
bize doğrudan:
Stream<Character>
vermiyor.
Bize:
IntStream
veriyor.
Yani karakterlerin sayısal değerleri geliyor.
w  → 119
0  → 48
0  → 48
t  → 116
Burada en önemli bilgi:
String.chars() → IntStream döndürür.

Bunu özellikle aklında tut.

filter   → bazılarını seç
map      → dönüştür
forEach  → her biriyle bir şey yap
reduce   → hepsini bir sonuçta birleştir
         */

        System.out.println("1) Yardımcı metotla (printChar) int'i char'a çevirip yazdırma:");
        str.chars()
                .forEach(IterateStringAsCharacters::printChar);
        /*
        IterateStringAsCharacters::printChar de bir method reference, ama
        IterateStringNumericOutput'taki System.out::println'den FARKLI bir
        tür: burada solda bir SINIF adı var ve printChar bir STATIC metot.
        Static olduğu için derleyici parametreyi metoda ARGÜMAN olarak
        geçirir:

            printChar(parametre);

        Çıktı artık harfler:
        w
        0
        0
        t
         */

        System.out.println("\n2) mapToObj ile en baştan Character akışına dönme:");
        str.chars()
                .mapToObj(ch -> Character.valueOf((char) ch))
                .forEach(System.out::println);
        /*
        chars() bir IntStream döndürdüğü için üzerinde map() çağırırsak
        sonuç da yine bir IntStream olurdu -- int'ten kurtulamayız. Stream
        elemanlarını NESNEYE (burada Character'a) çevirmek istediğimizde
        map() DEĞİL, mapToObj() kullanılır: ilkel (primitive) bir stream'i
        nesne (object) stream'ine çevirmenin standart yolu budur. Bu sayede
        akışın geri kalanında (filter, sort, collect, ...) artık gerçek
        Character elemanlarıyla çalışabiliriz.
         */
    }
}
