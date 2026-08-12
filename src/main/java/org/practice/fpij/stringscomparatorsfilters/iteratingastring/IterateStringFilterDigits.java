package org.practice.fpij.stringscomparatorsfilters.iteratingastring;

public class IterateStringFilterDigits {

    private static void printChar(final int aChar) {
        System.out.println((char) aChar);
    }

    public static void main(String[] args) {
        final String str = "w00t";

        System.out.println("1) Lambda ile filtreleme:");
        str.chars()
                .filter(ch -> Character.isDigit(ch))
                .forEach(ch -> printChar(ch));
        /*
        Önceki bölümlerden tanıdık: filter() bir ara işlem, forEach() bir
        terminal işlem. Sonuç sadece rakamlar:
        0
        0
         */

        System.out.println("\n2) Method reference ile aynı filtreleme:");
        str.chars()
                .filter(Character::isDigit)
                .forEach(IterateStringFilterDigits::printChar);
        /*
        Character::isDigit de STATIC bir metoda referans -- yapısal olarak
        String::toUpperCase (bir INSTANCE metodu) ile birebir aynı görünür
        (Sınıf::metotAdı), ama derleyici ikisini FARKLI yorumlar:

          - metot bir INSTANCE metoduysa  -> parametre.metot()
                                              (parametre, ÇAĞRILAN nesne olur)
          - metot bir STATIC metoduysa     -> Sinif.metot(parametre)
                                              (parametre, metoda ARGÜMAN olur)

        ÖNEMLİ TUZAK -- Method Reference Belirsizliği (Ambiguity):
        Eğer bir sınıfta AYNI İMZADA hem bir instance metodu hem de bir
        static metot varsa, derleyici hangisini kastettiğinizi ÇÖZEMEZ ve
        derleme hatası verir. Örneğin Double sınıfında hem
        public String toString() (instance) hem de
        public static String toString(double) (static) var -- bu yüzden
        Double::toString yazmak DERLEME HATASINA yol açar. Böyle bir
        çakışmayla karşılaşırsanız çözüm basit: method reference'tan
        vazgeçip açık bir lambda expression'a geri dönün.
         */
    }
}
