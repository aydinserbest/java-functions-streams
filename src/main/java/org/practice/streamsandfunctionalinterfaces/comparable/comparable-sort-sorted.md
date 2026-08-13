# `Comparable`, `sort()` ve `sorted()` İlişkisi

## `Comparable`'ın Tek Görevi

> Bir nesneye kendi **doğal sıralama kuralını** (natural order) öğretmek.

`Person`'da şunu yazdın:

```java
public class Person implements Comparable<Person> {

    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age);
    }
}
```

Burada `Comparable`'ın işi bitti. Artık **`Person`'ın natural order'ı =
AGE** — Java biliyor.

Liste bile gerekmez, iki nesneyi doğrudan karşılaştırabilirsin:

```java
Person john = new Person("John", 30);
Person sara = new Person("Sara", 21);

System.out.println(john.compareTo(sara));
```

```text
Comparable
    ↓
compareTo()
    ↓
iki Person'ı karşılaştırabilme yeteneği
```

---

## `sort()` Nereden Geliyor?

Şimdi elimizde tek `Person` değil, bir sürü `Person` var:

```java
List<Person> people = Arrays.asList(
        new Person("John", 30),
        new Person("Sara", 21),
        new Person("Jane", 41),
        new Person("Greg", 35)
);
```

Diyoruz ki: *"Madem `Person`'lara `compareTo()` ile doğal sıralamalarını
öğrettik, bu listeyi o kurala göre sırala."*

```java
people.sort(null);
```

```text
people.sort(null)
        ↓
"Natural order kullan"
        ↓
Person implements Comparable<Person>
        ↓
compareTo()
        ↓
AGE
        ↓
21, 30, 35, 41
```

**`Comparable`'ı öğretirken `sort(null)` çok güzel bir test aracıdır. Ama
`Comparable` = `sort()` değildir.**

---

## `sorted()` Ne Zaman Geliyor?

`Comparable` tamamen anlaşıldıktan sonra şunu sorabiliriz: *"Tamam,
`Person`'ın natural order'ı zaten var. Aynı natural order'ı Stream de
kullanabilir mi?"*

Evet:

```java
List<Person> sortedPeople = people.stream()
        .sorted()
        .toList();
```

`.sorted()` da der ki: *"Bana `Comparator` verilmemiş. Elemanların
natural order'ını kullanayım."* Yine aynı yere gider:

```text
Person
    ↓
Comparable<Person>
    ↓
compareTo()
    ↓
AGE
```

Yani `people.sort(null)` ile `people.stream().sorted().toList()` **aynı
natural order bilgisinden faydalanır — sadece çalışma şekilleri farklı.**

---

## Üç Katmanlı Model

Bunu kafana yerleştir:

```text
KATMAN 1 — KURAL
Comparable<Person> → compareTo() → Natural Order = AGE

KATMAN 2 — KURALI KULLANAN ARAÇ
List.sort(null) → Natural order'ı kullan

KATMAN 3 — BAŞKA BİR KULLANIM YOLU
Stream.sorted() → Natural order'ı kullan
```

`sort()` ve `sorted()`, `Comparable`'ın **parçaları değil** — `Comparable`'ın
oluşturduğu natural order bilgisinin **müşterileri** gibi düşün.

### Aynı Şey `Integer`'da da Var

`Integer` zaten `Comparable`:

```java
Integer implements Comparable<Integer>
```

```java
List<Integer> numbers = new ArrayList<>(List.of(40, 5, 20, 10));
```

Natural order zaten `5 → 10 → 20 → 40`. Bunu `List.sort()` ile de
kullanabilirsin:

```java
numbers.sort(null);
```

ya da Stream ile:

```java
numbers.stream()
        .sorted()
        .toList();
```

**Natural order aynı. Araç farklı.**

> `Comparable` sıralama kuralını (natural order) tanımlar; `sort()` ve
> `sorted()` ise bu kuralı kullanabilen iki farklı araçtır.

---

## Bonus: `Collections.sort()` de Var

Bunu üçüncü, ayrı bir sıralama mantığı gibi düşünme — eski API'den gelen
başka bir **giriş kapısı**. Şu an bildiğimiz üç yazım:

```java
people.sort(null);            // List.sort()
Collections.sort(people);     // Collections yardımcı sınıfı
people.stream().sorted().toList(); // Stream.sorted()
```

İlk ikisi birbirine çok yakın; üçüncüsü Stream yaklaşımı.

### 1. `Collections.sort(people)`

`Collections`, Java'nın çeşitli collection işlemleri için static yardımcı
metotlar sunduğu sınıf (`java.util.Collections`):

```java
Collections.sort(people);
```

Comparator vermediğimiz için yine **natural order** kullanılır:

```text
Collections.sort(people)
        ↓
natural order
        ↓
Person implements Comparable<Person>
        ↓
compareTo()
        ↓
AGE
```

Sonuç:

```text
Sara - 21
John - 30
Greg - 35
Jane - 41
```

### 2. `people.sort(null)` ile İlişkisi

Modern Java'da `Collections.sort(people)`, esasen `people.sort(null)`
üzerinden çalışır. Comparable açısından ikisinin söylediği şey aynı:
**"Natural order'a göre bu listeyi sırala."** Ve **ikisi de mevcut
listeyi değiştirir.**

Yeni kodda genellikle:

```java
people.sort(null);
// veya daha açık:
people.sort(Comparator.naturalOrder());
```

tercih edilir. `Collections.sort()` özellikle eski Java kodlarında sık
karşına çıkar.

### 3. Neden İki Farklı API Oluşmuş?

Tarihsel bir sebep var:

```text
ESKİ / KLASİK
Collections.sort(people)
        ↓
     Java 8+
people.sort(...)
        ↓
STREAM YAKLAŞIMI
people.stream().sorted()
```

Sonuncusu ilk ikisinin doğrudan yerine geçen bir API değil — davranışı
farklı (kaynağı değiştirmez).

### Üçünü Tek Tabloda Oturt

| Kod | Kime ait? | Natural order kullanabilir? | Orijinal liste değişir mi? |
|---|---|:---:|:---:|
| `Collections.sort(people)` | `Collections` | ✅ | ✅ |
| `people.sort(null)` | `List` | ✅ | ✅ |
| `people.stream().sorted()` | `Stream` | ✅ | ❌ |

Comparable dersi açısından üçünün de bağlandığı yer aynı:

```text
                   Person
                     ↓
          Comparable<Person>
                     ↓
                 compareTo()
                     ↓
              NATURAL ORDER
                     ↓
        ┌────────────┼────────────┐
        ↓            ↓            ↓
Collections.sort   List.sort   Stream.sorted
```

Kısa özet:

```java
// Natural order ile sıralama:
Collections.sort(people);               // eski/klasik API, liste değişir
people.sort(null);                      // List API, liste değişir
people.sort(Comparator.naturalOrder()); // aynı niyeti daha açık yazar

people.stream()
        .sorted()                       // Stream API, kaynak liste değişmez
        .toList();
```

**Comparable açısından asıl öğrenilmesi gereken bunlardan herhangi biri
değil; asıl mesele `compareTo()` ile natural order'ı tanımlamak.** Bunlar
sadece o natural order'ı kullanabilen farklı sıralama yolları.

---

## Deney: `compareTo()`'yu Değiştir

Şu anda:

```java
@Override
public int compareTo(Person other) {
    return Integer.compare(this.age, other.age);
}
```

var, yani **natural order = AGE**. Bunu geçici olarak şöyle değiştir:

```java
@Override
public int compareTo(Person other) {
    return this.name.compareTo(other.name);
}
```

Artık **natural order = NAME**. Sonra hiçbir şeyi değiştirmeden çalıştır:

```java
people.sort(null);
```

Sonuç:

```text
Greg - 35
Jane - 41
John - 30
Sara - 21
```

Aynı şekilde `people.stream().sorted().toList()` da alfabetik sıralar.

**Neden?** Çünkü `sort()` veya `sorted()`, *"Yaşa göre sırala"*
demiyordu. Onlar *"Natural order'a göre sırala"* diyordu. Natural
order'ın ne olduğunu belirleyen `compareTo()` idi.

Bu ayrımı iyi oturt:

```text
compareTo()
    ↓
KURAL: "Person'ın natural order'ı nedir?"

sort(null)
    ↓
O KURALI KULLAN ve mevcut List'i sırala

sorted()
    ↓
O KURALI KULLAN ve Stream'i sırala
```

---

## Özet Diyagram

```text
public class Person implements Comparable<Person> {

    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age);
    }
}
        ↓
  Comparable<Person>
        ↓
    compareTo()
        ↓
(Person, Person karşılaştırması)
        ↓
  negatif / 0 / pozitif
        ↓
  NATURAL ORDER = AGE
        ↓
   ┌───────────────┐
   ↓               ↓
List.sort()     Stream.sorted()
   ↓               ↓
 SIRALA          SIRALA
```
