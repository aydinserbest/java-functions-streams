package org.practice.fpij.stringscomparatorsfilters.iteratingastring;

import java.util.stream.IntStream;

public class IterateString {
    public static void main(String[] args) {
        String str = "w00t";
    /*
    Bu String'in karakterlerini tek tek yazdırmak istiyoruz.
Klasik yöntem:
     */
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
        }

    /*
    çıktı:
    w
    0
    0
    t
     */
        //Functional style:
        str.chars()
                .forEach(System.out::println); //System.out.println(ch)
        /*
        forEach:
Her eleman için bunu yap demek.

Ama bir problem var.
        çıktı:
        119
        48
        48
        116
         */
        /*
        .chars() bize doğrudan Stream<Character> vermiyor.
        Bize IntStream veriyor.
         karakterlerin sayısal değerleri geliyor.
         w  → 119
         String.chars() → IntStream döndürür.

         */
        IntStream chars = str.chars();
    /*
    method reference sadece kodu kısalttı.
Veriyi değiştirmedi.
Hâlâ: IntStream var,

Sayıyı karaktere çevirelim
bunun için bir metod yazabiliriz-klasik yöntemle,
ve o methodu, method reference ile çağırabiliriz.
     */
        str.chars()
                .forEach(IterateString::printChar);
        /*
        :: nedir?
Şu:
System.out::println
method reference.
Bunu Türkçe zihninde şöyle okuyabilirsin:
"System.out nesnesinin println metodunu kullan."

içinde bulunduğumuz IterateString classında printChar metodu oluşturduk ve
method refrence ile kullandık -> .forEach(IterateString::printChar);
         */
        /*
        IterateString::printChar
kabaca:
ch -> IterateString.printChar(ch)
demek.
Yani:
.forEach(ch -> IterateString.printChar(ch));
yerine:
.forEach(IterateString::printChar);
yazıyoruz.
Tekrar aynı kural:
x -> method(x)
çoğu zaman:
Class::method
şeklinde sadeleşebilir.
         */

        /*
        Eğer baştan Character istiyorsak
         */
        str.chars()
                .mapToObj(ch -> (char) ch)//dönüş tipi IntStream değil, Stream<Character>
                .forEach(System.out::println);

    }
    private static void printChar(int aChar) {
        System.out.println((char) aChar);
    }
    /*
    Buradaki:
(char) aChar
bir cast.
Yani:
Bu sayıyı karakter olarak yorumla.

Örneğin:
(char) 119
sonucu:
w
olur.

Şimdi:
str.chars()
   .forEach(IterateString::printChar);
yazabiliriz.
     */
}
