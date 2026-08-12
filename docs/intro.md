# Stream, Lambda ve Method Reference Notları

## Stream Metotlarının Kısa Özeti

| Metot | Ne yapar |
|---|---|
| **filter** | bazılarını seç |
| **map** | dönüştür |
| **forEach** | her biriyle bir şey yap |
| **reduce** | hepsini bir sonuçta birleştir |

names.stream()                          // stream()  → Listeyi Stream'e çevirir
.filter(name -> name.length() > 4) // filter()  → SEÇER      | Predicate<T> gerekir
.map(String::toUpperCase)           // map()     → DÖNÜŞTÜRÜR | Function<T,R> gerekir
.forEach(System.out::println);      // forEach() → UYGULAR    | Consumer<T> gerekir

---

## Lambda'yı Tam Anlayalım

Şu kod:

```java
ch -> System.out.println(ch)
```

bir **lambda expression**.

Bunu şimdilik matematik gibi düşünme. Şöyle oku:

```
ch geliyor
   ↓
System.out.println(ch) yap
```

Yani:

```java
ch -> System.out.println(ch)
```

kabaca **"bana bir ch ver, ben onu yazdırayım"** demek.

### Örnekler

Mesela:

```java
x -> x * 2
```

şu: `x geliyor → ikiyle çarp`

Mesela:

```java
name -> name.toUpperCase()
```

şu: `name geliyor → büyük harfe çevir`

---

## Lambda'dan Method Reference'a Geçiş

Şimdi şu koda bakalım:

```java
str.chars()
   .forEach(ch -> System.out.println(ch));
```

Lambda'nın yaptığı tek şey şu:

```
ch geliyor
   ↓
println(ch)
```

Yani ch üzerinde **hiçbir ekstra işlem yapmıyoruz**. Sadece bir metoda gönderiyoruz. Java diyor ki:

> "Bunu daha kısa yazabilirsin."

```java
str.chars()
   .forEach(System.out::println);
```

Bu, `ch -> System.out.println(ch)` ile **aynı mantıkta**.

### Çok Önemli Pratik Kural

Şunu gördüğünde:

```java
x -> birMetot(x)
```

aklına hemen şu gelsin:

```java
...::birMetot
```

Örneğin:

```java
x -> System.out.println(x)
```

↓

```java
System.out::println
```

**Method reference'ın temel mantığı bu.**

---

## `::` Nedir?

Şu:

```java
System.out::println
```

bir **method reference**. Bunu Türkçe zihninde şöyle okuyabilirsin:

> "System.out nesnesinin println metodunu kullan."

Burada henüz `println(...)` **çağırmıyoruz**.

### Dikkat: Çağırmak ile Referans Vermek Aynı Şey Değil

| İfade | Anlamı |
|---|---|
| `System.out.println()` | Metodu **çalıştır**. |
| `System.out::println` | Bu metodu, **gerektiğinde çağırılacak işlem olarak ver**. |

**Bu fark çok önemli.**
