# Alıştırma Cevapları: Comparator ile Sıralama ve En Uç Elemanı Bulma

Karar şeması:

```text
Sıralı bir LİSTE isteniyor, orijinal veri korunmalı  → stream().sorted(comparator).collect(toList())
Aynı kuralın TERSİ isteniyor (tekrar yazmadan)         → mevcutComparator.reversed()
Sadece TEK BİR uç eleman isteniyor (küçük)             → stream().min(comparator)
Sadece TEK BİR uç eleman isteniyor (büyük)             → stream().max(comparator)
min()/max() sonucu her zaman Optional<T> döner          → ifPresent(...) / orElse(...) ile güvenle kullan
```

---

### 1. Ürünleri Fiyata Göre Listeleme

```java
List<Product> ascendingByPrice = products.stream()
        .sorted((p1, p2) -> p1.getPrice() - p2.getPrice())
        .collect(Collectors.toList());
```

**Neden:** "Sıralı bir liste göster" + "orijinal sıra bozulmamalı" —
`products.sort(...)` YERİNDE (in place) değiştireceği için burada yanlış
olurdu; `stream().sorted(...)` orijinale dokunmadan YENİ bir liste döner.

---

### 2. Aynı Listeyi Pahalıdan Ucuza Gösterme

```java
Comparator<Product> byPriceAscending = (p1, p2) -> p1.getPrice() - p2.getPrice();
Comparator<Product> byPriceDescending = byPriceAscending.reversed();

List<Product> descending = products.stream()
        .sorted(byPriceDescending)
        .collect(Collectors.toList());
```

**Neden:** "Kuralı yeniden yazmadan tersini elde etme" cümlesi doğrudan
`reversed()`'ın tanımı. Artan kuralı BİR yerde tanımlayıp, azalanı ondan
türetiyoruz — `SortByAgeDescendingDuplication`'daki gibi ikinci bir
elle-ters-çevrilmiş lambda YAZMIYORUZ.

---

### 3. Çalışanları Soyadına Göre Sıralama

```java
List<Employee> sortedBySurname = employees.stream()
        .sorted((e1, e2) -> e1.getSurname().compareTo(e2.getSurname()))
        .collect(Collectors.toList());
```

**Neden:** Sayısal değil, ALFABETİK bir sıralama isteniyor —
`SortByNameAndFindMinMax`'taki gibi String'in kendi `compareTo` metodu
kullanılıyor; `sorted()` iskeleti aynı kalıyor, sadece karşılaştırma
mantığı değişiyor.

---

### 4. En Ucuz Ürünü Bulma

```java
products.stream()
        .min((p1, p2) -> p1.getPrice() - p2.getPrice())
        .ifPresent(cheapest -> System.out.println("En ucuz: " + cheapest));
```

**Neden:** "SADECE en ucuz olanı bul, tüm listeyi sıralamaya gerek yok"
cümlesi `min()`'i işaret ediyor — `sorted().findFirst()` de aynı sonucu
verirdi ama gereksiz yere TÜM listeyi sıralardı; `min()` tek geçişte
sonuca ulaşır.

---

### 5. En Yüksek Tutarlı Siparişi Bulma

```java
orders.stream()
        .max((o1, o2) -> o1.getAmount() - o2.getAmount())
        .ifPresent(biggest -> System.out.println("En yüksek tutar: " + biggest));
```

**Neden:** Soru 4 ile aynı mantık, sadece "en YÜKSEK" olduğu için `min`
yerine `max`.

---

### 6. Uçuşları Süreye Göre İki Farklı Ekranda Gösterme

```java
Comparator<Flight> byDurationAscending = (f1, f2) -> f1.getDurationMinutes() - f2.getDurationMinutes();
Comparator<Flight> byDurationDescending = byDurationAscending.reversed();

List<Flight> shortestFirst = flights.stream().sorted(byDurationAscending).collect(Collectors.toList());
List<Flight> longestFirst = flights.stream().sorted(byDurationDescending).collect(Collectors.toList());
```

**Neden:** Soru 2 ile birebir aynı ihtiyaç — "aynı kuralın iki yönü, tek
tanımdan türetilecek" — `reversed()`.

---

### 7. Oyun Skor Tablosu (Leaderboard)

```java
List<Player> leaderboard = players.stream()
        .sorted((p1, p2) -> p2.getScore() - p1.getScore())
        .collect(Collectors.toList());
```

**Neden:** Burada tek bir yön (en yüksekten en düşüğe) isteniyor, tekrar
kullanılacak ikinci bir sıralama yok — bu yüzden `reversed()`'a gerek
kalmadan, parametreleri BAŞTAN ters sırada karşılaştıran tek bir lambda
(`p2 - p1`) yeterli ve daha basit.

---

### 8. Kitapları Yayın Yılına Göre Sıralama

```java
List<Book> byYearAscending = books.stream()
        .sorted((b1, b2) -> b1.getYear() - b2.getYear())
        .collect(Collectors.toList());
```

**Neden:** "Eskiden yeniye" = artan sıralama — Soru 1 ile aynı kalıp,
sadece alan (yıl) farklı.

---

### 9. En Erken Randevuyu Bulma

```java
appointments.stream()
        .min((a1, a2) -> a1.getTime().compareTo(a2.getTime()))
        .ifPresentOrElse(
                earliest -> System.out.println("İlk randevu: " + earliest),
                () -> System.out.println("Bugün randevu yok")
        );
```

**Neden:** "SADECE en erken olanı bul" — `min()`. Ama burada AYRICA "hiç
randevu olmayabilir" ihtimali AÇIKÇA yönetilmesi gerektiği için (sadece
sessiz geçmek değil, "randevu yok" mesajı da gösterilmesi isteniyor)
`ifPresent` yerine iki kollu `ifPresentOrElse` kullanılıyor
(`pickingelement` paketinden tanıdık bir kalıp).

---

### 10. En Yüksek Stoklu Ürünü Bulma

```java
shelves.stream()
        .max((s1, s2) -> s1.getQuantity() - s2.getQuantity())
        .ifPresent(fullest -> System.out.println("En yüksek stok: " + fullest));
```

**Neden:** "Böyle bir raf yoksa hiçbir şey gösterilmemeli" — sadece
"varsa göster" davranışı, `ifPresent` yeterli; `orElse`/`ifPresentOrElse`
gibi bir "yoksa da bir şey yap" ihtiyacı burada YOK.

---

## Genel özet

```text
stream().sorted(comparator).collect(toList())  → orijinali bozmadan sıralı YENİ liste
list.sort(comparator)                          → listeyi YERİNDE (mutasyonla) sıralar, burada TERCİH EDİLMEDİ
comparator.reversed()                          → aynı kuralın tersini, tekrar yazmadan üretir (DRY)
stream().min(comparator) / max(comparator)     → tüm listeyi sıralamadan TEK bir uç elemanı bulur
min()/max() sonucu                             → her zaman Optional<T>; ifPresent/ifPresentOrElse/orElse ile yönetilir
```
