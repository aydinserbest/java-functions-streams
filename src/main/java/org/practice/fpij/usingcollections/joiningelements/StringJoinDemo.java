package org.practice.fpij.usingcollections.joiningelements;

import java.util.List;

public class StringJoinDemo {
    public static void main(String[] args) {
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        /*
        PrintListImperative'teki tüm o indeks aritmetiği, tek bir satıra
        indirgeniyor: String.join(ayrac, koleksiyon).
         */
        System.out.println(String.join(", ", friends));
        // Çıktı: Brian, Nate, Neal, Raju, Sara, Scott

        /*
        String.join(CharSequence delimiter, Iterable<? extends CharSequence> elements)
        imzasına sahiptir. İçeride, StringJoiner sınıfını kullanarak ikinci
        argümandaki (burada varargs değil, bir List/Iterable) değerleri,
        ilk argümanla (", ") ayırarak TEK bir String'e birleştirir. Ne
        elle son eleman kontrolü, ne indeks hesaplaması, ne de fazladan
        virgül sorunu -- kütüphane BİZİM YERİMİZE hallediyor.

        String.join'in bir de varargs (değişken sayıda argüman) alan
        overload'u var, doğrudan liste yerine tek tek String vererek de
        kullanılabilir:
         */
        System.out.println(String.join(" - ", "2026", "08", "09"));
        // Çıktı: 2026 - 08 - 09

        /*
        Virgülle sınırlı değiliz -- herhangi bir ayraç kullanılabilir.
        Örneğin bir dosya/classpath biçimi üretmek isteseydik:
         */
        List<String> paths = List.of("/usr/local/bin", "/opt/app/lib", "/home/user/tools");
        System.out.println(String.join(":", paths));
        // Çıktı: /usr/local/bin:/opt/app/lib:/home/user/tools

        /*
        Ama String.join, elemanları OLDUĞU GİBİ (dönüştürmeden) birleştirir.
        Elemanları önce dönüştürüp (örn. büyük harfe çevirip) SONRA
        birleştirmek istersek, String.join yeterli olmaz -- Stream
        tabanlı collect(joining(...)) devreye girer. Bkz. CollectJoiningDemo.
         */
    }
}
