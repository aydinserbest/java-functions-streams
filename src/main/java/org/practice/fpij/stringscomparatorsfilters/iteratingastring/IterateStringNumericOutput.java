package org.practice.fpij.stringscomparatorsfilters.iteratingastring;

public class IterateStringNumericOutput {

    public static void main(String[] args) {
        final String str = "w00t";

        System.out.println("1) Lambda expression ile forEach:");
        str.chars()
                .forEach(ch -> System.out.println(ch));
        /*
        Çıktı harfler değil, SAYILAR:
        119
        48
        48
        116

        Çünkü String'in chars() metodu (CharSequence arayüzünden gelir)
        bir Stream<Character> DEĞİL, bir IntStream döner -- her karakterin
        int karşılığını (code point) verir. Bu, ilk bakışta şaşırtıcı ama
        bilinçli bir tasarım: char, Java'da zaten 16 bitlik bir sayısal
        tiptir; IntStream kullanmak Character kutulama (boxing) maliyetinden
        kaçınır.
         */

        System.out.println("\n2) Method reference ile forEach (System.out::println):");
        str.chars()
                .forEach(System.out::println);
        /*
        Aynı çıktı, ama parametre yönlendirmesi artık derleyiciye bırakıldı.
        Burada method reference'ın SOLUNDA bir sınıf adı değil, bir İFADE
        (System.out -- bir PrintStream instance'ı) var. Hedef (çağrılacak
        nesne) zaten belirtildiği için, derleyici lambda'nın parametresini
        doğrudan println'e ARGÜMAN olarak geçirir:

            System.out.println(parametre);
         */
    }
}
