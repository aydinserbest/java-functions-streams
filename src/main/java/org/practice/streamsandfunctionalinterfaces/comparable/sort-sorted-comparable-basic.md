# `Comparable` + `sort()`/`sorted()` — Hızlı Özet

`Comparable`, iki nesnenin nasıl karşılaştırılacağının kuralını belirler
(`compareTo`). Bu kural **natural order** olur. `sort()` veya `sorted()`
da bu kuralı **kullanarak** sıralama yapabilir.

```text
Comparable
    ↓
compareTo()
    ↓
KARŞILAŞTIRMA KURALINI BELİRLER
(natural order)
    ↓
    ├── sort()   → listeyi sıralayabilir
    │
    └── sorted() → stream'i sıralayabilir
```

## Örnek

```java
@Override
public int compareTo(Person other) {
    return Integer.compare(this.age, other.age);
}
```

Burada `Comparable` → *"`Person`'ları YAŞA göre karşılaştır."* diyor.
Sonra:

```java
people.sort(null);
```

veya:

```java
people.stream()
        .sorted()
        .toList();
```

ikisi de bu yaşa göre natural order kuralından yararlanabilir.

> `compareTo()` → karşılaştırır / kuralı verir.
> `sort()` / `sorted()` → o karşılaştırmalara göre sıralar.

`sort()` ve `sorted()`, `Comparable`'ın **parçaları değil** —
`Comparable`'ın oluşturduğu natural order bilgisinin **müşterileri**
gibi düşün.

> `Comparable` sıralama kuralını (natural order) tanımlar; `sort()` ve
> `sorted()` ise bu kuralı kullanabilen iki farklı araçtır.

## `sorted()` vs `sort()`

- **`sorted()`** bir **Stream ara işlemidir**. Parametresiz çağrıldığında
  elemanların doğal sırasını (natural ordering) kullanır — yani her
  elemanın kendi `compareTo()` metodunu. Bunun çalışabilmesi için
  elemanların `Comparable` implement etmesi gerekir.

- **`sort(...)`** ise `List` arayüzünün kendi metodu (Stream değil).
  `List` içinde `void sort(Comparator<? super E> c)` şeklinde tanımlıdır
  ve listeyi **yerinde (in place) mutasyona uğratır** — `people`'ın
  kendisi değişir, yeni liste dönmez.

## Üç Katmanlı Model

```text
KATMAN 1 — KURAL
Comparable<Person> → compareTo() → Natural Order = AGE

KATMAN 2 — KURALI KULLANAN ARAÇ
List.sort(null) → Natural order'ı kullan

KATMAN 3 — BAŞKA BİR KULLANIM YOLU
Stream.sorted() → Natural order'ı kullan
```

Detaylı işleniş için: [`comparable-sort-sorted.md`](./comparable-sort-sorted.md)
(natural order testi, `Collections.sort()` karşılaştırması, `compareTo()`
deneyi) ve `Arrays.asList`/`List.of` farkı için:
[`list-of-arrays-aslist-sort.md`](./list-of-arrays-aslist-sort.md).
