package org.practice.fpij.usingcollections.pickingelement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterAndCollectElements {
    public static void main(String[] args) {
        /*
         Bu class "Picking an Element" konusunun HAZIRLIK adımı: koşula uyan
         BİRDEN FAZLA elemanı (filter + collect) seçmeyi gösteriyor. Asıl konu
         olan "TEK bir elemanı seçmek" (findFirst + Optional) için
         PickElementImperative ve PickElementElegant class'larına bakın --
         bu ikisi arasındaki fark tam olarak burada, dosyanın sonundaki 4.
         nottan doğuyor.

         Amaç, names listesinden "N" ile başlayan bütün isimleri seçmektir.
         Burada tek bir eleman bulma (findFirst/findAny) değil, koşula uyan
         elemanlardan yeni bir grup oluşturma işlemi vardır.
        */

        // Geleneksel yaklaşım: dolaşma, koşul ve sonuç listesini biz yönetiyoruz.
        List<String> nameStartsWithN = new ArrayList<>();
        List<String> names = new ArrayList<>();
        names.add("John");
        names.add("Noel");
        names.add("Samuel");
        names.add("Nieuw");
        names.add("Jane");
        names.add("Noord");
        for (String name : names) {
            if (name.startsWith("N")) {
                nameStartsWithN.add(name);
            }
        }
        System.out.println(nameStartsWithN.size());
        System.out.println(nameStartsWithN); //[Noel, Nieuw, Noord]

        /*
         Stream yaklaşımı:

         filter(), bir Predicate<String> bekler. Lambda'daki name bir String'dir.
         name.startsWith("N") boolean döndürdüğü için Predicate sözleşmesine uyar:

         String alır -> boolean döndürür

         filter() lazy'dir; yalnızca ara işlem tanımlar. forEach() terminal işlemi
         geldiğinde kaynak listedeki isimler tek tek test edilir.
        */
        Stream<String> stringStream = names.stream()
                .filter(name -> name.startsWith("N"));

        // forEach(), kalan her isim için Consumer'ın accept() davranışını çalıştırır.
        stringStream.forEach(System.out::println);

        // Çıktı:
        /*
         Noel
         Nieuw
         Noord
        */

        /*
         collect(), filtrelenen elemanları yeni bir List içinde toplar.
         İlk Stream forEach() ile tüketildiği için yeniden kullanılamaz; bu nedenle
         names listesinden yeni bir Stream oluşturulur.
        */
        List<String> collectedNames = names.stream()
                .filter(name -> name.startsWith("N"))
                .collect(Collectors.toList());

        System.out.println(collectedNames); //[Noel, Nieuw, Noord]
        collectedNames.forEach(System.out::println);

    }
}

/*
 1. System.out.println(collectedNames) ile collectedNames.forEach(...) farkı

    System.out.println(collectedNames);
    -> List nesnesinin tamamını tek çağrıda yazdırır:
       [Noel, Nieuw, Noord]

    collectedNames.forEach(System.out::println);
    -> forEach(), her eleman için println metodunu ayrı ayrı çağırır:
       Noel
       Nieuw
       Noord

 2. nameStartsWithN::contains ne anlama gelirdi?

    contains(), List interface'inde tanımlanan ve ArrayList tarafından uygulanan
    bir instance metodudur. Çağrılabilmesi için hangi liste üzerinde arama
    yapılacağının bilinmesi gerekir. Burada o nesne nameStartsWithN listesidir:

    nameStartsWithN::contains

    şu lambda ile aynı davranışı temsil eder:

    name -> nameStartsWithN.contains(name)

    contains(name), isim listede varsa true, yoksa false döndürür. Bu yüzden
    Predicate<String> sözleşmesine uyabilir ve filter() içinde kullanılabilir.

    Ancak bu sınıfta böyle kullanmak iyi bir Stream çözümü değildir:

    names.stream()
         .filter(nameStartsWithN::contains)

    Bu kod "N ile başlayanları seç" demez; "daha önce geleneksel döngüyle
    doldurulmuş nameStartsWithN listesinde bulunanları seç" der. Dolayısıyla
    Stream sonucu önceki çözümün sonucuna bağımlı olur. Asıl business koşulunu
    doğrudan yazmak daha açıktır:

    names.stream()
         .filter(name -> name.startsWith("N"))

 3. name.startsWith("N") neden boolean üretir?

    String.startsWith(String prefix) metodunun dönüş tipi boolean'dır:

    "Noel".startsWith("N")   -> true
    "John".startsWith("N")   -> false

    filter() kavramsal olarak her isim için Predicate'in test(name) metodunu
    çağırır. true dönen isimler Stream'de kalır, false dönenler elenir:

    name -> name.startsWith("N")
    String alır -> boolean döndürür -> Predicate<String>

 4. Bu sınıf "finding an element" mı yapıyor?

    Daha doğru ifade "picking/filtering elements" olur. Kod, koşula uyan bütün
    isimleri seçer. Tek bir isim aranıyor olsaydı filter() sonrasında findFirst()
    veya findAny() gibi bir terminal işlem ve Optional<String> sonucu görürdük.
    (Tam olarak bunu PickElementImperative / PickElementElegant class'larında
    ele alıyoruz.)
*/
