package org.practice.streamsandfunctionalinterfaces.comparable;

import java.util.Arrays;
import java.util.List;

public class SortOrCompareToDemo {
    static void main() {

        /*
        sort() ile compareTo() aynı işi yapmıyor. Aralarındaki ilişkiyi netleştirelim.
Sen Main'de şunu yaptın:
people.sort(null);
Bu bütün listeyi sıralar.
Ama:
john.compareTo(greg);
sadece iki Person'ı karşılaştırır.
         */
        Person p1 = new Person("John", 30);
        Person p2 = new Person("Sara", 21);
        Person p3 = new Person("Jane", 41);
        Person p4 = new Person("Greg", 35);

        //Result of 'Person.compareTo()' is ignored
        p1.compareTo(p2); // 1 → p1, p2'den sonra gelir

        /*
        Result of 'Person.compareTo()' is ignored uyarısı verir
        çünkü compareTo() metodunun dönüş değeri kullanılmıyor.
        Bu uyarıyı önlemek için dönüş değerini bir değişkene atayabilir
        veya bir if koşulu içinde kullanabilirsiniz. Örneğin:
         */
        int result1 = p1.compareTo(p2); // -1 → p2, p1'den önce gelir
        int result2 = p2.compareTo(p1); //  0 → sıralama açısından eşit

        System.out.println(p1.compareTo(p2));

        //compareTo() nesneleri herhangi bir yere taşımaz, listeyi sıralamaz.

        /*
        sort() ise compareTo()yu kullanarak işi tamamlar
Sen:
people.sort(null);
dediğinde Java perde arkasında listedeki Person'ları birbirleriyle karşılaştırmak zorunda.
Bunun için senin yazdığın:
compareTo()
metodunu defalarca çağırır.
Mantıksal olarak buna benzer karşılaştırmalar yapar:
john.compareTo(sara);
sara.compareTo(jane);
john.compareTo(greg);
// vs...
         */
        List<Person> personList = Arrays.asList(p1, p2, p3, p4);

        //Biz compareTo() ile Java'ya sadece "iki Person'ı nasıl karşılaştıracağını" öğretiyoruz.
        // sort() ise bu bilgiyi kullanıp bütün listeyi sıralıyor.

        personList.sort(null); // natural ordering (Comparable)
        /*
        sort() sonuç döndürmez (void), mevcut listeyi yerinde değiştirir.
        personList.sort(null);
Bu satır sıralamayı yaptı ama ekrana hiçbir şey yazdırmadı.
Sonucu görmek için:
         */
        System.out.println("After sorting:");

        System.out.println(personList); //[Sara - 21, John - 30, Greg - 35, Jane - 41] yazar
        personList.forEach(System.out::println);
        /*
        Sara - 21
John - 30
Greg - 35
Jane - 41
yazar
         */
        /*
        İkisi de aynı `Person` nesnelerini yazdırıyor ama **yazdırma şekilleri farklı**.

### 1. Listenin tamamını tek seferde yazdırıyorsun

```java
System.out.println(personList);
```

Çıktı:

```text
[Sara - 21, John - 30, Greg - 35, Jane - 41]
```

Burada `println`'e verdiğin şey:

```java
personList
```

yani **List nesnesinin tamamı**.

Java kabaca:

```text
List'in toString()'i
        ↓
[
 eleman1.toString(),
 eleman2.toString(),
 eleman3.toString()
]
```

şeklinde çalışır.

Bu yüzden:

- `[` `]` görürsün
- elemanlar `,` ile ayrılır
- hepsi aynı satırdadır.

---

### 2. Elemanları tek tek yazdırıyorsun

```java
personList.forEach(System.out::println);
```

Çıktı:

```text
Sara - 21
John - 30
Greg - 35
Jane - 41
```

Burada `println`'e **listeyi değil**, her seferinde bir `Person` gönderiyorsun.

Mantığı:

```java
personList.forEach(person -> System.out.println(person));
```

Java kabaca:

```text
Sara nesnesi → println → Sara - 21

John nesnesi → println → John - 30

Greg nesnesi → println → Greg - 35

Jane nesnesi → println → Jane - 41
```

`println` her seferinde yeni satıra geçtiği için alt alta görüyorsun.

### En kısa fark

```java
System.out.println(personList);
```

→ **LISTEYİ yazdır**

```text
[Sara - 21, John - 30, Greg - 35, Jane - 41]
```

```java
personList.forEach(System.out::println);
```

→ **LİSTENİN ELEMANLARINI TEK TEK yazdır**

```text
Sara - 21
John - 30
Greg - 35
Jane - 41
```

Ve ikisinde de senin `Person.toString()` metodun devreye giriyor:

```java
@Override
public String toString() {
    return name + " - " + age;
}
```

Fark şu:

```text
println(personList)
        ↓
List.toString()
        ↓
içeride her Person'ın toString()'i


forEach(println)
        ↓
her Person ayrı ayrı println'e gider
        ↓
her Person'ın toString()'i
```

Yani ilkinde **List'in `toString()`i Person'ların `toString()`lerini topluca kullanıyor**,
ikincisinde **her Person doğrudan ayrı ayrı yazdırılıyor**.
         */
    }
}
