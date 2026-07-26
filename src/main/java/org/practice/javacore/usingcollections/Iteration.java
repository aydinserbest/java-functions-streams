package org.practice.javacore.usingcollections;

import java.util.List;
import java.util.function.Consumer;

/**
 * Business açısından burada ne yapılıyor?
 *
 * Bir sosyal uygulamada kullanıcının arkadaşları listelenmek isteniyor.
 * Sistemin elindeki Alice, Bob, Charlie ve David isimlerini sırasıyla alıp
 * her birini ekranda ayrı bir satırda göstermesi gerekiyor.
 */
public class Iteration {
    public static void main(String[] args) {
        //List<String> friends = Arrays.asList("Alice", "Bob", "Charlie", "David");
        /*
        To create an immutable list, we may use the more recently introduced function
        List.of() instead of Arrays.asList().
         */
        List<String> friends = List.of("Alice", "Bob", "Charlie", "David");

        /*
        Here’s the habitual, but not so desirable, way to iterate and print each of the elements.

         */
        for (int i = 0; i < friends.size(); i++) {
            System.out.println(friends.get(i));
        }
        //for' loop can be replaced with enhanced 'for'
        /*
        Java also offers a construct that’s a little bit more civilized than the good old for loop.

         */
        for (String name : friends) {
            System.out.println(name);
        }

        /*
        Bundan sonraki örneklerde dolaşma işini biz for döngüsü yazarak yönetmiyoruz.
        Iterable.forEach(), listedeki her elemanı sırayla kendisine verilen
        Consumer<String> davranışına gönderiyor.

        Aşağıdaki START ve END satırları forEach()'in parçası değildir. Yalnızca
        bu örneğin konsol çıktısının nerede başlayıp bittiğini ayırt etmek için
        yazdırılan işaretlerdir.
         */
        System.out.println("//" + "START:INTERNAL_FOR_EACH_OUTPUT");

        /*
        forEach() parametre olarak Consumer<String> ister. Burada Consumer,
        anonymous class ile uzun biçimde oluşturuluyor.

        Liste: [Alice, Bob, Charlie, David]

        forEach() her isim için accept() metodunu çağırır:
        accept("Alice")   -> Alice yazdırılır
        accept("Bob")     -> Bob yazdırılır
        accept("Charlie") -> Charlie yazdırılır
        accept("David")   -> David yazdırılır

        Consumer bir sonuç döndürmez. Her ismi ekrana yazdırmak, yapılan işlemin
        kendisidir. Bu kullanım geçerlidir fakat lambda'ya göre gereksiz uzundur.
         */
        friends.forEach(new Consumer<String>() {
            public void accept(final String name) {
                System.out.println(name);
            }
        });

        System.out.println("//" + "END:INTERNAL_FOR_EACH_OUTPUT");

        /*
        Aynı Consumer davranışı şimdi lambda ile ifade ediliyor.

        (final String name) -> System.out.println(name)

        Sol taraf accept() metodunun String parametresini, sağ taraf ise metodun
        her isim geldiğinde yapacağı işi temsil eder. Lambda yalnızca davranışı
        tanımlar; davranış listedeki her eleman için forEach() tarafından
        çalıştırılır.
         */
        System.out.println("//" + "START:INTERNAL_OUTPUT");
        friends.forEach((final String name) -> System.out.println(name));
        System.out.println("//" + "END:INTERNAL_OUTPUT");

        /*
        Parametre tipi yazılmayabilir. friends bir List<String> olduğu ve
        forEach() Consumer<String> beklediği için Java, name'in String olduğunu
        hedef tipten çıkarır. Tek parametre bulunduğu halde parantez kullanılabilir.
         */
        friends.forEach((name) -> System.out.println(name));

        /*
        Tek lambda parametresinde tip yazılmıyorsa parantez de kaldırılabilir.
        Bu satır bir üstteki satırla tamamen aynı davranışı gerçekleştirir.
         */
        friends.forEach(name -> System.out.println(name));

        /*
        Lambda yalnızca gelen değeri başka bir metoda iletiyorsa method reference
        kullanılabilir:

        name -> System.out.println(name)
        System.out::println

        İki ifade de "forEach() tarafından verilen String'i println() metoduna
        gönder" anlamına gelir ve aynı isimleri aynı sırayla yazdırır.
         */
        friends.forEach(System.out::println);

    }
}
