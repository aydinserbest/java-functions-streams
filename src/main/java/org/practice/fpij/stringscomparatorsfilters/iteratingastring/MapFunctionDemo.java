package org.practice.fpij.stringscomparatorsfilters.iteratingastring;

import java.util.Arrays;
import java.util.List;

public class MapFunctionDemo {
    public static void main(String[] args) {
        /*
        mapın tek görevi: DÖNÜŞTÜRMEK
        map = her elemanı al → başka bir şeye dönüştür
         */
        //Bunları büyük harfe dönüştürelim:
        String[] names = {"Alice", "Bob", "Charlie", "David"};

        // 1. Yöntem: Klasik for döngüsü ile dönüştürme
        String[] upperCaseNames = new String[names.length];

        for (int i = 0; i < names.length; i++) {
            upperCaseNames[i] = names[i].toUpperCase();
        }

        for (String name : upperCaseNames) {
            System.out.println(name);
        }
        // 2. Yöntem: Stream API ile dönüştürme
        //ara note:  Arrays.stream(names) → String[] dizisini Stream<String> akışına dönüştürür.
        Arrays.stream(names)
                .map(String::toUpperCase)
                .forEach(System.out::println);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        //List.of(1, 2, 3, 4);
        /*
        elimizde:
1, 2, 3, 4, 5 var
        map ile her elemanı 2 ile çarpıp ekrana yazdırmak istiyoruz.
         */
        numbers.stream()
                .map(n -> n * 2) //Her numberı al → number * 2 haline getir.
                .forEach(System.out::println);

/*
ARA NOT:

List`, `Set` gibi `Collection` tipleri kendi üzerlerinde doğrudan
`.stream()` metoduna sahiptir (bu metot `Collection` arayüzünden gelir).
**Array bir `Collection` değildir** — Java'nın yerleşik, ilkel bir veri
yapısıdır. Bu yüzden bir array üzerinde `.stream()` çağırmak **derleme
hatası** verir.

```java
String[] words = {"apple", "banana"};

words.stream(); // Derleme hatası: String[] içinde stream() metodu yoktur
```

## Çözüm: `Arrays.stream(...)`

Bir array'den Stream oluşturmak için `java.util.Arrays` sınıfındaki
static `stream(...)` metodunu kullanırız:

```java
String[] words = {"apple", "banana"};

Stream<String> streamOfWords = Arrays.stream(words);
```
 */
        /*
        mapın en önemli özelliği
map sadece sayıyı sayıya veya String'i String'e çevirmek zorunda değil.
String'leri uzunluklarına dönüştürebiliriz:
         */
        List<String> words = Arrays.asList("apple", "banana", "cherry");
        words.stream()
                .map(String::length) //Her kelimeyi al → kelimenin uzunluğunu al
                .forEach(System.out::println); //uzunlukları yazdır name -> name.length()

        /*
        .map(name -> name.length())
şunu yapıyor:
Bana String ver → ben sana Integer vereyim.

mapın gerçek gücü burada.
         */
        /*
        .map(x -> x'i_neye_dönüştürmek_istiyorsan)
Örneğin:
.map(x -> x * 2)          // sayı → başka sayı
.map(x -> x + 10)         // sayı → başka sayı
.map(x -> x.toUpperCase())// String → başka String
.map(x -> x.length())     // String → Integer
         */
        /*
        filter ile karıştırmamak için
Bu ikisini ayırman çok önemli.
filter
Seçer:
numbers.stream()
       .filter(number -> number > 2)
       .forEach(System.out::println);
Başlangıç:
1  2  3  4
Sonuç:
3  4
Elemanları değiştirmedi.
Sadece bazılarını attı.
map
Değiştirir/dönüştürür:
numbers.stream()
       .map(number -> number * 10)
       .forEach(System.out::println);
Başlangıç:
1  2  3  4
Sonuç:
10  20  30  40
Dolayısıyla:
filter → KİM KALSIN?

map    → KALANLAR NEYE DÖNÜŞSÜN?
         */
    }
}
