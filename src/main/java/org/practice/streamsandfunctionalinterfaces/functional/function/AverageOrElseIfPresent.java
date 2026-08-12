package org.practice.streamsandfunctionalinterfaces.functional.function;

import java.util.List;
import java.util.stream.IntStream;

public class AverageOrElseIfPresent {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");

        IntStream intStream = names.stream()
                .mapToInt(String::length);

        // sum();
        int total = intStream.sum();
        /*
        Burada dikkat:
.sum()
doğrudan:
int
döndürür.
Bu yüzden:
int total = ...
yazabiliyoruz.
         */

        //average();
        double average = names.stream()
                .mapToDouble(String::length)
                .average()
                .orElse(0.0);

        /*
        .orElse(0)
"Neden sum()da yoktu ama average()da var?"
Çünkü average() doğrudan double döndürmüyor.
Şunu döndürüyor:
OptionalDouble
Neden?
Liste boş olabilir:
List<String> names = List.of();
O zaman:
Ortalama nedir?
diye sorduğumuzda cevap yok.
Çünkü:
toplam / eleman sayısı

0 / 0
anlamlı bir ortalama değil.
Java bu yüzden:
.average()
sonucunu OptionalDouble içerisinde verir.
Biz de:
.orElse(0)
diyoruz:
Eğer ortalama varsa onu ver, yoksa 0 ver.

Şimdilik Optional konusuna fazla girmeyelim; ayrıca öğreniriz.
         */

        // min();
        names.stream()
                .mapToInt(String::length)
                .min()
                .ifPresent(min -> System.out.println("Minimum uzunluk: " + min));
    }
}
/*
Evet. `orElse(...)` ile `ifPresent(...)` birbirinin güncel/eski alternatifi değil; **farklı amaçlar için kullanılır.** Güncel Java API’sinde ikisi de var.

Senin örneğin üzerinden gidelim:

```java
OptionalDouble average = names.stream()
        .mapToInt(String::length)
        .average();
```

Burada `average()` bize doğrudan `double` değil:

```java
OptionalDouble
```

veriyor. Çünkü liste boşsa ortalama olmayabilir.

### `orElse(...)`: “Bana mutlaka bir değer ver”

```java
double average = names.stream()
        .mapToInt(String::length)
        .average()
        .orElse(0);

System.out.println(average);
```

Mantığı:

```text
ortalama varsa   → onu ver
ortalama yoksa   → 0 ver
```

Mesela:

```java
List<String> names = List.of("Ali", "Mehmet");
```

sonuç:

```text
4.5
```

Ama:

```java
List<String> names = List.of();
```

sonuç:

```text
0.0
```

Çünkü:

```java
.orElse(0)
```

dedik.

Yani `orElse` kullandığında sonunda elinde gerçek bir değer vardır:

```java
OptionalDouble
      ↓
orElse(0)
      ↓
double
```

---

### `ifPresent(...)`: “Değer varsa bir şey yap”

Şöyle:

```java
names.stream()
        .mapToInt(String::length)
        .average()
        .ifPresent(System.out::println);
```

Mantığı:

```text
ortalama varsa → println yap
ortalama yoksa → hiçbir şey yapma
```

Örneğin:

```java
List<String> names = List.of("Ali", "Mehmet");
```

çıktı:

```text
4.5
```

Ama:

```java
List<String> names = List.of();
```

çıktı:

```text
hiçbir şey
```

Burada `0` yazmaz.

Çünkü `ifPresent`:

> “Yoksa şu değeri kullan.”

demiyor.

> “Varsa şu işlemi yap.”

diyor.

---

En önemli fark şu:

```java
.orElse(0)
```

→ **DEĞER ÜRETİR**

```java
.ifPresent(...)
```

→ **İŞLEM YAPAR**

Bunu şöyle ezberleyebilirsin:

```text
orElse    → DEĞER LAZIM
ifPresent → AKSİYON LAZIM
```

Mesela sonucu değişkende kullanacaksan:

```java
double average = names.stream()
        .mapToInt(String::length)
        .average()
        .orElse(0);
```

Burada `orElse` mantıklı.

Çünkü aşağıda:

```java
if (average > 5) {
    ...
}
```

gibi kullanacaksın.

Ama amacın sadece:

> “Ortalama varsa ekrana yaz.”

ise:

```java
names.stream()
        .mapToInt(String::length)
        .average()
        .ifPresent(System.out::println);
```

daha doğal.

### IntelliJ neden `ifPresent` öneriyor?

Muhtemelen şöyle bir kod yazıyorsun:

```java
System.out.println(
        names.stream()
                .mapToInt(String::length)
                .average()
                .orElse(0)
);
```

IntelliJ bazı durumlarda şunu fark edebilir:

> “Sen aslında bu değeri sadece varsa kullanmak/yazdırmak istiyor olabilirsin.”

O yüzden:

```java
.ifPresent(System.out::println)
```

önerebilir.

Ama bu **otomatik olarak daha iyi** anlamına gelmez.

Çünkü davranışları farklıdır:

```java
.orElse(0)
```

boş stream'de:

```text
0.0
```

üretir.

```java
.ifPresent(System.out::println)
```

boş stream'de:

```text
hiçbir şey
```

yapar.

Dolayısıyla hangisinin doğru olduğu senin istediğin davranışa bağlı.

### Bir de `orElseThrow()` var

Mesela boş olması aslında hata ise:

```java
double average = names.stream()
        .mapToInt(String::length)
        .average()
        .orElseThrow();
```

Bu:

```text
değer varsa → ver
yoksa       → exception
```

demek. Oracle'ın güncel API'sinde `OptionalInt`/`OptionalDouble` için bu yaklaşım da doğrudan destekleniyor.

Ben sana şimdilik şu karar kuralını öneririm:

```text
Sonucu değişkende kullanacağım
        ↓
orElse(...)

Sadece varsa bir işlem yapacağım
        ↓
ifPresent(...)

Boş olması program açısından hata
        ↓
orElseThrow()
```

Örneğin:

```java
// 1. Bir değer lazım
double average = stream.average().orElse(0);
```

```java
// 2. Sadece varsa yazdır
stream.average().ifPresent(System.out::println);
```

```java
// 3. Mutlaka olması gerekiyor
double average = stream.average().orElseThrow();
```

Ve burada `ifPresent` hangi functional interface'i ister diye de bağlayalım:

```java
.ifPresent(System.out::println)
```

`OptionalDouble.ifPresent(...)`:

```text
DoubleConsumer
```

ister.

Mantığı:

```text
double → void
```

Yani değer gelir, bir işlem yapılır, geriye değer dönmez.

Senin öğrendiğin `Consumer<T>`'ın primitive `double` versiyonu gibi düşünebilirsin.
 */