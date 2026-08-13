# Java'da Sıralama: Genel Çerçeve

Toplu bir çerçeve kurulabilir, ama bir noktayı düzeltelim: **`Collection`
interface'inin `sort()` metodu yok.** `Collections.sort()` ayrı bir
static yardımcı metot.

En temiz genel resim şu:

> Java'da sıralama yapan metotlar, elemanların hangi sıraya gireceğine
> karar verebilmek için bir **karşılaştırma kuralına** ihtiyaç duyar. Bu
> kural ya `Comparable → compareTo()` ile natural order'dan gelir ya da
> `Comparator → compare()` ile dışarıdan verilir.

```text
                   SIRALAMA
                      │
        "Hangi eleman önce gelsin?"
                      │
             KARŞILAŞTIRMA KURALI
                 /           \
                /             \
       Comparable           Comparator
           ↓                    ↓
       compareTo()           compare()
           ↓                    ↓
     NATURAL ORDER          ÖZEL KURAL
                \             /
                 \           /
                  ↓         ↓
             SIRALAMA ARAÇLARI
                      │
       ┌──────────────┼────────────────┐
       ↓              ↓                ↓
   List.sort()   Stream.sorted()   Collections.sort()
```

---

## Natural Order ile Üç Kullanım

**Natural order** kullanırsak:

```java
people.sort(null);
```

```java
people.stream()
        .sorted()
        .toList();
```

```java
Collections.sort(people);
```

Üçü de uygun eleman tiplerinde doğal sıralamaya dayanır:

```text
Comparable
    ↓
compareTo()
    ↓
natural order
```

Örneğin `Person`:

```java
class Person implements Comparable<Person> {

    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age);
    }
}
```

Artık natural order `AGE ↑` olduğu için bu üç sıralama aracı da bunu
kullanabilir.

---

## `Comparator` Verildiğinde Ne Değişir?

Birazdan öğreneceğimiz **`Comparator` verirsek**, artık natural order'ı
kullanmak zorunda değiller:

```java
people.sort(byName);
```

veya:

```java
people.stream()
        .sorted(byName)
        .toList();
```

Burada kullanılan:

```text
Comparable.compareTo() ❌
Comparator.compare()   ✅
```

---

## Özet

```text
JAVA'DA SIRALAMA

Sıralama araçları:
- List.sort(...)
- Stream.sorted(...)
- Collections.sort(...)  // eski/klasik static yardımcı metot

Sıralama yapabilmek için karşılaştırma kuralı gerekir.

1) Comparable → compareTo()
   → natural order

2) Comparator → compare()
   → dışarıdan verilen özel sıralama kuralı

sort/sorted        = SIRALAMAYI YAPAR
compareTo/compare  = HANGİSİNİN ÖNCE GELECEĞİNE KARAR VERİR
```

Bütün konunun özeti bu son satırda:

> **`sort()` / `sorted()` sıralar; `compareTo()` / `compare()` sıralamanın
> kuralını sağlar.**

Bir sonraki adım: `Comparator`'a geçmek.
