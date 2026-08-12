package org.practice.fpij.stringscomparatorsfilters.iteratingastring;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MapToMethodesDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        names.stream()
                .map(n -> n.length()) // Her ismin uzunluğunu al String::length
                .forEach(System.out::println); // Uzunlukları ekrana yazdır

        Stream<Integer> integerStream = names.stream()
                .map(String::length);
        /*
        Stream<String>

↓ map(String::length)

Stream<Integer> oluşur
         */
        /*
        integerStream değişkeni  bir List<Integer> değildir.
        Stream<Integer>
yani bir veri işleme hattıdır (pipeline).
         */

        System.out.println("Uzunluklar: " + integerStream);
        //çıktı:
        //Uzunluklar: java.util.stream.ReferencePipeline$3@452b3a41
        //Stream nesnesinin kendisini yazdır.

        /*
        Çünkü Java'ya:
"Stream'in içindeki elemanları yazdır."

demedin.
Sadece:
"integerStream nesnesinin kendisini yazdır."

dedin.
Dolayısıyla Java sana nesnenin temsilini veriyor:
         */
        System.out.println(integerStream.toList());
        /*
        artık Java'ya:
"Bu stream'i çalıştır ve sonuçlarını bir List'e topla."

diyorsun.
Dolayısıyla:
Stream<Integer>
      ↓
   toList()
      ↓
List<Integer>
      ↓
[3, 6, 4]
Sonuç:
[3, 6, 4]
         */
        /*
        Burada çok önemli başka bir Stream konusu var
Şuna dikkat:
names.stream()
     .map(String::length);
map() bir intermediate operation'dır.
Yani ara işlem.
Stream pipeline'ını oluşturur ama sonuçları hemen tüketmez.
Şöyle düşün:
names.stream()
     .map(String::length);
Java'ya:
"İsimler gelecek ve geldiğinde uzunluklarını hesaplayacağız."

diyorsun.
Ama henüz:
"Hadi çalıştır!"

demedin.
6. toList() terminal operation'dır
Şunu eklediğinde:
.toList()
pipeline artık tüketilir.
List<Integer> lengths = names.stream()
        .map(String::length)
        .toList();
Akış:
names

["Ali", "Mehmet", "Ayse"]
          ↓
       stream()
          ↓
     Stream<String>
          ↓
  map(String::length)
          ↓
     Stream<Integer>
          ↓
       toList()
          ↓
   List<Integer>
          ↓
      [3, 6, 4]
Burada:
map()
intermediate operation
ama:
toList()
terminal operation.
Bu iki kavramı bundan sonra çok göreceksin.

         */
        Stream<Integer> intermediateMapOperation = names.stream()
                .map(name -> {
                    System.out.println("Mapping: " + name);
                    return name.length();
                });
        System.out.println(intermediateMapOperation.toList());

        /*
        buna Stream'lerin lazy (tembel) çalışması deniyor.
Yani kabaca:
"Terminal operation gelmeden gereksiz yere hesaplama yapma."
         */
    }
}
