# Alıştırma Cevapları: Koleksiyonu Tek Bir Değere İndirgemek

Karar şeması:

```text
Sayıları toplamak                         → mapToInt/mapToDouble(...).sum()
En büyüğü/en küçüğü bulmak (özel amaçlı)   → max(Comparator...) / min(Comparator...)
Genel amaçlı, elle karşılaştırma mantığı   → reduce((a, b) -> ...)
Liste boş olabilir, sonuç Optional olmalı  → reduce(...) ya da max/min (Optional döner)
Liste boş olabilir ama varsayılan değer var → reduce(varsayılan, (a, b) -> ...) (Optional dönmez)
```

---

### 1. Sipariş Toplamı

```java
double total = order.items().stream()
        .mapToDouble(Item::price)
        .sum();
```

**Neden:** Basit bir toplama ihtiyacı — `mapToInt`/`mapToDouble` ile
sayısal bir akışa geçip `sum()` çağırmak, `SumOfLengths` örneğindeki
`mapToInt(name -> name.length()).sum()` ile birebir aynı kalıp.

---

### 2. Mağazadaki En Pahalı Ürün

```java
Optional<Product> mostExpensive = products.stream()
        .max(Comparator.comparing(Product::price));
```

**Neden:** "En büyüğü bul" ihtiyacı, hazır ve niyeti açık olan `max()` +
`Comparator` ile karşılanır; genel amaçlı `reduce` yazmaya gerek yok.

---

### 3. Yarışta En İyi Derece

```java
Optional<Runner> winner = runners.stream()
        .min(Comparator.comparing(Runner::finishTime));
```

**Neden:** "En kısa/en hızlı" = en küçük değer → `min()` + `Comparator`.

---

### 4. Şubeler Arası Toplam Satış

```java
double companyTotal = branchSales.stream()
        .mapToDouble(Double::doubleValue)
        .sum();
```

**Neden:** Elde zaten sayıların listesi var, doğrudan toplanacak —
`mapToDouble(...).sum()` (ya da liste `List<Double>` ise
`stream().mapToDouble(x -> x).sum()`).

---

### 5. Belgede En Uzun Kelime

```java
Optional<String> longestWord = words.stream()
        .reduce((w1, w2) -> w1.length() >= w2.length() ? w1 : w2);
```

**Neden:** "Birden fazla kelime aynı uzunluktaysa İLK karşılaşılan
seçilsin" kuralı, `max(Comparator.comparing(String::length))`'in
DAVRANIŞINI garanti etmez (JDK, eşitlik durumunda hangi elemanın
seçileceğini net taahhüt etmez). `ReduceToLongestName`'deki gibi elle
yazılan `reduce` ile `>=` kullanmak, "eşitlikte ilk gelen kazanır"
davranışını KESİN olarak garantiler.

---

### 6. Boş Sepette Toplam Tutar

```java
double total = cartItems.stream()
        .mapToDouble(Item::price)
        .sum();
```

**Neden:** Bu bir tuzak değil — `sum()` zaten boş bir `IntStream`/
`DoubleStream` üzerinde çağrılırsa `0` döner (toplamanın "birim/identity"
değeri zaten sıfırdır). Ekstra bir boş-liste kontrolüne gerek yok;
`Optional` de gerekmez, çünkü `sum()` bir sayı (`double`) döner, `Optional`
değil.

---

### 7. Takımın Ortalama Yaşı

```java
IntSummaryStatistics stats = players.stream()
        .mapToInt(Player::age)
        .summaryStatistics();

System.out.println("Toplam: " + stats.getSum() + ", Ortalama: " + stats.getAverage());

// ya da ayrı ayrı:
int totalAge = players.stream().mapToInt(Player::age).sum();
double averageAge = players.stream().mapToInt(Player::age).average().orElse(0);
```

**Neden:** Hem toplam hem ortalama isteniyor. `IntStream`'in `average()`
metodu bir `OptionalDouble` döner (liste boşsa ortalama tanımsız olur);
tek seferde ikisini de istiyorsak `summaryStatistics()` pratik bir
kısayoldur.

---

### 8. En Yüksek Sınav Puanı

```java
Optional<Result> topResult = results.stream()
        .reduce((r1, r2) -> r1.score() >= r2.score() ? r1 : r2);
```

**Neden:** Soru 5 ile aynı gerekçe: "eşit puanda ilk sıradaki kazansın"
kuralı KESİN garanti isteniyor, bu yüzden `max(Comparator...)` yerine
elle yazılan `reduce` + `>=` tercih edilir.

---

### 9. Ürün Yorumlarının Ortalama Puanı

```java
IntSummaryStatistics stats = reviews.stream()
        .mapToInt(Review::stars)
        .summaryStatistics();

System.out.println("Toplam: " + stats.getSum() + ", Ortalama: " + stats.getAverage());
```

**Neden:** Soru 7 ile birebir aynı kalıp, farklı iş alanı.

---

### 10. Rotadaki En Ağır Paket (Varsayılan Değerle)

```java
Package heaviest = route.stream()
        .reduce(referencePackage, (p1, p2) ->
                p1.weight() >= p2.weight() ? p1 : p2);
```

**Neden:** Burada `Optional` DÖNMEMESİ isteniyor ("sonucun her zaman bir
değeri olması gerekiyor") — bu, `reduce`'ın taban (identity) değer alan
overload'unun tam senaryosu (bkz. `ReduceWithIdentity`). Rota boş olsa
bile sonuç doğrudan `referencePackage` olur; rota doluysa normal
`reduce` mantığıyla karşılaştırma yapılır.

---

## Genel özet

```text
sum()                         → toplama, boş koleksiyonda 0 döner (Optional yok)
average()/summaryStatistics() → ortalama de isteniyorsa
max(Comparator) / min(Comparator) → "en büyük/en küçük", eşitlikte JDK davranışı garanti değil
reduce((a,b) -> ...)          → genel amaçlı, "eşitlikte ilk kazanır" gibi kesin kurallar için, Optional döner
reduce(taban, (a,b) -> ...)   → boş koleksiyonda bile Optional DEĞİL, taban değer döner
```
