# `filter()` ve `Predicate` Notları

Kaynak: `BasicFilterDemo`

## `filter()` Ne Yapar?

> **filter = SEÇ / ELE**

Tek mantığı şu:

```
Her elemanı tek tek al
        ↓
   Predicate'e sor
        ↓
    true / false
    ↙        ↘
 KALSIN     ELENSİN
```

**`filter()` elemanı değiştirmez.** Sadece "kalacak mı, elenecek mi?"
kararını verir.

---

## Sayılarla Temel Örnekler

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 5'ten büyük olan sayıları filtrele ve yazdır
numbers.stream()
        .filter(number -> number > 5)   // SEÇER  | Predicate<Integer>
        .forEach(System.out::println);  // UYGULAR | Consumer<Integer>

// Çift sayıları seçelim
numbers.stream()
        .filter(number -> number % 2 == 0)
        .forEach(System.out::println);
```

`filter()`'ın yaptığı tek şey: **Predicate `true` döndürüyorsa geçir.**

---

## String Örnekleri

```java
List<String> names = List.of("Ali", "Ayşe", "Mehmet", "Fatma");

// Uzunluğu 4'ten büyük isimleri seç
names.stream()
        .filter(name -> name.length() > 4)
        .forEach(System.out::println);

// "A" harfiyle başlayanları seç
names.stream()
        .filter(name -> name.startsWith("A"))
        .forEach(System.out::println);
```

---

## Birden Fazla Şart

**Tek `filter()` içinde `&&` ile:**

> A ile başlasın **VE** uzunluğu 4'ten büyük olsun.

```java
names.stream()
        .filter(name -> name.startsWith("A") && name.length() > 4)
        .forEach(System.out::println);
```

**Ya da iki ayrı `filter()` zincirleyerek** — aynı sonucu verir:

```java
names.stream()
        .filter(name -> name.startsWith("A"))
        .filter(name -> name.length() > 4)
        .forEach(System.out::println);
```

---

## `filter` ile `map` Farkı

**`filter`** sadece **seçer**, elemanları değiştirmez.
**`map`** ise elemanları **dönüştürür**.

```java
names.stream()
        .filter(name -> name.startsWith("A"))   // SEÇER      | Predicate<String>
        .map(String::toUpperCase)               // DÖNÜŞTÜRÜR | Function<String, String>
        .forEach(System.out::println);          // UYGULAR    | Consumer<String>
```

---

## `Predicate`'i Ayrı Değişken Olarak Yazmak

```java
Predicate<String> startsWithA = name -> name.startsWith("A");

names.stream()
        .filter(startsWithA)
        .forEach(System.out::println);
```

```java
Predicate<String> uzunMu = name -> name.length() > 4;

names.stream()
        .filter(uzunMu)
        .forEach(System.out::println);
```

---

## Neden `filter()` bir `Predicate` İster?

`filter`'ın ihtiyacı şu:

> "Bana her eleman için `true` veya `false` söyleyebilecek bir şey ver."

`Predicate` tam olarak bunu yapıyor:

```
Predicate<T>

T → boolean
```

```java
List<Integer> sayilar = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
Predicate<Integer> ciftMi = sayi -> sayi % 2 == 0;

sayilar.stream()
        .filter(ciftMi)
        .forEach(System.out::println);
```

---

## `negate()` ile Tersini Almak

```java
List<Integer> ciftOlmayanlar = sayilar.stream()
        .filter(ciftMi.negate())   // negate() ile tersini alabiliriz
        .toList();

System.out.println(ciftOlmayanlar);
```

`filter`, her elemanı `Predicate`'e gönderir; `true` dönen elemanı geçirir,
`false` dönen elemanı eler.

---

## Kısa Formül

```
filter    = SEÇER
Predicate = SORAR
```

**Predicate:** "Kalsın mı?"

| Sonuç | Anlamı |
|---|---|
| `true`  | EVET, geçir |
| `false` | HAYIR, ele |
